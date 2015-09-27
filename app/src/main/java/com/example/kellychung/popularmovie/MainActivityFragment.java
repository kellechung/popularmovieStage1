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
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

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

        // Log.e("On Create View", "new instance");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieView = (GridView) rootView
                .findViewById(R.id.list_movie_item);


        imageAdapter = new ImageAdapter(getActivity(), movieArrayList);
        movieView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

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

            //Log.e("On Create", "new instance");

        } else {
            //Retrieving movie from parcel
            movieArrayList = savedInstanceState.getParcelableArrayList("movieKey");
            //Log.e("On Create", "Use parcelable");

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
        // Log.e("On Destroy:", "On destroyed invoked");

        //Unregistering preference listener on destroy
        SharedPreferences sortPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPrefs.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }


    public void updateMovie() {

        SharedPreferences sortPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sortPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.most_popular));
        //Log.e("Call to movie update ", " fetching data");
        Uri uri;
        String url;

        if (sortOrder.equalsIgnoreCase(getString(R.string.highest_rated))) {

            uri = Uri.parse(getString(R.string.movie_Base_Url)).buildUpon()
                    .appendQueryParameter("sort_by", "vote_count.desc")
                    .build();


        } else {
            uri = Uri.parse(getString(R.string.movie_Base_Url)).buildUpon()
                    .appendQueryParameter("sort_by", "popularity.desc")
                    .build();
        }

        url = uri.toString() + "&api_key=" + getString(R.string.apiKey);
        //Log.e("uri builder ", url);


        if (isNetworkAvailable()) {


            fetchMoviePoster posterTask = new fetchMoviePoster();
            posterTask.execute(url);


        }
    }


    //Based on a stackoverflow snippet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        movie selectedMovie = movieArrayList.get(position);
        intent.putExtra("original_Title", selectedMovie.original_title);
        intent.putExtra("posterUrl", selectedMovie.posterUrl);
        intent.putExtra("releaseDate", selectedMovie.release_Date);
        intent.putExtra("overview", selectedMovie.overview);
        intent.putExtra("vote_average", selectedMovie.vote_average);
        intent.putExtra("movieVideoIds", selectedMovie.movieVideoKeys);
        intent.putExtra("movieReviews", selectedMovie.movieReviews);
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
            final String ID = "id";
            final String MOVIE_PAGE = "results";
            final String OVERVIEW = "overview";
            final String ORIGINAL_TITLE = "original_title";
            final String VOTE_AVERAGE = "vote_average";
            final String RELEASE_DATE = "release_date";


            JSONObject movieJson = new JSONObject(movieJsonStr);

            //Storing movie info in array
            JSONArray movieInfoArray = movieJson.getJSONArray(MOVIE_PAGE);


            if ( movieInfoArray != null ) {
            movieArray = new movie[movieInfoArray.length()];



            for (int i = 0; i < movieInfoArray.length(); i++) {

                JSONObject singleJsonMovieObj = movieInfoArray.getJSONObject(i);

                String movieID = singleJsonMovieObj.getString(ID);
                String original_title = singleJsonMovieObj.getString(ORIGINAL_TITLE);
                String vote_average = singleJsonMovieObj.getString(VOTE_AVERAGE);
                String overview = singleJsonMovieObj.getString(OVERVIEW);
                String release_Date = singleJsonMovieObj.getString(RELEASE_DATE);
                String posterUrl = getString(R.string.image_Base_Url) + singleJsonMovieObj.getString(POSTER);
                movie movieObj = new movie(movieID, original_title, posterUrl, overview, release_Date, vote_average,null,null);
                movieArray[i] = movieObj;

            }}

            return movieArray;
        }


        private String[] getMovieData(String movieDataString, String path)  throws org.json.JSONException {

            final String MOVIE_RESULTS = "results";



            String[] movieData = null;

            if (movieDataString != null) {
            org.json.JSONObject movieJsonObj = new org.json.JSONObject(movieDataString);
                org.json.JSONArray  movieJsonArray = movieJsonObj.getJSONArray(MOVIE_RESULTS);

            //Storing movie data in an array



            if (movieJsonArray != null || movieJsonArray != org.json.JSONObject.NULL){
            movieData= new String[movieJsonArray.length()];}


            for(int i=0; i<movieJsonArray.length();i++) {

                JSONObject singleJsonMovieObj = movieJsonArray.getJSONObject(i);
                movieData[i]= singleJsonMovieObj.getString(path);
            }}



        return movieData;
        }

        protected movie[] doInBackground(String... params) {


            String movieUrl = params[0];
            String movieJsonStr = openConnection(movieUrl);
            movie[] movieArray = null;



            try {
                movieArray = getMovieData(movieJsonStr);

            } catch (JSONException e) {
                Log.d(LOG_TAG, "Json exception", e);
                e.printStackTrace();
            }


            for( movie m : movieArray) {

                String videoUrl = Utility.buildMovieVideoUrl(getActivity(), m.movieID);

                String videoDataStr = openConnection(videoUrl);
                String[] movieVideoIds = null;
                String[] movieReviews = null;

                String reviewUrl = Utility.buildMovieReviewUrl(getActivity(), m.movieID);
                String reviewDataStr = openConnection(reviewUrl);

                try

                {
                    movieVideoIds = getMovieData(videoDataStr, "key");
                    m.setMovieVideoKey(movieVideoIds);

                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }


                try {

                    movieReviews = getMovieData(reviewDataStr, "content");
                    m.setMovieReviews(movieReviews);
                    //Log.e("Movie review", movieReviews.length + "");
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

            return movieArray;
            }









        //Open url connection to fetch data
        protected String openConnection(String movieUrl) {
            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;



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
                if (inputStream == null ) {
                return "";

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));



                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //return null;
                }

                movieJsonStr = buffer.toString();



            }

            catch(java.net.MalformedURLException e) {Log.e(LOG_TAG, e.getMessage(),e);}

            catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
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
            return movieJsonStr;
        }

        @Override
        protected void onPostExecute(movie[] movieResult) {


            if (movieResult != null) {
                imageAdapter.clear();


                for(movie result: movieResult) {
                    imageAdapter.add(result);



                }


            }

        }


    }

}