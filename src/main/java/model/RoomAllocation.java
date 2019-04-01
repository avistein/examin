package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomAllocation extends Classroom {

    private List<Student> studentList;
    private Map<Integer, Integer> roomAllocationMap;
    private String examDetailsId;


    public RoomAllocation() {

        this.studentList = new ArrayList<>();
        this.roomAllocationMap = new HashMap<>();
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

    public Map<Integer, Integer> getRoomAllocationMap() {
        return roomAllocationMap;
    }

    public void setRoomAllocationMap(Map<Integer, Integer> roomAllocationMap) {
        this.roomAllocationMap = roomAllocationMap;
    }
}

