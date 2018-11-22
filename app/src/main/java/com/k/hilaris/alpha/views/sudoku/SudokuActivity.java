package com.k.hilaris.alpha.views.sudoku;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.k.hilaris.alpha.R;
import com.k.hilaris.alpha.models.Sudoku;

import org.w3c.dom.Text;

public class SudokuActivity extends AppCompatActivity implements InputButtonsGridFragment.TextClicked {

    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed(){
        if(mOnKeyBackPressedListener != null){
            mOnKeyBackPressedListener.onBack();
        }else{
            super.onBackPressed();
        }
    }

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

    @Override
    public void sendText(String text){
        SudokuGridFragment sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        sudokuGridFragment.getInput(text);
    }
}
