package com.example.kellychung.popularmovie;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;



public class MovieDetailActivity extends AppCompatActivity implements OnItemClickListener{

    String[] movieVideoKeys;
    String[] movieTrailer;
    TextView txtViewReviewTitle, txtViewMovieTitle;
    Button btnFavorite;
    java.util.ArrayList<String> mReviewList;
    java.util.ArrayList<String> mVideoKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
          txtViewReviewTitle = (TextView) findViewById(R.id.txtViewReviewTitle);
          txtViewMovieTitle = (TextView) findViewById(R.id.txtViewMovieTitle);
          btnFavorite = (Button) findViewById(R.id.btnFavorite);
          btnFavorite.setOnClickListener(btnClickListener);


    }

        OnClickListener btnClickListener = new OnClickListener() {
       @Override
       public void onClick(android.view.View v) {

           btnFavorite.setText("Favorite Movie");
           Resources resources = getResources();
           int orangeColor = resources.getColor(R.color.orange);
           btnFavorite.setBackgroundColor(orangeColor);

       }
   };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        //Setting the movie title
        String movieTitle = intent.getStringExtra("original_Title");
        android.widget.TextView txtViewMovieTitle = (android.widget.TextView) findViewById(R.id.txtViewMovieTitle);
        txtViewMovieTitle.setText(movieTitle);

        //Setting the movie overview
        String movieOverview = intent.getStringExtra("overview");
        android.widget.TextView txtViewOverview = (android.widget.TextView) findViewById(R.id.txtViewOverview);
        txtViewOverview.setText(movieOverview);

        //Getting the movie url and loading it in image view
        String posterUrl = intent.getStringExtra("posterUrl");
        //android.util.Log.e("Poster url", posterUrl);
        android.widget.ImageView imageViewMovie = (android.widget.ImageView) findViewById(R.id.imageViewMovie);

        com.squareup.picasso.Picasso.with(getApplicationContext())
                .load(posterUrl)
                .fit()
                .into(imageViewMovie);

        //Setting the vote average
        String voteAvg = intent.getStringExtra("vote_average");
        android.widget.TextView txtViewVoteAvg = (android.widget.TextView) findViewById(R.id.txtViewVoteAvg);
        txtViewVoteAvg.setText(voteAvg + "/10");

        //Setting the release date
        String release_date = intent.getStringExtra("releaseDate");
        android.widget.TextView txtViewReleaseDate = (android.widget.TextView) findViewById(R.id.txtViewReleaseDate);

        if (release_date != null && release_date.length() >= 4) txtViewReleaseDate.setText(release_date.substring(0, 4));

        //Getting the movieVideo

        movieVideoKeys = intent.getStringArrayExtra("movieVideoIds");

        String[] movieReviews = intent.getStringArrayExtra("movieReviews");

        mReviewList = new java.util.ArrayList<> (java.util.Arrays.asList(movieReviews));
        mVideoKeys = new java.util.ArrayList<> (java.util.Arrays.asList(movieVideoKeys));


            movieTrailer = new String[mVideoKeys.size()];
            android.widget.ListView listViewVideo = (android.widget.ListView)findViewById(R.id.list_movie_video);
            for(int i = 0; i < mVideoKeys.size(); i++) {
                movieTrailer[i] = "Watch Trailer " + (i+1);

            }


            if (mVideoKeys.size() > 0) {

                android.widget.ArrayAdapter videoListAdapter = new android.widget.ArrayAdapter(getApplicationContext(), R.layout.list_movie_video, R.id.txtViewMovieLink, movieTrailer);

                listViewVideo.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();
                listViewVideo.setOnItemClickListener(this);}

            else {txtViewMovieTitle.setText("");}





        android.widget.ListView listViewReview = (android.widget.ListView) findViewById(R.id.list_movie_review);

        //Creating an array so that each movie gets a matching trailer number. Trailer num starts with 1



        if (mReviewList.size()> 0) {
            android.widget.ArrayAdapter reviewListAdapter = new android.widget.ArrayAdapter(getApplicationContext(), R.layout.list_movie_review, R.id.txtViewMovieReview, movieReviews);
            listViewReview.setAdapter(reviewListAdapter);
            reviewListAdapter.notifyDataSetChanged();}
        else {txtViewReviewTitle.setText("");}
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

        String movieUrl = Utility.buildYouTubeLink(this, mVideoKeys.get(position));
        android.net.Uri uri =  android.net.Uri.parse(movieUrl);
        playMedia(uri);


    }

    //Code snippet from android developer reference
    public void playMedia(android.net.Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
