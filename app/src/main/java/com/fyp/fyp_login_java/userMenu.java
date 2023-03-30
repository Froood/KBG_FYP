package com.fyp.fyp_login_java;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userMenu extends AppCompatActivity {

    Button btn;
    int childCountComplaint =0;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kgb-registeration-firebase-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        Button btncmppage= findViewById(R.id.button_go_complaintPage);
        TextView textView = findViewById(R.id.total_complaints_count);
        TextView textView2 = findViewById(R.id.feedback_count);


        //Getting Active user Phone number to insert data
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.activeId,0);
        String retID = sharedPreferences.getString("activeUserID","");
        DatabaseReference referenceComp = databaseReference.child("complaints/"+ retID);
        //


        referenceComp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    childCountComplaint = (int)snapshot.getChildrenCount();
                    Log.d(TAG, "ChildCount Complaint: " + childCountComplaint);
                    textView.setText(String.valueOf(childCountComplaint));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference feedbackRef = databaseReference.child("feedback/");
        feedbackRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    childCountComplaint = (int)snapshot.getChildrenCount();
                    Log.d(TAG, "ChildCount Complaint: " + childCountComplaint);
                    textView2.setText(String.valueOf(childCountComplaint));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                editor.apply();
                //
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(userMenu.this, MainActivity.class));
                Toast.makeText(userMenu.this, "LoggedOut Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}