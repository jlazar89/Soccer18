package com.bluetag.wc.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.callback.OnItemClickListener;
import com.bluetag.wc.model.Stadium;
import com.bluetag.wc.model.StadiumModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jeffy on 4/1/2017.
 */

public class StadiumListAdapter extends RecyclerView.Adapter<StadiumListAdapter.HolderView> {
    private Context mContext;
    private List<Stadium> mStadiumList;
    private OnItemClickListener clickListener;


    public StadiumListAdapter(Context context,List<Stadium> stadiumList) {
        this.mContext = context;
        this.mStadiumList = stadiumList;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stadium_list, parent, false);

        return new HolderView(itemView);
    }

    @Override
    public void onBindViewHolder(final HolderView holder, int position) {
        final Stadium stadium  = mStadiumList.get(position);
        holder.stadiumName.setText(stadium.getStadiumName());
        Picasso.with(mContext).load(stadium.getStadiumImageUrl()).placeholder(R.drawable.ic_placeholder).into(holder.stadiumImage);

        ViewCompat.setTransitionName(holder.stadiumImage, stadium.getStadiumName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(stadium, holder.stadiumImage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStadiumList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        public TextView stadiumName;
        private ImageView stadiumImage;

        public HolderView(View view) {
            super(view);
            stadiumName = (TextView) view.findViewById(R.id.stadium_name);
            stadiumImage = (ImageView) view.findViewById(R.id.stadium_photo);
        }
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
