package com.k.hilaris.alpha.models;

import java.io.Serializable;
import java.util.List;

public class Sudoku implements Serializable {
    List<String> cells; // 162 characters, |1| | |3| | | |7| |...etc.
    String id;
    int difficulty; // 0,1,2,3: easy, medium, hard, insane
    public Sudoku() {
    }
    public Sudoku(List<String> cells, String id, int difficulty) {
        this.cells = cells;
        this.id = id;
        this.difficulty = difficulty;
    }

    public List<String> getCells() {
        return cells;
    }
    public void setCells(List<String> cells) {
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
