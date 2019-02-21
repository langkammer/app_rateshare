package br.com.rateshare.ui.adapter.listener;


import android.graphics.Bitmap;
import android.widget.RatingBar;

import br.com.rateshare.model.Post;

public interface OnRateChangeListener {

    void onRateChange(Post post, int posicao, RatingBar ratingBar, float rating, boolean fromUser,Bitmap image);


}
