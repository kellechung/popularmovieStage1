package com.example.kellychung.popularmovie;

import android.content.Context;
import android.net.Uri;

/**
 * Created by kellychung on 9/12/15.
 */
public class Utility {

    public static String buildYouTubeLink(Context context, String vidMovieKey) {

        return context.getResources().getString(R.string.youTubeBaseUrl) + vidMovieKey;
    }

    public static String buildMovieVideoUrl(Context context, String movieId) {

        String movieBaseUrl = context.getResources().getString(R.string.movieExtraBaseUrl);

        Uri uri;
        uri = Uri.parse(movieBaseUrl).buildUpon()
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", context.getResources().getString(R.string.apiKey)).build();

        return
        uri.toString();
    }

    public static String buildMovieReviewUrl(Context context, String movieId) {

        String movieBaseUrl = context.getResources().getString(R.string.movieExtraBaseUrl);

        Uri uri;
        uri = Uri.parse(movieBaseUrl).buildUpon()
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", context.getResources().getString(R.string.apiKey)).build();

        return uri.toString();
    }

    public static String buildPosterImageUrl(Context context, String posterId) {
         return context.getResources().getString(R.string.image_Base_Url) + posterId;

    }

    public static String[] getMovieVideoIds(Context context, String movieId) {



        String movieVidUrl = buildMovieVideoUrl(context, movieId );
        String movieVideoIds[] = null;

        fetchMovieDetail fetchMovieVidKey = new fetchMovieDetail();
        fetchMovieVidKey.execute(movieVidUrl, "key");

        try

        {
             movieVideoIds = fetchMovieVidKey.get();


        } catch (
                InterruptedException e
                )

        {
            e.printStackTrace();

        } catch (
                java.util.concurrent.ExecutionException e
                )

        {
            e.printStackTrace();

        }

        return movieVideoIds;
    }

    public static String[] getMovieReviews(Context context, String movieId) {

        String movieReviews[] = null;
        String movieReviewUrl = buildMovieReviewUrl(context, movieId);

        fetchMovieDetail fetchMovieVidKey = new fetchMovieDetail();
        fetchMovieVidKey.execute(movieReviewUrl, "content");

        try

        {
             movieReviews = fetchMovieVidKey.get();


        } catch (
                InterruptedException e
                )

        {
            e.printStackTrace();

        } catch (
                java.util.concurrent.ExecutionException e
                )

        {
            e.printStackTrace();

        }
    return movieReviews;

    }



}
