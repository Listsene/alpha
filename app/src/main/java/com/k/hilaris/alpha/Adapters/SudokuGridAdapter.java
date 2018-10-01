package com.k.hilaris.alpha.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.k.hilaris.alpha.Models.SudokuGrid;
import com.k.hilaris.alpha.R;


public class SudokuGridAdapter extends BaseAdapter {
    private Context mContext;
    private SudokuGrid sudokuGrid;

    public SudokuGridAdapter(Context c, SudokuGrid sudokuGrid) {
        this.mContext = c;
        this.sudokuGrid = sudokuGrid;
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
        String number = sudokuGrid.getCells().get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_sudoku_cell, null);
        }

        EditText cell = convertView.findViewById(R.id.cell);

        if(number.isEmpty()) {
            cell.setActivated(true);
            cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.text_color_with_primary));
        }
        else {
            cell.setText(number);
            cell.setActivated(false);
            cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return sudokuGrid.getCells().size();
    }
}
