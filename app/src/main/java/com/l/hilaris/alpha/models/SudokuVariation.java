package com.l.hilaris.alpha.models;

import java.io.Serializable;
import java.util.List;

public class SudokuVariation extends Sudoku implements Serializable {
    public List<String> solution;
    public String guid;
    public String mode;
    public int score;
    public int position;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
