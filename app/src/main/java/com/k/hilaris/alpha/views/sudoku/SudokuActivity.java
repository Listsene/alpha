package com.k.hilaris.alpha.views.sudoku;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.Sudoku;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class SudokuActivity extends AppCompatActivity implements InputButtonsGridFragment.TextClicked {
    private Toolbar mToolbar;
    TextView timerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.SudokuGridFragment, new SudokuGridFragment());
        ft.add(R.id.InputButtonsFragment, new InputButtonsGridFragment());
        ft.commit();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Timer();
    }

    long timeRemaining = 300000;

    public boolean Timer(){
        new CountDownTimer(timeRemaining, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms;
                if((TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))<10)){
                    hms =  ("0"+TimeUnit.MILLISECONDS.toHours(millis))+":0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":0"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }else{
                    hms =  ("0"+TimeUnit.MILLISECONDS.toHours(millis))+":0"+ (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                }

                timerTv = findViewById(R.id.timerTextView);
                timerTv.setText(hms);
                timeRemaining = millis;
            }
            public void onFinish() {
                timerTv.setText("done!");
            }
        }.start();
        return true;
    }


    @Override
    public void sendText(String text){
        SudokuGridFragment sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        sudokuGridFragment.getInput(text);
    }
}
