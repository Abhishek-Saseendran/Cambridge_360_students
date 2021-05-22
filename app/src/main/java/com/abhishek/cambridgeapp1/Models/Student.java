package com.abhishek.cambridgeapp1.Models;

public class Student {

    private String usn;
    private String name;
    private String branch;
    private String sem;
    private String section;
    private String email;
    private String dob;
    private String phone;
    private String imageUrl;
    private String gender;

    public Student() {
    }

    public Student(String usn, String name, String email, String phone, String gender, String dob, String imageUrl) {
        this.usn = usn;
        this.name = name;
        this.email = email;
        this.branch = "";
        this.sem = "";
        this.section = "";
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.imageUrl = imageUrl;
    }

    public Student(String usn, String name, String branch, String sem, String section, String email, String dob, String phone,
                   String imageUrl, String gender) {
        this.usn = usn;
        this.name = name;
        this.branch = branch;
        this.sem = sem;
        this.section = section;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.gender = gender;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
