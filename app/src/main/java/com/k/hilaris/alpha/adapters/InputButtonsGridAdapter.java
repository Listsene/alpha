package com.k.hilaris.alpha.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.views.sudoku.InputButtonsGridFragment;

import java.util.List;

public class InputButtonsGridAdapter extends BaseAdapter {
    private Context mContext;
    List<String> inputs;
    InputButtonsGridFragment.InputClicked inputClicked;
    String btNum;
    Button inputButton;

    public InputButtonsGridAdapter(Context mContext, List<String> inputs, InputButtonsGridFragment.InputClicked inputClicked) {
        this.mContext = mContext;
        this.inputs = inputs;
        this.inputClicked = inputClicked;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String input = inputs.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_input_cell, null);
        }

        inputButton = convertView.findViewById(R.id.inputButton);
        inputButton.setText(input);

        isFinish();

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button bt = (Button)view;
                String text = bt.getText().toString();
                btNum = bt.getText().toString();
                inputClicked.sendInput(text);
            }
        });

        return convertView;
    }

    public void isFinish(){
        Boolean isFinish;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref",0);
        isFinish = sharedPreferences.getBoolean("isFinish", false);
        if(isFinish){
            inputButton.setEnabled(false);
            this.notifyDataSetChanged();
        }else{
            inputButton.setEnabled(true);
            this.notifyDataSetChanged();
        }
    }

    public void notifyThis(){
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return inputs.size();
    }
}


