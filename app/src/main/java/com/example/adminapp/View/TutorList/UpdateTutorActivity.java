package com.example.adminapp.View.TutorList;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.example.adminapp.Common.Common;
import com.example.adminapp.Interface.ItemClickListener;
import com.example.adminapp.Model.Tutor;
import com.example.adminapp.R;
import com.example.adminapp.View.DetailUpdateTutor.DetailUpdateTutorActivity;
import com.example.adminapp.ViewHolder.StaffViewHolder;
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

public class UpdateTutorActivity extends AppCompatActivity {
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference tutorRef;
    private FirebaseDatabase database;
    //private StaffAdapter staffAdapter;
    private FirebaseRecyclerAdapter<Tutor, StaffViewHolder> adapter;
    private FirebaseRecyclerAdapter<Tutor, StaffViewHolder> searchAdapter;
    private ArrayList<Tutor> tutorList;
    private MaterialSearchBar materialSearchBar;
    private List<String> suggestList=new ArrayList<>();
    private MaterialSpinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tutor);
        database= FirebaseDatabase.getInstance();
        tutorRef =database.getReference("Tutor");
        recyclerMenu=(RecyclerView)findViewById(R.id.listTutor);
        recyclerMenu.setHasFixedSize(true);
        spinner=(MaterialSpinner)findViewById(R.id.statusSpinnerTutor);
        layoutManager= new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        loadTutor();
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.search_barUp);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        //materialSearchBar.setTextColor(Color.parseColor("#000"));
        materialSearchBar.setCardViewElevation(10);
        setUpSearchBar();
        setSpinner();
    }

    private void setSpinner() {

        spinner.setItems("Kích hoạt","Vô hiệu hóa");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(spinner.getSelectedIndex()==0){
                    loadTutor();

                }
                else {
                    loadInActiveTutor();
                }
            }
        });
    }

    private void loadInActiveTutor() {
        adapter = new FirebaseRecyclerAdapter<Tutor, StaffViewHolder>
                (Tutor.class, R.layout.tutor_layout,
                        StaffViewHolder.class,
                        tutorRef.orderByChild("ckWork").equalTo(0)) {
            @Override
            protected void populateViewHolder(final StaffViewHolder viewHolder, final Tutor model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profileImage);
                deleteCourse(adapter.getRef(position).getKey(),viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(UpdateTutorActivity.this, DetailUpdateTutorActivity.class);
                        intent.putExtra("phoneKey",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
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
        searchAdapter=new FirebaseRecyclerAdapter<Tutor, StaffViewHolder>(
                Tutor.class,
                R.layout.tutor_layout,
                StaffViewHolder.class,
                tutorRef.orderByChild("username").equalTo(text.toString())//compare by itemName
        ) {
            @Override
            protected void populateViewHolder(StaffViewHolder viewHolder, Tutor model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                //viewHolder.txtDescript.setText(model.getDescript());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profileImage);
                final Tutor local=model;
                deleteCourse(searchAdapter.getRef(position).getKey(),viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(UpdateTutorActivity.this, DetailUpdateTutorActivity.class);
                        intent.putExtra("phoneKey",searchAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerMenu.setAdapter(searchAdapter);//Set adapter for RecycleView when search Result
    }
    private void loadSuggest() {
        tutorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren() ){
                    Tutor tutorItem=postSnapshot.getValue(Tutor.class);
                    suggestList.add(tutorItem.getUsername());//load list of suggest item

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void loadTutor(){
        adapter = new FirebaseRecyclerAdapter<Tutor, StaffViewHolder>
                (Tutor.class, R.layout.tutor_layout,
                        StaffViewHolder.class,
                        tutorRef.orderByChild("ckWork").equalTo(1)) {
            @Override
            protected void populateViewHolder(final StaffViewHolder viewHolder, final Tutor model, int position) {
                viewHolder.txtName.setText(model.getUsername());
                viewHolder.txtEmail.setText(model.getEmail());
                Glide.with(getApplicationContext())
                        .load(model.getAvatar())
                        .centerCrop()
                        .into(viewHolder.profileImage);
                deleteCourse(adapter.getRef(position).getKey(),viewHolder);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent=new Intent(UpdateTutorActivity.this, DetailUpdateTutorActivity.class);
                        intent.putExtra("phoneKey",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            //  setOnClickItem(item.getOrder(),docKey);
            // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else {
        }
        return super.onContextItemSelected(item);

    }
    private void deleteCourse(final String key, StaffViewHolder holder) {
        if(key!=null){
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDialog(key);
                }
            });
        }
        else{
            Log.e("Error key",key);
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
                HashMap<String, Object>map=new HashMap<>();
                map.put("ckWork",0);
                tutorRef.child(key).updateChildren(map);
                Toast.makeText(UpdateTutorActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
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
}