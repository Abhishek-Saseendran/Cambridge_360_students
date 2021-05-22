package com.abhishek.cambridgeapp1.Models;

import java.util.List;

public class StudentSubjectEnrolled {

    private String usn;
    private String email;
    private String name;
    private String gender;
    private String imageUrl;
    private List<String> subjectEnrolledNames;

    public StudentSubjectEnrolled() {
    }

    public StudentSubjectEnrolled(String usn, String email, String name,String gender, String imageUrl, List<String> subjectEnrolledNames) {
        this.usn = usn;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.subjectEnrolledNames = subjectEnrolledNames;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSubjectEnrolledNames() {
        return subjectEnrolledNames;
    }

    public void setSubjectEnrolledNames(List<String> subjectEnrolledNames) {
        this.subjectEnrolledNames = subjectEnrolledNames;
    }
}
