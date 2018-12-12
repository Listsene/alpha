package com.l.hilaris.alpha.views.sudoku.multi;

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
import com.l.hilaris.alpha.views.sudoku.singleplayer.SudokuActivity;

import java.util.ArrayList;
import java.util.List;

public class MultiListFragment extends Fragment {
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
                " | | | |6|1| |5| |" +
                        " | |8| | | |2|1| |" +
                        " | | |2|5| | | |3|" +
                        "7| | | | | | |3| |" +
                        " | |3|8|1|5|9| | |" +
                        " |2| | | | | | |5|" +
                        "2| | | |8|7| | | |" +
                        " |3|4| | | |8| | |" +
                        " |7| |3|9| | | | |";
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
                "3|9|2|4|6|1|7|5|8|" +
                        "5|6|8|7|3|9|2|1|4|" +
                        "4|1|7|2|5|8|6|9|3|" +
                        "7|8|5|9|4|2|1|3|6|" +
                        "6|4|3|8|1|5|9|2|7|" +
                        "1|2|9|6|7|3|4|8|5|" +
                        "2|5|6|1|8|7|3|4|9|" +
                        "9|3|4|5|2|6|8|7|1|" +
                        "8|7|1|3|9|4|5|6|2|";
        splitCells = solCells.split("[|]", 0);
        for (int i = 0; i < splitCells.length; i++) {
            solution.add(splitCells[i]);
        }
        sudoku.setSolution(solution);
        sudoku.setId("Sudoku 21");
        sudokus.add(sudoku);

        gson = new Gson();
        String json = gson.toJson(cells);
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