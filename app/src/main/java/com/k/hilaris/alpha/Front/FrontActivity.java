package com.k.hilaris.alpha.Front;
/*
This activity is the Front screen of the application seen after logging in.
From here the user can navigate to the rest of the application

 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.k.hilaris.alpha.R;

public class FrontActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
    }
}
