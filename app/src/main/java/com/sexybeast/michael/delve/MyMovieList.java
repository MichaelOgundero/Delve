package com.sexybeast.michael.delve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
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

        //floating action botton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        //setting up realm
        Realm.init(this);
        config = new RealmConfiguration.Builder()
                .name("movieList.realm")
                .build();
        realm = Realm.getInstance(config);

        //retrieve
        RealmHelper helper = new RealmHelper(realm);
        mymovielist = helper.retrieve();

        myMovieListAdapter = new MyMovieListAdapter(mymovielist, this);
        recyclerView.setAdapter(myMovieListAdapter);
        context = this;

        //moviecount
        movieCount = (TextView) findViewById(R.id.movieCount);
        movieCount.setText("("+mymovielist.size()+")");


 }

    @Override
    protected void onPause() {
        super.onPause();

        Realm.getInstance(config).close();
    }

    public void openDialog(){
        CustomDialogClass customDialogClass = new CustomDialogClass(this);
        customDialogClass.show();

    }

    public static void addMovie(TextView movieName){
        Realm.getInstance(config).beginTransaction();
        Movie movie = Realm.getInstance(config).createObject(Movie.class, UUID.randomUUID().toString());
        movie.setName(movieName.getText().toString());
//        movie.setGenre(movieGenre.getSelectedItem().toString());

        Realm.getInstance(config).commitTransaction();
        mymovielist.add(movie);

        //save
        RealmHelper helper = new RealmHelper(realm);
        helper.save(movie);

        //refresh
        mymovielist = helper.retrieve();
        myMovieListAdapter = new MyMovieListAdapter(mymovielist, context);
        recyclerView.setAdapter(MyMovieList.myMovieListAdapter);
        myMovieListAdapter.notifyDataSetChanged();

        Toast.makeText(MyMovieList.context, movie.getName() + " Added!", Toast.LENGTH_SHORT).show();

        movieCount.setText("("+mymovielist.size()+")");
    }


    public void swipeGesturetoDelete(RecyclerView rv){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(MyMovieList.this, mymovielist.get(position).getName() + " Deleted!", Toast.LENGTH_SHORT).show();
                Realm.getInstance(config).beginTransaction();
                mymovielist.get(position).deleteFromRealm();
                Realm.getInstance(config).commitTransaction();
                mymovielist.remove(position);
                myMovieListAdapter.notifyDataSetChanged();
                movieCount.setText("("+mymovielist.size()+")");

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                final ColorDrawable background = new ColorDrawable(Color.RED);
                background.setBounds(0, itemView.getTop(), (int) (itemView.getLeft()+dX), itemView.getBottom());
                background.draw(c);

            }
        };
        //attaching swipe gesture to recyclerview
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper((simpleItemTouchCallBack));
        itemTouchHelper.attachToRecyclerView(rv);
    }


}
