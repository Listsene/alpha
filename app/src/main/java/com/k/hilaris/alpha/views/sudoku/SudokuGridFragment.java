package com.k.hilaris.alpha.views.sudoku;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.k.hilaris.alpha.adapters.SudokuGridAdapter;
import com.k.hilaris.alpha.models.Sudoku;
import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.SudokuVariation;

import java.util.ArrayList;
import java.util.List;

public class SudokuGridFragment extends Fragment {
    private GridView gridView;
    private Sudoku grid;
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
    public Sudoku createSudoku() { // Create Sample Sudoku Board for testing
        grid = new Sudoku();
        List<String> cellList = new ArrayList<>();
        String cells =  "5|3| | |7| | | | |" +
                        "6| | |1|9|5| | | |" +
                        " |9|8| | | | |6| |" +
                        "8| | | |6| | | |3|" +
                        "4| | |8| |3| | |1|" +
                        "7| | | |2| | | |6|" +
                        " |6| | | | |2|8| |" +
                        " | | |4|1|9| | |5|" +
                        " | | | |8| | |7|9|";
        String[] array = cells.split("[|]", 0);
        for(int i = 0; i < array.length; i++) {
            String cell = array[i];
            cellList.add(cell);
        }
        grid.setCells(cellList);
        return grid;
    }

    public SudokuVariation createVariation(Sudoku sudoku) {
        SudokuVariation sv = new SudokuVariation(sudoku);
        sv.setGuid(createGUID(sv));
        sv.setCells(randomizeTokens(sv));
        sv.setCells(scrambleGrid(sv));
        sv.setCells(rotate(sv));
        return sv;
    }

    public void getInput(String input){
        int nSelectedPos = Adapter.getnSelectedPos();
        List<String> list = grid.getCells();
        list.set(nSelectedPos, input);
        Adapter.notifyDataSetChanged();
    }

    private String createGUID(SudokuVariation sv) {
        String guid = "0710896a-1959-4d0d-87ba-dd3bcd02c948"; // example
        // makes unique guid
        return guid;
    }
    private List<String> randomizeTokens(SudokuVariation sv) {
        List<String> tokensRandomized = sv.getCells();
        // randomize tokens e.g. remap 123456789 to 356472189

        return tokensRandomized;
    }
    private List<String> scrambleGrid(SudokuVariation sv) {
        List<String> gridScrambled = sv.getCells();
        // scrambles grid i.e. swap rows and cols

        return gridScrambled;
    }
    private List<String> rotate(SudokuVariation sv) {
        List<String> rotated = sv.getCells();
        // rotates grid; 0, 90, 180, or 270 degrees

        return rotated;
    }

}
