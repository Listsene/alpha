package com.l.hilaris.alpha.views.sudoku.singleplayer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;
import com.l.hilaris.alpha.views.sudoku.multiplayer.team.TeamActivity;
import com.l.hilaris.alpha.views.sudoku.multiplayer.versus.VersusActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SudokuGridFragment extends Fragment implements SudokuActivity.onKeyBackPressedListener, MultiplayerSudokuActivity.onKeyBackPressedListener, VersusActivity.onKeyBackPressedListener, TeamActivity.onKeyBackPressedListener{
    private GridView gridView;
    private SudokuVariation sudoku;
    private SudokuGridAdapter Adapter;
    private int score = 0;
    private int scoreCount = 0;
    private String mode;
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

        Intent i = getActivity().getIntent();
        sudoku = (SudokuVariation) i.getSerializableExtra("sudoku");

        getSavedState();
        sudoku.setScore(score);


        gridView = view.findViewById(R.id.SudokuGridView);
        Adapter = new SudokuGridAdapter(getContext(), sudoku);
        gridView.setAdapter(Adapter);

        Gson gson = new Gson();
        if(serializedObject != null){
            Type type = new TypeToken<List<SudokuCellData>>(){}.getType();
            cells = gson.fromJson(serializedObject,type);
            sudoku.setCells(cells);
        }else {
            cells = original;
            sudoku.setCells(cells);
        }

        return view;
    }

    public SudokuVariation getInput(String input){
        int nSelectedPos = Adapter.getnSelectedPos();
        SudokuCellData cellData = new SudokuCellData("");
        try {
            cellData = sudoku.getCells().get(nSelectedPos);
        } catch(ArrayIndexOutOfBoundsException exception) {
            Toast.makeText(getActivity(), "Click on a cell!", Toast.LENGTH_SHORT).show();
        }
        String number = cellData.getNumber();
        sudoku.setPosition(nSelectedPos);

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
                    if(number.equals(sudoku.getSolution().get(nSelectedPos))) {
                        cellData.clearMemo();
                        cellData.setSolved(true);
                        cellData.setInput(cellData.getNumber());
                        score = score + 100;
                        scoreCount = 0;
                        sudoku.setScore(score);
                        checkFinish();
                    }
                    else {
                        Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                        cellData.setInput("");
                        decreaseScore();
                        sudoku.setScore(score);
                    }
                    Adapter.notifyDataSetChanged();
                    break;
                default:
                    cellData.setNumber(input);
            }
        }
        return sudoku;
    }

    private Boolean validInput(String input, String number) {
        switch(input) {
            case "Memo":
                if(number==null || number.matches("\\s")) {
                    return false;
                }
                else {
                    break;
                }
            case "Enter":
                if(number==null || number.matches("\\s")) {
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
        switch(mode) {
            case "single":
                SudokuActivity sudokuaActivity = (SudokuActivity) getActivity();
                sudokuaActivity.setOnKeyBackPressedListener(null);
                sudokuaActivity.onBackPressed();
                break;
            case "versus":
                VersusActivity versusActivity = (VersusActivity) getActivity();
                versusActivity.setOnKeyBackPressedListener(null);
                versusActivity.onBackPressed();
                break;
            case "team":
                TeamActivity teamActivity = (TeamActivity) getActivity();
                teamActivity.setOnKeyBackPressedListener(null);
                teamActivity.onBackPressed();
                break;
            default:
                MultiplayerSudokuActivity multiplayerSudokuActivity = (MultiplayerSudokuActivity) getActivity();
                multiplayerSudokuActivity.setOnKeyBackPressedListener(null);
                multiplayerSudokuActivity.onBackPressed();
                break;
        }
        saveCellState();
        saveScore(score);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Intent i = getActivity().getIntent();
        sudoku = (SudokuVariation) i.getSerializableExtra("sudoku");
        mode = sudoku.getMode();

        switch(mode) {
            case "single":
                ((SudokuActivity) context).setOnKeyBackPressedListener(this);
                break;
            case "versus":
                ((VersusActivity) context).setOnKeyBackPressedListener(this);
                break;
            case "team":
                ((TeamActivity) context).setOnKeyBackPressedListener(this);
                break;
            default:
                ((MultiplayerSudokuActivity) context).setOnKeyBackPressedListener(this);
                break;
        }
    }

    public void saveScore(int score){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(sudoku.getId()+"score", score);
        editor.apply();
    }

    public void saveCellState(){
        setList(sudoku.getId()+"saved", cells);
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

        editor.remove(sudoku.getId()+"saved").apply();
        editor.remove(sudoku.getId()+"score").apply();
    }

    public void getSavedState(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref",0);
        serializedObject = sharedPreferences.getString(sudoku.getId()+"saved", null);
        score = sharedPreferences.getInt(sudoku.getId()+"score",0);

        Gson gson = new Gson();
        String StOriginal;
        StOriginal = sharedPreferences.getString(sudoku.getId(),null);
        Type type = new TypeToken<List<SudokuCellData>>(){}.getType();
        original = gson.fromJson(StOriginal,type);
    }

    public SudokuGridAdapter getAdapter() {
        return Adapter;
    }

    public void decreaseScore(){
        Double d = Math.pow(2, scoreCount);
        int minus = 10*(d.intValue());

        score = score-minus;
        scoreCount++;
    }

    public void checkFinish(){
        boolean isFinish = true;
        boolean solved;
        for(int i = 0; i < 81; i++){
            solved = cells.get(i).getSolved();
            if(!solved){
                isFinish = false;
                break;
            }
        }
        if(isFinish){
            switch(mode) {
                case "single":
                    SudokuActivity sudokuaActivity = (SudokuActivity) getActivity();
                    sudokuaActivity.setFinish();
                    break;
                case "versus":
                    VersusActivity versusActivity = (VersusActivity) getActivity();
                    versusActivity.setFinish();
                    break;
                case "team":
                    TeamActivity teamActivity = (TeamActivity) getActivity();
                    teamActivity.setFinish();
                    break;
                default:
                    MultiplayerSudokuActivity multiplayerSudokuActivity = (MultiplayerSudokuActivity) getActivity();
                    multiplayerSudokuActivity.setFinish();
            }
        }

    }
}