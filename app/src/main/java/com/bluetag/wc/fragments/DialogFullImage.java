package com.bluetag.wc.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bluetag.wc.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.bluetag.wc.utils.Constants.BundleKeys.BUNDLE_KEY_IMAGE_URL;

/**
 * Created by Jeffy on 4/2/2017.
 */

public class DialogFullImage extends DialogFragment {

    /**
     * The dummy content this fragment is presenting.
     */
    private String mImageUrl;
    public DialogFullImage() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                .TRANSPARENT));
        return dialog;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_full_image, container,
                false);
        if (getArguments().containsKey(BUNDLE_KEY_IMAGE_URL)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mImageUrl = getArguments().getString(BUNDLE_KEY_IMAGE_URL);
            ImageView stadium_backdrop = (ImageView) rootView.findViewById(R.id.image);
            picassoLoader(getActivity(), stadium_backdrop, mImageUrl);

            // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
            new PhotoViewAttacher(stadium_backdrop);

        }

        return rootView;
    }

    public void picassoLoader(Context context, ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
