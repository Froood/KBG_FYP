package com.fyp.fyp_login_java;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverPortal extends AppCompatActivity {

    private TextView textView,textView2;
    private Button statusBtn;
    public String X, assigned;
    public String subEntry2,subEntryValue;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kgb-registeration-firebase-default-rtdb.asia-southeast1.firebasedatabase.app/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_portal);
        //Getting Active user Phone number to insert data
        SharedPreferences sharedPreferences = getSharedPreferences(driverSignIn.activeDriverID,0);
        String driverID = sharedPreferences.getString("activeDriverID","");
        //
        textView=findViewById(R.id.driverStatus);
        textView2=findViewById(R.id.driverName);
        statusBtn=findViewById(R.id.driverStatusUpdateBtn);
        //
        String userID = driverID;
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("drivers").child(userID);
        assigned= "assigned_complaint";

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subEntryValue = dataSnapshot.child("status").getValue(String.class);
                subEntry2 = dataSnapshot.child("fullname").getValue(String.class);
                textView.setText(subEntryValue);
                X= subEntryValue;
                textView2.setText(subEntry2);
                // do something with the sub-entry value string here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle any errors here
            }
        });

        //
        databaseReference.child("drivers/"+driverID);
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /* Work for Button */
                if(X.equals("available")){
                    Toast.makeText(DriverPortal.this, "No work is currently assigned", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("drivers").child(driverID).child("status").setValue("available");
                    textView.setText("availalbe");

                    // delete subkey
                    databaseRef.child(assigned).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // subkey successfully deleted
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // failed to delete subkey, handle the error here
                        }
                    });

                }

            }
        });


    }
}