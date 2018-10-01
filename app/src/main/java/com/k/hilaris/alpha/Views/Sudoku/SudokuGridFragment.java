package com.k.hilaris.alpha.Views.Sudoku;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.k.hilaris.alpha.Adapters.SudokuGridAdapter;
import com.k.hilaris.alpha.Models.SudokuGrid;
import com.k.hilaris.alpha.R;

import java.util.ArrayList;
import java.util.List;

public class SudokuGridFragment extends Fragment {
    private GridView gridView;
    private SudokuGrid grid;
    private SudokuGridAdapter Adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sudoku_grid, container, false);
        grid = createSudoku();

        gridView = view.findViewById(R.id.SudokuGridView);
        Adapter = new SudokuGridAdapter(getContext(), grid);
        gridView.setAdapter(Adapter);



        return view;
    }
    public SudokuGrid createSudoku() { // Create Sample Sudoku Board for testing
        grid = new SudokuGrid();
        List<String> cellList = new ArrayList<>();
        String cells = "|5|3| | |7| | | | |" +
                        "|6| | |1|9|5| | | |" +
                        "|8| | | |6| | | |3|" +
                        "|4| | |8| |3| | |1|" +
                        "|7| | | |2| | | |6|" +
                        "| |6| | | | |2|8| |" +
                        "| | | |4|1|9| | |5|" +
                        "| | | | |8| | |7|9|";
        String[] array = cells.split("[|]", 0);
        for(int i = 0; i < array.length; i++) {
            String cell = array[i];
            cellList.add(cell);
        }
        //Adapter.notifyDataSetChanged();
        grid.setCells(cellList);
        return grid;
    }
}
