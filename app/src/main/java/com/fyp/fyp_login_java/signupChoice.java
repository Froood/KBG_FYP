package com.fyp.fyp_login_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class signupChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_choice);

        Button userSignup_choice_btn =  findViewById(R.id.userSignup_choice_btn);
        Button driverSignup_choice_btn =  findViewById(R.id.driverSignup_choice_btn);

        userSignup_choice_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(signupChoice.this,SignUp.class);
                startActivity(intent);
                finish();
            }

        });

        driverSignup_choice_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(signupChoice.this, driver_signup.class));
                finish();
            }

        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(signupChoice.this,signin_signup_choice.class);
        startActivity(intent);
        finish();
    }
}