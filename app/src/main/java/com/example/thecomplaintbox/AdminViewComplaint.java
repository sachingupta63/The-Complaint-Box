package com.example.thecomplaintbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thecomplaintbox.Adapter.ComplaintAdapter;
import com.example.thecomplaintbox.model.complainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminViewComplaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton replyButton;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private EditText replyText;
    private ArrayList<String> category=new ArrayList<>();
    private DatabaseReference databaseReference;
    public static String problemType;

    private ComplaintAdapter complaintAdapter;
    private ArrayList<HashMap<String,complainData>> listMap=new ArrayList<>();
    private ArrayList<String> key=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_complaint);

        getSupportActionBar().hide();

        replyButton=findViewById(R.id.replyButton);
        recyclerView=findViewById(R.id.AdminRecyclerview);
        spinner=findViewById(R.id.spinner2);
        replyText=findViewById(R.id.adminReplyEditText);


        databaseReference= FirebaseDatabase.getInstance().getReference("Complaints");

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

       complaintAdapter=new ComplaintAdapter(this,listMap,key,problemType,"Admin");
       recyclerView.setAdapter(complaintAdapter);

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(replyText.getText().toString().trim())) {
                    Toast.makeText(AdminViewComplaint.this, "Enter text in box to reply", Toast.LENGTH_SHORT).show();
                }else{
                    replyByAdmin();
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                category.clear();
                for(DataSnapshot dp:snapshot.getChildren()){
                    category.add(dp.getKey());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void replyByAdmin(){
        String reply=replyText.getText().toString();
        complainData data=new complainData("Admin",reply);

        databaseReference.child(problemType).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    replyText.setText("");
                    fetchData();
                }else{
                    Toast.makeText(AdminViewComplaint.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void fetchData(){
        databaseReference.child(problemType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMap.clear();
                key.clear();

                for(DataSnapshot dp:snapshot.getChildren()){
                    HashMap<String,complainData> complain=new HashMap<>();

                    complainData data;
                    String Key=dp.getKey();
                    data=dp.getValue(complainData.class);
                    complain.put(Key,data);

                    key.add(Key);

                    listMap.add(complain);
                   // Toast.makeText(AdminViewComplaint.this, Key, Toast.LENGTH_SHORT).show();
                }
                complaintAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        problemType=parent.getItemAtPosition(position).toString();
        fetchData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}