package com.l.hilaris.alpha.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SudokuCellData implements Serializable {
    String input;
    List<Memo> memo = new ArrayList<>();
    Boolean solved = false;

    public SudokuCellData(String input) {this.input = input;}

    public String getInput() {return input;}

    public void setInput(String input){ this.input = input; }

    public List<Memo> getMemo() { return memo; }

    public Boolean getSolved() { return solved; }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }
    public void clearMemo() {
        for(int i = 0; i < memo.size(); i++) {
            memo.get(i).setActive(false);
        }
    }

    public void addMemo(String number, Boolean active) {
        memo.add(new Memo(number, active));
    }
}
