package com.l.hilaris.alpha.views.sudoku.multiplayer.team;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.l.hilaris.alpha.models.Sudoku;
import com.l.hilaris.alpha.views.sudoku.InputButtonsGridFragment;
import com.l.hilaris.alpha.views.sudoku.multiplayer.MultiplayerSudokuActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TeamActivity extends MultiplayerSudokuActivity implements InputButtonsGridFragment.InputClicked {

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
    protected void Score() {
        scoreTv.setText(String.valueOf(score + score2));
    }

    @Override
    public void sendInput(String input){
        super.sendInput(input);
    }

}