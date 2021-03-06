package com.jin35.vk.adapters;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jin35.vk.ProfileActivity;
import com.jin35.vk.R;
import com.jin35.vk.model.IModelListener;
import com.jin35.vk.model.NotificationCenter;
import com.jin35.vk.model.PhotoStorage;
import com.jin35.vk.model.UserInfo;

public class SimpleUserListItem extends ModelObjectListItem<UserInfo> {

    private final boolean needOnClickListener;

    public SimpleUserListItem(UserInfo object, boolean needOnClickListener) {
        super(object);
        this.needOnClickListener = needOnClickListener;
    }

    @Override
    public int getViewId() {
        return R.layout.simple_user_list_item;
    }

    @Override
    public void updateView(View view) {
        if (getObject() == null) {
            ((ImageView) view.findViewById(R.id.photo_iv)).setImageDrawable(PhotoStorage.getInstance().getDefaultPhoto());
            view.findViewById(R.id.online_indicator_iv).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.name_tv)).setText(R.string.not_dowanloaded_name);
            return;
        }
        if (getObject().getPhoto() != null) {
            ((ImageView) view.findViewById(R.id.photo_iv)).setImageDrawable(getObject().getPhoto());
        }
        int onlineVisibility = getObject().isOnline() ? View.VISIBLE : View.GONE;
        view.findViewById(R.id.online_indicator_iv).setVisibility(onlineVisibility);
        ((TextView) view.findViewById(R.id.name_tv)).setText(getObject().getFullName());
        if (needOnClickListener) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity.start(v.getContext(), getObject().getId());
                }
            });
        }
    }

    @Override
    public boolean needListener() {
        return true;
    }

    @Override
    public void subsribeListenerForObject(IModelListener listener) {
        NotificationCenter.getInstance().addObjectListener(getObject().getId(), listener);
    }
}
