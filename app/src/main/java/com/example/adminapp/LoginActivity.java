package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminapp.Presenter.Login.ILoginView;
import com.example.adminapp.Presenter.Login.LoginPresenter;
import com.example.adminapp.View.MainActivity;
import com.rey.material.widget.CheckBox;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private EditText username,password;
    private Button login;
    private LoginPresenter loginPresenter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.edtPhoneLogin);
        password= (EditText)findViewById(R.id.edtPassword);
        login= (Button)findViewById(R.id.btnLogin);
        loginPresenter=new LoginPresenter(this);
        onLogin();
    }
    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
    }

    private void onLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=getProgressDialog();
                loginPresenter.onLogin(username.getText().toString(),password.getText().toString());
            }
        });
    }
    @Override
    public void setDisplaySuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("phoneUser",username.getText().toString());
        startActivity(intent);
    }

    @Override
    public void setDisplayError(String msg) {
        progressDialog.cancel();
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
