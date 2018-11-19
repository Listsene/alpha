package com.k.hilaris.alpha.models;

public class SudokuCellData {
    String input;
    Boolean[] bMemo = new Boolean[9];

    public SudokuCellData(String input) {this.input = input;}



    public String getInput() {return input;}

    public void setInput(String input){ this.input = input; }

    public Boolean[] getbMemo() { return bMemo; }

    public Boolean getbMemoByPos(int nPos) { return bMemo[nPos]; }

    public void clearMemo() { this.bMemo = new Boolean[9]; }

    public void setbMemoByPos(int nPos){
        Boolean bl = false;

        if(this.bMemo[nPos] == null || this.bMemo[nPos] == false){
            this.bMemo[nPos] = true;
        }else if(this.bMemo[nPos] == true){
            this.bMemo[nPos] = false;
        }

        for(int i=0; i<this.bMemo.length; i++){
            if(this.bMemo[i] == null || this.bMemo[i] == false){

            }else if(this.bMemo[i] == true){
                bl = true;
            }
        }

        if(bl == false){
            setInput("");
        }
    }
}
