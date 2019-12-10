package com.example.adminapp.View.DetailUpdateTutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.adminapp.Common.Common;
import com.example.adminapp.Presenter.DetailUpdateTutor.DetailTutorPresenter;
import com.example.adminapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailUpdateTutorActivity extends AppCompatActivity implements IDetailTutorView {
    private EditText edtPass, edtUsername,edtExp,edtProfile;
    private Button btnInsert, btnChooseFile;
    private EditText edtEmail;
    private String phoneKey;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private DetailTutorPresenter detailTutorPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_update_tutor);
        edtUsername = (EditText)findViewById(R.id.edtTutorNameUpdate);
        edtPass = (EditText)findViewById(R.id.edtPasswordTutorUpdate);
        edtEmail = (EditText)findViewById(R.id.edtTutorMailUpdate);
        edtExp=(EditText)findViewById(R.id.edtExperienceUpdate);
        edtProfile=(EditText)findViewById(R.id.edtProfile);
        btnInsert = (Button)findViewById(R.id.btnUpdateTT);
        btnChooseFile =(Button)findViewById(R.id.btnUpdateFile);
        detailTutorPresenter=new DetailTutorPresenter(this);
       // setupUI(findViewById(R.id.parentTutor));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("Tutor");
        if (getIntent() != null)
            phoneKey = getIntent().getStringExtra("phoneKey");
        if (!phoneKey.isEmpty() && phoneKey != null) {
            if (Common.isConnectedToInternet(this)) {
                detailTutorPresenter.loadData(phoneKey);
                onClickUpdate();

            } else {
                Toast.makeText(DetailUpdateTutorActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        onClickChooseImage();


    }

    private void onClickUpdate() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=getProgress();
                HashMap<String,Object> edtMap=new HashMap<>();
                edtMap.put("imageUri",imageUri);
                edtMap.put("name",edtUsername.getText().toString());
                edtMap.put("password",edtPass.getText().toString());
                edtMap.put("email",edtEmail.getText().toString());
                edtMap.put("profile",edtProfile.getText().toString());
                edtMap.put("exp",edtExp.getText().toString());
                detailTutorPresenter.onClick(edtMap,phoneKey);
            }
        });
    }

    private void onClickChooseImage() {
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        if(ContextCompat.checkSelfPermission(MyAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                chooseImage();
                btnChooseFile.setEnabled(true);
                //         }
//            else{
//                ActivityCompat.requestPermissions(MyAccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//
//            }

            }
        });
    }

//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//
//    public void setupUI(View view) {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(DetailUpdateTutorActivity.this);
//                    return false;
//                }
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }
    private ProgressDialog getProgress(){
        progressDialog=new ProgressDialog(DetailUpdateTutorActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Đang tải...");
        progressDialog.setProgress(0);
        progressDialog.show();
        return progressDialog;
    }
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
//        {
        chooseImage();
//        }
//        else
//            Toast.makeText(MyAccountActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            edtProfile.setText(data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(DetailUpdateTutorActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(DetailUpdateTutorActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailed(String msg) {
        progressDialog.cancel();
        Toast.makeText(DetailUpdateTutorActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(int percent) {
        progressDialog.setProgress(percent);
    }

    @Override
    public void onLoadData(HashMap<String, Object> map) {
        edtUsername.setText(map.get("userName").toString());
        edtPass.setText(map.get("password").toString());
        edtExp.setText(map.get("exp").toString());
        edtEmail.setText(map.get("email").toString());
        edtProfile.setText(map.get("image").toString());
    }
}
