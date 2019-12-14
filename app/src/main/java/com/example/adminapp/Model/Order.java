package com.example.adminapp.Model;

public class Order {
    private String CourseId;
    private String CourseName;
    private String Price;
    private String Discount;
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    public String getTutorPhone() {
        return tutorPhone;
    }

    public void setTutorPhone(String tutorPhone) {
        this.tutorPhone = tutorPhone;
    }

    private String tutorPhone;
    private String schedule;
    public Order(String courseId, String courseName, String price, String discount) {
        this.CourseId = courseId;
        this.CourseName = courseName;
        Price = price;
        Discount = discount;
    }
    public Order(){

    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        this.CourseId = courseId;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
