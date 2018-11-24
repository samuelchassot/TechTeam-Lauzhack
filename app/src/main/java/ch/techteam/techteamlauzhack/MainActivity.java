package ch.techteam.techteamlauzhack;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String PLAYLIST_URL = "http://www.google.com";

    private StateMode stateMode_;
    private RunningMode runningMode_;
    private float meanHeartRate_;
    private float slowIntervalTime_;
    private float fastIntervalTime_;
    private RequestQueue queue_;
    private List<String> playlist_;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        queue_ = Volley.newRequestQueue(this);

    }

    private void playlistDependingOnStateMode(){
        switch (stateMode_){
            case WARMUP:
                playlistWarmup();
                break;
            case RUN:
                playlistDependingOnRunningMode();
                break;
            case RECOVERY:
                break;
            default:
                Log.e("MAINACTIVITY", "NO STATE MODE");
        }
    }

    private void playlistDependingOnRunningMode(){
        switch (runningMode_){
            case WALK:
                break;
            case RUN:
                break;
            case INTERVAL:
                break;
            default:
                Log.e("MAINACTIVITY", "NO RUNNING MODE");
        }
    }

    private void playlistWarmup(){

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PLAYLIST_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    //mTextView.setText("Response is: "+ response.substring(0,500));
                    
                }
            },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MAIN", "Cannot retrieve warmup playlist");
            }
        });

        // Add the request to the RequestQueue.
        queue_.add(stringRequest);
    }
}
