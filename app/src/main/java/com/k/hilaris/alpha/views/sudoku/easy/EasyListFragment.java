package com.k.hilaris.alpha.views.sudoku.easy;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.adapters.SudokuListAdapter;
import com.k.hilaris.alpha.models.SudokuCellData;
import com.k.hilaris.alpha.models.SudokuVariation;
import com.k.hilaris.alpha.utilities.ItemClickSupport;
import com.k.hilaris.alpha.views.sudoku.singleplayer.SudokuActivity;

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
        SudokuVariation grid = new SudokuVariation();
        List<SudokuCellData> cells = new ArrayList<>();
        List<String> solution = new ArrayList<>();
        SudokuCellData cell;

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
        String[] sudoku = sudokuCells.split("[|]", 0);
            for (int i = 0; i < sudoku.length; i++) {
                cell = new SudokuCellData(sudoku[i]);
                if (!cell.getInput().equals(" ")) {
                    cell.setSolved(true);
                }
                cells.add(cell);
            }
        grid.setCells(cells);

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
        sudoku = solCells.split("[|]", 0);
        for (int i = 0; i < sudoku.length; i++) {
            solution.add(sudoku[i]);
        }
        grid.setSolution(solution);
        grid.setId("Sudoku 01");
        sudokus.add(grid);

        Adapter.notifyDataSetChanged();
    }

    public void moveActivity(SudokuVariation sudoku) {
        Intent intent = new Intent(getActivity(), SudokuActivity.class);
        intent.putExtra("sudoku", sudoku);
        startActivity(intent);
    }
}

