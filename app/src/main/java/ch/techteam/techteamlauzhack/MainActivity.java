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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final static String PLAYLIST_URL = "https://api.spotify.com/v1/";

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

        stateMode_ = StateMode.WARMUP;
        playlistDependingOnStateMode();

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
            case RUN_DISTANCE:
                break;
            case RUN_TIME:
                break;
            case INTERVAL:
                break;
            default:
                Log.e("MAINACTIVITY", "NO RUNNING MODE");
        }
    }

    private void playlistWarmup(){

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PLAYLIST_URL + "playlists/37i9dQZF1DX3PIAZMcbo2T/tracks", null,
            new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //mTextView.setText("Response: " + response.toString());
                    Log.e("MAIN", "Received playlist");
                }
                },
            new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("MAIN", "Cannot retrieve warmup playlist");

                }
        })


        {

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue_.add(request);
    }
}
