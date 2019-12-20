package com.example.adminapp.View.InsertTutor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.adminapp.Presenter.InsertTutor.InsertTutorPresenter;
import com.example.adminapp.R;

import java.util.HashMap;

public class InsertTutorActivity extends AppCompatActivity implements IInsertView {
    private EditText edtPhone, password, edtUsername,edtExp;
    private Button btnInsert;
    private EditText emailId;
    private InsertTutorPresenter insertTutorPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_tutor);
        edtUsername = (EditText)findViewById(R.id.edtTutorName);
        edtPhone = (EditText)findViewById(R.id.edtTutorPhone);
        password = (EditText)findViewById(R.id.edtPasswordTutor);
        emailId = (EditText)findViewById(R.id.edtTutorMail);
        edtExp=(EditText)findViewById(R.id.edtExperience);
        btnInsert = findViewById(R.id.btnInsertTutor);
        final HashMap<String,Object>map=new HashMap<>();
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.put("userName",edtUsername.getText().toString());
                map.put("password",password.getText().toString());
                map.put("email",emailId.getText().toString());
                map.put("exp",edtExp.getText().toString());
                map.put("phone",edtPhone.getText().toString());
                insertTutorPresenter=new InsertTutorPresenter(InsertTutorActivity.this);
                insertTutorPresenter.insertTutor(map);
            }
        });


    }


    @Override
    public void onSuccessDisplay(String msg) {
        Toast.makeText(InsertTutorActivity.this,msg,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailedDisplay(String msg) {
        Toast.makeText(InsertTutorActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
