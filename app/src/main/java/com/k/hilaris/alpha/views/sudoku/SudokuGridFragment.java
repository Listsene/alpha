package com.k.hilaris.alpha.views.sudoku;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.k.hilaris.alpha.adapters.SudokuGridAdapter;
import com.k.hilaris.alpha.models.Memo;
import com.k.hilaris.alpha.models.Sudoku;
import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.SudokuCellData;
import com.k.hilaris.alpha.models.SudokuVariation;

import java.util.ArrayList;
import java.util.List;

public class SudokuGridFragment extends Fragment {
    private GridView gridView;
    private SudokuVariation grid;
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
    public SudokuVariation createSudoku() { // Create Sample Sudoku Board for testing
        grid = new SudokuVariation();
        List<SudokuCellData> cellList = new ArrayList<>();
        String cells =  "4|6| |8| |5| | |3|" +
                        " | |3| |7| | | | |" +
                        " |7|5|9| |1| |6| |" +
                        " |8|4| | | | |7| |" +
                        "9| | |7| |6| | |1|" +
                        " |3| | |2| |6|5| |" +
                        " |9| |4| |2|8|3| |" +
                        " | | | |8| |5| | |" +
                        "3| | |5| |9| |2|7|";
        String[] array = cells.split("[|]", 0);
        for(int i = 0; i < array.length; i++) {
            String cell = array[i];
            cellList.add(new SudokuCellData(cell));
        }
        grid.setCells(cellList);

        List<String> solution = new ArrayList<>();
        String solCells =
                        "4|6|9|8|2|5|7|1|3|" +
                        "8|1|3|6|7|4|2|9|5|" +
                        "2|7|5|9|3|1|4|6|8|" +
                        "6|8|4|1|5|3|9|7|2|" +
                        "9|5|2|7|4|6|3|8|1|" +
                        "7|3|1|2|9|8|6|5|4|" +
                        "5|9|7|4|1|2|8|3|6|" +
                        "1|2|6|3|8|7|5|4|9|" +
                        "3|4|8|5|6|9|1|2|7|";
        array = solCells.split("[|]", 0);
        for(int i = 0; i < array.length; i++) {
            String cell = array[i];
            solution.add(cell);
        }
        grid.setSolution(solution);
        return grid;
    }

   /* public SudokuVariation createVariation(Sudoku sudoku) {
        SudokuVariation sv = new SudokuVariation(sudoku);
        sv.setGuid(createGUID(sv));
        sv.setCells(randomizeTokens(sv));
        sv.setCells(scrambleGrid(sv));
        sv.setCells(rotate(sv));
        return sv;
    }*/

    public void getInput(String input){
        int nSelectedPos = Adapter.getnSelectedPos();
        SudokuCellData cellData = new SudokuCellData(" ");
        try {
            cellData = grid.getCells().get(nSelectedPos);
        } catch(ArrayIndexOutOfBoundsException exception) {
            Toast.makeText(getActivity(), "Click on a cell!", Toast.LENGTH_SHORT).show();
        }
        String number = cellData.getInput();

        if(!cellData.getSolved()) {
            switch(input)
            {
                case "Memo" :
                    List<Memo> memo = cellData.getMemo();
                    Boolean exists = false;
                    int position = 0;
                    int memoActiveCount = 0;
                    for(int i = 0; i < memo.size(); i++) {
                        if(memo.get(i).getActive()) {
                            memoActiveCount++;
                        }
                        if(memoExists(number, memo.get(i))) {
                            position = i;
                            exists = true;
                        }
                    }
                    if(exists) {
                        removeMemo(memo.get(position), memoActiveCount, cellData);
                    }
                    else {
                        cellData.addMemo(number, true);
                    }
                    Adapter.notifyDataSetChanged();
                    break;
                case "Clear" :
                    cellData.clearMemo();
                    cellData.setInput("");
                    Adapter.notifyDataSetChanged();
                    break;
                case "Enter" :
                    if(number.equals(grid.getSolution().get(nSelectedPos))) {
                        cellData.clearMemo();
                        cellData.setSolved(true);
                    }
                    else {
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                        cellData.setInput("");
                    }
                    Adapter.notifyDataSetChanged();
                    break;
                default:
                    cellData.setInput(input);
            }
        }
    }

    private boolean memoExists(String number, Memo memo) {
        return memo.getNumber().equals(number);
    }

    private void removeMemo(Memo memo, int memoActiveCount, SudokuCellData cellData) {
        memo.setActive(!memo.getActive()); // if memo is active then deactivate or vice-versa

        if(memoActiveCount == 1 && !memo.getActive()) {
            cellData.clearMemo();
            cellData.setInput("");
        }
    }

    private String createGUID(SudokuVariation sv) {
        String guid = "0710896a-1959-4d0d-87ba-dd3bcd02c948"; // example
        // makes unique guid
        return guid;
    }
   /* private List<String> randomizeTokens(SudokuVariation sv) {
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
    }*/

}
