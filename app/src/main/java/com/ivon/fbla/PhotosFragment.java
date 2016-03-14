package com.ivon.fbla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotosFragment extends Fragment implements OnFabClickListener,
        OnCardClickListener, ValueEventListener {

    public PhotosFragment() {
    }

    private ListView mListView;
    private PhotoAdapter mAdapter;
    private Firebase mFirebaseRef;

    private static final int CAMERA_IMAGE_REQUEST = 0;
    private static final int SAVE_PHOTO_REQUEST = 1;
    private Uri mMediaUri;

    private static final int PERMISSIONS_REQUEST_CAMERA = 0;

    private FloatingActionButton getFab() {
        return ((MainActivity) getActivity()).getFab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.main_list);

        List<Photo> samples = new ArrayList<>();
        mAdapter = new PhotoAdapter(getActivity(), samples, this);
        mListView.setAdapter(mAdapter);

        View headerView = new View(getActivity());
        headerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.card_margin_bottom)));
        mListView.addHeaderView(headerView);

        View footerView = new View(getActivity());
        footerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.card_margin_top)));
        mListView.addFooterView(footerView);

        ((MainActivity) getActivity()).setFabClickListener(this);

        mFirebaseRef = new Firebase("https://fbla-mobile-app.firebaseio.com/photos");
        mFirebaseRef.addValueEventListener(this);

        return rootView;
    }

    @Override
    public void onCardClick(View view, int position) {
        Logger.log("Clicked " + position);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.TYPE_REVIEW);
        intent.putExtra(ReviewsFragment.EXTRA_PHOTO_ID, mAdapter.getItem(position).id);
        startActivity(intent);
    }

    @Override
    public boolean onCardLongClick(View view, int position) {
        return false;
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {
        launchImageCamera();
        //Intent intent = new Intent(getActivity(), AccountActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchImageCamera();
                } else {
                    Snackbar.make(getFab(), "Permission denied. Unable to take photos.", Snackbar.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.log("Activity result: " + resultCode);
        if (requestCode == CAMERA_IMAGE_REQUEST) {
            if (resultCode == MainActivity.RESULT_OK) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                String imageFile = Utils.bitmapToString(imageBitmap);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_TYPE, MainActivity.TYPE_SAVE);
                intent.putExtra(SavePhotoFragment.EXTRA_IMAGE, imageFile);
                startActivity(intent);
            } else {
                Logger.log("Camera failed: " + resultCode);
            }
        }
    }

    private void launchImageCamera() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(imageIntent, CAMERA_IMAGE_REQUEST);
            } else {
                Snackbar.make(getFab(), "Camera not available", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        }
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) snapshot.getValue();
        if (map != null) {
            mAdapter.clear();
            List<Photo> photos = new ArrayList<>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> item = entry.getValue();
                String owner = (String) item.get("owner");
                String image = (String) item.get("image");
                long timestamp = (Long) item.get("timestamp");
                Photo photo = new Photo(key, owner, image, timestamp);
                photos.add(photo);
            }
            Collections.sort(photos, new Comparator<Photo>() {
                @Override
                public int compare(Photo a, Photo b) {
                    return Long.valueOf(b.timestamp).compareTo(a.timestamp);
                }
            });
            mAdapter.addAll(photos);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled(FirebaseError error) {

    }
}
