package com.jin35.vk.model;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class UserInfo extends ModelObject {

    private String name;
    private String familyName;
    // private boolean isMale;
    // private long birthday;
    private boolean online;
    private int importance;
    private String photoUrl;

    UserInfo(long userId) {
        super(userId);
    }

    @Override
    protected int getMaskForNotify() {
        return NotificationCenter.FRIENDS;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Drawable getPhoto() {
        return PhotoStorage.getInstance().getPhoto(photoUrl);
    }

    public String getFullName() {
        String result = "";
        if (!TextUtils.isEmpty(name))
            result = result.concat(name);
        if (!TextUtils.isEmpty(familyName)) {
            if (result.length() > 0) {
                result = result.concat(" ");
            }
            result = result.concat(familyName);
        }
        return result;
    }
}
