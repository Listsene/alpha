package com.l.hilaris.alpha.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;

import java.util.List;

public class SudokuListAdapter extends RecyclerView.Adapter<SudokuListAdapter.MyViewHolder> {

    private List<SudokuVariation> sudokus;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name; // time;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            //    time = view.findViewById(R.id.time);
        }
    }


    public SudokuListAdapter(List<SudokuVariation> sudokus) {
        this.sudokus = sudokus;
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
        holder.name.setText(sudoku.getId());

        // TODO
        // holder.time.setText(sudoku.getTime());
    }

    @Override
    public int getItemCount() {
        return sudokus.size();
    }

}