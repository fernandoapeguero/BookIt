package com.fernandoapeguero.bookit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nico on 6/1/2017.
 */

public class BookAdapter extends ArrayAdapter<BookKeeper> {

    public final static String LOG_TAG = BookAdapter.class.getName();

    public BookAdapter(Activity context, ArrayList<BookKeeper> books) {
        super(context, 0 , books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        BookKeeper currentPosition = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.main_title);
        if (currentPosition != null) {
            title.setText(currentPosition.getmTitle());
        }

        TextView author = (TextView) convertView.findViewById(R.id.author);
        if (currentPosition != null) {
            author.setText(currentPosition.getmAuthor());
        }

        TextView published = (TextView) convertView.findViewById(R.id.published_date);
        if (currentPosition != null) {
            published.setText(currentPosition.getmPublishedDate());
        }


        return convertView;
    }
}
