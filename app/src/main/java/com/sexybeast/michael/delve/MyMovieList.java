package com.sexybeast.michael.delve;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyMovieList extends AppCompatActivity {
    private static List<Movie> mymovielist = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static MyMovieListAdapter myMovieListAdapter;
    private static Realm realm;
    private static RealmConfiguration config;
    private static Context context;
    private static TextView movieCount;
    private TextView listStatus;
    private ImageView listStatusIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmylist);


        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_fav);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        swipeGesturetoDelete(recyclerView);

        //setting up realm
        Realm.init(getApplicationContext());

        config = new RealmConfiguration.Builder()
                .name("mymovieList.realm")
                .build();

        realm = Realm.getInstance(config);

        //retrieve
        RealmHelper helper = new RealmHelper(realm);
        mymovielist = helper.retrieve();


        if (mymovielist.isEmpty() == true) {
            listStatusIcon = (ImageView) findViewById(R.id.listStatusIcon);
            listStatusIcon.setImageResource(R.drawable.emptylist);

            listStatus = (TextView) findViewById(R.id.listStatus);
            listStatus.setText("Your watchlist is empty hit back to add a movie");
        } else if (mymovielist.isEmpty() == false) {
           ImageView listStatusIcon = (ImageView) findViewById(R.id.listStatusIcon);
            listStatusIcon.setAlpha(0);

            listStatus = (TextView) findViewById(R.id.listStatus);
            listStatus.setText("");
        }


        myMovieListAdapter = new MyMovieListAdapter(mymovielist, getApplicationContext());
        recyclerView.setAdapter(myMovieListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = mymovielist.get(position);
                switchActivity(movie);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        context = this;

//        //moviecount
        movieCount = (TextView) findViewById(R.id.movieCount);
        movieCount.setText("(" + mymovielist.size() + ")");

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Realm.getInstance(config).close();
    }

    public void switchActivity(Movie movie) {
        Intent intent = new Intent(this, MovieInfoLayoutActivity.class);
        intent.putExtra("Movie id", movie.getTmdbID());
        startActivity(intent);
    }


    public static void addMovie(String movieID) {

        Realm.getInstance(config).beginTransaction();
        Movie movie = Realm.getInstance(MyMovieList.config).createObject(Movie.class, UUID.randomUUID().toString());
        movie.setTmdbID(movieID);
        Realm.getInstance(MyMovieList.config).commitTransaction();
        MyMovieList.mymovielist.add(movie);


        // save
        RealmHelper helper = new RealmHelper(MyMovieList.realm);
        helper.save(movie);

        //refresh
        MyMovieList.mymovielist = helper.retrieve();
        MyMovieList.myMovieListAdapter = new MyMovieListAdapter(MyMovieList.mymovielist, MyMovieList.context);
        MyMovieList.recyclerView.setAdapter(MyMovieList.myMovieListAdapter);
        MyMovieList.myMovieListAdapter.notifyDataSetChanged();


        movieCount.setText("(" + mymovielist.size() + ")");
    }


    public void swipeGesturetoDelete(RecyclerView rv) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                //Toast.makeText(MyMovieList.this, mymovielist.get(position).getName() + "Deleted",Toast.LENGTH_LONG).show();
                Realm.getInstance(config).beginTransaction();
                mymovielist.get(position).deleteFromRealm();
                Realm.getInstance(config).commitTransaction();
                mymovielist.remove(position);
                myMovieListAdapter.notifyDataSetChanged();
                movieCount.setText("(" + mymovielist.size() + ")");

                if (mymovielist.isEmpty() == true) {
                    listStatusIcon = (ImageView) findViewById(R.id.listStatusIcon);
                    listStatusIcon.setImageResource(R.drawable.emptylist);
                    listStatusIcon.setAlpha(225);

                    listStatus = (TextView) findViewById(R.id.listStatus);
                    listStatus.setText("Your watchlist is empty hit back to add a movie");

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                final ColorDrawable background = new ColorDrawable(Color.RED);
                background.setBounds(0, itemView.getTop(), (int) (itemView.getLeft() + dX), itemView.getBottom());
                background.draw(c);

            }
        };
        //attaching swipe gesture to recyclerview
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((simpleItemTouchCallBack));
        itemTouchHelper.attachToRecyclerView(rv);
    }


}
