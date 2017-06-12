package com.fernandoapeguero.bookit;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookKeeper>> {

    private static String searchInfo = "harry potter";
    private String GOOGLE_BOOK_API = "https://www.googleapis.com/books/v1/volumes?q=" + searchInfo + "&maxResults=10";
    private static final int BOOK_LOADER_ID = 1;

    private static final String LOG_TAG = MainActivity.class.getName();

    private TextView emptyStateView;
    private ProgressBar progressBar;

    private LoaderManager loadManager;

    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<BookKeeper>());

        listView.setAdapter(mAdapter);

        loadManager = getLoaderManager();

        loadManager.initLoader(BOOK_LOADER_ID, null, this);

        emptyStateView = (TextView) findViewById(R.id.empty_state);
        listView.setEmptyView(emptyStateView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        editFieldSearch();

    }

    @Override
    public Loader<List<BookKeeper>> onCreateLoader(int id, Bundle args) {

        return new BookLoader(this, GOOGLE_BOOK_API);
    }

    @Override
    public void onLoadFinished(Loader<List<BookKeeper>> loader, List<BookKeeper> data) {
        progressBar.setVisibility(View.GONE);
        ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            emptyStateView.setText("No Internet Connection Please Check");
        } else {
            emptyStateView.setText("No Books Found");
        }

        if (data != null && !data.isEmpty()) {
            mAdapter.clear();
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<BookKeeper>> loader) {
        mAdapter.clear();

    }

    private void editFieldSearch() {

        final EditText searchButton = (EditText) findViewById(R.id.search_for_it);

        searchButton.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchInfo = searchButton.getText().toString().trim();

                    GOOGLE_BOOK_API = "https://www.googleapis.com/books/v1/volumes?q=" + searchInfo + "&maxResults=10";

                    loadManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    progressBar.setVisibility(View.VISIBLE);
                    searchButton.clearFocus();

                }
                return false;
            }
        });

    }

}