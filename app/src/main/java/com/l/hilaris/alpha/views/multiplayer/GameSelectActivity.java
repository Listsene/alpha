package com.l.hilaris.alpha.views.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.views.sudoku.multiplayer.team.TeamActivity;
import com.l.hilaris.alpha.views.sudoku.multiplayer.versus.VersusActivity;

public class GameSelectActivity extends AppCompatActivity implements View.OnClickListener{
    private Button versus, team;
    private SudokuVariation sudoku;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        sudoku = (SudokuVariation) getIntent().getSerializableExtra("sudoku");
        getIntent().putExtra("sudoku", sudoku);

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
                sudoku.setMode("versus");
                intent.putExtra("sudoku", sudoku);
                startActivity(intent);
                break;

            case R.id.team:
                intent = new Intent(this, TeamActivity.class);
                sudoku.setMode("team");
                intent.putExtra("sudoku", sudoku);
                startActivity(intent);
                break;

        }
    }
}


