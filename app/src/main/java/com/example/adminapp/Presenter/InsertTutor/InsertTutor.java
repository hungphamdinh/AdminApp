package com.example.adminapp.Presenter.InsertTutor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InsertTutor {
    private TutorListener tutorListener;
    public InsertTutor(TutorListener tutorListener){
        this.tutorListener=tutorListener;
    }
    public void insert(final HashMap<String,Object>editText){
    final DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("Tutor");
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
            final String userName = editText.get("userName").toString();
            final String pass = editText.get("password").toString();
            final String phone = editText.get("phone").toString();
            final String email = editText.get("email").toString();
            final String exp=editText.get("exp").toString();
            final String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
            if (userName.equals("") || pass.equals("") || phone.equals("") || email.equals("")||exp.equals("")||userName.length()!=10) {
                tutorListener.onFailer("Please check your inform");
            } else {
                final boolean check = dataSnapshot.child(phone).exists();
                if (check == false) {
                    DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("Tutor");
                    emailRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (email.trim().matches(emailPattern)) {
                                if (check == true) {
                                    //progress.dismiss();
                                    tutorListener.onFailer("This phone is exist");
                                } else if (snapshot.exists()) {
                                    tutorListener.onFailer("This email is exist");
                                } else {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("username", userName);
                                    map.put("password", pass);
                                    map.put("email", email);//
                                    map.put("status", "offline");
                                    map.put("experience",exp);
                                    map.put("avatar", "default");
                                    table_user.child(phone).setValue(map);
                                    tutorListener.onSuccess("Sign up success");
                                }
                            } else {
                                tutorListener.onFailer("Invalid email");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    tutorListener.onFailer("This phone is exist");
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
        }
}
