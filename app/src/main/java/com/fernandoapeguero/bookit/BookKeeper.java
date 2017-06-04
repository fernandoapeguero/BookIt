package com.fernandoapeguero.bookit;

/**
 * Created by nico on 6/1/2017.
 */

public class BookKeeper {

    private String mTitle;
    private String mAuthor;
    private String mPublishedDate ;

    public BookKeeper(String title , String author , String published){
        mTitle = title;
        mAuthor = author;
        mPublishedDate = published;

    }

    public String getmTitle(){
        return mTitle;
    }
    public String getmAuthor(){
        return mAuthor;
    }

    public String getmPublishedDate(){
        return mPublishedDate;
    }
}
