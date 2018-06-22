package com.bluetag.wc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.activities.BrowserActivity;
import com.bluetag.wc.activities.MascotActivity;
import com.bluetag.wc.model.PastWinner;
import com.bluetag.wc.utils.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_PAST_WINNER_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE;

/**
 * Created by Jeffy on 4/19/2017.
 */

public class PastWinnerDetailFragment extends Fragment {
    /**
     * The dummy content this fragment is presenting.
     */
    private PastWinner mItem;

    public PastWinnerDetailFragment() {
    }

    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(BUNDLE_KEY_PAST_WINNER_MODEL)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (PastWinner) getArguments().getSerializable(BUNDLE_KEY_PAST_WINNER_MODEL);
            String transitionName = getArguments().getString(BUNDLE_TRANSITION_KEY_PAST_WINNER_IMAGE);
            postponeEnterTransition();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.past_winner_detail, container, false);

        //set toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mItem.getYear() + " - " + mItem.getWinnerName());

        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //year
            ((TextView) rootView.findViewById(R.id.year)).setText(mItem.getYear());

            //Winning country details
            ((TextView) rootView.findViewById(R.id.winner_team_name)).setText(mItem.getWinnerName());
            ImageView winnerImageFlag = (ImageView) rootView.findViewById(R.id.winner_team_image);
            //Utils.picassoLoader(getActivity(), winnerImageFlag,mItem.getWinnerFlag());

            Picasso.with(getContext())
                    .load(mItem.getWinnerFlag())
                    .placeholder(R.drawable.ic_placeholder_square)
                    .noFade()
                    .into(winnerImageFlag, new Callback() {
                        @Override
                        public void onSuccess() {
                            startPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            startPostponedEnterTransition();
                        }
                    });

            //Runner up country details
            ((TextView) rootView.findViewById(R.id.runner_up_team_name)).setText(mItem.getRunnerUpName());
            ImageView runnerImageFlag = (ImageView) rootView.findViewById(R.id.runner_up_team_image);
            //Utils.picassoLoader(getActivity(), runnerImageFlag,mItem.getRunnerUpFlag());
            Picasso.with(getContext())
                    .load(mItem.getRunnerUpFlag())
                    .placeholder(R.drawable.ic_placeholder_square)
                    .noFade()
                    .into(runnerImageFlag, new Callback() {
                        @Override
                        public void onSuccess() {
                            startPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            startPostponedEnterTransition();
                        }
                    });

            //Host
            ((TextView) rootView.findViewById(R.id.txt_host_country_name)).setText(mItem.getHostCountry());
            ((TextView) rootView.findViewById(R.id.txt_host_stadium_name)).setText(mItem.getHostStadium());

            //scorers
            LinearLayout goldenBallLayout = (LinearLayout) rootView.findViewById(R.id.ll_golden_ball);
            if (!TextUtils.isEmpty(mItem.getGoldenBall())) {
                goldenBallLayout.setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.txt_golden_ball_name)).setText(mItem.getGoldenBall());
            }

            LinearLayout goldenBootLayout = (LinearLayout) rootView.findViewById(R.id.ll_golden_boot);
            if (!TextUtils.isEmpty(mItem.getGoldenBoot())) {
                goldenBootLayout.setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.txt_golden_boot_name)).setText(mItem.getGoldenBoot());
            }


            LinearLayout goldenGloveLayout = (LinearLayout) rootView.findViewById(R.id.ll_golden_glove);
            if (!TextUtils.isEmpty(mItem.getGoldenGlove())) {
                goldenGloveLayout.setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.lbl_golden_glove_name)).setText(getResources().getString(R.string.label_golden_glove));
                ((TextView) rootView.findViewById(R.id.txt_golden_glove_name)).setText(mItem.getGoldenGlove());
            } else if (!TextUtils.isEmpty(mItem.getBestGoalkeeper())) {
                goldenGloveLayout.setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.lbl_golden_glove_name)).setText(getResources().getString(R.string.label_goalkeeper));
                ((TextView) rootView.findViewById(R.id.txt_golden_glove_name)).setText(mItem.getBestGoalkeeper());
            }

            //final score
            ((TextView) rootView.findViewById(R.id.txt_final_score)).setText(mItem.getScore());

            //((TextView) rootView.findViewById(R.id.stadium_host_city)).setText(getResources().getString(R.string.stadium_host_city) + mItem.getRunnerUpName());
            //((TextView) rootView.findViewById(R.id.stadium_location)).setText(getResources().getString(R.string.stadium_location) + mItem.getHostCountry());
            //((TextView) rootView.findViewById(R.id.stadium_capacity)).setText(getResources().getString(R.string.stadium_capacity) + mItem.getYear());
            //TextView readMore = (TextView) rootView.findViewById(R.id.stadium_more_Detail);
            //SpannableString content = new SpannableString(getResources().getString(R.string.read_more));
            //content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            //readMore.setText(content);
            //readMore.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        Intent intent = new Intent(getActivity(), BrowserActivity.class);
            //        intent.putExtra(BUNDLE_KEY_DETAIL_URL, mItem.getGoldenBall());
            //        startActivity(intent);
            //    }
            //});
//
            //ImageView speak_name = (ImageView) rootView.findViewById(R.id.stadium_name_pronounce);
            //speak_name.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //    }
            //});
        }

        return rootView;
    }
}
