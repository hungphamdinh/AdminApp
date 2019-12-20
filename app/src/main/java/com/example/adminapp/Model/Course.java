package com.example.adminapp.Model;

public class Course {
    private String courseName;
    private String descript;
    private String discount;
    private String price;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;
    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    private String begin;
    private String end;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
    public String getCourseDoc() {
        return courseDoc;
    }

    public void setCourseDoc(String courseDoc) {
        this.courseDoc = courseDoc;
    }

    private String courseDoc;
    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    private String isBuy;
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    private String schedule;
    public String getTutorPhone() {
        return tutorPhone;
    }

    public void setTutorPhone(String tutorPhone) {
        this.tutorPhone = tutorPhone;
    }

    private String tutorPhone;
    public Course(){

    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
