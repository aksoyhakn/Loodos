package com.example.loodos.ınterface;

public interface PresenterImpl {

    interface MainPresenterImpl{
        void searchList(String query);
    }

    interface DetailsPresenterImpl{
        void showMovieDetails(String query);
    }
}
