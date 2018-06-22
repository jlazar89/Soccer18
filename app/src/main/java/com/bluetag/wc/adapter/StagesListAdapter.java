package com.bluetag.wc.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.callback.OnGroupItemClickListener;
import com.bluetag.wc.model.Group;

import java.util.List;

/**
 * Created by Matrix on 04-03-2018.
 */

public class StagesListAdapter extends RecyclerView.Adapter<StagesListAdapter.HolderView> {
    private Context mContext;
    private List<Group> mGroupList;
    private OnGroupItemClickListener clickListener;

    public StagesListAdapter(Context context,List<Group> groupList) {
        this.mContext = context;
        this.mGroupList = groupList;
    }

    @Override
    public StagesListAdapter.HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_list, parent, false);

        return new StagesListAdapter.HolderView(itemView);
    }

    @Override
    public void onBindViewHolder(final StagesListAdapter.HolderView holder, int position) {
        final Group group  = mGroupList.get(position);
        holder.groupName.setText(group.getGroupName());

        ViewCompat.setTransitionName(holder.groupName, group.getGroupName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(group, holder.groupName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        public TextView groupName;

        public HolderView(View view) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.group_name);
        }
    }

    public void setClickListener(OnGroupItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
