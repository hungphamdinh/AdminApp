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
import com.example.adminapp.Model.Tutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DetailUpdateTutorActivity extends AppCompatActivity {
    private EditText  password, edtUsername,edtExp;
    private Button btnInsert;
    private EditText emailId;
    private String phoneKey;
    private String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_update_tutor);
        edtUsername = (EditText)findViewById(R.id.edtTutorNameUpdate);
        password = (EditText)findViewById(R.id.edtPasswordTutorUpdate);
        emailId = (EditText)findViewById(R.id.edtTutorMailUpdate);
        edtExp=(EditText)findViewById(R.id.edtExperienceUpdate);
        btnInsert = findViewById(R.id.btnUpdateTT);
        setupUI(findViewById(R.id.parentTutor));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("Tutor");
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        if (getIntent() != null)
            phoneKey = getIntent().getStringExtra("phoneKey");
        if (!phoneKey.isEmpty() && phoneKey != null) {
            if (Common.isConnectedToInternet(this)) {
                updateTutor(table_user,phoneKey);

            } else {
                Toast.makeText(DetailUpdateTutorActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }

    public void updateTutor(final DatabaseReference table_user, final String phoneKey) {

        table_user.child(phoneKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                edtUsername.setText(tutor.getUsername());
                edtExp.setText(tutor.getExperience());
                emailId.setText(tutor.getEmail());
                password.setText(tutor.getPassword());
                btnInsert.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        if (Common.isConnectedToInternet(getBaseContext())) {
                            final ProgressDialog progress = new ProgressDialog(DetailUpdateTutorActivity.this);
                            progress.setTitle("Loading");
                            progress.setMessage("Wait while loading...");
                            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                            progress.show();

                            final String usernameTemp = edtUsername.getText().toString();
                            final String passwordTemp = password.getText().toString();
                            final String emailTemp = emailId.getText().toString();
                            final String expTemp=edtExp.getText().toString();
                            //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            if (usernameTemp.equals("") || passwordTemp.equals("") || emailTemp.isEmpty()||expTemp.isEmpty()) {
                                progress.dismiss();
                                Toast.makeText(DetailUpdateTutorActivity.this, "Please fill your inform", Toast.LENGTH_SHORT).show();
                            } else {
                                if (emailTemp.trim().matches(emailPattern)) {
                                    progress.dismiss();
                                    HashMap<String, Object> map = new HashMap<>();
                                    //User user = new User(usernameTemp, passwordTemp,"");
                                    map.put("username", usernameTemp);
                                    map.put("password", passwordTemp);
                                    map.put("email", emailTemp);//
                                    map.put("experience", expTemp);
                                    table_user.child(phoneKey).updateChildren(map);
                                    Toast.makeText(DetailUpdateTutorActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                    //finish();
                                }
                                else {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //      }

                        } else {
                            Toast.makeText(DetailUpdateTutorActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    hideSoftKeyboard(DetailUpdateTutorActivity.this);
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
