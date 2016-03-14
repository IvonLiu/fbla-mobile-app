package com.ivon.fbla;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

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
 * Created by Owner on 2/18/2016.
 */
public class ReviewsFragment extends Fragment implements OnFabClickListener,
        OnCardClickListener, ValueEventListener {

    public static final String EXTRA_PHOTO_ID = "photoId";

    private ImageView mPhotoHeaderView;
    private ListView mListView;
    private ReviewAdapter mAdapter;
    private String mPhotoId;
    private Firebase mFirebaseRef;

    public static ReviewsFragment newInstance(String photoId) {
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.mPhotoId = photoId;
        return fragment;
    }

    private FloatingActionButton getFab() {
        return ((MainActivity) getActivity()).getFab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mListView = (ListView) rootView.findViewById(R.id.detail_list);

        List<Review> samples = new ArrayList<>();
        mAdapter = new ReviewAdapter(getActivity(), samples, this);
        mListView.setAdapter(mAdapter);

        mPhotoHeaderView = new ImageView(getActivity());
        mPhotoHeaderView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.photo_header_height)));
        mListView.addHeaderView(mPhotoHeaderView);

        View headerView = new View(getActivity());
        headerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.card_margin_bottom)));
        mListView.addHeaderView(headerView);

        View footerView = new View(getActivity());
        footerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.card_margin_top)));
        mListView.addFooterView(footerView);

        ((MainActivity) getActivity()).setFabClickListener(this);

        mFirebaseRef = new Firebase("https://fbla-mobile-app.firebaseio.com/photos/" + mPhotoId);
        mFirebaseRef.addValueEventListener(this);

        return rootView;
    }

    @Override
    public void onCardClick(View view, int position) {
        Logger.log("Clicked " + position);
    }

    @Override
    public boolean onCardLongClick(View view, int position) {
        return false;
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.create_review_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Review");
        builder.setView(view);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int stylish = (int) ((RatingBar) view.findViewById(R.id.stylish)).getRating();
                int professional = (int) ((RatingBar) view.findViewById(R.id.professional)).getRating();
                boolean dressCode = ((CheckBox) view.findViewById(R.id.dressCode)).isChecked();
                String comment = ((EditText) view.findViewById(R.id.comment)).getText().toString();
                String owner = ((EditText) view.findViewById(R.id.username)).getText().toString();
                long timestamp = System.currentTimeMillis();
                Review review = new Review(stylish, professional, dressCode, comment, owner, timestamp);
                mFirebaseRef.child("reviews").push().setValue(review.format());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

        if (map != null) {

            mPhotoHeaderView.setImageBitmap(Utils.stringToBitmap((String) map.get("image")));

            Map<String, Map<String, Object>> reviewsMap = (Map<String, Map<String, Object>>) map.get("reviews");
            if (reviewsMap != null) {
                mAdapter.clear();
                List<Review> reviews = new ArrayList<>();
                for (Map.Entry<String, Map<String, Object>> entry : reviewsMap.entrySet()) {
                    String key = entry.getKey();
                    Map<String, Object> item = entry.getValue();
                    int stylish = (int) ((long) item.get("stylish"));
                    int professional = (int) ((long) item.get("professional"));
                    boolean dressCode = (Boolean) item.get("dressCode");
                    String comment = (String) item.get("comment");
                    String owner = (String) item.get("owner");
                    long timestamp = (Long) item.get("timestamp");
                    Review review = new Review(stylish, professional, dressCode, comment, owner, timestamp);
                    reviews.add(review);
                }
                Collections.sort(reviews, new Comparator<Review>() {
                    @Override
                    public int compare(Review a, Review b) {
                        return Long.valueOf(b.timestamp).compareTo(a.timestamp);
                    }
                });
                mAdapter.addAll(reviews);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onCancelled(FirebaseError error) {

    }
}
