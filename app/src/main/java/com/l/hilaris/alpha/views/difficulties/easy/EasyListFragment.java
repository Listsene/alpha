package com.l.hilaris.alpha.views.difficulties.easy;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.adapters.SudokuListAdapter;
import com.l.hilaris.alpha.models.SudokuCellData;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.utilities.ItemClickSupport;
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;
import com.l.hilaris.alpha.views.sudoku.SudokuBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class EasyListFragment extends Fragment {
    private java.util.List<SudokuVariation> sudokus = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SudokuListAdapter Adapter;
    public TextView scoreTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = view.findViewById(R.id.list);


        Adapter = new SudokuListAdapter(getContext(),sudokus);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(Adapter);

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        SudokuVariation sudoku = sudokus.get(position);
                        moveActivity(sudoku);
                    }
                });

        createSudokus();
        return view;
    }

    private void createSudokus() {
        SudokuVariation sudoku = new SudokuVariation();
        List<SudokuCellData> cells;
        List<String> solution;
        SudokuCellData cell;

        Gson gson;
        SharedPreferences preferences = this.getActivity().getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = preferences.edit();

        cells = new ArrayList<>();
        String sudokuCells =
                "4| |9||2||7|1|3|" +
                        "||3||7||2||5|" +
                        "2|7||9||1|4||8|" +
                        "|||1|5||9|7|2|" +
                        "|5||7|4||3|8|1|" +
                        "|||||8|||4|" +
                        "|9||4||2||3|6|" +
                        "|2|||8||5|4|9|" +
                        "3|||5||9|||7|";
        String[] splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
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
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 01");
        sudokus.add(sudoku);

        gson = new Gson();
        String json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 02
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =
                " |4| | |7| | |9| |" +
                        " | | | | |9|4| |5|" +
                        "7| | |4| |1| |6|3|" +
                        "9|5| | |3| | |1|7|" +
                        " | |1| | | |3| | |" +
                        "8|7| | |6| | |5|2|" +
                        "4|8| |5| |3| | |6|" +
                        "1| |5|6| | | | | |" +
                        " |3| | |2| | |4| |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells =
                "5|4|2|3|7|6|1|9|8|" +
                        "3|1|6|2|8|9|4|7|5|" +
                        "7|9|8|4|5|1|2|6|3|" +
                        "9|5|4|8|3|2|6|1|7|" +
                        "2|6|1|7|9|5|3|8|4|" +
                        "8|7|3|1|6|4|9|5|2|" +
                        "4|8|9|5|1|3|7|2|6|" +
                        "1|2|5|6|4|7|8|3|9|" +
                        "6|3|7|9|2|8|5|4|1|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 02");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 03
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   "6| | | |3| | | | |" +
                "3|5| |9| |1| |7| |" +
                " |9|8|7| |6|5| | |" +
                "2|6| | | | | |5| |" +
                " | |4|2| |8|7| | |" +
                " |8| | | | | |3|1|" +
                " | |5|6| |3|1|2| |" +
                " |2| |4| |7| |8|6|" +
                " | | | |8| | | |5|";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "6|1|7|8|3|5|4|9|2|" +
                "3|5|2|9|4|1|6|7|8|" +
                "4|9|8|7|2|6|5|1|3|" +
                "2|6|1|3|6|9|8|5|4|" +
                "5|3|4|2|1|8|7|6|9|" +
                "7|8|9|5|6|4|2|3|1|" +
                "8|4|5|6|9|3|1|2|7|" +
                "1|2|3|4|5|7|9|8|6|" +
                "9|7|6|1|8|2|3|4|5|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 03");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 04
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   " | |7| |6| | | | |" +
                "9|4| |1| |2| |7| |" +
                "6| |2|8| |5| | |9|" +
                "1| |4| | | | | |6|" +
                " |8| |6| |9| |5| |" +
                "7| | | | | |9| |2|" +
                "8| | |4| |3|1| |7|" +
                " |7| |2| |8| |6|3|" +
                " | | | |1| |2| | |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "5|1|7|9|6|4|3|2|8|" +
                "9|4|8|1|3|2|6|7|5|" +
                "6|3|2|8|7|5|4|1|9|" +
                "1|9|4|5|2|7|8|3|6|" +
                "2|8|3|6|4|9|7|5|1|" +
                "7|6|5|3|8|1|9|4|2|" +
                "8|2|6|4|5|3|1|9|7|" +
                "4|7|1|2|9|8|5|6|3|" +
                "3|5|9|7|1|6|2|8|4|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 04");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 05
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   " | |2| |7| |5| | |" +
                " |4| |5| |8|1|2| |" +
                " | | | | |4| |6|8|" +
                " |7|8| |4| |3|1| |" +
                "5| | | | | | | |6|" +
                " |3|6| |5| |8|9| |" +
                "2|8| |9| | | | | |" +
                " |5|4|6| |2| |3| |" +
                " | |9| |3| |2| | |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "8|6|2|1|7|9|5|4|3|" +
                "3|4|7|5|6|8|1|2|9|" +
                "1|9|5|3|2|4|7|6|8|" +
                "9|7|8|2|4|6|3|1|5|" +
                "5|2|1|8|9|3|4|7|6|" +
                "4|3|6|7|5|1|8|9|2|" +
                "2|8|3|9|1|7|6|5|4|" +
                "7|5|4|6|8|2|9|3|1|" +
                "6|1|9|4|3|5|2|8|7|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 05");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 06
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   " |7| |4| |8| |6|3|" +
                " | |6|2| |7|9|8| |" +
                " | | | |3| | | |2|" +
                " |6| | | | | |2|1|" +
                " | |7|9| |1|5| | |" +
                "4|3| | | | | |9| |" +
                "6| | | |9| | | | |" +
                " |1|4|3| |2|6| | |" +
                "2|9| |7| |5| |1| |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "9|7|2|4|5|8|1|6|3|" +
                "3|4|6|2|1|7|9|8|5|" +
                "1|8|5|6|3|9|7|4|2|" +
                "5|6|9|8|7|3|4|2|1|" +
                "8|2|7|9|4|1|5|3|6|" +
                "4|3|1|5|2|6|8|9|7|" +
                "6|5|3|1|9|4|2|7|8|" +
                "7|1|4|3|8|2|6|5|9|" +
                "2|9|8|7|6|5|3|1|4|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 06");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 07
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   " |4| | |1| | |9| |" +
                " |5|6|7| |9|1| | |" +
                "9| |2|4| | | | | |" +
                " |7|1| |6| |4|2| |" +
                "6| | | | | | | |7|" +
                " |2|8| |5| |3|1| |" +
                " | | | | |5|7| |2|" +
                " | |5|6| |2|9|3| |" +
                " |9| | |8| | |6| |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "7|4|3|5|1|6|2|9|8|" +
                "8|5|6|7|2|9|1|4|3|" +
                "9|1|2|4|3|8|6|7|5|" +
                "5|7|1|8|6|3|4|2|9|" +
                "6|3|9|2|4|1|8|5|7|" +
                "4|2|8|9|5|7|3|1|6|" +
                "3|6|4|1|9|5|7|8|2|" +
                "1|8|5|6|7|2|9|3|4|" +
                "2|9|7|3|8|4|5|6|4|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 07");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 08
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   "4| |8|2| |6|1| | |" +
                " |5|2|1| |9| |8| |" +
                "9| | | |4| | | | |" +
                "7| |9| | | |8| | |" +
                " |3| |7| |5| |1| |" +
                " | |5| | | |4| |6|" +
                " | | | |5| | | |8|" +
                " |8| |9| |4|7|6| |" +
                " | |7|3| |1|5| |9|";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "4|7|8|2|3|6|1|9|5|" +
                "3|5|2|1|7|9|6|8|4|" +
                "9|1|6|5|4|8|2|3|7|" +
                "7|6|9|4|1|2|8|5|3|" +
                "8|3|4|7|6|5|9|1|2|" +
                "1|2|5|8|9|3|4|7|6|" +
                "2|9|1|6|5|7|3|4|8|" +
                "5|8|3|9|2|4|7|6|1|" +
                "6|4|7|3|8|1|5|2|9|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 08");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 09
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   " |8| | |5| | | | |" +
                " |6|5|7| |3|2| | |" +
                "1| |2|4| |6| | |8|" +
                " |1|4| | | |5| | |" +
                "7| | |5| |2| | |3|" +
                " | |8| | | |6|2| |" +
                "8| | |6| |7|9| |5|" +
                " | |7|1| |9|8|4| |" +
                " | | | |4| | |6| |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "4|8|3|2|5|1|7|9|6|" +
                "9|6|5|7|8|3|2|1|4|" +
                "1|7|2|4|9|6|3|5|8|" +
                "2|1|4|3|6|8|5|7|9|" +
                "7|9|6|5|1|2|4|8|3|" +
                "5|3|8|9|7|4|6|2|1|" +
                "8|4|1|6|2|7|9|3|5|" +
                "6|5|7|1|3|9|8|4|2|" +
                "3|2|9|8|4|5|1|6|7|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 09");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        // Sudoku 10
        sudoku = new SudokuVariation();
        cells = new ArrayList<>();
        sudokuCells =   " | |7| |2| | | | |" +
                "8|3| |4| |7| | |1|" +
                " |1|2|3| |6| |4| |" +
                " |7|5| | | | |1| |" +
                "9| | |5| |8| | |4|" +
                " |8| | | | |6|2| |" +
                " |5| |9| |4|7|8| |" +
                "1| | |7| |2| |5|6|" +
                " | | | |8| |1| | |";
        splitCells = sudokuCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            cell = new SudokuCellData(splitCells[i]);
            if (!cell.getInput().equals(" ")) {
                cell.setSolved(true);
            }
            cells.add(cell);
        }
        sudoku.setCells(cells);

        solution = new ArrayList<>();
        solCells = "4|6|7|8|2|1|5|3|9|" +
                "8|3|9|4|5|7|2|6|1|" +
                "5|1|2|3|9|6|8|4|7|" +
                "6|7|5|2|4|3|9|1|8|" +
                "9|2|1|5|6|8|3|7|4|" +
                "3|8|4|1|7|9|6|2|5|" +
                "2|5|6|9|1|4|7|8|3|" +
                "1|9|8|7|3|2|4|5|6|" +
                "7|4|3|6|8|5|1|9|2|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 10");
        sudokus.add(sudoku);

        gson = new Gson();
        json = gson.toJson(cells);
        editor.putString(sudoku.getId(), json);
        editor.apply();

        Adapter.notifyDataSetChanged();
    }

    public void moveActivity(SudokuVariation sudoku) {
        Intent intent = getActivity().getIntent();
        String mode = (String) intent.getSerializableExtra("mode");
        if(mode.equals("single")) {
            intent = new Intent(getActivity(), SudokuBaseActivity.class);
            intent.putExtra("sudoku", sudoku);
            startActivity(intent);
        }
        else {
            intent = new Intent(getActivity(), MultiplayerSudokuActivity.class);
            intent.putExtra("sudoku", sudoku);
            startActivity(intent);
        }
    }
}
