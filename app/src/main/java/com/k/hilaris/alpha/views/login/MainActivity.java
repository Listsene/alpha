package com.k.hilaris.alpha.views.login;
/*
*Login Activity through Google sign in

 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.k.hilaris.alpha.views.front.FrontActivity;
import com.k.hilaris.alpha.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_sign_in;
    private Button login, signUp;
    // Google's sign in code thing
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_sign_in = findViewById(R.id.google_sign_in);
        google_sign_in.setOnClickListener(this);
        login = findViewById(R.id.email_sign_in);
        login.setOnClickListener(this);
        signUp = findViewById(R.id.sign_up);
        signUp.setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in:
                signIn();
                break;
            case R.id.email_sign_in:
                Intent intent = new Intent(this, FrontActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_up:
                // TODO
                Toast.makeText(getBaseContext(), "Not yet Implemented", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login fail", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        super.onStart();
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account != null) {
            Intent intent = new Intent(this, FrontActivity.class);
            startActivity(intent);
        }
        else {
            //Toast.makeText(getBaseContext(), "Sign in Failed", Toast.LENGTH_SHORT).show();
            Log.e("Signed in Check", "Not Signed In");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
