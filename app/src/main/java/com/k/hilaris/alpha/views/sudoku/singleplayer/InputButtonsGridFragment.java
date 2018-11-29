package com.k.hilaris.alpha.views.sudoku.singleplayer;
/*
This fragment holds the View for the Input Buttons used to Solve the Sudoku.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.adapters.InputButtonsGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class InputButtonsGridFragment extends Fragment {
    private GridView gridView;
    private InputButtonsGridAdapter Adapter;

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

        Adapter = new InputButtonsGridAdapter(getContext(), prepareInputs(),clickCB);
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
