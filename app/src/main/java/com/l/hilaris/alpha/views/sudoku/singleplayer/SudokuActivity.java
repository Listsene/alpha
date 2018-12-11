package com.l.hilaris.alpha.views.sudoku.singleplayer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;

import java.util.concurrent.TimeUnit;

public class SudokuActivity extends AppCompatActivity implements InputButtonsGridFragment.InputClicked {
    private Toolbar mToolbar;
    TextView timerTv, scoreTv;
    private int score, score2;
    long fiveMinutes;
    private SudokuGridFragment sudokuGridFragment = new SudokuGridFragment();
    private InputButtonsGridFragment inputButtonsGridFragment = new InputButtonsGridFragment();
    CountDownTimer timer = null;
    boolean isFinish, success;
    SudokuVariation Sudoku;
    private int testy = 0;

    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Sudoku.getId()+"time",fiveMinutes);

        editor.putInt(Sudoku.getId()+"score", score);
        editor.apply();
        if(mOnKeyBackPressedListener != null){
            mOnKeyBackPressedListener.onBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);

        isFinish = false;

        SudokuVariation sudoku = (SudokuVariation) getIntent().getSerializableExtra("sudoku");
        getIntent().putExtra("sudoku", sudoku);

        Sudoku = sudoku;
        score = sharedPreferences.getInt(Sudoku.getId()+"score",0);
        success = sharedPreferences.getBoolean(Sudoku.getId()+"success",false);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.SudokuGridFragment, sudokuGridFragment);
        ft.add(R.id.InputButtonsFragment, inputButtonsGridFragment);
        ft.commit();
        fiveMinutes = sharedPreferences.getLong(Sudoku.getId()+"time", 300000);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        timerTv = findViewById(R.id.timer);
        scoreTv = findViewById(R.id.score);

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

    public void resetTimer(){
        timer.cancel();
        fiveMinutes = 300000;
        Timer();
    }
    public void resetGrid(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        sudokuGridFragment.newGame();
        ft.replace(R.id.SudokuGridFragment, sudokuGridFragment = new SudokuGridFragment());
        ft.replace(R.id.InputButtonsFragment, inputButtonsGridFragment = new InputButtonsGridFragment());
        ft.commit();
    }
    public void resetScore(){
        score = 0;
        scoreTv.setText(String.valueOf(score));
    }


    public void Timer(){
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
    public void Score() {
        scoreTv.setText(String.valueOf(score));
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

    public void setFinish(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        success = true;
        editor.putBoolean(Sudoku.getId()+"success",success);
        editor.apply();

        timer.cancel();
        fiveMinutes = 0;
        Timer();
    }
}
