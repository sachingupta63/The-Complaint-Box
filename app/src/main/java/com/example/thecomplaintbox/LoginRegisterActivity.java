package com.example.thecomplaintbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterActivity extends AppCompatActivity {
    private Button login,register;
    // private FirebaseAuth auth;
    // private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        try {
            getSupportActionBar().hide();
        }
        catch (IllegalArgumentException e){

        }



        login=(Button) findViewById(R.id.login);
        register=(Button) findViewById(R.id.register);

        /*auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(this,UserActivity.class));
            finish();
        }*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginRegisterActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginRegisterActivity.this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }
}