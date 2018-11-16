package com.sexybeast.michael.delve;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyMovieListAdapter extends RecyclerView.Adapter<MyMovieListAdapter.MyViewHolder> {
private List<Movie> mymoviesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre;
        public MyViewHolder( View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            genre = (TextView) itemView.findViewById(R.id.genre);

        }
    }

    public MyMovieListAdapter(List<Movie> mymoviesList) {
        this.mymoviesList = mymoviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_movie_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie  = mymoviesList.get(position);
        holder.title.setText(movie.getName());
        holder.genre.setText(movie.getGenre());
    }

    @Override
    public int getItemCount() {
       return mymoviesList.size();
    }


}
