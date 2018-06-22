package com.bluetag.wc.callback;

import android.widget.ImageView;

import com.bluetag.wc.model.PastWinner;
import com.bluetag.wc.model.Stadium;

/**
 * Created by Jeffy on 4/1/2017.
 */

public interface OnPastWinnerItemClickListener {
    void onItemClick(PastWinner item, ImageView sharedImageView);
}
