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
                bt = (Button)findViewById(R.id.button_1);
                st = bt.getText().toString();
                break;
            case R.id.button_2:
                bt = (Button)findViewById(R.id.button_2);
                st = bt.getText().toString();
                break;
            case R.id.button_3:
                bt = (Button)findViewById(R.id.button_3);
                st = bt.getText().toString();
                break;
            case R.id.button_4:
                bt = (Button)findViewById(R.id.button_4);
                st = bt.getText().toString();
                break;
            case R.id.button_5:
                bt = (Button)findViewById(R.id.button_5);
                st = bt.getText().toString();
                break;
            case R.id.button_6:
                bt = (Button)findViewById(R.id.button_6);
                st = bt.getText().toString();
                break;
            case R.id.button_7:
                bt = (Button)findViewById(R.id.button_7);
                st = bt.getText().toString();
                break;
            case R.id.button_8:
                bt = (Button)findViewById(R.id.button_8);
                st = bt.getText().toString();
                break;
            case R.id.button_9:
                bt = (Button)findViewById(R.id.button_9);
                st = bt.getText().toString();
                break;

            case R.id.button_submit:
                clearMemo(selectedCell.getId());
                tv=selectedCell;
                tv.setText(st);
                tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                break;

            case R.id.button_memo:
                if(v.getId()==R.id.button_memo && bt==null){

                }else{
                    inputMemo(selectedCell.getId(),Integer.parseInt(bt.getText().toString()));
                }
                break;

            case R.id.button_clear:
                tv=selectedCell;
                tv.setText("");
                clearMemo(selectedCell.getId());
                break;

            case R.id.textView11_:
                selectedCell=findViewById(R.id.textView11_);
                break;
            case R.id.textView12_:
                selectedCell=findViewById(R.id.textView12_);
                break;
            case R.id.textView13_:
                selectedCell=findViewById(R.id.textView13_);
                break;
            case R.id.textView14_:
                selectedCell=findViewById(R.id.textView14_);
                break;
            case R.id.textView15_:
                selectedCell=findViewById(R.id.textView15_);
                break;
            case R.id.textView16_:
                selectedCell=findViewById(R.id.textView16_);
                break;
            case R.id.textView17_:
                selectedCell=findViewById(R.id.textView17_);
                break;
            case R.id.textView18_:
                selectedCell=findViewById(R.id.textView18_);
                break;
            case R.id.textView19_:
                selectedCell=findViewById(R.id.textView19_);
                break;


            default:
                break;
        }

    }

    public void inputMemo(int cellNum, int btNum){
        tv=findViewById(cellNum);
        tv.setText("");
        for(int i=1; i<10; i++){
            tv=findViewById(cellNum+btNum);
            isThereMemo();
        }
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
