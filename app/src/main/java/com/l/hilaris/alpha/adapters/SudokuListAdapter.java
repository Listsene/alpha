package com.l.hilaris.alpha.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;

import java.util.List;

public class SudokuListAdapter extends RecyclerView.Adapter<SudokuListAdapter.MyViewHolder> {

    private Context mContext;
    private List<SudokuVariation> sudokus;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name; // time;
        public TextView score;


        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            score = view.findViewById(R.id.scoreInList);
            //    time = view.findViewById(R.id.time);
        }
    }


    public SudokuListAdapter(Context mContext,List<SudokuVariation> sudokus) {
        this.sudokus = sudokus;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sudoku_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SudokuVariation sudoku = sudokus.get(position);

        SharedPreferences preferences = mContext.getSharedPreferences("pref",0);
        int scoreList = preferences.getInt(sudoku.getId()+"score",0);



        holder.name.setText(sudoku.getId());
        holder.score.setText("score: "+String.valueOf(scoreList));


        // TODO
        // holder.time.setText(sudoku.getTime());
    }


    @Override
    public int getItemCount() {
        return sudokus.size();
    }

}