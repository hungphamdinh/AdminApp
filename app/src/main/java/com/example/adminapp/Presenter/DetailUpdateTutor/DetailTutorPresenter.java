package com.example.adminapp.Presenter.DetailUpdateTutor;

import com.example.adminapp.View.DetailUpdateTutor.IDetailTutorView;

import java.util.HashMap;
import java.util.logging.Handler;

public class DetailTutorPresenter implements DetailUpdateTutorListener{
    private DetailTutor mainInterator;
    private IDetailTutorView detailTutorView;
    public DetailTutorPresenter(IDetailTutorView detailTutorView){
        this.detailTutorView=detailTutorView;
        mainInterator =new DetailTutor(this);
    }
    public void loadData(String phoneKey){
        mainInterator.updateTutor(phoneKey);
    }
    public void onClick(HashMap<String,Object>edtMap,String phoneKey){
        mainInterator.setData(edtMap,phoneKey);
    }
    @Override
    public void onSuccess(String msg) {
        detailTutorView.onSuccess(msg);
    }

    @Override
    public void onFailed(String msg) {
        detailTutorView.onFailed(msg);
    }

    @Override
    public void onLoading(int percent) {
        detailTutorView.onLoading(percent);
    }

    @Override
    public void onLoadData(HashMap<String, Object> map) {
        detailTutorView.onLoadData(map);
    }
}
