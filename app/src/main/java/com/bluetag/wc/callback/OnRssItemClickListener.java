package com.bluetag.wc.callback;

import com.bluetag.wc.api.rss.converter.RssItem;

/**
 * Created by Jeffy on 5/6/2017.
 */

public interface OnRssItemClickListener {
    void onItemSelected(RssItem rssItem);
}
