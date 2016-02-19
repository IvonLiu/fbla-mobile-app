package com.ivon.fbla;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Owner on 2/18/2016.
 */
public class PhotoAdapter extends ArrayAdapter<Photo> {

    private List<Photo> mList;
    private OnCardClickListener mCardClickListener = null;

    public PhotoAdapter(Context context, List<Photo> list, OnCardClickListener listener) {
        super(context, android.R.layout.simple_list_item_1, list);
        mList = list;
        mCardClickListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Photo photo = mList.get(position);

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.photo_adapter_item, null);
        }

        CardView cardView = (CardView) view.findViewById(R.id.photo_card);
        ImageView photoImage = (ImageView) view.findViewById(R.id.photo_image);
        TextView ownerText = (TextView) view.findViewById(R.id.owner_text);
        TextView dateText = (TextView) view.findViewById(R.id.date_text);

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

        photoImage.setImageBitmap(Utils.stringToBitmap(photo.image));
        ownerText.setText(photo.owner);
        dateText.setText(format(photo.timestamp));

        return view;
    }

    private String format(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return sdf.format(new Date(timestamp));
    }
}
