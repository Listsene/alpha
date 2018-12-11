package com.l.hilaris.alpha.views.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.views.sudoku.multiplayer.VersusActivity;

public class GameSelectActivity extends AppCompatActivity implements View.OnClickListener{
    private Button versus, team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        versus = findViewById(R.id.versus);
        team = findViewById(R.id.team);

        versus.setOnClickListener(this);
        team.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.versus:
                Intent intent = new Intent(this, VersusActivity.class);
                intent.putExtra("mode", "versus");
                startActivity(intent);
                break;

            case R.id.team:
                intent = new Intent(this, MultiPlayerMenuActivity.class);
                intent.putExtra("mode", "team");
                startActivity(intent);
                break;

        }
    }
}


