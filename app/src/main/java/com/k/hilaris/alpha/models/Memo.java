package com.k.hilaris.alpha.models;

import java.io.Serializable;

public class Memo implements Serializable {
    private String number;
    private Boolean active;
    public Memo(String number, Boolean active) {
        this.number = number;
        this.active = active;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
}
