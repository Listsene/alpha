package com.k.hilaris.alpha.models;

import java.util.List;

public class SudokuGrid extends Sudoku {
    public List<String> solution;
    public String guid;

    public SudokuGrid() {
    }

    public SudokuGrid(List<String> cells, String id, List<String> solution, String guid) {
        super(cells, id);
        this.solution = solution;
        this.guid = guid;
    }

    public List<String> getSolution() {
        return solution;
    }

    public void setSolution(List<String> solution) {
        this.solution = solution;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
