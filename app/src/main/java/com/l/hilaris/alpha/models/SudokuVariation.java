package com.l.hilaris.alpha.models;

import java.util.List;

public class SudokuVariation extends Sudoku {
    public List<String> solution;
    public String guid;

    public SudokuVariation() {
    }

    public SudokuVariation(Sudoku sudoku) {

        super(sudoku.getCells(), sudoku.getId(), sudoku.getDifficulty());
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
