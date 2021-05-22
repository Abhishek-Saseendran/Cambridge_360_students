package com.abhishek.cambridgeapp1.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Subjects {

    private String subjectId;
    private String subjectName;
    private int totalMarks;
    private Map<String, Integer> totalAttendance;
    private Map<String, ArrayList<String>> handledBy;
    private boolean isElective;
    private int electiveSet;
    private String sem;
    private String branch;

    public Subjects() {
    }

    public Subjects(String subjectId, String subjectName, int totalMarks, Map<String, Integer> totalAttendance,
                    Map<String, ArrayList<String>> handledBy, boolean isElective, int electiveSet, String sem, String branch) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.totalMarks = totalMarks;
        this.totalAttendance = totalAttendance;
        this.handledBy = handledBy;
        this.isElective = isElective;
        this.electiveSet = electiveSet;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Map<String, Integer> getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(Map<String, Integer> totalAttendance) {
        this.totalAttendance = totalAttendance;
    }

    public Map<String, ArrayList<String>> getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Map<String, ArrayList<String>> handledBy) {
        this.handledBy = handledBy;
    }

    public boolean isIsElective() {
        return isElective;
    }

    public void setIsElective(boolean elective) {
        isElective = elective;
    }

    public int getElectiveSet() {
        return electiveSet;
    }

    public void setElectiveSet(int electiveSet) {
        this.electiveSet = electiveSet;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
