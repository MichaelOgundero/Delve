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
import com.sexybeast.michael.delve.Model_ID.Example_ID;
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
        public MyViewHolder( View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            genre = (TextView) itemView.findViewById(R.id.genre);
            year = (TextView) itemView.findViewById(R.id.year);
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
        Call<Example_ID> callXXX = movieInterface.getMovieDetails("movie/"+movie.getTmdbID()+"?api_key=623eeab48528051330ddc3ca73959483");
        callXXX.enqueue(new Callback<Example_ID>() {
            @Override
            public void onResponse(Call<Example_ID> call, Response<Example_ID> response) {
                if (response.isSuccessful()) {
                    //movie poster
                    String poster = response.body().getPosterPath();
                    Glide.with(context).load("http://image.tmdb.org/t/p/original" + poster).into(holder.moviePoster);

                    //movie name
                    holder.title.setText(response.body().getTitle());

                    //movie genre
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean appendSeparator = false;
                    for (int i = 0; i < response.body().getGenres().size(); i++) {
                        if (appendSeparator)
                            stringBuilder.append(" | ");
                        appendSeparator = true;

                        stringBuilder.append(response.body().getGenres().get(i).getName());
                    }
                    holder.genre.setText(stringBuilder.toString());

                    //movie year
                    String[] year = response.body().getReleaseDate().split("-");
                    holder.year.setText("(" + year[0] + ")");

                }
            }

            @Override
            public void onFailure(Call<Example_ID> call, Throwable t) {

            }
        });


    }

    @Override
    public int getItemCount() {
       return mymoviesList.size();
    }

}
