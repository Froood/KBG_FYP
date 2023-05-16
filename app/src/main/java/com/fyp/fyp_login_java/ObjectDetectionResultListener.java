package com.fyp.fyp_login_java;

import org.json.JSONObject;

public interface ObjectDetectionResultListener {
    void onObjectDetectionResult(JSONObject detectedObjects);
}
