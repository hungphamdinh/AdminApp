package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.adminapp.Common.Common;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InsertTutorActivity extends AppCompatActivity {
    private EditText edtPhone, password, edtUsername,edtExp;
    private Button btnInsert;
    private EditText emailId;
    private String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";

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
        setupUI(findViewById(R.id.parentSignUp));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("Tutor");
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        SignUp(table_user);


    }

    public void SignUp(final DatabaseReference table_user) {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog progress = new ProgressDialog(InsertTutorActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                    final String usernameTemp = edtUsername.getText().toString();
                    final String passwordTemp = password.getText().toString();
                    final String emailTemp = emailId.getText().toString();
                    final String expTemp=edtExp.getText().toString();
                    table_user.orderByChild("email").equalTo(emailTemp).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (usernameTemp.equals("") || passwordTemp.equals("") || edtPhone.getText().toString().equals("") || emailTemp.isEmpty()||expTemp.isEmpty()) {
                                progress.dismiss();
                                Toast.makeText(InsertTutorActivity.this, "Please fill your inform", Toast.LENGTH_SHORT).show();
                            } else {
                                if (emailTemp.trim().matches(emailPattern)) {
                                    if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                        progress.dismiss();
                                        Toast.makeText(InsertTutorActivity.this, "This phone number is exist", Toast.LENGTH_SHORT).show();
                                    }
                                    if(dataSnapshot.exists()){
                                        progress.dismiss();
                                        Toast.makeText(InsertTutorActivity.this, "This email is exist, please check your email", Toast.LENGTH_SHORT).show();                                            }
                                    else {
                                        progress.dismiss();
                                        HashMap<String, String> map = new HashMap<>();
                                        //User user = new User(usernameTemp, passwordTemp,"");
                                        map.put("username", usernameTemp);
                                        map.put("password", passwordTemp);
                                        map.put("email", emailTemp);//
                                        map.put("experience",expTemp);
                                        table_user.child(edtPhone.getText().toString()).setValue(map);
                                        Toast.makeText(InsertTutorActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                        //finish();


                                    }
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //      }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(InsertTutorActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(InsertTutorActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
