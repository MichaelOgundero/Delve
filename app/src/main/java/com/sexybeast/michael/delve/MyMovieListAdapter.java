package com.sexybeast.michael.delve;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sexybeast.michael.delve.model.Example;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyMovieListAdapter extends RecyclerView.Adapter<MyMovieListAdapter.MyViewHolder> {
private List<Movie> mymoviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre, year;
        public ImageView moviePoster;
        public CheckBox checkBox;
        public MyViewHolder( View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            genre = (TextView) itemView.findViewById(R.id.genre);
            year = (TextView) itemView.findViewById(R.id.year);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);

        }
    }

    public MyMovieListAdapter(List<Movie> mymoviesList, Context context) {
        this.mymoviesList = mymoviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_movie_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Movie movie  = mymoviesList.get(position);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<Example> callXXX = movieInterface.getMovieSearch(MovieInterface.API_KEY, movie.getName());
        callXXX.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //movie poster
                String poster = response.body().getResults().get(0).getPosterPath();
                Glide.with(context).load("http://image.tmdb.org/t/p/original"+poster).into(holder.moviePoster);

                //movie name
                holder.title.setText(response.body().getResults().get(0).getTitle());

                //movie genre
                StringBuilder stringBuilder = new StringBuilder();
                boolean appendSeparator = false;
                for(int i=0 ; i<response.body().getResults().get(0).getGenreIds().size();i++){
                    if(appendSeparator)
                        stringBuilder.append(" | ");
                    appendSeparator = true;

                    stringBuilder.append(movieGenre(response.body().getResults().get(0).getGenreIds().get(i)));
                }
                holder.genre.setText(stringBuilder.toString());

                //movie year
                String[] year = response.body().getResults().get(0).getReleaseDate().split("-");
                holder.year.setText("("+  year[0] + ")");

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

        //checkBox
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    holder.title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    holder.title.setPaintFlags(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
       return mymoviesList.size();
    }

    public String movieGenre(int id){
        if(id == 28){
            return "Action";
        }
        if(id == 12){
            return "Adventure";
        }
        if(id == 16){
            return "Animation";
        }
        if(id == 35){
            return "Comedy";
        }
        if(id == 80){
            return "Crime";
        }
        if(id == 99){
            return "Documentary";
        }
        if(id == 18){
            return "Drama";
        }
        if(id == 10751){
            return "Family";
        }
        if(id == 14){
            return "Fantasy";
        }
        if(id == 36){
            return "History";
        }
        if(id == 27){
            return "Horror";
        }
        if(id == 10402){
            return "Music";
        }
        if(id == 9648){
            return "Mystery";
        }
        if(id == 10749){
            return "Romance";
        }
        if(id == 878){
            return "Science Fiction";
        }
        if(id == 10770){
            return "TV Movie";
        }
        if(id == 53){
            return "Thriller";
        }
        if(id == 10752){
            return "War";
        }
        if(id == 37){
            return "Western";
        }
        else{
            return "N/A";
        }
    }


}
