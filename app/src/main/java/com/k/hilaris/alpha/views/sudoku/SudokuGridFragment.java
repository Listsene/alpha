package com.k.hilaris.alpha.views.sudoku;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.k.hilaris.alpha.adapters.SudokuGridAdapter;
import com.k.hilaris.alpha.models.Sudoku;
import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.SudokuCellData;
import com.k.hilaris.alpha.models.SudokuVariation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SudokuGridFragment extends Fragment implements SudokuActivity.onKeyBackPressedListener{
    private GridView gridView;
    private Sudoku grid;
    private SudokuGridAdapter Adapter;
    String serializedObject;
    List<SudokuCellData> cellList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sudoku_grid, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        serializedObject = sharedPreferences.getString("cellDataList", null);
        Gson gson = new Gson();
        if(serializedObject != null){
            Type type = new TypeToken<List<SudokuCellData>>(){}.getType();
            cellList = gson.fromJson(serializedObject,type);
        }


        grid = createSudoku();
        gridView = view.findViewById(R.id.SudokuGridView);
        Adapter = new SudokuGridAdapter(getContext(), grid);
        gridView.setAdapter(Adapter);


        return view;
    }
    public Sudoku createSudoku() { // Create Sample Sudoku Board for testing
        grid = new Sudoku();
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
        if(serializedObject == null){
            for(int i = 0; i < array.length; i++) {
                String cell = array[i];
                cellList.add(new SudokuCellData(cell));
            }
        }
        grid.setCells(cellList);
        return grid;
    }

    public Sudoku createCompleteSudoku() { // Create Sample Sudoku Board for testing
        grid = new Sudoku();
        List<SudokuCellData> cellList = new ArrayList<>();
        String cells =
                "4|6|9|8|2|5|7|1|3|" +
                "8|1|3|6|7|4|2|9|5|" +
                "2|7|5|9|3|1|4|6|8|" +
                "6|8|4|1|5|3|9|7|2|" +
                "9|5|2|7|4|6|3|8|1|" +
                "7|3|1|2|9|8|6|5|4|" +
                "5|9|7|4|1|2|8|3|6|" +
                "1|2|6|3|8|7|5|4|9|" +
                "3|4|8|5|6|9|1|2|7|";
        String[] array = cells.split("[|]", 0);
        for(int i = 0; i < array.length; i++) {
            String cell = array[i];
            cellList.add(new SudokuCellData(cell));
        }
        grid.setCells(cellList);
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
        List<SudokuCellData> list = grid.getCells();

        if(nSelectedPos != -1){
            SudokuCellData cellData = list.get(nSelectedPos);
            switch(input)
            {
                case "Memo" :
                    if(cellData.getNumber() != null){
                        String preInput = cellData.getNumber();
                        int pos = Integer.parseInt(preInput)-1;
                        cellData.setInput(preInput);
                        cellData.setbMemoByPos(pos);
                    }
                    Adapter.notifyDataSetChanged();
                    break;
                case "Clear" :
                    cellData.clearMemo();
                    cellData.setInput("");
                    Adapter.notifyDataSetChanged();
                    break;
                case "Enter" :
                    if(cellData.getNumber() != null){
                        String preInput = cellData.getNumber();
                        cellData.setInput(preInput);
                        cellData.clearMemo();
                    }
                    Adapter.notifyDataSetChanged();
                    break;
                case "9" :
                    newGame();
                    Adapter.notifyDataSetChanged();
                    break;
                default:
                    cellData.setNumber(input);
            }
        }
        //Adapter.notifyDataSetChanged();
    }
    @Override
    public void onBack(){
        SudokuActivity activity = (SudokuActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
        saveCellState();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        ((SudokuActivity) context).setOnKeyBackPressedListener(this);
    }

    public void saveCellState(){
        setList("cellDataList", cellList);
    }

    public <T> void setList(String key, List<T> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key,json);
    }

    public void set(String key, String value){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void newGame(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("cellDataList").commit();
        serializedObject = pref.getString("cellDataList", null);
        cellList = new ArrayList<>();
        grid = createSudoku();
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
