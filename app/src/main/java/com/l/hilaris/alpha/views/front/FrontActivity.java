package com.l.hilaris.alpha.views.front;
/*
This activity is the Front screen of the application seen after logging in.
From here the user can navigate to the rest of the application

 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.l.hilaris.alpha.views.login.LoginActivity;
import com.l.hilaris.alpha.views.multiplayer.MultiPlayerMenuActivity;
import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.views.singleplayer.SinglePlayerMenuActivity;

public class FrontActivity extends AppCompatActivity implements View.OnClickListener {
    private Button single, multi, logoutbutton;
    private TextView disabled;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        loggedIn = (boolean) getIntent().getSerializableExtra("loggedIn");

        single = findViewById(R.id.single);
        multi = findViewById(R.id.multi);
        logoutbutton = findViewById(R.id.logout);
        disabled = findViewById(R.id.multi_disabled);
        disabled.setVisibility(View.INVISIBLE);

        single.setOnClickListener(this);
        logoutbutton.setOnClickListener(this);
        multi.setOnClickListener(this);

        if(!loggedIn) {
            multi.setEnabled(false);
            disabled.setVisibility(View.VISIBLE);
            logoutbutton.setText(getResources().getText(R.string.not_logged_in));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single:
                Intent intent = new Intent(this, SinglePlayerMenuActivity.class);
                startActivity(intent);
                break;

            case R.id.multi:
                intent = new Intent(this, MultiPlayerMenuActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                if(loggedIn) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                    Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            });
                }
                else{
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

}
