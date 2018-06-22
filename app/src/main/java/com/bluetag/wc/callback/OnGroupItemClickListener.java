package com.bluetag.wc.callback;

import android.widget.ImageView;
import android.widget.TextView;

import com.bluetag.wc.model.Group;
import com.bluetag.wc.model.Stadium;

/**
 * Created by Jeffy on 4/1/2017.
 */

public interface OnGroupItemClickListener {
    void onItemClick(Group item, TextView sharedView);
}
