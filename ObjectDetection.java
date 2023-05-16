package com.example.fyp_objectdetection;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ObjectDetection {
    public static ObjectDetectionResultListener objectDetectionResultListener;
    private static final String API_BASE_URL = "https://kachrabegone-cv.cognitiveservices.azure.com/";
        private static final String API_SUBSCRIPTION_KEY = "8d18f556dabd4c3683f5809634d359c0";

    public ObjectDetection(ObjectDetectionResultListener objectDetectionResultListener) {
        this.objectDetectionResultListener = objectDetectionResultListener;
    }

    // Method to send the image byte array to Azure Computer Vision API and get the object detection result
        public static void  analyzeImage(byte[] imageData1) throws Exception, IOException {

            final byte[][] imageData = {imageData1};
            final JSONObject[] jsonObject = new JSONObject[1];

            HandlerThread handlerThread = new HandlerThread("DetectFaces");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());

            handler.post(() -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .build();

            RequestBody requestBody = RequestBody.create(imageData[0], MediaType.parse("application/octet-stream"));
            imageData[0] = null;
            System.gc();
            Request request = new Request.Builder()
                    .url(API_BASE_URL + "/vision/v3.2/detect")
                    .header("Ocp-Apim-Subscription-Key", API_SUBSCRIPTION_KEY)
                    .header("Content-Type", "application/octet-stream")
                    .post(requestBody)
                    .build();

            Response response = null;
            try {

                response = client.newCall(request).execute();
                String result = response.body().string();
                jsonObject[0] =  new JSONObject(result);
            }catch (SocketTimeoutException e) {
                // handle the timeout
                Log.e("HTTP Timeout", "Timeout while executing the request", e);
                // show a message to the user or retry the request
            } catch (IOException | JSONException e) {
                Log.e("Connect Reset", "Connection Reset!!!", e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            if (jsonObject[0]!=null) {
                objectDetectionResultListener.onObjectDetectionResult(jsonObject[0]);
            }
                handler.removeCallbacksAndMessages(null);
            });

        }

    }
