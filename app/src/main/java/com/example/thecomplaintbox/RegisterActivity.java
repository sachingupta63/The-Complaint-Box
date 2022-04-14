package com.example.thecomplaintbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thecomplaintbox.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private EditText name,email,password;
    private Button register;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        name=findViewById(R.id.rName);
        email=findViewById(R.id.rEmail);
        password=findViewById(R.id.rPassword);
        register=findViewById(R.id.rRegister);

        auth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("User");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }
    private void SignUp(){
        String Name,Email,Password;
        Name=name.getText().toString();
        Email=email.getText().toString();
        Password=password.getText().toString();

        if(validate(Name,Email,Password)){
            auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user=new User(Name,Email,Password);
                        databaseReference.child(auth.getCurrentUser().getUid()).setValue(user);
                        Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(RegisterActivity.this,UserActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(RegisterActivity.this, "Can't Register With This Email", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }else{
            Toast.makeText(RegisterActivity.this, "Enter All Valid Details or password greater than 6", Toast.LENGTH_SHORT).show();

        }

    }

    private boolean validate(String name,String email,String password){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && password.length()>6){
            email=email.toLowerCase(Locale.ROOT);
            if(email.contains("@knit.ac.in")){
                return true;
            }else{
                return false;
            }


        }

        return false;


    }
}