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

    public SudokuGridAdapter(Context mContext, SudokuGrid sudokuGrid) {
        this.mContext = mContext;
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

        if(number.isEmpty() || number.matches("\\s")) { //checks for empty or white space
            cell.setFocusable(true);
            cell.setText(" ");
            //cell.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
            //cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.text_color_with_primary));
        }
        else {
            cell.setText(number);
            //cell.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_with_primary));
            cell.setFocusable(false);
            //cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return sudokuGrid.getCells().size();
    }
}
