package com.k.hilaris.alpha.adapters;

import android.content.Context;
import android.content.res.Resources;
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
    InputButtonsGridFragment.TextClicked textClicked;

    public InputButtonsGridAdapter(Context mContext, List<String> inputs, InputButtonsGridFragment.TextClicked textClicked) {
        this.mContext = mContext;
        this.inputs = inputs;
        this.textClicked = textClicked;
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
        String input = inputs.get(position);

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
                textClicked.sendText(text);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return inputs.size();
    }
}


