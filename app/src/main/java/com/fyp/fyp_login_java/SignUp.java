package com.fyp.fyp_login_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.vishnusivadas.advanced_httpurlconnection.PutData;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.concurrent.TimeUnit;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$");
    CountryCodePicker ccp;
    EditText fullname, email, phone, password, conPassword, t1;
    Button b1;
    // create object of DatabaseReference class to access firebase's Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kgb-registeration-firebase-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        t1=findViewById(R.id.t1);
        ccp=findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(t1);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        b1 = findViewById(R.id.b1);
        conPassword = findViewById(R.id.conPassword);

        final TextView loginNowBtn = findViewById(R.id.loginNow);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get data from EditTexts into String variables
                final String fullnameTxt = fullname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();
                final String t1Txt = t1.getText().toString();

                // check if user fill all the fields before sending data to firebase
                if (fullnameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || conPasswordTxt.isEmpty() || t1Txt.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
                    Toast.makeText(SignUp.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }

                else if(!PASSWORD_PATTERN.matcher(passwordTxt).matches()) {
                    Toast.makeText(SignUp.this, "Password must be atleast 6 characters, at least 1 lowercase, 1 uppercase", Toast.LENGTH_SHORT).show();
                }

                else if(!fullnameTxt.matches("^[a-zA-Z ]*$")) {
                    Toast.makeText(SignUp.this, "Name must be alphabetic", Toast.LENGTH_SHORT).show();
                }

                else if(!t1Txt.matches("([0-9]{11})")) {
                    Toast.makeText(SignUp.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                }

                // check if passwords are matching with each other
                //  if not matching with each other then show a toast messsage
                else if (!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(SignUp.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {

                    databaseReference.child("users");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // check if phone is not SignUped before
                            if (snapshot.hasChild(t1Txt)) {
                                Toast.makeText(SignUp.this, "Phone is already SignUped", Toast.LENGTH_SHORT).show();
                            } else {

                                // sending data to firebase Realtime Database.
                                // we are using phone number as unique identity of every user.
                                // so all the other details of user comes under phone number
                                databaseReference.child("users").child(t1Txt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("users").child(t1Txt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(t1Txt).child("password").setValue(passwordTxt);

                                // show a success message then finish the activity
                                Intent intent = new Intent(SignUp.this, manageotp.class);
                                intent.putExtra("fullname", fullnameTxt);
                                intent.putExtra("email", emailTxt);
                                intent.putExtra("password", passwordTxt);
                                intent.putExtra("mobile", ccp.getFullNumberWithPlus().replace(" ", ""));
                                startActivity(intent);
                                //Toast.makeText(SignUp.this, "User SignUped successfully.", Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

        });
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    public void sendOTP(View view) {
//        final String fullnameTxt = fullname.getText().toString();
//        final String emailTxt = email.getText().toString();
//        final String passwordTxt = password.getText().toString();
//        Intent intent = new Intent(SignUp.this, manageotp.class);
//        intent.putExtra("fullname",fullnameTxt);
//        intent.putExtra("email",emailTxt);
//        intent.putExtra("password",passwordTxt);
//        intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
//        startActivity(intent);
//
//    }
}