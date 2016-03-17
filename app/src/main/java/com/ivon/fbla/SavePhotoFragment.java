package com.ivon.fbla;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

/**
 * Created by Owner on 2/18/2016.
 */
public class SavePhotoFragment extends Fragment implements OnFabClickListener {

    public static final String EXTRA_IMAGE = "image";

    private String mImageFile;
    private Firebase mFirebaseRef;
    private EditText mEditText;

    public static SavePhotoFragment newInstance(String imageFile) {
        SavePhotoFragment fragment = new SavePhotoFragment();
        fragment.mImageFile = imageFile;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_save_photo, container, false);
        mEditText = (EditText) rootView.findViewById(R.id.username);
        ((ImageView) rootView.findViewById(R.id.image)).setImageBitmap(Utils.stringToBitmap(mImageFile));
        ((MainActivity) getActivity()).setFabClickListener(this);
        mFirebaseRef = new Firebase("https://fbla-mobile-app.firebaseio.com/photos");

        return rootView;
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {
        String username = Utils.getUsername(getActivity());
        Photo photo = new Photo("", username, mImageFile, System.currentTimeMillis());
        mFirebaseRef.push().setValue(photo.format());
        getActivity().finish();
    }
}
