package com.sexybeast.michael.delve;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogClass extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public Button btn_add, btn_cancel;
    public TextView movieName;


    public CustomDialogClass(Activity activity) {
        super(activity);

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        movieName = (TextView) findViewById(R.id.movieName);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_add.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_add:
                if(TextUtils.isEmpty(movieName.getText().toString())){
                    movieName.setError("Please enter a movie name");
                }else {
                    MyMovieList.addMovie(movieName);
                    dismiss();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}
