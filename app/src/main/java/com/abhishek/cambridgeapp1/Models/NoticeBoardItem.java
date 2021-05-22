package com.abhishek.cambridgeapp1.Models;

public class NoticeBoardItem {

    private String fileId;
    private String name;
    private String description;
    private String fileUrl;
    private String extension;
    private boolean isImage;
    private String setBy;
    private String author;
    private String branch;
    private String sem;

    public NoticeBoardItem() {
    }

    public NoticeBoardItem(String fileId, String name, String description, String fileUrl, String extension, boolean isImage, String setBy,
                           String author, String branch, String sem) {
        this.fileId = fileId;
        this.name = name;
        this.description = description;
        this.fileUrl = fileUrl;
        this.extension = extension;
        this.isImage = isImage;
        this.setBy = setBy;
        this.author = author;
        this.branch = branch;
        this.sem = sem;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isIsImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public String getSetBy() {
        return setBy;
    }

    public void setSetBy(String setBy) {
        this.setBy = setBy;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
