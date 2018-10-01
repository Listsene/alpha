package com.k.hilaris.alpha.Models;

import java.io.Serializable;
import java.util.List;

public class SudokuGrid implements Serializable {
    List<String> cells;
    public SudokuGrid() {
    }
    public SudokuGrid(List<String> cells) {
        this.cells = cells;
    }

    public List<String> getCells() {
        return cells;
    }
    public void setCells(List<String> cells) {
        this.cells = cells;
    }
}
