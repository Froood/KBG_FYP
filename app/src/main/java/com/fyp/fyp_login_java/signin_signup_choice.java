package com.fyp.fyp_login_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.google.android.material.button.MaterialButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class signin_signup_choice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup_choice);


        Button signin_page_btn  =  findViewById(R.id.signin_page_btn);
        Button signup_page_btn=    findViewById(R.id.signup_page_btn);

        signin_page_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(signin_signup_choice.this,loginChoice.class);
                startActivity(intent);
                finish();
            }

        });
        signup_page_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(signin_signup_choice.this,signupChoice.class);
                startActivity(intent);
                finish();
            }

        });

    }
    @Override
    public void onBackPressed() {
      super.onBackPressed();
    }

}