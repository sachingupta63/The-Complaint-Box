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

import com.example.thecomplaintbox.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText email,password;
    private Button login;
    private FirebaseAuth auth;
    private String[] arr=new String[]{"-- Select --","User","Admin"};
    private Spinner spinner;
    private String User=null;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        login=findViewById(R.id.lLogin);
        email=findViewById(R.id.lEmail);
        password=findViewById(R.id.lPassword);
        spinner=findViewById(R.id.lspinner);

        spinner.setOnItemSelectedListener(this);



        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);




        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            if(User.equals("-- Select --")){
                    Toast.makeText(LoginActivity.this, "Please select the user", Toast.LENGTH_SHORT).show();
                }else if(User.equals("User")){
                    SignInUser();
                }else{
                    if(password.getText().toString().equals("Admin@Admin") && email.getText().toString().equals("Admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




    }



    private void SignInUser(){
        String Email=email.getText().toString();
        String Password=password.getText().toString();
        if(validate(Email,Password)){
            auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                        if(User.equals("User")) {
                            startActivity(new Intent(LoginActivity.this, UserActivity.class));
                            finish();
                            //Toast.makeText(LoginActivity.this, auth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();


                        }else{
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Can't Login this email or wrong password", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }else{
            Toast.makeText(LoginActivity.this, "All field must be filled", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validate(String email,String password){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            email=email.toLowerCase(Locale.ROOT);
            if(email.contains("@knit.ac.in")){
               return true;
            }else{
                return false;
            }


        }

        return false;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        User=parent.getItemAtPosition(position).toString();
        //Toast.makeText(LoginActivity.this, User, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}