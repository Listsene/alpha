package com.k.hilaris.alpha.views.singlePlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.views.sudoku.SudokuActivity;

public class SinglePlayerMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button easy, medium, hard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_menu);

        easy = findViewById(R.id.easy);
        easy.setOnClickListener(this);
        medium = findViewById(R.id.medium);
        medium.setOnClickListener(this);
        hard = findViewById(R.id.hard);
        hard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easy:
                Intent intent = new Intent(this, SudokuActivity.class);
                startActivity(intent);
                break;
            case R.id.medium:
                // TODO
                break;
            case R.id.hard:
                // TODO
                break;
        }
    }
}
