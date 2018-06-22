package com.bluetag.wc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.model.MainScreenModel;

import java.util.ArrayList;

/**
 * Created by Jeffy on 6/3/2017.
 */

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.ViewHolder>  {

    ArrayList mValues;
    Context mContext;
    protected ItemListener mListener;

    public MainScreenAdapter(Context context, ArrayList values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        MainScreenModel item;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
        }

        public void setData(MainScreenModel item) {
            this.item = item;

            textView.setText(item.text);
            imageView.setImageResource(item.drawable);
            //relativeLayout.setBackgroundColor(Color.parseColor(item.color));
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public MainScreenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_main_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData((MainScreenModel) mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(MainScreenModel item);
    }
}