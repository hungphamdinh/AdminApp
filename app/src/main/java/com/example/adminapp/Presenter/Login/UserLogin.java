package com.example.adminapp.Presenter.Login;

import android.content.Context;

import com.example.adminapp.Common.Common;
import com.example.adminapp.Model.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLogin {
    private Context context;
    public IUserLoginListener userLoginListener;
    public UserLogin(IUserLoginListener userLoginListener){
        this.userLoginListener=userLoginListener;
    }
    public void isValidData(final String phone, final String password) {
            DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");
            adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (phone.equals("") || password.equals("")) {
                        userLoginListener.onLoginError("Vui lòng kiểm tra lại thông tin");
                    } else {
                        if (dataSnapshot.child(phone).exists()) {
                            Admin admin = dataSnapshot.child(phone).getValue(Admin.class);
                            //admin.setUsername(admin.getUsername());
                            if (password.equals(admin.getPassword())) {
                                userLoginListener.onLoginSucess("Đăng nhập thành công");
                            } else {
                                userLoginListener.onLoginError("Vui lòng kiểm tra lại mật khẩu");
                            }

                        } else {
                            userLoginListener.onLoginError("Số điện thoại này không tồn tại");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



}
