package com.a.hilaris.alpha.models;

import java.io.Serializable;
import java.util.List;

public class Sudoku implements Serializable {
    List<SudokuCellData> cells; // 162 characters, |1| | |3| | | |7| |...etc.
    String id;
    int difficulty; // 0,1,2,3: easy, medium, hard, insane
    public Sudoku() {
    }
    public Sudoku(List<SudokuCellData> cellDatas, String id, int difficulty) {
        this.cells = cellDatas;
        this.id = id;
        this.difficulty = difficulty;
    }

    public List<SudokuCellData> getCells() {
        return cells;
    }
    public void setCells(List<SudokuCellData> cells) {
        this.cells = cells;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
