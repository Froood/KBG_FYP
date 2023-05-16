package com.example.fyp_objectdetection;

import org.json.JSONObject;

public interface ObjectDetectionResultListener {
    void onObjectDetectionResult(JSONObject detectedObjects);
}
