package com.bluetag.wc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.callback.OnGroupItemClickListener;
import com.bluetag.wc.callback.OnHighLighItemClickListener;
import com.bluetag.wc.model.HighLightsModel;
import com.bluetag.wc.model.YoutubeVideoModel;
import com.bluetag.wc.utils.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 10/11/17.
 */

public class HighLightsAdapter extends RecyclerView.Adapter<HighLightsAdapter.YoutubeViewHolder> {
    private static final String TAG = HighLightsAdapter.class.getSimpleName();
    private Context context;
    private List<HighLightsModel.HighLight> mHighlights;
    private OnHighLighItemClickListener clickListener;


    public HighLightsAdapter(Context context, List<HighLightsModel.HighLight> ahighlights) {
        this.context = context;
        this.mHighlights = ahighlights;
    }

    @Override
    public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.youtube_video_custom_layout, parent, false);
        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YoutubeViewHolder holder, final int position) {

        final HighLightsModel.HighLight  highLight = mHighlights.get(position);

        holder.videoTitle.setText(highLight.getMatchName());
        holder.videoDuration.setText("Match "+highLight.getMatchNo());


        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.videoThumbnailImageView.initialize(Constants.YT_DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(highLight.getMatchVideoId());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure");

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(highLight.getMatchVideoId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mHighlights.size();
    }

    class YoutubeViewHolder extends RecyclerView.ViewHolder {

        public YouTubeThumbnailView videoThumbnailImageView;
        public TextView videoTitle, videoDuration;

        public YoutubeViewHolder(View itemView) {
            super(itemView);
            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            videoTitle = itemView.findViewById(R.id.video_title_label);
            videoDuration = itemView.findViewById(R.id.video_duration_label);
        }
    }

    public void setClickListener(OnHighLighItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
