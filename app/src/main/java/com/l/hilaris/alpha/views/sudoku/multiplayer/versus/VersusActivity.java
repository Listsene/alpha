package com.l.hilaris.alpha.views.sudoku.multiplayer.versus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.models.SudokuVariation;
import com.l.hilaris.alpha.views.sudoku.InputButtonsGridFragment;
import com.l.hilaris.alpha.views.sudoku.SudokuGridFragment;
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class VersusActivity extends MultiplayerSudokuActivity implements InputButtonsGridFragment.InputClicked {

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

        scoreTv2.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendInput(String input){
        sudokuGridFragment = (SudokuGridFragment) getFragmentManager().findFragmentById(R.id.SudokuGridFragment);
        SudokuVariation sudoku = sudokuGridFragment.getInput(input);
        if (score != sudoku.getScore()) { // checks if score is changed
            score = sudoku.getScore();
            //channel.write(ByteBuffer.wrap(String.valueOf(score).getBytes()));
            connection.writeMessage(uniqueID + String.valueOf(score));
            Score();
        }
        String cell = sudoku.getCells().get(sudoku.getPosition()).getInput();
        if (!(cell.isEmpty() || cell.matches("\\s"))) { // checks if new solved cell
            connection.writeMessage("i" + String.valueOf(cell) + String.valueOf(sudoku.getPosition()));
        }
    }
}