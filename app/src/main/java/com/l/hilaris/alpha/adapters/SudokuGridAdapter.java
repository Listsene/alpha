package com.l.hilaris.alpha.adapters;

import android.content.Context;
<<<<<<< HEAD:app/src/main/java/com/l/hilaris/alpha/adapters/SudokuGridAdapter.java
=======
import android.content.SharedPreferences;
>>>>>>> f6ceb98601346f940324a287fc08b7f1abc794a8:app/src/main/java/com/l/hilaris/alpha/adapters/SudokuGridAdapter.java
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.l.hilaris.alpha.models.Memo;
import com.l.hilaris.alpha.models.Sudoku;
import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuCellData;

import java.util.List;


public class SudokuGridAdapter extends BaseAdapter {
    private Context mContext;
    private Sudoku sudoku;
    private int nSelectedPos = -1;
    Button cell;


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
        List<Memo> memo = cellData.getMemo();

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_sudoku_cell, null);
        }

        cell = convertView.findViewById(R.id.cell);

        if(number.isEmpty() || number.matches("\\s")) { //checks for empty or white space
            cell.setText(" ");
            setBackground(cell, position);
        }
        else {
            cell.setText(number);
            setBackground(cell, position);
        }

        isFinish();


        if(memo != null){
            for(int i = 0 ; i < memo.size() ; i++){
                int memoId = mContext.getResources().getIdentifier("memo_cell_" + memo.get(i).getNumber(), "id", mContext.getPackageName());
                TextView memoTextView = convertView.findViewById(memoId);
                if(memo.get(i).getActive()) {
                    memoTextView.setVisibility(View.VISIBLE);
                    cell.setText(" ");
                    cell.setBackgroundColor(ContextCompat.getColor(convertView.getContext(), R.color.transparent));
                }
                else
                    memoTextView.setVisibility(View.INVISIBLE);
            }
        }

        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                if(nSelectedPos != -1){
                    Button preSelBtn = parent.getChildAt(nSelectedPos).findViewById(R.id.cell);
                    preSelBtn.setSelected(false);
                    if(nSelectedPos == position){
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
    public void setBackground(Button cell, int position) {
        int location = position % 9;
        int row;
        if (position < 27) {
            row = 1;
        } else if (position < 54) {
            row = 2;
        } else {
            row = 1;
        }
        if (row == 1) {
            if ((location < 6) && (location > 2)) {
                cell.setBackgroundResource(R.drawable.cell_button_secondary);
            } else {
                cell.setBackgroundResource(R.drawable.cell_button);
            }
        } else {
            if ((location < 6) && (location > 2)) {
                cell.setBackgroundResource(R.drawable.cell_button);
            } else {
                cell.setBackgroundResource(R.drawable.cell_button_secondary);
            }
        }
    }
    public void isFinish(){
        Boolean isFinish;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref",0);
        isFinish = sharedPreferences.getBoolean("isFinish", false);
        if(isFinish){
            this.cell.setSelected(false);
            this.cell.setEnabled(false);
            this.notifyDataSetChanged();
        }else{
            this.cell.setEnabled(true);
            this.notifyDataSetChanged();
        }
    }

    public void notifyThis(){
        this.notifyDataSetChanged();
    }

    public int getnSelectedPos(){
        return nSelectedPos;
    }

    @Override
    public int getCount() {
        return sudoku.getCells().size();
    }
}
