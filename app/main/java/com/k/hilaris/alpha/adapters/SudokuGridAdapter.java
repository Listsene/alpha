package com.l.hilaris.alpha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.l.hilaris.alpha.models.Sudoku;
import com.l.hilaris.alpha.R;


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

        Button cell = convertView.findViewById(R.id.cell);
        cell.requestFocus();
        cell.setEnabled(true);

        if(number.isEmpty() || number.matches("\\s")) { //checks for empty or white space
            cell.setText(" ");
        }
        else {
            cell.setText(number);
            //cell.setEnabled(false);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return sudoku.getCells().size();
    }
}
