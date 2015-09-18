package com.example.kellychung.popularmovie;


/**
 * Created by kellychung on 9/12/15.
 *
 * fetchMovieDetail fetches the movie reviews & youTube video Keys
 * The url passed as parameter determines what is being fetched
 *
 */
public class fetchMovieDetail extends android.os.AsyncTask<String , Void, String[]> {

    private String LOG_TAG = fetchMovieDetail.class.getName();
    String movieExtraData;

    private String[] getMovieData(String movieDataStr) throws org.json.JSONException {

        final String MOVIE_RESULTS = "results";

        org.json.JSONObject movieJsonObj = new org.json.JSONObject(movieDataStr);
        org.json.JSONArray movieJsonArray = movieJsonObj.getJSONArray(MOVIE_RESULTS);
        String[] videoKeys = new String[movieJsonArray.length()];

        for(int i = 0; i < movieJsonArray.length(); i++) {

            org.json.JSONObject singleJsonObj = movieJsonArray.getJSONObject(i);
            videoKeys[i] = singleJsonObj.getString(movieExtraData);
            android.util.Log.e("movie video key array", videoKeys[i]);
        }

        return videoKeys;

    }


    protected String[] doInBackground(String... params) {

        java.net.HttpURLConnection urlConnection = null;
        java.io.BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieDataStr = null;

        String movieReviewUrl = params[0];
        movieExtraData = params[1];

        //Constructing URL to fetch movie poster
        try {

            java.net.URL url = new java.net.URL(movieReviewUrl);

            //Creating url connection
            urlConnection = (java.net.HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            java.io.InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieDataStr = buffer.toString();

            //Log.v(LOG_TAG, "Movie poster string: " + movieJsonStr);
        } catch (java.io.IOException e) {
            android.util.Log.e(LOG_TAG, "Error ", e);
            return null;


        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final java.io.IOException e) {
                    android.util.Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }


        try {
            return getMovieData(movieDataStr);

        } catch (org.json.JSONException e) {
            android.util.Log.d(LOG_TAG, "Json exception", e);
            e.printStackTrace();
        }
        return null;
    }



}
