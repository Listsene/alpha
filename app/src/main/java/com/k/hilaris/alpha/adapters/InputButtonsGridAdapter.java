package com.k.hilaris.alpha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.views.sudoku.singleplayer.InputButtonsGridFragment;

import java.util.List;

public class InputButtonsGridAdapter extends BaseAdapter {
    private Context mContext;
    List<String> inputs;
    InputButtonsGridFragment.InputClicked inputClicked;
    String btNum;

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

        Button inputButton = convertView.findViewById(R.id.inputButton);
        inputButton.setText(input);

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

    @Override
    public int getCount() {
        return inputs.size();
    }
}


