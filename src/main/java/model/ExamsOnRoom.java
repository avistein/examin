package model;

import java.util.ArrayList;
import java.util.List;

public class ExamsOnRoom extends RoomAllocation {

    private List<Exam> examsList;

    public ExamsOnRoom() {
        this.examsList = new ArrayList<>();
    }

    public List<Exam> getExamsList() {
        return examsList;
    }

    public void setExamsList(List<Exam> examsList) {
        this.examsList = examsList;
    }
}
