package com.abhishek.cambridgeapp1.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Teacher {

    private String id;
    private String name;
    private List<String> branch;
    private String email;
    private String dob;
    private String phone;
    private String imageUrl;
    private String gender;
    private boolean isAdmin;
    private List<HashMap<String,String>> subjectsHandlingNames;

    public Teacher() {
    }

    public Teacher(String id, String name, String email, String phone, String gender, String dob, String imageUrl) {
        this.id = id;
        this.name = name;
        this.branch = new ArrayList<>();
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.imageUrl = imageUrl;
        this.isAdmin = false;
        this.subjectsHandlingNames = new ArrayList<>();
    }

    public Teacher(String id, String name, List<String> branch, String email, String dob, String phone, String imageUrl, String gender, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.branch = branch;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.isAdmin = isAdmin;
        this.subjectsHandlingNames = new ArrayList<>();
    }

    public Teacher(String id, String name, List<String> branch, String email, String dob, String phone, String imageUrl, String gender,
                   boolean isAdmin, List<HashMap<String,String>> subjectsHandlingNames) {
        this.id = id;
        this.name = name;
        this.branch = branch;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.isAdmin = isAdmin;
        this.subjectsHandlingNames = subjectsHandlingNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBranch() {
        return branch;
    }

    public void setBranch(List<String> branch) {
        this.branch = branch;
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

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<HashMap<String,String>> getSubjectsHandlingNames() {
        return subjectsHandlingNames;
    }

    public void setSubjectsHandlingNames(List<HashMap<String,String>> subjectsHandlingNames) {
        this.subjectsHandlingNames = subjectsHandlingNames;
    }
}
