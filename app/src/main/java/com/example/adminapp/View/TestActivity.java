package com.example.adminapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminapp.Adapter.TestAdapter;
import com.example.adminapp.Common.Common;
import com.example.adminapp.Model.Doc;
import com.example.adminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {
    private EditText edtTest;
    private EditText edtName;
    private String docId;
    private Button btnAddTest,btnUpdateTest;
    private TestAdapter testAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        edtTest=(EditText)findViewById(R.id.edtTest);
        edtName=(EditText)findViewById(R.id.edtTestName);
        btnAddTest=(Button)findViewById(R.id.btnAddTest);
        //btnUpdateTest=(Button)findViewById(R.id.btnUpdateTest);
        recyclerView=(RecyclerView)findViewById(R.id.listTest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (getIntent() != null)
            docId = getIntent().getStringExtra("courseID");
        if (!docId.isEmpty() && docId != null) {
            if (Common.isConnectedToInternet(this)) {
                onClickAddTest();
                setAdapter();

            } else {
                Toast.makeText(TestActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }



    private void onClickAddTest() {
        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(docId);
            }
        });
    }

    private void setAdapter() {
        DatabaseReference testRef= FirebaseDatabase.getInstance().getReference("Doc");
        testRef.orderByChild("status").equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Doc> docList=new ArrayList<>();
                ArrayList<String> docKey=new ArrayList<>();
                for (DataSnapshot childSnap : dataSnapshot.getChildren()){
                    Doc doc=childSnap.getValue(Doc.class);
                    if(doc.getCourseId().equals(docId)) {
                    if(!doc.getType().equals("doc")) {
                        docList.add(doc);
                        docKey.add(childSnap.getKey());
                        testAdapter = new TestAdapter(TestActivity.this, docList, docKey);
                        recyclerView.setAdapter(testAdapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setData(String docId) {
        String formPatter = "https://+forms\\.+gle+/+[a-zA-Z0-9._-]+";
        if(edtName.getText().toString().equals("")&&edtTest.getText().toString().equals("")||!edtTest.getText().toString().trim().matches(formPatter)){
            Toast.makeText(TestActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference docRef = FirebaseDatabase.getInstance().getReference("Doc");
            HashMap<String, Object> map = new HashMap<>();
            map.put("courseId", docId);
            map.put("docName", edtName.getText().toString());
            map.put("type", "test");
            map.put("status",1);
            map.put("docUrl", edtTest.getText().toString());
            docRef.push().setValue(map);
            Toast.makeText(TestActivity.this,"Thêm bài test thành công",Toast.LENGTH_SHORT).show();
        }
    }




}
