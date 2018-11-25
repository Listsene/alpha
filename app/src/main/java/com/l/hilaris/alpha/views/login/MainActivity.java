package com.l.hilaris.alpha.views.login;
/*
Main Activity, first activity opened on application start.
This activity does nothing but open LoginActivity.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.l.hilaris.alpha.views.login.LoginActivity;
import com.l.hilaris.alpha.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
