package com.example.gg.keyboard;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar mToolbar;
    TextView timerTv;
    TextView tv;
    String st;
    Button bt;
    TextView selectedCell;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                timerTv=(TextView) findViewById(R.id.timerTextView);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
            case R.id.button_2:
            case R.id.button_3:
            case R.id.button_4:
            case R.id.button_5:
            case R.id.button_6:
            case R.id.button_7:
            case R.id.button_8:
            case R.id.button_9:
                bt = (Button)v;
                st = bt.getText().toString();
                break;

            case R.id.button_submit:
                if(selectedCell!=null){
                    clearMemo(selectedCell.getId());
                    selectedCell.setText(st);
                    selectedCell.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                }
                break;

            case R.id.button_memo:
                if(v.getId()==R.id.button_memo && bt!=null){
                    inputMemo(selectedCell.getId(),Integer.parseInt(st));
                }
                break;

            case R.id.button_clear:
                if(selectedCell!=null){
                    selectedCell.setText("");
                    clearMemo(selectedCell.getId());
                }
                break;

            case R.id.textView11_:
            case R.id.textView12_:
            case R.id.textView13_:
            case R.id.textView14_:
            case R.id.textView15_:
            case R.id.textView16_:
            case R.id.textView17_:
            case R.id.textView18_:
            case R.id.textView19_:
            case R.id.textView21_:
            case R.id.textView22_:
            case R.id.textView23_:
            case R.id.textView24_:
            case R.id.textView25_:
            case R.id.textView26_:
            case R.id.textView27_:
            case R.id.textView28_:
            case R.id.textView29_:
            case R.id.textView31_:
            case R.id.textView32_:
            case R.id.textView33_:
            case R.id.textView34_:
            case R.id.textView35_:
            case R.id.textView36_:
            case R.id.textView37_:
            case R.id.textView38_:
            case R.id.textView39_:
            case R.id.textView41_:
            case R.id.textView42_:
            case R.id.textView43_:
            case R.id.textView44_:
            case R.id.textView45_:
            case R.id.textView46_:
            case R.id.textView47_:
            case R.id.textView48_:
            case R.id.textView49_:
            case R.id.textView51_:
            case R.id.textView52_:
            case R.id.textView53_:
            case R.id.textView54_:
            case R.id.textView55_:
            case R.id.textView56_:
            case R.id.textView57_:
            case R.id.textView58_:
            case R.id.textView59_:
            case R.id.textView61_:
            case R.id.textView62_:
            case R.id.textView63_:
            case R.id.textView64_:
            case R.id.textView65_:
            case R.id.textView66_:
            case R.id.textView67_:
            case R.id.textView68_:
            case R.id.textView69_:
            case R.id.textView71_:
            case R.id.textView72_:
            case R.id.textView73_:
            case R.id.textView74_:
            case R.id.textView75_:
            case R.id.textView76_:
            case R.id.textView77_:
            case R.id.textView78_:
            case R.id.textView79_:
            case R.id.textView81_:
            case R.id.textView82_:
            case R.id.textView83_:
            case R.id.textView84_:
            case R.id.textView85_:
            case R.id.textView86_:
            case R.id.textView87_:
            case R.id.textView88_:
            case R.id.textView89_:
            case R.id.textView91_:
            case R.id.textView92_:
            case R.id.textView93_:
            case R.id.textView94_:
            case R.id.textView95_:
            case R.id.textView96_:
            case R.id.textView97_:
            case R.id.textView98_:
            case R.id.textView99_:
                selectedCell=(TextView)v;
                break;

            default:
                break;
        }
    }

    public void inputMemo(int cellNum, int btNum){
        tv=findViewById(cellNum);
        tv.setText("");
        tv=findViewById(cellNum+btNum);
        isThereMemo();
    }

    public void isThereMemo(){
        if(tv.getText()==st){
            tv.setText("");
        }else{
            tv.setText(st);
        }
    }

    public void clearMemo(int cellNum){
        for(int i=1;i<10;i++){
            tv=findViewById(cellNum+i);
            tv.setText("");
        }
    }
}
