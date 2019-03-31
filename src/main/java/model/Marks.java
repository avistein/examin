package model;

import javafx.beans.property.SimpleStringProperty;

public class Marks extends Exam {

    private SimpleStringProperty regId;

    public Marks() {
        this.regId = new SimpleStringProperty("");
    }

    public String getRegId() {
        return regId.get();
    }


    public void setRegId(String regId) {
        this.regId.set(regId);
    }
}
