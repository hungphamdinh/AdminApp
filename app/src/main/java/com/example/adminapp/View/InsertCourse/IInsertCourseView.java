package com.example.adminapp.View.InsertCourse;

public interface IInsertCourseView {
    void onSuccess(String msg);
    void onFailed(String msg);
    void onSuccessUploadStorage(String courseId);
    void onLoadDing(int percent);
}
