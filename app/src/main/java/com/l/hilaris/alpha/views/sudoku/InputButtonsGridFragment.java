package com.l.hilaris.alpha.views.sudoku;
/*
This fragment holds the View for the Input Buttons used to Solve the Sudoku.
 */
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.adapters.InputButtonsGridAdapter;
import com.l.hilaris.alpha.models.SudokuVariation;

import java.util.ArrayList;
import java.util.List;

public class InputButtonsGridFragment extends Fragment {
    private GridView gridView;
    private InputButtonsGridAdapter Adapter;
    private SudokuVariation sudoku;

    InputClicked clickCB;
    public interface InputClicked{
        void sendInput(String input);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_grid, container, false);

        gridView = view.findViewById(R.id.InputGrid);

        clickCB = (InputClicked) getActivity();
        Intent i = getActivity().getIntent();
        sudoku = (SudokuVariation) i.getSerializableExtra("sudoku");

        Adapter = new InputButtonsGridAdapter(getContext(), prepareInputs(),clickCB,sudoku);
        gridView.setAdapter(Adapter);

        return view;
    }

    private List<String> prepareInputs() {
        List<String> inputs = new ArrayList<>();
        for(int number = 1; number < 10; number++) {
            inputs.add(String.valueOf(number));
        }
        inputs.add(getResources().getString(R.string.Clear));
        inputs.add(getResources().getString(R.string.Memo));
        inputs.add(getResources().getString(R.string.Submit));
        return inputs;
    }

    public InputButtonsGridAdapter getAdapter(){
        return Adapter;
    }
}
