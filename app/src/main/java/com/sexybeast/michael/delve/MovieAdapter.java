package com.sexybeast.michael.delve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.sexybeast.michael.delve.Model_ID.Example_ID;
import com.sexybeast.michael.delve.model.Example;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView thumbnail, overflow;
        public MyViewHolder( View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Movie movie = movieList.get(position);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<Example_ID> callXXX = movieInterface.getMovieDetails("movie/"+movie.getTmdbID()+"?api_key=623eeab48528051330ddc3ca73959483");
        callXXX.enqueue(new Callback<Example_ID>() {
            @Override
            public void onResponse(Call<Example_ID> call, Response<Example_ID> response) {
            if(response.isSuccessful()){
                holder.name.setText(response.body().getTitle());

                String poster = response.body().getPosterPath();
                Glide.with(context).load("http://image.tmdb.org/t/p/original" + poster).into(holder.thumbnail);
                movie.setName(response.body().getTitle());
             }
            }

            @Override
            public void onFailure(Call<Example_ID> call, Throwable t) {

            }
        });

//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               showPopupMenu(holder.overflow);
//            }
//        });
    }

//    //for pop up menu when you touch the overflow icon
//    private void showPopupMenu(View view){
//        PopupMenu popupMenu = new PopupMenu(context, view);
//        MenuInflater inflater = popupMenu.getMenuInflater();
//        inflater.inflate(R.menu.overflow_menu, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popupMenu.show();
//    }
//
//    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//        public MyMenuItemClickListener(){}
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()){
//                case R.id.action_add_to_watchlist:
//
//                    Toast.makeText(context, "Add to watchlist", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
       return movieList.size();
    }


}
