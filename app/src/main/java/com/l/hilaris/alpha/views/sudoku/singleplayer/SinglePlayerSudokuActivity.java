package com.l.hilaris.alpha.views.sudoku.singleplayer;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.views.sudoku.InputButtonsGridFragment;
import com.l.hilaris.alpha.views.sudoku.SudokuBaseActivity;
import com.l.hilaris.alpha.views.sudoku.SudokuGridFragment;

public class SinglePlayerSudokuActivity extends SudokuBaseActivity implements InputButtonsGridFragment.InputClicked {

    public interface onKeyBackPressedListener {
        void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Sudoku.getId()+"time",fiveMinutes);

        editor.putInt(Sudoku.getId()+"score", score);
        editor.apply();
        if(mOnKeyBackPressedListener != null){
            mOnKeyBackPressedListener.onBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void sendInput(String input) {
        sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        SudokuVariation sudoku = sudokuGridFragment.getInput(input);
        if (score != sudoku.getScore()) { // checks if score is changed
            score = sudoku.getScore();
            Score();
        }
    }
}
