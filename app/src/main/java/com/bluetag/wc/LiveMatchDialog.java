package com.bluetag.wc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bluetag.wc.interfaces.LiveMatchCallback;
import com.bluetag.wc.model.LiveMatchModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveMatchDialog extends DialogFragment{

    @BindView(R.id.match_1_card)
    CardView match1;

    @BindView(R.id.match_2_card)
    CardView match2;

    @BindView(R.id.txt1)
    AppCompatTextView text1;

    @BindView(R.id.txt2)
    AppCompatTextView text2;

    String match1Details;
    String match2Details;
    private LiveMatchCallback livematchcallback;

    public LiveMatchDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static LiveMatchDialog newInstance(LiveMatchModel liveMatchModel) {
        LiveMatchDialog frag = new LiveMatchDialog();
        Bundle args = new Bundle();
        args.putSerializable("live", liveMatchModel);
        frag.setArguments(args);
        return frag;
    }

    public void setMatchCallback(LiveMatchCallback callback){
        this.livematchcallback = callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_live_match, container, false);
        ButterKnife.bind(this, v);

        getDialog().setTitle("Select Live Match");

        // Fetch arguments from bundle and set title
        final LiveMatchModel liveMatchModel = (LiveMatchModel) getArguments().getSerializable("live");

        //settext
        text1.setText(liveMatchModel.getMatch_1_details());
        text2.setText(liveMatchModel.getMatch_2_details());

        match1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livematchcallback.onLiveMatchSelected(liveMatchModel.getLive_match_1_url());
                dismiss();
            }
        });

        match2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                livematchcallback.onLiveMatchSelected(liveMatchModel.getLive_match_2_url());
                dismiss();
            }
        });

        // Do all the stuff to initialize your custom view

        return v;
    }
}
