package ch.techteam.techteamlauzhack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity implements Observer {

    private final static String PLAYLIST_URL = "https://api.spotify.com/v1/";

    private StateMode stateMode_;
    private RunningMode runningMode_;
    private float meanHeartRate_;
    private int slowIntervalTime_;
    private int fastIntervalTime_;
    private int time_;
    private double distance_;
    private RequestQueue queue_;
    private List<String> playlist_;
    private MockData mockdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        Intent intent = getIntent();
        runningMode_ = (RunningMode) intent.getSerializableExtra("mode");

        switch (runningMode_){
            case RUN_TIME:
                time_ = intent.getIntExtra("time", 1800);
                break;
            case RUN_DISTANCE:
                distance_ = intent.getDoubleExtra("distance", 5.0);
                break;
            case WALK:
                time_ = intent.getIntExtra("time", 1800);
                break;
            case INTERVAL:
                slowIntervalTime_ = intent.getIntExtra("slowIntervalTime", 240);
                fastIntervalTime_ = intent.getIntExtra("fastIntervalTime", 600);
                break;
        }

        queue_ = Volley.newRequestQueue(this);

        stateMode_ = StateMode.WARMUP;
        Button state = findViewById(R.id.button_main);

        state.setText("End warm up");
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stateMode_){
                    case WARMUP:
                        stateMode_ = StateMode.RUN;
                        ((Button)v.findViewById(R.id.button_main)).setText("End run");
                        playlistDependingOnRunningMode();
                        break;
                    case RUN:
                        stateMode_ = StateMode.RECOVERY;
                        ((Button)v.findViewById(R.id.button_main)).setText("End recovery");
                        playlistRecovery();
                        break;
                    case RECOVERY:
                        Intent homeIntent = new Intent(v.getContext(), HomeActivity.class);
                        startActivity(homeIntent);
                        break;
                }
            }
        });

        mockdata = new MockData(this);
        mockdata.addObserver(this);
        mockdata.run();

        playlistWarmup();

    }

    private void playlistRecovery(){

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

    @Override
    public void update(Observable o, Object arg) {
        MockData m = (MockData) o;
        Log.i("mainMockData", "heartbeat = " + m.getHeartBeat());
    }
}
