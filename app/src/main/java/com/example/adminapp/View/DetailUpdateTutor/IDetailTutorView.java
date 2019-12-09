package com.example.adminapp.View.DetailUpdateTutor;

import java.util.HashMap;

public interface IDetailTutorView {
    void onSuccess(String msg);
    void onFailed(String msg);
    void onLoading(int percent);
    void onLoadData(HashMap<String,Object> map);
}
