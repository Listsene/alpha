package com.k.hilaris.alpha.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.k.hilaris.alpha.models.Memo;
import com.k.hilaris.alpha.models.Sudoku;
import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.SudokuCellData;

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
            cell.setBackgroundResource(R.drawable.cell_button);
        }
        else {
            cell.setText(number);
            cell.setBackgroundResource(R.drawable.cell_button);
        }

        Boolean isFinish;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref",0);
        isFinish = sharedPreferences.getBoolean("isFinish", false);
        Log.d("isFinish in Adapter",String.valueOf(isFinish));
        if(isFinish == true){
            this.cell.setSelected(false);
            this.cell.setEnabled(false);
            this.notifyDataSetChanged();
        }else if(isFinish==false){
            this.cell.setEnabled(true);
            this.notifyDataSetChanged();
        }


        if(memo != null){
            for(int i = 0 ; i < memo.size() ; i++){
                int memoId = mContext.getResources().getIdentifier("memo_cell_" + memo.get(i).getNumber(), "id", mContext.getPackageName());
                TextView memoTextView = convertView.findViewById(memoId);
                if(memo.get(i).getActive()) {
                    memoTextView.setVisibility(View.VISIBLE);
                    cell.setText(" ");
                    cell.setBackgroundColor(convertView.getResources().getColor(R.color.transparent));
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
