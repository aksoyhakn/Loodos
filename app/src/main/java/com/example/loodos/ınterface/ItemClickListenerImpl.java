package com.example.loodos.ınterface;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemClickListenerImpl {

    void onItemClick(RecyclerView.ViewHolder holder, int position, ImageView imageView);
}
