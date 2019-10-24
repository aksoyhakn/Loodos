package com.example.loodos.ınterface;

import com.example.loodos.model.Search;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ViewImpl {

    interface MainImpl{

        void showGIF();
        void hideGIF();
        void emptyGIF();
        void onError();
        void showSearchList(ArrayList<Search> searchArrayList);

    }

    interface DetailsImpl{

        void showMovieDetails(JSONObject movieObject);
        void onError();

    }



}
