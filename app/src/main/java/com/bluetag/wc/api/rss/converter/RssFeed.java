package com.bluetag.wc.api.rss.converter;

import java.util.List;

/**
 * RSS Feed response model
 */

public class RssFeed {

    /**
     * List of parsed {@link RssItem} objects
     */
    private List<RssItem> mItems;

    public List<RssItem> getItems() {
        return mItems;
    }

    void setItems(List<RssItem> items) {
        mItems = items;
    }

    //create a new item count
    private int _itemcount = 0;

    public RssItem getItem(int location) {

        //return the item at the chosen position
        return mItems.get(location);
    }

    public int getItemCount() {

        //return the number of items in the feed
        return getItems().size()>0 ? mItems.size() : _itemcount;

    }
}
