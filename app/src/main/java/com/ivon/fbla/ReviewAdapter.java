package com.ivon.fbla;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Owner on 2/18/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    private List<Review> mList;
    private OnCardClickListener mCardClickListener = null;

    public ReviewAdapter(Context context, List<Review> list, OnCardClickListener listener) {
        super(context, android.R.layout.simple_list_item_1, list);
        mList = list;
        mCardClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Review review = mList.get(position);

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_adapter_item, null);
        }

        CardView cardView = (CardView) view.findViewById(R.id.review_card);
        TextView ownerText = (TextView) view.findViewById(R.id.owner_text);
        RatingBar stylish = (RatingBar) view.findViewById(R.id.stylish);
        RatingBar professional = (RatingBar) view.findViewById(R.id.professional);
        CardView dressCode = (CardView) view.findViewById(R.id.dressCode);
        TextView dressCodeText = (TextView) view.findViewById(R.id.dressCodeText);
        TextView comments = (TextView) view.findViewById(R.id.comments);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCardClickListener != null) {
                    mCardClickListener.onCardClick(view, position);
                }
            }
        });
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return mCardClickListener != null && mCardClickListener.onCardLongClick(view, position);
            }
        });

        ownerText.setText(review.owner);
        Logger.log("" + review.stylish);
        stylish.setRating(review.stylish);
        professional.setRating(review.professional);
        dressCode.setCardBackgroundColor(review.dressCode ? Utils.getThemeAccentColor(getContext()) : 0xffdbdbdb);
        dressCodeText.setText(review.dressCode ? "yes" : "no");
        comments.setText(review.comment);

        return view;
    }
}
