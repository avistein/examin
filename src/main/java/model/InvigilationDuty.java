package model;

import javafx.beans.property.SimpleStringProperty;

public class InvigilationDuty extends Exam{

    private SimpleStringProperty profId;
    private SimpleStringProperty roomNo;

    public InvigilationDuty() {
        this.profId = new SimpleStringProperty("");
        this.roomNo = new SimpleStringProperty("");
    }

    public String getProfId() {
        return profId.get();
    }


    public void setProfId(String profId) {
        this.profId.set(profId);
    }

    public String getRoomNo() {
        return roomNo.get();
    }


    public void setRoomNo(String roomNo) {
        this.roomNo.set(roomNo);
    }
}
