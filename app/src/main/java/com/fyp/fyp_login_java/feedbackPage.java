package com.fyp.fyp_login_java;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class feedbackPage extends AppCompatActivity {
    //assigning variables
    EditText comment;
    RadioButton r1,r2,r3,r4,r5,r6;
    MaterialButton btnfeedbacksubmit;
    MaterialButton button1;

    int childCount=0;
    String name="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kgb-registeration-firebase-default-rtdb.asia-southeast1.firebasedatabase.app/");
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);

        //assigning buttons
        comment = findViewById(R.id.feedbackView);
        r1 =findViewById(R.id.radiotBtn1);
        r2 =findViewById(R.id.radiotBtn2);
        r3 =findViewById(R.id.radiotBtn3);
        r4 =findViewById(R.id.radiotBtn4);
        r5 =findViewById(R.id.radiotBtn5);
        r6 =findViewById(R.id.radiotBtn6);
        btnfeedbacksubmit= (MaterialButton) findViewById(R.id.submitBtn);
        button1 = (MaterialButton) findViewById(R.id.button_return);
        //
         DatabaseReference temp;
         temp= databaseReference.child("feedback");
         temp.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists())
                 {
                     childCount = (int)snapshot.getChildrenCount();
                     Log.d(TAG, "ChildCount: " + childCount);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(feedbackPage.this, "FeedBack not recorded!", Toast.LENGTH_SHORT).show();
             }
         });
        //
        SharedPreferences sharedPreferences3 = getSharedPreferences(MainActivity.activeId,0);
        String retID = sharedPreferences3.getString("activeUserID","");
        DatabaseReference databaseReferenceX = FirebaseDatabase.getInstance().getReference("users/"+ retID );
        databaseReferenceX.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method will be called whenever data is changed or added to the specified location
                // Use the dataSnapshot object to retrieve the data
                name = dataSnapshot.child("fullname").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                Log.d(TAG, "Value is: " + email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // This method will be called if there is an error retrieving data
                Log.e(TAG, "Error retrieving data", databaseError.toException());
            }
        });

        //
/*
        btnfeedbacksubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String commenttxt = comment.getText().toString();
                databaseReference.child("feedback");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        //Getting Active user Phone number to insert data
                        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.activeId,0);
                        String retID = sharedPreferences.getString("activeUserID","");

                        if(r1.isChecked()) {
                            databaseReference.child("feedback").child(String.valueOf(childCount)).child("rating").setValue("Very Satisfied");

                        }
                        else if(r2.isChecked()) {
                            databaseReference.child("feedback").child(String.valueOf(childCount)).child("rating").setValue("Satisfied");
                        }
                        else if(r3.isChecked()) {
                            databaseReference.child("feedback").child(String.valueOf(childCount)).child("rating").setValue("Good");
                        }
                        else if(r4.isChecked()) {
                            databaseReference.child("feedback").child(String.valueOf(childCount)).child("rating").setValue("OK");
                        }
                        else if(r5.isChecked()) {
                            databaseReference.child("feedback").child(String.valueOf(childCount)).child("rating").setValue("Dissatisfied");
                        }
                        else {
                            databaseReference.child("feedback").child(String.valueOf(childCount)).child("rating").setValue("Very Dissatisfied");
                        }

                        databaseReference.child("feedback").child(String.valueOf(childCount)).child("phone").setValue(retID);
                        databaseReference.child("feedback").child(String.valueOf(childCount)).child("description").setValue(commenttxt);
                        Toast.makeText(feedbackPage.this,"Complaint Submitted", Toast.LENGTH_SHORT ).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(feedbackPage.this, "FeedBack not recorded!", Toast.LENGTH_SHORT).show();
                    }
                });
                //Go back to UserMenu
               *//*
            }
        });
        */
        btnfeedbacksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commenttxt = comment.getText().toString();
                //Getting Active user Phone number to insert data
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.activeId,0);
                String retID = sharedPreferences.getString("activeUserID","");
                //

                //
                if(r1.isChecked()) {
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("username").setValue(name);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("phone").setValue(retID);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("description").setValue(commenttxt);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("rating").setValue("Very Satisfied");

                }
                else if(r2.isChecked()) {
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("username").setValue(name);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("phone").setValue(retID);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("description").setValue(commenttxt);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("rating").setValue("Satisfied");
                }
                else if(r3.isChecked()) {
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("username").setValue(name);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("phone").setValue(retID);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("description").setValue(commenttxt);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("rating").setValue("Good");
                }
                else if(r4.isChecked()) {
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("username").setValue(name);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("phone").setValue(retID);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("description").setValue(commenttxt);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("rating").setValue("OK");
                }
                else if(r5.isChecked()) {
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("username").setValue(name);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("phone").setValue(retID);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("description").setValue(commenttxt);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("rating").setValue("Dissatisfied");
                }
                else {
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("username").setValue(name);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("phone").setValue(retID);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("description").setValue(commenttxt);
                    databaseReference.child("feedback").child(String.valueOf(childCount+1)).child("rating").setValue("Very Dissatisfied");
                }
                 Intent intent = new Intent(feedbackPage.this,userMenu.class);
                                startActivity(intent);
                                finish();
                Toast.makeText(feedbackPage.this,"Complaint Submitted", Toast.LENGTH_SHORT ).show();



            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(feedbackPage.this,userMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

}