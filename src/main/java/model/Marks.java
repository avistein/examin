package model;

import javafx.beans.property.SimpleStringProperty;

public class Marks extends Exam {

    private SimpleStringProperty regId;
    private SimpleStringProperty obtainedMarks;

    public Marks() {

        this.regId = new SimpleStringProperty("");
        this.obtainedMarks = new SimpleStringProperty("");
    }

    public String getRegId() {
        return regId.get();
    }


    public void setRegId(String regId) {
        this.regId.set(regId);
    }

    public String getObtainedMarks() {
        return obtainedMarks.get();
    }


    public void setObtainedMarks(String obtainedMarks) {
        this.obtainedMarks.set(obtainedMarks);
    }
}
