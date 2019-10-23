package com.example.loodos.ınterface;

import com.example.loodos.pojo.Search;

import java.util.ArrayList;

public interface ViewImpl {

    interface MainImpl{

        void showProgress();
        void hideProgress();
        void showSearchList(ArrayList<Search> searchArrayList);

    }



}
