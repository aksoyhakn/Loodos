package com.example.loodos.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.loodos.R;
import com.example.loodos.model.Search;
import com.example.loodos.ınterface.ItemClickListenerImpl;

import java.util.ArrayList;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Search> arrayList;
    private ItemClickListenerImpl itemClickListener;

    public MainAdapter(Context mContext, ArrayList<Search> arrayList, ItemClickListenerImpl itemClickListener) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.itemClickListener=itemClickListener;
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

        if(!search.getPoster().equals("N/A")){

            Glide.with(mContext)
                    .load(search.getPoster())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(viewHolder.imageView);

        }else{

            viewHolder.imageView.setImageResource(R.drawable.empty_poster);

        }



        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(viewHolder,i,viewHolder.imageView);
            }
        });


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
            imageView = view.findViewById(R.id.img_movie_poster);

        }
    }


}

