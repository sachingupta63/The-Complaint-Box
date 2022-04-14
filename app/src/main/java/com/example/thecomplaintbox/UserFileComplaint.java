package com.example.thecomplaintbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thecomplaintbox.model.complainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFileComplaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText fileComplaint;
    private Button raiseComplaint;
    private Spinner spinner;
    private String[] category=new String[]{"-- Select --","Electricity","Water","Mess","Hostel","Institute"};
    private String complaintType;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_file_complaint);

        fileComplaint=findViewById(R.id.editTextFileComplaint);
        raiseComplaint=findViewById(R.id.buttonFileComplaint);
        spinner=findViewById(R.id.fileComplaintSpinner);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Complaints");

        raiseComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileComplaint();

            }
        });
    }

    private void fileComplaint(){
        String complaintText=fileComplaint.getText().toString().trim();

        if(complaintType.equals(category[0])){
            Toast.makeText(UserFileComplaint.this, "Select the Type of Problem", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(complaintText)){
            Toast.makeText(UserFileComplaint.this, "Write Your Problem", Toast.LENGTH_SHORT).show();

        }else{
            complainData complain=new complainData(firebaseUser.getEmail(),complaintText);
            databaseReference.child(complaintType).push().setValue(complain).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UserFileComplaint.this, "Successfully Registered Your Commplaint", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UserFileComplaint.this,UserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(UserFileComplaint.this, "Some Problem Occured", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UserFileComplaint.this,UserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }
            });


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        complaintType=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}