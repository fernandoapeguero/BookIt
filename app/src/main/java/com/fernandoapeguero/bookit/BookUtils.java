package com.fernandoapeguero.bookit;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nico on 6/1/2017.
 */

public class BookUtils {

    private static final String LOG_TAG = BookUtils.class.getName();

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "malformer error = " + exception);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonresponse = "";

        if (url == null) {
            return jsonresponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000/*miliseconds */);
            urlConnection.setConnectTimeout(15000 /* miliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonresponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "error code reponde " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        }

        return jsonresponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<BookKeeper> extractFeatureFromJson(String jsonBooks) {

        if (TextUtils.isEmpty(jsonBooks)) {
            return null;
        }
        List<BookKeeper> books = new ArrayList<>();

        try {
            JSONObject baseResponse = new JSONObject(jsonBooks);
            JSONArray items = baseResponse.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject currentBook = items.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    String author = authors.getString(0);
                    String published = volumeInfo.getString("publishedDate");

                    BookKeeper book = new BookKeeper(title, author, published);

                    books.add(book);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static List<BookKeeper> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the Http request", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }
}
