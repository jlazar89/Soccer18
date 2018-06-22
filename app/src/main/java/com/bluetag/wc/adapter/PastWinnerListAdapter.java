package com.bluetag.wc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.callback.OnItemClickListener;
import com.bluetag.wc.callback.OnPastWinnerItemClickListener;
import com.bluetag.wc.model.PastWinner;
import com.github.vipulasri.timelineview.TimelineView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class PastWinnerListAdapter extends RecyclerView.Adapter<PastWinnerListAdapter.PastWinnersViewHolder> {

    private List<PastWinner> mFeedList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private OnPastWinnerItemClickListener clickListener;

    public PastWinnerListAdapter(List<PastWinner> feedList) {
        mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public PastWinnersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        view = mLayoutInflater.inflate(R.layout.item_past_winners, parent, false);

        return new PastWinnersViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final PastWinnersViewHolder holder, int position) {

        final PastWinner pastWinner = mFeedList.get(position);

        holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));

        if (!pastWinner.getYear().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(pastWinner.getYear());
        } else
            holder.mDate.setVisibility(View.GONE);

        holder.mMessage.setText(pastWinner.getWinnerName());
        ViewCompat.setTransitionName(holder.mWinnerFlag, pastWinner.getWinnerName());
        // Picasso.with(mContext).load(pastWinner.getWinnerFlag()).placeholder(R.mipmap.ic_launcher).into(holder.mWinnerFlag);

        Picasso.with(mContext)
                .load(pastWinner.getWinnerFlag())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                /* Save the bitmap or do something with it here */

                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                int mutedLight = palette.getDominantColor(mContext.getResources().getColor(android.R.color.black));
                                holder.placeNameHolder.setBackgroundColor(mutedLight);
                            }
                        });
                        //Set it in the ImageView
                        holder.mWinnerFlag.setImageBitmap(bitmap);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(pastWinner, holder.mWinnerFlag);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    public class PastWinnersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_timeline_date)
        TextView mDate;
        @BindView(R.id.text_timeline_title)
        TextView mMessage;
        @BindView(R.id.time_marker)
        TimelineView mTimelineView;

        @BindView(R.id.winner_flag)
        ImageView mWinnerFlag;

        @BindView(R.id.layout_year)
        LinearLayout placeNameHolder;

        public PastWinnersViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            ButterKnife.setDebug(true);
            mTimelineView.initLine(viewType);
        }
    }


    public void setClickListener(OnPastWinnerItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
