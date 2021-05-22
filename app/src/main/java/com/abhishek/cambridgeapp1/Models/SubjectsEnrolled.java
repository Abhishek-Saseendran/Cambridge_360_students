package com.abhishek.cambridgeapp1.Models;

public class SubjectsEnrolled {

    private String subjectId;
    private String subjectName;
    private float internal1;
    private float internal2;
    private float internal3;
    private float attendance;

    public SubjectsEnrolled() {
    }

    public SubjectsEnrolled(String subjectId, String subjectName, float internal1, float internal2, float internal3, float attendance) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.internal1 = internal1;
        this.internal2 = internal2;
        this.internal3 = internal3;
        this.attendance = attendance;
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

    public float getInternal1() {
        return internal1;
    }

    public void setInternal1(float internal1) {
        this.internal1 = internal1;
    }

    public float getInternal2() {
        return internal2;
    }

    public void setInternal2(float internal2) {
        this.internal2 = internal2;
    }

    public float getInternal3() {
        return internal3;
    }

    public void setInternal3(float internal3) {
        this.internal3 = internal3;
    }

    public float getAttendance() {
        return attendance;
    }

    public void setAttendance(float attendance) {
        this.attendance = attendance;
    }
}
