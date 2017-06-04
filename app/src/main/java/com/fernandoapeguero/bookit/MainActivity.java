package com.fernandoapeguero.bookit;

import android.app.LoaderManager;
import android.app.Notification;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookKeeper>> {

   private static  String searchInfo = "Snow";
    private static final String GOOGLE_BOOK_API = "https://www.googleapis.com/books/v1/volumes?q="+ searchInfo +"&maxResults=10";
    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(R.id.list);

        mAdapter = new BookAdapter(this, new ArrayList<BookKeeper>());

        listView.setAdapter(mAdapter);

        android.app.LoaderManager loadManager = getLoaderManager();

        loadManager.initLoader(BOOK_LOADER_ID, null, this);
        editTheSearch();

    }

    @Override
    public Loader<List<BookKeeper>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, GOOGLE_BOOK_API);
    }

    @Override
    public void onLoadFinished(Loader<List<BookKeeper>> loader, List<BookKeeper> data) {

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<BookKeeper>> loader) {
        mAdapter.clear();

    }

    private void editTheSearch(){
        final EditText searchForIt = (EditText) findViewById(R.id.search_for_it);



        searchForIt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                   searchInfo = searchForIt.getText().toString();
                }
                return false;
            }
        });



    }
}
