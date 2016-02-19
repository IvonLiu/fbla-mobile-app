package com.ivon.fbla;

import android.view.View;

/**
 * Created by Owner on 2/18/2016.
 */
public interface OnCardClickListener {
    void onCardClick(View view, int position);
    boolean onCardLongClick(View view, int position);
}