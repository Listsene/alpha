package com.k.hilaris.alpha.models;

import java.io.Serializable;
import java.util.List;

public class Sudoku implements Serializable {
    List<String> cells; // 162 characters, |1| | |3| | | |7| |...etc.
    String id;
    public Sudoku() {
    }
    public Sudoku(List<String> cells, String id) {
        this.cells = cells;
        this.id = id;
    }

    public List<String> getCells() {
        return cells;
    }
    public void setCells(List<String> cells) {
        this.cells = cells;
    }
    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
}
