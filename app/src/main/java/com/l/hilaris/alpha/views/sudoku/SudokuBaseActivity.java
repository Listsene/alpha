package com.l.hilaris.alpha.views.sudoku;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.views.sudoku.singleplayer.SinglePlayerSudokuActivity;

import java.util.concurrent.TimeUnit;

public abstract class SudokuBaseActivity extends AppCompatActivity implements InputButtonsGridFragment.InputClicked {
    protected Toolbar mToolbar;
    protected TextView timerTv, scoreTv, scoreTv2;
    protected int score, score2;
    protected long fiveMinutes;
    protected SudokuGridFragment sudokuGridFragment = new SudokuGridFragment();
    protected InputButtonsGridFragment inputButtonsGridFragment = new InputButtonsGridFragment();
    protected CountDownTimer timer = null;
    protected boolean isFinish, success;
    protected SudokuVariation Sudoku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref", 0);

        isFinish = false;

        SudokuVariation sudoku = (SudokuVariation) getIntent().getSerializableExtra("sudoku");
        getIntent().putExtra("sudoku", sudoku);

        Sudoku = sudoku;
        score = sharedPreferences.getInt(Sudoku.getId() + "score", 0);
        score2 = 0;
        success = sharedPreferences.getBoolean(Sudoku.getId()+"success",false);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.SudokuGridFragment, sudokuGridFragment);
        ft.add(R.id.InputButtonsFragment, inputButtonsGridFragment);
        ft.commit();
        fiveMinutes = sharedPreferences.getLong(Sudoku.getId() + "time", 300000);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        timerTv = findViewById(R.id.timer);
        scoreTv = findViewById(R.id.score);
        scoreTv2 = findViewById(R.id.score2);
        scoreTv2.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Timer();
        Score();

        Button newGameButton = findViewById(R.id.newGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGrid();
                resetTimer();
                resetScore();
                isFinish = false;
                success = false;
                putIsFinish();
            }
        });
    }

    protected void resetTimer() {
        timer.cancel();
        fiveMinutes = 300000;
        Timer();
    }

    protected void resetGrid() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        sudokuGridFragment.newGame();
        ft.replace(R.id.SudokuGridFragment, sudokuGridFragment = new SudokuGridFragment());
        ft.replace(R.id.InputButtonsFragment, inputButtonsGridFragment = new InputButtonsGridFragment());
        ft.commit();
    }

    protected void resetScore() {
        score = 0;
        score2 = 0;
        Score();
    }

    protected void Timer(){
        timer = new CountDownTimer(fiveMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                isFinish = false;
                putIsFinish();
                sudokuGridFragment.getAdapter().notifyThis();
                inputButtonsGridFragment.getAdapter().notifyThis();
                long millis = millisUntilFinished;
                String time;
                if((TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))<10)){
                    time =  "0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":0"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }else{
                    time =  "0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }
                timerTv.setText(time);
                fiveMinutes = millis;
            }
            public void onFinish() {

                if(success){
                    timerTv.setText("Success!");
                }else{
                    timerTv.setText(getResources().getText(R.string.Timer_Complete));
                }

                isFinish=true;
                putIsFinish();
                if(sudokuGridFragment.getAdapter() !=null){
                    sudokuGridFragment.getAdapter().notifyThis();
                }
                if(inputButtonsGridFragment.getAdapter() != null){
                    inputButtonsGridFragment.getAdapter().notifyThis();
                }
            }
        }.start();
    }

    protected void Score() {
        scoreTv.setText(String.valueOf(score));
        scoreTv2.setText(String.valueOf(score2));
    }

    @Override
    public void sendInput(String input) {
        sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        SudokuVariation sudoku = sudokuGridFragment.getInput(input);
        if (score != sudoku.getScore()) { // checks if score is changed
            score = sudoku.getScore();
            Score();
        }
    }

    public void putIsFinish() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Sudoku.getId() + "isFinish", isFinish);
        editor.putBoolean(Sudoku.getId() + "success", success);
        editor.apply();
    }

    public void setFinish() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        success = true;
        editor.putBoolean(Sudoku.getId() + "success", success);
        editor.apply();

        timer.cancel();
        fiveMinutes = 0;
        Timer();
    }
}