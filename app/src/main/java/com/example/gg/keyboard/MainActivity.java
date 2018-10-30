package com.example.gg.keyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv;
    String st;
    Button bt;
    TextView selectedCell;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_1:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_1);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_2:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_2);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_3:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_3);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_4:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_4);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_5:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_5);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_6:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_6);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_7:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_7);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_8:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_8);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_9:
                //tv = (TextView)findViewById(R.id.textView);
                bt = (Button)findViewById(R.id.button_9);
                st = bt.getText().toString();
                //tv.setText(st);
                break;

            case R.id.button_submit:
                for(int i=1;i<10;i++){
                    tv=findViewById(R.id.textView11_+i);
                    tv.setText("");
                }
                tv = (TextView)findViewById(R.id.textView11_);

                tv.setText(st);
                tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

                break;

            case R.id.button_memo:
                tv=findViewById(R.id.textView11_);
                tv.setText("");
                if(v.getId()==R.id.button_memo && bt==null){

                }else{
                    switch(bt.getId()){
                        case R.id.button_1:
                            tv = (TextView)findViewById(R.id.textView11_1);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_2:
                            tv = (TextView)findViewById(R.id.textView11_2);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_3:
                            tv = (TextView)findViewById(R.id.textView11_3);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_4:
                            tv = (TextView)findViewById(R.id.textView11_4);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_5:
                            tv = (TextView)findViewById(R.id.textView11_5);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_6:
                            tv = (TextView)findViewById(R.id.textView11_6);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_7:
                            tv = (TextView)findViewById(R.id.textView11_7);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_8:
                            tv = (TextView)findViewById(R.id.textView11_8);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_9:
                            tv = (TextView)findViewById(R.id.textView11_9);
                            if(tv.getText()==st){
                                tv.setText("");
                            }else{
                                tv.setText(st);
                            }
                            break;

                        case R.id.button_submit:
                            break;

                        case R.id.button_memo:
                            break;

                        case R.id.button_clear:
                            break;
                }
                }
                break;

            case R.id.button_clear:
                tv = (TextView)findViewById(R.id.textView11_);
                bt = (Button)findViewById(R.id.button_clear);
                tv.setText(" ");
                for(int i=1;i<10;i++){
                    tv=findViewById(R.id.textView11_+i);
                    tv.setText("");
                }
                break;

            case R.id.textView11_:
                //tv=findViewById(R.id.textView11_);
                selectedCell=findViewById(R.id.textView11_);


            default:
                break;
        }

    }}
