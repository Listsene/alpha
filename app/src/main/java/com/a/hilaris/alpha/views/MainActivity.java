package com.a.hilaris.alpha.views;
/*
First Activity opened upon App start.
Login Activity through Google sign in

 */
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.a.hilaris.alpha.views.front.FrontActivity;
import com.a.hilaris.alpha.R;
import com.squareup.picasso.Picasso;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import android.support.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_sign_in;
    private Button login;
    private ImageView logo;
    private static final int RC_SIGN_IN = 9001;  // Google's sign in code thing
    public static final String TAG ="MY_TAG";
    private FirebaseAuth mAuth;
    //String webClientId=getString(R.string.webclient);
    //default_web_client_id
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance(); //파이어베이스 인증객체선언.

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
         //       .requestEmail()
        //        .build();
        // -> modified Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logo = findViewById(R.id.logo);
        Picasso.get().load(R.drawable.logo).resize(800, 800).into(logo); // original image is 1890x1417

        google_sign_in = findViewById(R.id.google_sign_in);
        //google_sign_in.setOnClickListener(this); //원래 리스너 설정

        google_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        }); //test용 리스너

        login = findViewById(R.id.skip);
        login.setOnClickListener(this);
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
            case R.id.skip:
                moveActivity(false);
                break;
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
            //handleSignInResult(task);
            //added ---> 추가
            try {
                // 파이어베이스 기반
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // 실패시
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                        // ...
                    }
                });
    }

    //일단 제외
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login fail", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    //*@Override
    //*protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //modified to -> 수정되었소
     //*   super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

    //*} //일단제외 쓸려면 *만살리면됌

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //GoogleSignInAccount account->to firebased user
    private void updateUI(FirebaseUser account) {
        if(account != null) {
            moveActivity(true);
        }
        else {
            //Toast.makeText(getBaseContext(), "Sign in Failed", Toast.LENGTH_SHORT).show();
            Log.e("Signed in Check", "Not Signed In");
        }
    }
    @Override
    public void onBackPressed() {
        // Prevent user from going back to empty view
        // Do nothing on back pressed
    }
    void moveActivity(boolean loggedIn) {
        Intent intent = new Intent(this, FrontActivity.class);
        intent.putExtra("loggedIn", loggedIn);
        startActivity(intent);
    }
}
