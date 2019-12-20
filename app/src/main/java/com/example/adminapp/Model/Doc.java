package com.example.adminapp.Model;

public class Doc {
    private String courseId;
    private String docName;
    private String docUrl;
    private String type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Doc(){}
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }
}
