package xyz.hsuyeemon.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.hsuyeemon.movies.PopularMovieApp;
import xyz.hsuyeemon.movies.R;
import xyz.hsuyeemon.movies.adapters.MovieAdapter;
import xyz.hsuyeemon.movies.data.models.MovieModel;
import xyz.hsuyeemon.movies.delegates.MovieItemActionDelegates;
import xyz.hsuyeemon.movies.events.LoadedMoviesEvent;

public class MainActivity extends AppCompatActivity
        implements MovieItemActionDelegates {

    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private MovieAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        moviesAdapter = new MovieAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(linearLayoutManager);
        rvMovies.setAdapter(moviesAdapter);

        MovieModel.getObjInstance().loadNews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onTabFab(View view) {
        Snackbar.make(view, "You tap Search", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    @Override
    public void onTapMovieItem() {
        Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTapMovieOverview() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMovieLoaded(LoadedMoviesEvent event) {
        Log.d(PopularMovieApp.LOG_TAG, "onMovieLoad" + event.getMovieList());
        moviesAdapter.setMovies(event.getMovieList());
    }
}
