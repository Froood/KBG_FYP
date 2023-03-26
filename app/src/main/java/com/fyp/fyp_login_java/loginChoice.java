package com.fyp.fyp_login_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class loginChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);


        MaterialButton userLogin_choice_btn= (MaterialButton) findViewById(R.id.userLogin_choice_btn);
        MaterialButton driverLogin_choice_btn= (MaterialButton) findViewById(R.id.driverLogin_choice_btn);

        userLogin_choice_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(loginChoice.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        driverLogin_choice_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(loginChoice.this,driverSignIn.class);
                startActivity(intent);
                finish();
            }

        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(loginChoice.this,signin_signup_choice.class);
        startActivity(intent);
        finish();
    }
}