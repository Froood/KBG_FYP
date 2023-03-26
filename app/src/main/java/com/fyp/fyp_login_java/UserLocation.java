package com.fyp.fyp_login_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserLocation extends AppCompatActivity {
    private TextView AddressText;
    private Button LocationButton;
    public static String longData= "longitudeFile";
    public static String latData= "latitudeFile";
    Button next;
    private LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        AddressText = findViewById(R.id.addressText);
        LocationButton = findViewById(R.id.locationButton);
        next = findViewById(R.id.next);



        locationRequest = new LocationRequest.Builder(5000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .setGranularity(Granularity.GRANULARITY_FINE)
                                .setDurationMillis(5000)
                                        .setMaxUpdateAgeMillis(0)
                                                .build();



        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationButton.setVisibility(View.INVISIBLE);
                getCurrentLocation();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLocation.this, complaintRegistration.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(UserLocation.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(UserLocation.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(UserLocation.this).removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() >0){
                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);



                                        //Saving Long and Lat in Preferences
                                        SharedPreferences sharedPreferences = getSharedPreferences(UserLocation.longData,0);
                                        SharedPreferences.Editor editor= sharedPreferences.edit();
                                        editor.putLong("longitudeCoordinates", Double.doubleToRawLongBits(longitude));
                                        editor.apply();
                                        SharedPreferences sharedPreferences1 = getSharedPreferences(UserLocation.latData,0);
                                        SharedPreferences.Editor editor1= sharedPreferences1.edit();
                                        editor1.putLong("latitudeCoordinates", Double.doubleToRawLongBits(latitude));
                                        editor1.apply();
                                        //returning "next" button's visibility
                                        next.setVisibility(View.VISIBLE);
//                                        // Retrieve long lat data from SharedPrefs
//
//                                        sharedPreferences = getSharedPreferences(UserLocation.longData, Context.MODE_PRIVATE);
//                                        long doubleValueInRawLongBits = sharedPreferences.getLong("longitudeCoordinates", Double.doubleToRawLongBits(0));
//                                        double yourDoubleValue = Double.longBitsToDouble(doubleValueInRawLongBits);
//
//                                        sharedPreferences1 = getSharedPreferences(UserLocation.latData, Context.MODE_PRIVATE);
//                                        long doubleValueInRawLongBits1 = sharedPreferences1.getLong("latitudeCoordinates", Double.doubleToRawLongBits(0));
//                                        double yourDoubleValue1 = Double.longBitsToDouble(doubleValueInRawLongBits1);


                                       // AddressText.setText("Latitude: "+ yourDoubleValue1 + "\n" + "Longitude: "+ yourDoubleValue);





                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(UserLocation.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(UserLocation.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
}