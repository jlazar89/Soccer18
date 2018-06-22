package com.bluetag.wc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetag.wc.R;
import com.bluetag.wc.activities.BrowserActivity;
import com.bluetag.wc.activities.StadiumDetailActivity;
import com.bluetag.wc.activities.StadiumListActivity;
import com.bluetag.wc.model.Stadium;
import com.bluetag.wc.utils.TTSService;
import com.bluetag.wc.utils.Utils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_DETAIL_URL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_NAME_PRONOUNCE;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_STADIUM_MODEL;
import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_TRANSITION_KEY_STADIUM_IMAGE;

/**
 * A fragment representing a single Stadium detail screen.
 * This fragment is either contained in a {@link StadiumListActivity}
 * in two-pane mode (on tablets) or a {@link StadiumDetailActivity}
 * on handsets.
 */
public class StadiumDetailFragment extends Fragment {

    /**
     * The dummy content this fragment is presenting.
     */
    private Stadium mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StadiumDetailFragment() {
    }

    private AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(BUNDLE_KEY_STADIUM_MODEL)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (Stadium) getArguments().getSerializable(BUNDLE_KEY_STADIUM_MODEL);
            String transitionName = getArguments().getString(BUNDLE_TRANSITION_KEY_STADIUM_IMAGE);
            postponeEnterTransition();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getStadiumName());

                KenBurnsView stadium_backdrop = (KenBurnsView)activity. findViewById(R.id.stadium_backdrop);
                stadium_backdrop.setTransitionListener(new KenBurnsView.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }
                    @Override
                    public void onTransitionEnd(Transition transition) {

                    }
                });

               // ImageView stadium_backdrop = (ImageView) activity.findViewById(R.id.stadium_backdrop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stadium_backdrop.setTransitionName(transitionName);
                }
                //Utils.picassoLoader(getActivity(), stadium_backdrop, mItem.getStadiumImageUrl());
                Picasso.with(getContext())
                        .load(mItem.getStadiumImageUrl())
                        .placeholder(R.drawable.ic_placeholder)
                        .noFade()
                        .into(stadium_backdrop, new Callback() {
                            @Override
                            public void onSuccess() {
                                startPostponedEnterTransition();
                            }

                            @Override
                            public void onError() {
                                startPostponedEnterTransition();
                            }
                        });
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stadium_detail, container, false);

        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.stadium_name)).setText(getResources().getString(R.string.stadium_name) + mItem.getStadiumName());
            ((TextView) rootView.findViewById(R.id.stadium_host_city)).setText(getResources().getString(R.string.stadium_host_city) + mItem.getStadiumHostCity());
            ((TextView) rootView.findViewById(R.id.stadium_location)).setText(getResources().getString(R.string.stadium_location) + mItem.getStadiumLocation());
            ((TextView) rootView.findViewById(R.id.stadium_capacity)).setText(getResources().getString(R.string.stadium_capacity) + mItem.getStadiumCapacity());
            TextView readMore = (TextView) rootView.findViewById(R.id.stadium_more_Detail);
            SpannableString content = new SpannableString(getResources().getString(R.string.read_more));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            readMore.setText(content);
            readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BrowserActivity.class);
                    intent.putExtra(BUNDLE_KEY_DETAIL_URL, mItem.getStadiumDetailUrl());
                    startActivity(intent);
                }
            });

            ImageView speak_name = (ImageView) rootView.findViewById(R.id.stadium_name_pronounce);
            speak_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    speakOut();
                }
            });
        }

        return rootView;
    }


    private void speakOut() {
        Intent serviceIntent = new Intent(getActivity(), TTSService.class);
        serviceIntent.putExtra(BUNDLE_KEY_NAME_PRONOUNCE, mItem.getStadiumName());
        getActivity().startService(serviceIntent);
    }
}
