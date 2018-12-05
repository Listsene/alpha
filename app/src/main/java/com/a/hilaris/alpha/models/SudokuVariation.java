package com.a.hilaris.alpha.models;

import java.io.Serializable;
import java.util.List;

public class SudokuVariation extends Sudoku implements Serializable {
    public List<String> solution;
    public String guid;
    public int score;

    public SudokuVariation() {
        score = 0;
    }

    public SudokuVariation(Sudoku sudoku) {
        super(sudoku.getCells(), sudoku.getId(), sudoku.getDifficulty());
        score = 0;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
