package com.fyp.fyp_login_java;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class complaintRegistration extends AppCompatActivity  implements ObjectDetectionResultListener{
    //Code for ComplaintRegistration data entry
    EditText commentView;
    Button checkButton;
    CheckBox c1,c2,c3;
    ImageView imgView;
    TextView textVisible;
   // ImageView imgCamera;
    EditText textComment;
    Bitmap imageBitmap;
    int childCountComplaint =0;
    public Uri imageUri;
    private final int CAMERA_REQ_CODE = 100;
    String checkBoxVariable= "";
    String checkBoxVariable2= "";
    String checkBoxVariable3= "";
    String name="";
    ObjectDetection objectDetection;

    private List<String> objectsList = new ArrayList<>();
    //Get UserID
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kgb-registeration-firebase-default-rtdb.asia-southeast1.firebasedatabase.app/");
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_registration);
        //Getting Active user Phone number to insert data
        SharedPreferences sharedPreferences3 = getSharedPreferences(MainActivity.activeId,0);
        String retID = sharedPreferences3.getString("activeUserID","");
        //Text Visibility
        textVisible = findViewById(R.id.text_view_preview);
        objectDetection = new ObjectDetection(this);

        // TODO: Add Objects Here
        objectsList.addAll(Arrays.asList("Plastic Bag","Plastic Bottle","Bottle","Paper", "Apple", "Office Supplies", "Fruits"));
        //Code for CheckBox data
        checkButton = findViewById(R.id.submitBtn);
        c1 = findViewById(R.id.plasticBox);
        c2 = findViewById(R.id.metalBox);
        c3 = findViewById(R.id.paperBox);
        commentView = findViewById(R.id.textComment);
        String waste1= "Plastic";
        String waste2= "Metal";
        String waste3= "Paper";
        // Create a new Date object with the given milliseconds

        Date date = new Date();

// Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

