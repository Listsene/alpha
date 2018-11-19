package com.k.hilaris.alpha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.k.hilaris.alpha.models.Sudoku;
import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.SudokuCellData;


public class SudokuGridAdapter extends BaseAdapter {
    private Context mContext;
    private Sudoku sudoku;
    private int nSelectedPos =-1;


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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        SudokuCellData cellData = sudoku.getCells().get(position);
        String number = cellData.getInput();
        Boolean[] bMemo = cellData.getbMemo();


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_sudoku_cell, null);
        }

        Button cell = convertView.findViewById(R.id.cell);
        //cell.requestFocus();
        cell.setEnabled(true);

        if(number.isEmpty() || number.matches("\\s")) { //checks for empty or white space
            cell.setText(" ");
        }
        else {
            cell.setText(number);
            //cell.setEnabled(false);       //??
        }


        for(int i = 0 ; i < bMemo.length ; i++){
            int memoId = mContext.getResources().getIdentifier("memo_cell_" + (i+1), "id", mContext.getPackageName());
            TextView memoTextView = (TextView) convertView.findViewById(memoId);
            if(bMemo[i] == null || !bMemo[i])
                memoTextView.setVisibility(View.INVISIBLE);
            else{
                memoTextView.setVisibility(View.VISIBLE);
                cell.setText("");
            }
        }

        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                if(nSelectedPos != -1){
                    Button preSelBtn = (Button)parent.getChildAt(nSelectedPos).findViewById(R.id.cell);
                    preSelBtn.setSelected(false);
                    if(nSelectedPos ==position){
                        nSelectedPos = -1;
                        return;
                    }
                    //nSelectedPos = -1;
                }
                btn.setSelected(!btn.isSelected());
                nSelectedPos = position;
            }
        });
        return convertView;
    }

    public int getnSelectedPos(){
        return nSelectedPos;
    }

    @Override
    public int getCount() {
        return sudoku.getCells().size();
    }
}
