package com.example.adminapp.Presenter.DetailUpdateTutor;

import java.util.HashMap;

public interface DetailUpdateTutorListener {
    void onSuccess(String msg);
    void onFailed(String msg);
    void onLoading(int percent);
    void onLoadData(HashMap<String,Object>map);
}
