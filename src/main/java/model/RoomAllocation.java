package model;


import java.util.ArrayList;
import java.util.List;

public class RoomAllocation extends Classroom {

    private List<Student> studentList;
    private List<String> roomAllocationIdlist;
    private String examDetailsId;


    public RoomAllocation() {

        this.studentList = new ArrayList<>();
        this.roomAllocationIdlist = new ArrayList<>();
        this.examDetailsId = "";
    }


    public String getExamDetailsId() {
        return examDetailsId;
    }


    public void setExamDetailsId(String examDetailsId) {
        this.examDetailsId = examDetailsId;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<String> getRoomAllocationIdlist() {

        return roomAllocationIdlist;
    }

    public void setRoomAllocationIdlist(List<String> roomAllocationIdlist) {

        this.roomAllocationIdlist = roomAllocationIdlist;
    }
}

