package com.k.hilaris.alpha.views.multiPlayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.k.hilaris.alpha.R;

public class MultiPlayerMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button search, create;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_menu);

        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        create = findViewById(R.id.create);
        create.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                // TODO
                break;
            case R.id.create:
                // TODO
                break;

        }
    }
}
