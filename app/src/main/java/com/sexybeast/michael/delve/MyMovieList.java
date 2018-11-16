package com.sexybeast.michael.delve;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyMovieList extends AppCompatActivity {
    private static List<Movie> mymovielist = new ArrayList<>();
    private RecyclerView recyclerView;
    private static MyMovieListAdapter myMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmylist);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_fav);
        myMovieListAdapter = new MyMovieListAdapter(mymovielist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(myMovieListAdapter);
        swipeGesturetoDelete(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });



        initialize();
    }

    private void initialize() {
        Movie a = new Movie("Aquaman", "ComicBook");
        mymovielist.add(a);

        myMovieListAdapter.notifyDataSetChanged();
    }

    public void openDialog(){
        CustomDialogClass customDialogClass = new CustomDialogClass(this);
        customDialogClass.show();

    }

    public static void addMovie(TextView movieName, TextView movieGenre){
        Movie movie = new Movie(movieName.getText().toString(), movieGenre.getText().toString());
        mymovielist.add(movie);
        myMovieListAdapter.notifyDataSetChanged();
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
                Toast.makeText(MyMovieList.this, mymovielist.get(position).getName() + " Deleted!", Toast.LENGTH_LONG).show();
                mymovielist.remove(position);
                myMovieListAdapter.notifyDataSetChanged();


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
