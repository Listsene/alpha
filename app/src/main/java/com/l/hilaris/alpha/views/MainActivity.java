package com.l.hilaris.alpha.views;
/*
Main Activity, first activity opened on application start.
This activity does nothing but open LoginActivity.
 */
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ImageView;

        import com.l.hilaris.alpha.views.login.LoginActivity;
        import com.l.hilaris.alpha.R;
        import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.logo);
        Picasso.get().load(R.drawable.logo).resize(800, 800).into(logo);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
