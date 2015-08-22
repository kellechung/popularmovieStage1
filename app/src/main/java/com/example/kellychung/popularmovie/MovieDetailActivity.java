package com.example.kellychung.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        //Setting the movie title
        String movieTitle = intent.getStringExtra("original_Title");
        TextView txtViewMovieTitle = (TextView) findViewById(R.id.txtViewMovieTitle);
        txtViewMovieTitle.setText(movieTitle);

        //Setting the movie overview
        String movieOverview = intent.getStringExtra("overview");
        TextView txtViewOverview = (TextView) findViewById(R.id.txtViewOverview);
        txtViewOverview.setText(movieOverview);

        //Getting the movie url and loading it in image view
        String posterUrl = intent.getStringExtra("posterUrl");
        //android.util.Log.e("Poster url", posterUrl);
        ImageView imageViewMovie = (ImageView) findViewById(R.id.imageViewMovie);

        com.squareup.picasso.Picasso.with(getApplicationContext())
                .load(posterUrl)
                .into(imageViewMovie);

        //Setting the vote average
        String voteAvg = intent.getStringExtra("vote_average");
        TextView txtViewVoteAvg = (TextView) findViewById(R.id.txtViewVoteAvg);
        txtViewVoteAvg.setText(voteAvg + "/10");

        //Setting the release date
        String release_date = intent.getStringExtra("releaseDate");
        TextView txtViewReleaseDate = (TextView) findViewById(R.id.txtViewReleaseDate);

        if (release_date.length() != 0 && release_date.length() >= 4) txtViewReleaseDate.setText(release_date.substring(0, 4));


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
}
