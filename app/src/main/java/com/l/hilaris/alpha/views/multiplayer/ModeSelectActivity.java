package com.l.hilaris.alpha.views.multiplayer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.google.gson.Gson;
import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.adapters.SudokuListAdapter;
import com.l.hilaris.alpha.models.SudokuCellData;
import com.l.hilaris.alpha.models.SudokuVariation;

import java.util.ArrayList;
import java.util.List;
import com.l.hilaris.alpha.views.sudoku.singleplayer.SudokuActivity;

public class ModeSelectActivity  extends AppCompatActivity implements View.OnClickListener {

    private Button team, versus, team_match;
    private java.util.List<SudokuVariation> sudokus = new ArrayList<>();
    private SudokuListAdapter Adapter;
    private SudokuVariation sudoku;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay_mode_select_menu);

        team = findViewById(R.id.button);
        team.setOnClickListener(this);
        versus = findViewById(R.id.button2);
        versus.setOnClickListener(this);
        team_match = findViewById(R.id.button3);
        team_match.setOnClickListener(this);
        Adapter = new SudokuListAdapter(getApplicationContext(),sudokus);
        createSudokus();
        sudoku = sudokus.get(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                //team
                //team search create 화면으로
                Intent intent = new Intent(this, TeamMultiPlayerMenuActivity.class);
                intent.putExtra("sudoku", sudoku);
                startActivity(intent);
                break;
            case R.id.button2:
                //versus
                //versus search create 화면으로
                intent = new Intent(this, VersusMultiplayerMenuActivity.class);
                intent.putExtra("sudoku", sudoku);
                startActivity(intent);
                break;
            case R.id.button3:
                //team match
                //team match search create 화면으로
                intent = new Intent(this, TeamMatchMultiplayerMenuActivity.class);
                intent.putExtra("sudoku", sudoku);
                startActivity(intent);
                break;
        }
    }

    private void createSudokus() {
        SudokuVariation sudoku = new SudokuVariation();
        List<SudokuCellData> cells;
        List<String> solution;
        SudokuCellData cell;

        Gson gson;
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = preferences.edit();

        cells = new ArrayList<>();
        String sudokuCells =
                "4| |9||2||7|1|3|" +
                        "||3||7||2||5|" +
                        "2|7||9||1|4||8|" +
                        "|||1|5||9|7|2|" +
                        "|5||7|4||3|8|1|" +
                        "|||||8|||4|" +
                        "|9||4||2||3|6|" +
                        "|2|||8||5|4|9|" +
                        "3|||5||9|||7|";
        String[] splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        String solCells =
                "4|6|9|8|2|5|7|1|3|" +
                        "8|1|3|6|7|4|2|9|5|" +
                        "2|7|5|9|3|1|4|6|8|" +
                        "6|8|4|1|5|3|9|7|2|" +
                        "9|5|2|7|4|6|3|8|1|" +
                        "7|3|1|2|9|8|6|5|4|" +
                        "5|9|7|4|1|2|8|3|6|" +
                        "1|2|6|3|8|7|5|4|9|" +
                        "3|4|8|5|6|9|1|2|7|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 01");
        sudokus.add(sudoku);

        gson = new Gson();
        String json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        Adapter.notifyDataSetChanged();
    }

}

