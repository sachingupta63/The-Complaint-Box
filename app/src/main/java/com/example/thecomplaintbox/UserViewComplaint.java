package com.example.thecomplaintbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.thecomplaintbox.Adapter.ComplaintAdapter;
import com.example.thecomplaintbox.model.complainData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserViewComplaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<String> category=new ArrayList<>();
    public static String problemType;
    //private List<complainData> complain;
    private ComplaintAdapter complaintAdapter;
    private ArrayList<HashMap<String,complainData>> complain;
    private ArrayList<String> keys=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_complaint);

        spinner=findViewById(R.id.spinner);
        recyclerView=findViewById(R.id.recyclerCiewUser);

        spinner.setOnItemSelectedListener(this);

        complain=new ArrayList<>();



        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        databaseReference= FirebaseDatabase.getInstance().getReference("Complaints");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dp: snapshot.getChildren()){
                    category.add(dp.getKey());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        complaintAdapter=new ComplaintAdapter(this,complain,keys,problemType, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(complaintAdapter);

    }

    public void fetchData(){
        databaseReference.child(problemType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complain.clear();
                keys.clear();

                for(DataSnapshot dp:snapshot.getChildren()){
                    complainData data;
                    data=dp.getValue(complainData.class);
                    String key=dp.getKey();

                    HashMap<String,complainData> rr=new HashMap<>();
                    rr.put(key,data);

                    keys.add(key);
                    complain.add(rr);
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
        complain.clear();
        fetchData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}