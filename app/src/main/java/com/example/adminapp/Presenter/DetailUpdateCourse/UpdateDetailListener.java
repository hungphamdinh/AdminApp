package com.example.adminapp.Presenter.DetailUpdateCourse;

import java.util.HashMap;

public interface UpdateDetailListener {
    void onSuccess(String msg);
    void onFailed(String msg);
    void onLoading(int percent);
    void onLoadData(HashMap<String,Object>data);
    void onLoadCourseDoc(HashMap<String,Object>data);
    void onNullItem(String msg);
}
