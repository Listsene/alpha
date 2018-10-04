package com.example.gg.keyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv;
    String st;
    Button bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_1:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_1);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_2:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_2);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_3:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_3);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_4:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_4);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_5:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_5);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_6:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_6);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_7:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_7);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_8:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_8);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_9:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_9);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_submit:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_submit);
                tv.setText(st);
                break;

            case R.id.button_memo:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_memo);
                tv.setText(tv.getText()+st);
                break;

            case R.id.button_clear:
                tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_clear);
                tv.setText(" ");
                break;

            default:
                break;
        }

    }}
