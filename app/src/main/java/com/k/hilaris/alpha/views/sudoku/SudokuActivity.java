package com.k.hilaris.alpha.views.sudoku;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.k.hilaris.alpha.R;

public class SudokuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.SudokuGridFragment, new SudokuGridFragment());
        ft.add(R.id.InputButtonsFragment, new InputButtonsGridFragment());
        ft.commit();
    }
}
