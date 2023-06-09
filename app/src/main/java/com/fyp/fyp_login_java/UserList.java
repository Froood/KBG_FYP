package com.fyp.fyp_login_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<UserData> list;

    String acc= "accepted";
    String status = "pending";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        //Getting Active user Phone number to insert data
        SharedPreferences sharedPreferences3 = getSharedPreferences(MainActivity.activeId,0);
        String retID = sharedPreferences3.getString("activeUserID","");
        recyclerView=findViewById(R.id.userListView);
        databaseReference= FirebaseDatabase.getInstance().getReference("complaints/"+ retID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){


                  //  if(status == acc){}
                        UserData userData = dataSnapshot.getValue(UserData.class);
                        Log.d("TAG", "onDataChange: " + userData.getStatus());
                        if(userData.getStatus().equals("accepted"))
                        {
                            list.add(userData);
                        }

                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){


                  //  if(status == acc){}
                        UserData userData = dataSnapshot.getValue(UserData.class);
                        Log.d("TAG", "onDataChange: " + userData.getStatus());
                        if(userData.getStatus().equals("rejected"))
                        {
                            list.add(userData);
                        }



                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){


                  //  if(status == acc){}
                        UserData userData = dataSnapshot.getValue(UserData.class);
                        Log.d("TAG", "onDataChange: " + userData.getStatus());
                        if(userData.getStatus().equals("pending"))
                        {
                            list.add(userData);
                        }



                }



                    myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserList.this,userMenu.class);
        startActivity(intent);
        finish();
    }
}

