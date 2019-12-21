package com.example.adminapp.View.CourseList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Course;
import com.example.adminapp.Model.User;
import com.example.adminapp.Notification.APIService;
import com.example.adminapp.Notification.Client;
import com.example.adminapp.Notification.Data;
import com.example.adminapp.Notification.MyRespone;
import com.example.adminapp.Notification.Sender;
import com.example.adminapp.Notification.Token;
import com.example.adminapp.Presenter.CourseList.CourseListPresenter;
import com.example.adminapp.R;
import com.example.adminapp.View.DetailUpdateCourse.DetailUpdateCourseActivity;
import com.example.adminapp.View.UserList.UpdateUserActivity;
import com.example.adminapp.ViewHolder.CourseViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseActivity extends AppCompatActivity implements CourseListView {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference course;
    private FirebaseDatabase database;
    private ArrayList<Course>courseList;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> adapter;
    private FirebaseRecyclerAdapter<Course, CourseViewHolder> searchAdapter;
    private CourseListPresenter courseListPresenter;
    private String docKey;
    private List<String> suggestList=new ArrayList<>();
    private MaterialSpinner spinnerStatusCourse,spinner;
    private MaterialSearchBar materialSearchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        database = FirebaseDatabase.getInstance();
        course = database.getReference("Course");
        recyclerMenu = (RecyclerView) findViewById(R.id.listCourse);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        spinnerStatusCourse =(MaterialSpinner)findViewById(R.id.statusSpinnerCourse);
        loadListCourse();
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.search_bar);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        //materialSearchBar.setTextColor(Color.parseColor("#000"));
        materialSearchBar.setCardViewElevation(10);
        setUpSearchBar();
        setSpinner();
    }

    private void setSpinner() {

        spinnerStatusCourse.setItems("Khóa đang có","Đã mua","Đang đợi xử lý","Chưa mua");
        spinnerStatusCourse.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(spinnerStatusCourse.getSelectedIndex()==0){
                    loadListCourse();
                }
                else if(spinnerStatusCourse.getSelectedIndex()==1){
                    loadListBuyCourse();
                }
                else if(spinnerStatusCourse.getSelectedIndex()==2){
                    loadListWaitBuyCourse();
                }
                else {
                    loadListNotBuyCourse();
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
                ArrayList<String> suggest=new ArrayList<String>();
                for(String search:suggestList ){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
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
                if(!enabled)
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
        searchAdapter=new FirebaseRecyclerAdapter<Course, CourseViewHolder>(
                Course.class,
                R.layout.course_layout,
                CourseViewHolder.class,
                course.orderByChild("courseName").equalTo(text.toString())//compare by itemName
        ) {
            @Override
            protected void populateViewHolder(CourseViewHolder viewHolder, Course model, int position) {
                courseListPresenter=new CourseListPresenter(CourseActivity.this,viewHolder);
                viewHolder.txtName.setText(model.getCourseName());
                viewHolder.txtPrice.setText("Giá: " + model.getPrice());
                viewHolder.txtDescript.setText(model.getDescript());
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .centerCrop()
                        .into(viewHolder.imgCourse);
                final Course local=model;
                courseListPresenter.onLoadCourse(searchAdapter.getRef(position).getKey());
                checkCourse(viewHolder, position);
                deleteCourse(searchAdapter.getRef(position).getKey(),viewHolder);
            }
        };
        recyclerMenu.setAdapter(searchAdapter);//Set adapter for RecycleView when search Result
    }
    private void loadSuggest() {
        course.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren() ){
                            Course courseItem=postSnapshot.getValue(Course.class);
                            suggestList.add(courseItem.getCourseName());//load list of suggest item

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void loadListNotBuyCourse() {
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                (Course.class, R.layout.course_layout,
                        CourseViewHolder.class,
                        course.orderByChild("isBuy").equalTo("false")) {
            @Override
            protected void populateViewHolder(final CourseViewHolder viewHolder, final Course model, final int position) {
                //viewHolder.itemView.setVisibility(View.VISIBLE);
//                DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
//                courseRef.child(adapter.getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Course courseItem=dataSnapshot.getValue(Course.class);
//                        if(courseItem.getStatus()==1){
//                            viewHolder.txtIsBuy.setVisibility(View.GONE);
//                            setUpViewHolder(viewHolder, model, position);
//                        }
//                        else {
//                            viewHolder.itemView.setVisibility(View.GONE);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                if(model.getStatus()==0){
                    viewHolder.txtIsBuy.setVisibility(View.VISIBLE);
                    setUpViewHolder(viewHolder, model, position);
                }
                else if(model.getStatus()==2){
                    viewHolder.txtIsBuy.setVisibility(View.VISIBLE);
                    viewHolder.txtIsBuy.setText("(Đang chờ xử lý)");
                    setUpViewHolder(viewHolder, model, position);
//                    viewHolder.itemView.setVisibility(View.GONE);
                }
                else {
                    viewHolder.txtIsBuy.setVisibility(View.GONE);
                    setUpViewHolder(viewHolder, model, position);
                }
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }

    private void checkCourse(CourseViewHolder viewHolder, final int position) {
        viewHolder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateOrderDialog(adapter.getRef(position).getKey());
            }
        });
    }

    private void loadListWaitBuyCourse() {
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                (Course.class, R.layout.course_layout,
                        CourseViewHolder.class,
                        course.orderByChild("status").equalTo(2)) {
            @Override
            protected void populateViewHolder(final CourseViewHolder viewHolder, final Course model, int position) {
                //viewHolder.itemView.setVisibility(View.VISIBLE);
                    setUpViewHolder(viewHolder, model, position);
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
    private void loadListBuyCourse() {
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                (Course.class, R.layout.course_layout,
                        CourseViewHolder.class,
                        course.orderByChild("isBuy").equalTo("true")) {
            @Override
            protected void populateViewHolder(final CourseViewHolder viewHolder, final Course model, final int position) {
                //viewHolder.itemView.setVisibility(View.VISIBLE);

                if(model.getStatus()==0){
                    viewHolder.txtIsBuy.setVisibility(View.VISIBLE);
                    setUpViewHolder(viewHolder, model, position);
                }
                else {
                    viewHolder.txtIsBuy.setVisibility(View.GONE);
                    setUpViewHolder(viewHolder, model, position);
//                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                }
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }
    private void loadListCourse() {
        adapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                (Course.class, R.layout.course_layout,
                        CourseViewHolder.class,
                        course.orderByChild("status").equalTo(1)) {
            @Override
            protected void populateViewHolder(final CourseViewHolder viewHolder, final Course model, int position) {
                    //viewHolder.itemView.setVisibility(View.VISIBLE);
                    setUpViewHolder(viewHolder, model, position);
            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }

    private void setUpViewHolder(CourseViewHolder viewHolder, Course model, int position) {
        viewHolder.txtName.setText(model.getCourseName());
        viewHolder.txtPrice.setText("Giá: " + model.getPrice());
        viewHolder.txtDescript.setText(model.getDescript());
        Glide.with(getApplicationContext())
                .load(model.getImage())
                .centerCrop()
                .into(viewHolder.imgCourse);
        courseListPresenter = new CourseListPresenter(CourseActivity.this, viewHolder);
        courseListPresenter.onLoadCourse(adapter.getRef(position).getKey());
        checkCourse(viewHolder, position);
        deleteCourse(adapter.getRef(position).getKey(), viewHolder);
    }

    private void deleteCourse(final String key,CourseViewHolder holder) {
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog(key);

            }
        });
    }




    //    private void deleteCourseIsBuy(final String key) {
//        DatabaseReference requestRef= FirebaseDatabase.getInstance().getReference("Requests");
//        requestRef.orderByChild("courseId").equalTo(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot childSnap:dataSnapshot.getChildren()) {
//                    if(childSnap.getValue(Request.class)!=null) {
//                        deleteDialogRequest(key);
//                    }
//                    else {
//                        deleteDialog(key);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void deleteDialogRequest(final String key) {
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("Xóa");
//        alertDialog.setMessage("Khóa học này đã có người mua, bạn có chắc muốn xóa?");
//        //alertDialog.create();
//        //alertDialog.show();
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                courseListPresenter.onDelete(key);
//                dialogInterface.dismiss();
//            }
//        });
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        alertDialog.show();
//    }
    private void deleteDialog(final String key) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có chắc muốn xóa?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                courseListPresenter.onDelete(key);
                suggestList.clear();
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onClickItem(CourseViewHolder courseViewHolder, final String key,final String docName) {
//        courseViewHolder.txtDoc.setText(docName);
        courseViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(CourseActivity.this, DetailUpdateCourseActivity.class);
                intent.putExtra("DetailList", key);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onError(String msg) {
        Toast.makeText(CourseActivity.this,msg,Toast.LENGTH_SHORT).show();
        recyclerMenu.setVisibility(View.INVISIBLE);
    }
    private void showUpdateOrderDialog(final String key) {
        LayoutInflater inflater=this.getLayoutInflater();
        View subView=inflater.inflate(R.layout.alert_dialog_update_status,null);
        final DatabaseReference courseRef=FirebaseDatabase.getInstance().getReference("Course");
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CourseActivity.this);
        alertDialog.setTitle("Cập nhật tài khoản");
        alertDialog.setMessage("Chọn trạng thái");
        //alertDialog.create();
        spinner=(MaterialSpinner) subView.findViewById(R.id.statusSpinner);
        spinner.setItems("Đang chờ","Kích hoạt","Đang xủ lý");
        alertDialog.setView(subView);
        courseRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course courseItem=dataSnapshot.getValue(Course.class);
                if(courseItem.getStatus()==1&&courseItem.getIsBuy().equals("false")){
                    spinner.setSelectedIndex(0);
                }
                else
                    spinner.setSelectedIndex(courseItem.getStatus());
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
                HashMap<String,Object> map=new HashMap<>();
                if(spinner.getSelectedIndex()==2) {
                    map.put("isBuy", "false");
                    map.put("status",2);
                }
                else if(spinner.getSelectedIndex()==1){
                    map.put("isBuy","true");
                    map.put("status",1);
                }
                courseRef.child(localKey).updateChildren(map);
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


}

