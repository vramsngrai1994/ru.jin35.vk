package com.jin35.vk.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

import com.jin35.vk.R;
import com.jin35.vk.model.db.DB;
import com.jin35.vk.net.OnPhotoRequestResult;
import com.jin35.vk.net.impl.BackgroundTasksQueue;
import com.jin35.vk.net.impl.PhotoRequestTask;

public class PhotoStorage {

    private static final int MAX_ATTEMPTS = 3;

    private final Drawable defaultPhoto;
    private static PhotoStorage instance;
    private static final String[] defaultUrls = new String[] { "http://vkontakte.ru/images/camera_a.gif", "http://vkontakte.ru/images/camera_b.gif",
            "http://vkontakte.ru/images/camera_c.gif" };

    private final LruCache<String, Drawable> photos = new LruCache<String, Drawable>(100) {
        @Override
        protected Drawable create(String key) {
            Bitmap photo = DB.getInstance().getPhoto(key);
            if (photo != null) {
                return new BitmapDrawable(photo);
            }
            return null;
        }
    };

    private PhotoStorage(Context context) {
        defaultPhoto = context.getResources().getDrawable(R.drawable.contact_no_photo);
    }

    public static synchronized void init(Context context) {
        instance = new PhotoStorage(context);
    }

    public static PhotoStorage getInstance() {
        return instance;
    }

    public synchronized Drawable getPhoto(final String photoUrl, final long userId) {
        for (String defaultUrl : defaultUrls) {
            if (defaultUrl.equalsIgnoreCase(photoUrl)) {
                return defaultPhoto;
            }
        }
        // not default photo

        Drawable result = photos.get(photoUrl);
        if (result != null) {
            return result;
        }

        // photo not downloaded

        BackgroundTasksQueue.getInstance().execute(new PhotoRequestTask(photoUrl, new OnPhotoRequestResult() {
            private int attemts = 0;

            @Override
            public void onPhotoRequestResult(Bitmap result) {
                synchronized (photos) {
                    photos.put(photoUrl, new BitmapDrawable(result));
                    NotificationCenter.getInstance().notifyObjectListeners(userId);
                    DB.getInstance().savePhoto(photoUrl, result);
                }
            }

            @Override
            public void onPhotoRequestFail() {
                if (attemts++ < MAX_ATTEMPTS) {
                    BackgroundTasksQueue.getInstance().execute(new PhotoRequestTask(photoUrl, this));
                }
            }
        }));

        return defaultPhoto;
    }

    public Drawable getPhoto(UserInfo user) {
        return getPhoto(user.getPhotoUrl(), user.getId());
    }

    public Drawable getDefaultPhoto() {
        return defaultPhoto;
    }
}
