package com.fernandoapeguero.bookit;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by nico on 6/2/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<BookKeeper>> {

    private static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookKeeper> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        return BookUtils.fetchBookData(mUrl);
    }
}
