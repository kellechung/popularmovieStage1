package com.example.kellychung.popularmovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnItemClickListener {

    movie[] movieArray;
    ImageAdapter imageAdapter;
    ArrayList<movie> movieArrayList;


    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieView = (GridView) rootView
                .findViewById(R.id.list_movie_item);


        imageAdapter = new ImageAdapter(getActivity(), movieArrayList);
        movieView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
        Log.e("On Create View", "new instance");
        movieView.setOnItemClickListener(this);
        setHasOptionsMenu(true);


        return rootView;


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        //Movie to parcel
        if (movieArrayList != null) {
            outState.putParcelableArrayList("movieKey", movieArrayList);
        }
        super.onSaveInstanceState(outState);


    }


    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey("movieKey")) {

            //Update movie if no parcel is found
            movieArrayList = new ArrayList<movie>();
            updateMovie();

            Log.e("On Create", "new instance");

        } else {
            //Retrieving movie from parcel
            movieArrayList = savedInstanceState.getParcelableArrayList("movieKey");
            Log.e("On Create", "Use parcelable");

        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("onResume", "Call to onResume");

        //Adding a listener to onResume so data is fetched where preference is updated
        SharedPreferences sortPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPrefs.registerOnSharedPreferenceChangeListener(listener);
        super.onResume();
    }

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(
                SharedPreferences prefs, String key) {
            updateMovie();
        }
    };


    @Override
    public void onDestroy() {
        Log.e("On Destroy:", "On destroyed invoked");

        //Unregistering preference listener on destroy
        SharedPreferences sortPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPrefs.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }



    public void updateMovie() {

        SharedPreferences sortPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sortPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.most_popular));
        Log.e("Call to movie update ", " fetching data");
        Uri uri;
        String url;

        if (sortOrder == getString(R.string.highest_rated)) {

            uri = Uri.parse(getString(R.string.movie_Base_Url)).buildUpon()
                    .appendQueryParameter("sort_by", "vote_count.desc")
                    .build();


        } else {
            uri = Uri.parse(getString(R.string.movie_Base_Url)).buildUpon()
                    .appendQueryParameter("sort_by", "popularity.desc")
                    .build();
        }

        url = uri.toString() + "&api_key=" + getString(R.string.apiKey);
        Log.e("uri builder ", url);


        if (isNetworkAvailable()) {
            Log.e("Network info", "Network connected");

            fetchMoviePoster posterTask = new fetchMoviePoster();
            posterTask.execute(url);
        }
    }


    //Based on a stackoverflow snippet & unused for now
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("original_Title", movieArrayList.get(position).original_title);
        intent.putExtra("posterUrl", movieArrayList.get(position).posterUrl);
        intent.putExtra("releaseDate", movieArrayList.get(position).release_Date);
        intent.putExtra("overview", movieArrayList.get(position).overview);
        intent.putExtra("vote_average", movieArrayList.get(position).vote_average);
        startActivity(intent);

    }


    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_movie_poster, menu);
    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {

            updateMovie();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public class fetchMoviePoster extends AsyncTask<String, Void, movie[]> {

        private final String LOG_TAG = fetchMoviePoster.class.getSimpleName();

        // This method takes the JSON string returned by movieDB and converts it into an object hierarchy
        //Object hierarchy makes it easy to process the data

        private movie[] getMovieData(String movieJsonStr) throws JSONException {

            final String POSTER = "poster_path";
            final String MOVIE_PAGE = "results";
            final String OVERVIEW = "overview";
            final String ORIGINAL_TITLE = "original_title";
            final String VOTE_AVERAGE = "vote_average";
            final String RELEASE_DATE = "release_date";


            JSONObject movieJson = new JSONObject(movieJsonStr);

            //Storing movie info in array
            JSONArray movieInfoArray = movieJson.getJSONArray(MOVIE_PAGE);


            movieArray = new movie[movieInfoArray.length()];


            for (int i = 0; i < movieInfoArray.length(); i++) {

                JSONObject singleJsonMovieObj = movieInfoArray.getJSONObject(i);

                String original_title = singleJsonMovieObj.getString(ORIGINAL_TITLE);
                String vote_average = singleJsonMovieObj.getString(VOTE_AVERAGE);
                String overview = singleJsonMovieObj.getString(OVERVIEW);
                String release_Date = singleJsonMovieObj.getString(RELEASE_DATE);
                String posterUrl = getString(R.string.image_Base_Url) + singleJsonMovieObj.getString(POSTER);

                movie movieObj = new movie(original_title, posterUrl, overview, release_Date, vote_average);
                movieArray[i] = movieObj;

            }

            return movieArray;
        }


        protected movie[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String movieUrl = params[0];

            //Constructing URL to fetch movie poster
            try {

                URL url = new URL(movieUrl);

                //Creating url connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                java.io.InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieJsonStr = buffer.toString();

                //Log.v(LOG_TAG, "Movie poster string: " + movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;


            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getMovieData(movieJsonStr);

            } catch (JSONException e) {
                Log.d(LOG_TAG, "Json exception", e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(movie[] movieResult) {


            if (movieResult != null) {
                imageAdapter.clear();


                for (int i = 0; i < movieResult.length; i++) {
                    imageAdapter.add(movieResult[i]);

                }


            }

        }


    }

}

