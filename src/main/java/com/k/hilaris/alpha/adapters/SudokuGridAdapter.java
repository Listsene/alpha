package com.k.hilaris.alpha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.k.hilaris.alpha.models.Sudoku;
import com.k.hilaris.alpha.R;


public class SudokuGridAdapter extends BaseAdapter {
    private Context mContext;
    private Sudoku sudoku;

    public SudokuGridAdapter(Context mContext, Sudoku sudoku) {
        this.mContext = mContext;
        this.sudoku = sudoku;
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
        String number = sudoku.getCells().get(position);

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
            cell.setFocusable(false);
            //cell.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_with_primary));
            //cell.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary));
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return sudoku.getCells().size();
    }
}
