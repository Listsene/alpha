package com.l.hilaris.alpha.views.sudoku.multiplayer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.l.hilaris.alpha.adapters.SudokuGridAdapter;
import com.l.hilaris.alpha.models.Memo;
import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuCellData;
import com.l.hilaris.alpha.models.SudokuVariation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MultiplayerSudokuGridFragment extends Fragment implements MultiplayerSudokuActivity.onKeyBackPressedListener{
    private GridView gridView;
    private SudokuVariation grid;
    private SudokuGridAdapter Adapter;
    private int score = 0;
    String serializedObject;
    List<SudokuCellData> cells = new ArrayList<>();
    List<SudokuCellData> original = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sudoku_grid, container, false);

        getSavedState();

        Intent i = getActivity().getIntent();
        grid = (SudokuVariation) i.getSerializableExtra("sudoku");

        grid.setScore(score);

        gridView = view.findViewById(R.id.SudokuGridView);
        Adapter = new SudokuGridAdapter(getContext(), grid);
        gridView.setAdapter(Adapter);

        Gson gson = new Gson();
        if(serializedObject != null){
            Type type = new TypeToken<List<SudokuCellData>>(){}.getType();
            cells = gson.fromJson(serializedObject,type);
            grid.setCells(cells);
            Adapter.notifyDataSetChanged();
        }else {
            cells = original;
            grid.setCells(cells);
        }

        return view;
    }

    public int getInput(String input){
        int nSelectedPos = Adapter.getnSelectedPos();
        SudokuCellData cellData = new SudokuCellData("");
        try {
            cellData = grid.getCells().get(nSelectedPos);
        } catch(ArrayIndexOutOfBoundsException exception) {
            Toast.makeText(getActivity(), "Click on a cell!", Toast.LENGTH_SHORT).show();
        }
        String number = cellData.getInput();

        if(solved(cellData) || !validInput(input, number)) {
            // do nothing
        }
        else {
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
                        score = score + 100;
                        grid.setScore(grid.getScore() + 100);
                    }
                    else {
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                        cellData.setInput("");
                        score = score - 10;
                        grid.setScore(grid.getScore() - 10);
                    }
                    Adapter.notifyDataSetChanged();
                    break;
                default:
                    cellData.setInput(input);
            }
        }
        return grid.getScore();
    }

    private Boolean validInput(String input, String number) {
        switch(input) {
            case "Memo":
                if(number.isEmpty() || number.matches("\\s")) {
                    return false;
                }
                else {
                    break;
                }
            case "Enter":
                if(number.isEmpty() || number.matches("\\s")) {
                    return false;
                }
                else {
                    break;
                }
            default:
                break;
        }
        return true;
    }
    private Boolean solved(SudokuCellData cellData) {
        return cellData.getSolved();
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
    @Override
    public void onBack(){
        MultiplayerSudokuActivity activity = (MultiplayerSudokuActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
        saveCellState();
        saveScore(score);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        ((MultiplayerSudokuActivity) context).setOnKeyBackPressedListener(this);
    }

    public void saveScore(int score){
        grid.setScore(score);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score", score);
        editor.apply();
    }

    public void saveCellState(){
        setList("cellDataList", cells);
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
        editor.apply();
    }

    public void newGame(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("cellDataList").apply();
        editor.remove("score").apply();
        cells = original;
        grid.setCells(cells);
        Adapter.notifyDataSetChanged();
    }

    public void getSavedState(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        serializedObject = sharedPreferences.getString("cellDataList", null);
        score = sharedPreferences.getInt("score",0);

        Gson gson = new Gson();
        String StOriginal;
        StOriginal = sharedPreferences.getString("original",null);
        Type type = new TypeToken<List<SudokuCellData>>(){}.getType();
        original = gson.fromJson(StOriginal,type);
    }

    public SudokuGridAdapter getAdapter() {
        return Adapter;
    }

}
