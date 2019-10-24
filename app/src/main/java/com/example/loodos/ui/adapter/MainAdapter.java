package com.example.loodos.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loodos.R;
import com.example.loodos.model.Search;

import java.util.ArrayList;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Search> arrayList;

    public MainAdapter(Context mContext, ArrayList<Search> arrayList) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, viewGroup, false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Search search = arrayList.get(i);

        Glide.with(mContext).load(search.getPoster()).into(viewHolder.imageView);
        viewHolder.title.setText(search.getTitle());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView imageView;

        public ViewHolder(@NonNull View view) {
            super(view);

            title = view.findViewById(R.id.txt_movie_title);
            imageView = view.findViewById(R.id.img_movie);

        }
    }


}

