package com.l.hilaris.alpha.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SudokuCellData implements Serializable {
    String input;
    String number;
    List<Memo> memo = new ArrayList<>();
    Boolean solved = false;
    int status = 0; // 0: default, 1: solved 2: other solved

    public void setNumber(String number){ this.number = number; }

    public String getNumber(){ return number; }

    public SudokuCellData(String input) {this.input = input;}

    public String getInput() {return input;}

    public void setInput(String input){ this.input = input; }

    public List<Memo> getMemo() { return memo; }

    public Boolean getSolved() { return solved; }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
