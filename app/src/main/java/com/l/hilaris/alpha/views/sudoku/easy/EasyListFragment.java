package com.l.hilaris.alpha.views.sudoku.easy;

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

import com.google.gson.Gson;
import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.adapters.SudokuListAdapter;
import com.l.hilaris.alpha.models.SudokuCellData;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.utilities.ItemClickSupport;
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;
import com.l.hilaris.alpha.views.sudoku.singleplayer.SudokuActivity;

import java.util.ArrayList;
import java.util.List;

public class EasyListFragment extends Fragment {
    private java.util.List<SudokuVariation> sudokus = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SudokuListAdapter Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = view.findViewById(R.id.list);

        Adapter = new SudokuListAdapter(sudokus);
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
                "4|6| |8| |5| | |3|" +
                " | |3| |7| | | | |" +
                " |7|5|9| |1| |6| |" +
                " |8|4| | | | |7| |" +
                "9| | |7| |6| | |1|" +
                " |3| | |2| |6|5| |" +
                " |9| |4| |2|8|3| |" +
                " | | | |8| |5| | |" +
                "3| | |5| |9| |2|7|";
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
        cells = new ArrayList<>();
        sudokuCells =
                        " |4| | |7| | |9| |" +
                        " | | | | |9|4| |5|" +
                        "9|5| | |3| | |1|7|" +
                        " | |1| | | |3| | |" +
                        "8|7| | |6| | |5|2|" +
                        "4|8| |5| |3| | |6|" +
                        " |9| |4| |2|8|3| |" +
                        " | | | |8| |5| | |" +
                        "3| | |5| |9| |2|7|";
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
        sudoku.setId("Sudoku 02");
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
            intent = new Intent(getActivity(), SudokuActivity.class);
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

