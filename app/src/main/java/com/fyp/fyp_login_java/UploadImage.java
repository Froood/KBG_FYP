package com.fyp.fyp_login_java;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class UploadImage extends AppCompatActivity {

    Button btn;
    ImageView imageView;
    Uri mImageUri;
    Bitmap imageBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kgb-registeration-firebase-default-rtdb.asia-southeast1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        btn = findViewById(R.id.buttonTest);
        imageView = findViewById(R.id.imageViewTest);


        btn.setOnClickListener(view -> {
            // Check for camera permission
            getPackageManager();
            if (ContextCompat.checkSelfPermission(UploadImage.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadImage.this,
                        new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
                Toast.makeText(this, "Camera Permission Initiated", Toast.LENGTH_SHORT).show();
            } else {
                // Start camera app to take picture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE /*&& resultCode == RESULT_OK*/) {
            // Set the captured image to ImageView
            Toast.makeText(this, "onAcitvityResult Called", Toast.LENGTH_SHORT).show();
            //Storing Bitmap and displaying it on ImageView

                    Bundle extras = data.getExtras();
                     imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);

            //Converting Bitmap to imageUri

            //Assuming bitmap is your Bitmap object
            mImageUri = null;
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), imageBitmap, "Title", null);
                mImageUri = Uri.parse(path);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //You can then use the URI as needed, such as passing it to another activity or displaying it in an ImageView.

            uploadImageToFirebase(mImageUri);

            // Grant permission to access image Uri
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CAMERA_PERMISSION);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start camera app to take picture
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }



    /*// Code for UploadingImage From Gallery (working)
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private void pickImageFromGallery() {
        // Create an intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // Start the activity to pick an image
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Get the selected image Uri
            Uri imageUri = data.getData();

            // Upload the image to Firebase
            uploadImageToFirebase(imageUri);
        }
    }
    //-----*/


    private void uploadImageToFirebase(Uri imageUri) {
        // Get a reference to the Firebase storage service
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference to where we will store the image
        StorageReference storageRef = storage.getReference().child("images/" + UUID.randomUUID().toString());

        //Get UserID
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.activeId,0);
        String retID = sharedPreferences.getString("activeUserID","");
        // Upload the image to Firebase storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload successful
                        // Get the download URL for the image
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Image uploaded and download URL available
                                String imageUrl = downloadUri.toString();
                                Log.d(TAG, "Image URL: " + imageUrl);
                                databaseReference.child("wastetype").child(retID).child("image").setValue(imageUrl);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
                        Log.e(TAG, "Error uploading image to Firebase: " + e.getMessage());
                    }
                });
    }


}