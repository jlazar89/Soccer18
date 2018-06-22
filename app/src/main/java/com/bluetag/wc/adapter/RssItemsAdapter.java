package com.bluetag.wc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.api.rss.converter.RssItem;
import com.bluetag.wc.callback.OnRssItemClickListener;
import com.bluetag.wc.utils.Constants;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for listing {@link RssItem}
 */
public class RssItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    // A menu item view type.
    private static final int RSS_ITEM_VIEW_TYPE = 0;

    // The Native Express ad view type.
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;

    private final List<Object> mItems = new ArrayList<>();
    private OnRssItemClickListener mListener;
    private final Context mContext;

    public RssItemsAdapter(Context context) {
        mContext = context;
    }

    /**
     * Item click listener
     *
     * @param listener listener
     */
    public void setListener(OnRssItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * Set {@link RssItem} list
     *
     * @param items item list
     */
    public void setItems(List<Object> items) {
        mItems.clear();
        mItems.addAll(items);
    }

   // @Override
   // public RssItemsAdapter.RssViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
   //     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rss_item, parent, false);
   //     return new RssViewHolder(view);
   // }

    /**
     * Creates a new view for a menu item view or a Native Express ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case RSS_ITEM_VIEW_TYPE:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rss_item, viewGroup, false);
                return new RssViewHolder(view);
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                View nativeExpressLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.native_express_ad_container,
                        viewGroup, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }

    }


 // @Override
 // public void onBindViewHolder(RssViewHolder holder, int position) {
 //     if (mItems.size() <= position) {
 //         return;
 //     }
 //     final RssItem item = mItems.get(position);
 //     holder.textTitle.setText(item.getTitle());
 //     holder.textPubDate.setText(item.getPublishDate());

 //     if (item.getImage() != null) {
 //         String url= item.getImage();
 //        url= url.replace("small.jpg","full-lnd.jpg");
 //        //Picasso.with(mContext).load(url).
 //        //        fit()
 //        //        .centerCrop()
 //        //        .into(holder.imageThumb);

 //         Picasso.with(mContext).load(url)
 //                 .placeholder(R.drawable.ic_placeholder)
 //                 .into(holder.imageThumb);
 //     }
 //     holder.itemView.setOnClickListener(new View.OnClickListener() {
 //         @Override
 //         public void onClick(View view) {
 //             if (mListener != null) mListener.onItemSelected(item);
 //         }
 //     });
 //     holder.itemView.setTag(item);

 // }

    /**
     *  Replaces the content in the views that make up the menu item view and the
     *  Native Express ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case RSS_ITEM_VIEW_TYPE:
                RssViewHolder rssholder = (RssViewHolder) holder;
                final RssItem item = (RssItem) mItems.get(position);

                // Get the menu item image resource ID.
                rssholder.textTitle.setText(item.getTitle());
                rssholder.textPubDate.setText(item.getPublishDate());

                if (item.getImage() != null) {
                    String url= item.getImage();
                    url= url.replace("small.jpg","full-lnd.jpg");
                    //Picasso.with(mContext).load(url).
                    //        fit()
                    //        .centerCrop()
                    //        .into(holder.imageThumb);
                    Log.d("URLSSS", url);
                    if(url.startsWith("//")){
                        url = url.replace("//","http://");
                    }

                    Picasso.with(mContext).load(url)
                            .placeholder(R.drawable.ic_placeholder)
                            .into(rssholder.imageThumb);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) mListener.onItemSelected(item);
                    }
                });
                holder.itemView.setTag(item);
                break;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mItems.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                // The NativeExpressAdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // NativeExpressAdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled NativeExpressAdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
        return (position % Constants.ITEMS_PER_AD == 0) ? NATIVE_EXPRESS_AD_VIEW_TYPE
                : RSS_ITEM_VIEW_TYPE;
    }


    static class RssViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView textTitle;

        @BindView(R.id.tvPubDate)
        TextView textPubDate;

        @BindView(R.id.ivThumb)
        ImageView imageThumb;

        RssViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * The {@link NativeExpressAdViewHolder} class.
     */
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }
}