// Use the SimpleDateFormat object to format the date as a string
        String dateString = dateFormat.format(date);



        DatabaseReference referenceComp = databaseReference.child("complaints/"+ retID);

        //
        referenceComp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    childCountComplaint = (int)snapshot.getChildrenCount();
                    Log.d(TAG, "ChildCount Complaint: " + childCountComplaint);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //
        textComment = findViewById(R.id.textComment);
        imgView = findViewById(R.id.imgViewComp);
        DatabaseReference databaseReferenceX = FirebaseDatabase.getInstance().getReference("users/"+ retID );
        //
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
        Button btnCamera = findViewById(R.id.button_takePhoto);
        btnCamera.setOnClickListener(view -> {
            getPackageManager();
            if (ContextCompat.checkSelfPermission(complaintRegistration.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(complaintRegistration.this,
                        new String[] { Manifest.permission.CAMERA }, CAMERA_REQ_CODE);
                Toast.makeText(complaintRegistration.this, "Camera Permission Initiated", Toast.LENGTH_SHORT).show();
            } else {
                // Start camera app to take picture
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQ_CODE);
            }
        });
        //
        MaterialButton btncmpsubmit= (MaterialButton) findViewById(R.id.submitBtn);
        btncmpsubmit.setOnClickListener(v -> {

            databaseReference.child("complaints");
            // Retrieve long lat data from SharedPrefs

            SharedPreferences sharedPreferences = getSharedPreferences(UserLocation.longData, Context.MODE_PRIVATE);
            long doubleValueInRawLongBits = sharedPreferences.getLong("longitudeCoordinates", Double.doubleToRawLongBits(0));
            double longitudeData = Double.longBitsToDouble(doubleValueInRawLongBits);

            SharedPreferences sharedPreferences1 = getSharedPreferences(UserLocation.latData, Context.MODE_PRIVATE);
            long doubleValueInRawLongBits1 = sharedPreferences1.getLong("latitudeCoordinates", Double.doubleToRawLongBits(0));
            double latitudeData = Double.longBitsToDouble(doubleValueInRawLongBits1);

            //Code for Complaint Box
            final String complaintBoxData = commentView.getText().toString();
            if(complaintBoxData.isEmpty()){
                Toast.makeText(complaintRegistration.this, "Comment Box is Empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Code for CheckBox data
                if(c1.isChecked() || c2.isChecked() || c3.isChecked()) {
                        //uploadImageToFirebase(imageUri);
                        if(imageUri==null ){
                            Toast.makeText(complaintRegistration.this, "image null", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (c1.isChecked()) {
                                checkBoxVariable = "W1";
                                databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child(checkBoxVariable).setValue(waste1);
                            }
                            if (c2.isChecked()) {
                                checkBoxVariable2 = "W2";
                                databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child(checkBoxVariable2).setValue(waste2);
                            }
                            if (c3.isChecked()) {
                                checkBoxVariable3 = "W3";
                                databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child(checkBoxVariable3).setValue(waste3);
                            }
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child("username").setValue(name);
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child("Comments").setValue(complaintBoxData);
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child("Longitude").setValue(longitudeData);
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child("Latitude").setValue(latitudeData);
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child("Date").setValue(dateString);
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint + 1)).child("status").setValue("pending");

                            Intent intent = new Intent(complaintRegistration.this, userMenu.class);
                            startActivity(intent);
                            Toast.makeText(complaintRegistration.this, "Complaint Submitted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(complaintRegistration.this, "Check Fields for missing data", Toast.LENGTH_SHORT).show();
                    }

                }
        });
        //
        MaterialButton btnreturn= (MaterialButton) findViewById(R.id.button_return);
        btnreturn.setOnClickListener(v -> {
            Intent intent = new Intent(complaintRegistration.this,userMenu.class);
            startActivity(intent);
            finish();
        });
        //
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start camera app to take picture
                imageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQ_CODE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK) {
            // Set the captured image to ImageView
            //Toast.makeText(this, "onAcitvityResult Called for Camera", Toast.LENGTH_SHORT).show();
            //Storing Bitmap and displaying it on ImageView
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            try {
                ObjectDetection.analyzeImage(convertBitmapToByteArray(imageBitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgView.setImageBitmap(imageBitmap);
            //Assuming bitmap is your Bitmap object
            imageUri = null;
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), imageBitmap, "Title", null);
                // Grant temporary read permission to another app
                imageUri = Uri.parse(path);
                String packageName = "com.example.otherapp";
                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // Get a reference to the Firebase storage service
        FirebaseStorage storage = FirebaseStorage.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.activeId,0);
        String retID = sharedPreferences.getString("activeUserID","");
        // Create a storage reference to where we will store the image
        StorageReference storageRef = storage.getReference().child("images/" + UUID.randomUUID().toString());
        if(imageUri != null){
            // Upload the image to Firebase storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image upload successful
                        // Get the download URL for the image
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Image uploaded and download URL available
                            String imageUrl = downloadUri.toString();
                            databaseReference.child("complaints").child(retID).child(String.valueOf(childCountComplaint )).child("image").setValue(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {

                    });
        }
        else
        {
            Toast.makeText(this, "Cannot submit complaint without adding Picture", Toast.LENGTH_SHORT).show();
        }
    }


    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public List<String> getObjectLabels(JSONObject jsonObject) {

        List<String> objectLabelList = new ArrayList<>();
        try {
            JSONArray objectsArray = jsonObject.getJSONArray("objects");
            for (int i = 0; i < objectsArray.length(); i++) {
                JSONObject object = objectsArray.getJSONObject(i);
//                if (object.getString("object").equalsIgnoreCase("plastic bag")){
//                    plasticBagCount++;
//                }
                objectLabelList.add(object.getString("object"));

            }
            return objectLabelList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
    }}

    @Override
    public void onObjectDetectionResult(JSONObject detectedObjects) {
        Log.d("OD_Tag", "object detection result initiated" );
        if (detectedObjects.has("objects")) {
            List<String> labelsList = getObjectLabels(detectedObjects);
            for(String a: labelsList){
//                textView.setText(textView.getText()+" "+a);
            }
            if (compareLists(labelsList).size()!=0) {

                uploadImageToFirebase(imageUri);

            }else{
                // TODO: Discard this image
                Toast.makeText(this, "No Object Detected/ Image Upload Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<String> compareLists(List<String> labelList){
        List<String> detectedObjects = new ArrayList<>();
        for (String label : labelList) {
            for (String object: objectsList){
                if (object.contains(label)){
                    detectedObjects.add(label);
                }
            }
        }
        return detectedObjects;
    }
}