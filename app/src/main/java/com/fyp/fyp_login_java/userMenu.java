package com.fyp.fyp_login_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class userMenu extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        MaterialButton btncmppage= (MaterialButton) findViewById(R.id.button_go_complaintPage);

        btncmppage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(userMenu.this,UserLocation.class);
                startActivity(intent);
                finish();
            }
        });
        MaterialButton btnfeedback= (MaterialButton) findViewById(R.id.button5);
        btnfeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(userMenu.this,feedbackPage.class);
                startActivity(intent);
                finish();
            }

        });

        Button showComplaintBtn = findViewById(R.id.showComplaintBtn);
        showComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userMenu.this,UserList.class);
                startActivity(intent);
                finish();
            }
        });

        btn=(Button)findViewById(R.id.Logout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //removing signin
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.isLoggedInPrefs,0);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean("UserLoggedIn",false);
                editor.commit();
                //
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(userMenu.this, MainActivity.class));
                Toast.makeText(userMenu.this, "LoggedOut Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}