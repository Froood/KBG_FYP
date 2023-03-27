package com.fyp.fyp_login_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ItemDetails extends AppCompatActivity {
    private TextView statusView;
    private TextView commentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        // Get references to the views in the layout
        statusView = findViewById(R.id.statusBox);
        commentView = findViewById(R.id.commentBox);
        // Get the data passed from the RecyclerView adapter
        Intent intent = getIntent();
        String status =intent.getStringExtra("status");
        String comments =intent.getStringExtra("comments");
        // Set the text of the TextViews
        statusView.setText(status);
        commentView.setText(comments);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ItemDetails.this,UserList.class);
        startActivity(intent);
        finish();
    }
}
