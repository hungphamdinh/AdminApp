package com.example.adminapp.Presenter.InsertTutor;

import com.example.adminapp.View.InsertTutor.IInsertView;

import java.util.HashMap;

public class InsertTutorPresenter implements TutorListener {
    private InsertTutor mainInterator;
    private IInsertView insertView;
    public InsertTutorPresenter(IInsertView insertView){
        this.insertView=insertView;
        mainInterator=new InsertTutor(this);
    }
    public void insertTutor(HashMap<String,Object>map){
        mainInterator.insert(map);
    }
    @Override
    public void onSuccess(String msg) {
        insertView.onSuccessDisplay(msg);
    }

    @Override
    public void onFailer(String msg) {
        insertView.onFailedDisplay(msg);
    }
}
