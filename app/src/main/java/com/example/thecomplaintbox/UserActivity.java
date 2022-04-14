package com.example.thecomplaintbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button viewComplaint,fileComplaint,logout;
    private TextView txtView;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        viewComplaint=findViewById(R.id.viewComplaint);
        fileComplaint=findViewById(R.id.fileComplaint);
        txtView=findViewById(R.id.utextView2);
        logout=findViewById(R.id.logut);

        auth=FirebaseAuth.getInstance();

        txtView.setText(auth.getCurrentUser().getEmail());

        viewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserActivity.this,UserViewComplaint.class);
                startActivity(intent);

            }
        });

        fileComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserActivity.this,UserFileComplaint.class);
                startActivity(intent);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();

            }
        });
    }
}