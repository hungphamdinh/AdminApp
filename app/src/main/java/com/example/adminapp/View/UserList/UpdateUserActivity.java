package com.example.adminapp.View.UserList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Tutor;
import com.example.adminapp.Model.User;
import com.example.adminapp.Notification.APIService;
import com.example.adminapp.Notification.Client;
import com.example.adminapp.Notification.Data;
import com.example.adminapp.Notification.MyRespone;
import com.example.adminapp.Notification.Sender;
import com.example.adminapp.Notification.Token;
import com.example.adminapp.R;
import com.example.adminapp.View.DetailUpdateTutor.DetailUpdateTutorActivity;
import com.example.adminapp.View.MainActivity;
import com.example.adminapp.ViewHolder.StaffViewHolder;
import com.example.adminapp.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    //private StaffAdapter staffAdapter;
    private FirebaseRecyclerAdapter<User, UserViewHolder> adapter;
    private FirebaseRecyclerAdapter<User, UserViewHolder> searchAdapter;
    private ArrayList<Tutor> tutorList;
    private MaterialSearchBar materialSearchBar;
    private List<String> suggestList = new ArrayList<>();
    private MaterialSpinner spinner,spinnerStatusUser;
    private String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("User");
        recyclerMenu = (RecyclerView) findViewById(R.id.listUser);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        recyclerMenu.setHasFixedSize(true);
        loadSuggest();
        loadActiveUser();
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_user);
        spinnerStatusUser=(MaterialSpinner)findViewById(R.id.statusSpinnerUser);
        setSpinner();
        materialSearchBar.setLastSuggestions(suggestList);
        if (getIntent() != null)
            userPhone = getIntent().getStringExtra("phoneUser");
        if (!userPhone.isEmpty() && userPhone != null) {
            if (Common.isConnectedToInternet(this)) {
            } else {
                Toast.makeText(UpdateUserActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //materialSearchBar.setTextColor(Color.parseColor("#000"));
        materialSearchBar.setCardViewElevation(10);
        setUpSearchBar();


    }

    private void setSpinner() {

        spinnerStatusUser.setItems("Đang chờ","Kích hoạt","Vô hiệu hóa");
        spinnerStatusUser.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(spinnerStatusUser.getSelectedIndex()==0){
                    loadWaitToCkUser();

                }
                else if(spinnerStatusUser.getSelectedIndex()==1){
                    loadActiveUser();
                }
                else {
                    loadInActiveUser();
                }
            }
        });
    }

    private void setUpSearchBar() {
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);//set suggest
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When searchbar is close, becoming original adapter
                if (!enabled)
                    recyclerMenu.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search is finish, show result
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.user_layout,
                UserViewHolder.class,
                userRef.orderByChild("username").equalTo(text.toString())//compare by itemName
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                //viewHolder.txtDescript.setText(model.getDescript());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profile);
                final User local = model;
                deleteUser(searchAdapter.getRef(position).getKey(), viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        showUpdateOrderDialog(searchAdapter.getRef(position).getKey());

                    }
                });

            }
        };
        recyclerMenu.setAdapter(searchAdapter);//Set adapter for RecycleView when search Result
    }

    private void loadSuggest() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tutor tutorItem = postSnapshot.getValue(Tutor.class);
                    suggestList.add(tutorItem.getUsername());//load list of suggest item

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadWaitToCkUser() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>
                (User.class, R.layout.user_layout,
                        UserViewHolder.class,
                        userRef.orderByChild("ckAccount").equalTo(0)) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, final User model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profile);
                deleteUser(adapter.getRef(position).getKey(), viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        showUpdateOrderDialog(adapter.getRef(position).getKey());
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
    private void loadActiveUser() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>
                (User.class, R.layout.user_layout,
                        UserViewHolder.class,
                        userRef.orderByChild("ckAccount").equalTo(1)) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, final User model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profile);
                deleteUser(adapter.getRef(position).getKey(), viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        showUpdateOrderDialog(adapter.getRef(position).getKey());
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
    private void loadInActiveUser() {
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>
                (User.class, R.layout.user_layout,
                        UserViewHolder.class,
                        userRef.orderByChild("ckAccount").equalTo(2)) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, final User model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profile);
                deleteUser(adapter.getRef(position).getKey(), viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        showUpdateOrderDialog(adapter.getRef(position).getKey());
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }


    private void deleteUser(final String key, UserViewHolder holder) {
        if (key != null) {
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDialog(key);
                }
            });
        } else {
            Log.e("Error key", key);
        }
    }

    private void deleteDialog(final String key) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Giảng viên vẫn đang hoạt động, bạn có chắc muốn xóa?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("ckAccount", 2);
                userRef.child(key).updateChildren(map);
                Toast.makeText(UpdateUserActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                suggestList.clear();
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    private void showUpdateOrderDialog(final String key) {
        LayoutInflater inflater=this.getLayoutInflater();
        View subView=inflater.inflate(R.layout.alert_dialog_update_status,null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateUserActivity.this);
        alertDialog.setTitle("Cập nhật tài khoản");
        alertDialog.setMessage("Chọn trạng thái");
        //alertDialog.create();
        spinner=(MaterialSpinner) subView.findViewById(R.id.statusSpinner);
        spinner.setItems("Đang chờ","Kích hoạt","Vô hiệu hóa");
        alertDialog.setView(subView);
        userRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                spinner.setSelectedIndex(user.getCkAccount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //alertDialog.show();
        final String localKey=key;
        alertDialog.setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                HashMap<String,Object>map=new HashMap<>();
                HashMap<String,Object>mapNotify=new HashMap<>();
                map.put("ckAccount",spinner.getSelectedIndex());
                mapNotify.put("sender",userPhone);
                mapNotify.put("reciever",key);
                mapNotify.put("adminName","Admin");
                mapNotify.put("msg","Tài khoản "+key+" đã thay đổi trạng thái");
                userRef.child(localKey).updateChildren(map);
                sendNotification(mapNotify);
            }
        });
        alertDialog.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendNotification(HashMap<String,Object>listChat) {
        final APIService apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        final String reciever=listChat.get("reciever").toString();
        final String sender=listChat.get("sender").toString();
        //      int ckAccount= (int) listChat.get("ckAccount");
        final String userName=listChat.get("adminName").toString();
        final String msg=listChat.get("msg").toString();
        DatabaseReference tokenRef=FirebaseDatabase.getInstance().getReference("Tokens");
        tokenRef.orderByKey().equalTo(reciever).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap:dataSnapshot.getChildren()){
                    Token token=childSnap.getValue(Token.class);
                    Data data=new Data(sender, R.drawable.doc,userName+": "+msg,"Thông báo",
                            reciever);
                    Sender send=new Sender(data,token.getToken());
                    apiService.sendNotification(send)
                            .enqueue(new Callback<MyRespone>() {
                                @Override
                                public void onResponse(Call<MyRespone> call, Response<MyRespone> response) {
                                    if(response.code()==200){
                                        if(response.body().success!=1){
                                            //signUpListener.onError("Failed");
                                            Log.e("Error respone",response.body().toString());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyRespone> call, Throwable t) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}