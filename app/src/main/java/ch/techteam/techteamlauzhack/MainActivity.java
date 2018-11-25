package ch.techteam.techteamlauzhack;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;


public class MainActivity extends AppCompatActivity implements Observer {

    private final static String SPOTIFY_URL = "https://api.spotify.com/v1/";

    private StateMode stateMode_;
    private RunningMode runningMode_;
    private IntervalMode intervalMode_;

    private int heartRate;
    private double totalDistance;
    private double slope;
    private double speed;

    private int slowIntervalTime_;
    private int fastIntervalTime_;
    private int time_;
    private double distance_;
    private RequestQueue queue_;
    private List<String> playlist_;
    private Map<String, Double> playlistBPM_ = new HashMap<>();
    private JSONObject jsonPlaylist_;
    private MockData mockdata;

    private Date startingTime_;
    private Timer timer_;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        Intent intent = getIntent();
        runningMode_ = (RunningMode) intent.getSerializableExtra("mode");

        timer_ = new Timer();

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
        requestPlaylist();

        stateMode_ = StateMode.WARMUP;
        Button state = findViewById(R.id.button_main);

        state.setText("End warm up");
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextMode(v);
            }
        });

        getSupportActionBar().hide();

        mockdata = new MockData(this);
        mockdata.addObserver(this);
        mockdata.run();

    }

    private void goToNextMode(){
        goToNextMode(this.getWindow().getDecorView());
    }

    private void goToNextMode(View v){
        switch (stateMode_){
            case WARMUP:
                timer_.cancel();
                stateMode_ = StateMode.RUN;
                ((Button)v.findViewById(R.id.button_main)).setText("End run");
                playlistDependingOnRunningMode();
                break;
            case RUN:
                timer_.cancel();
                stateMode_ = StateMode.RECOVERY;
                ((Button)v.findViewById(R.id.button_main)).setText("End recovery");
                playlistRecovery();
                break;
            case RECOVERY:
                timer_.cancel();
                stopSong();
                Intent homeIntent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(homeIntent);
                break;
        }
    }


    /**-----------PLAY AND SORT PLAYLISTS-----------*/

    private void playlistWarmup(){
        Log.e("PLAYLISTBPM", playlistBPM_.toString());

        LinkedList<Map.Entry<String, Double>> map = new LinkedList<> (playlistBPM_.entrySet());
        Collections.sort(map, new Comparator<Map.Entry<String,Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }

        });

        timer_.schedule(new NextModeTimer(), 45000);
        playSong(map);

    }

    private void playlistDependingOnRunningMode(){
        switch (runningMode_){
            case WALK:
                timer_ = new Timer();
                timer_.schedule(new StopRunPlayer(),time_*1000);
                break;
            case RUN_DISTANCE:
                timer_ = new Timer();
                timer_.schedule(new CheckDistanceTimer(),10000);
                break;
            case RUN_TIME:
                timer_ = new Timer();
                timer_.schedule(new StopRunPlayer(),time_*1000);
                break;
            case INTERVAL:
                timer_ = new Timer();
                intervalMode_ = IntervalMode.FAST;
                timer_.schedule(new FastIntervalTimer(), 0, fastIntervalTime_+slowIntervalTime_);
                timer_.schedule(new SlowIntervalTimer(), fastIntervalTime_, fastIntervalTime_+slowIntervalTime_);
                break;
            default:
                Log.e("MAINACTIVITY", "NO RUNNING MODE");
        }
    }

    private void playlistIntervalFast(){

    }

    private void playlistIntervalSlow(){

    }

    private void playlistBounded(final int min, final int max){
        LinkedList<Map.Entry<String, Double>> map = new LinkedList<> (playlistBPM_.entrySet());

        map.removeIf(new Predicate<Map.Entry<String, Double>>() {
            @Override
            public boolean test(Map.Entry<String, Double> stringIntegerEntry) {
                return stringIntegerEntry.getValue() > min && stringIntegerEntry.getValue() < max;
            }
        });

        Collections.sort(map
                , new Comparator<Map.Entry<String,Double>>() {
                    @Override
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return Double.compare(o2.getValue(), o1.getValue());
                    }

                });

        playSong(map);
    }

    private void playlistRecovery(){
        LinkedList<Map.Entry<String, Double>> map = new LinkedList<> (playlistBPM_.entrySet());

        map.removeIf(new Predicate<Map.Entry<String, Double>>() {
            @Override
            public boolean test(Map.Entry<String, Double> stringIntegerEntry) {
                return stringIntegerEntry.getValue() > 120;
            }
        });

        Collections.sort(map
                , new Comparator<Map.Entry<String,Double>>() {
                    @Override
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return Double.compare(o2.getValue(), o1.getValue());
                    }

                });

        timer_ = new Timer();
        timer_.schedule(new NextModeTimer(), 45000);
        playSong(map);
    }

    /**-----------CONTROL PLAYER-----------*/

    //TODO !!!!!!!!!!!
    private void playSong(List<Map.Entry<String, Double>> songsID){
        String jsonUris = "{\"uris\":[";
        for(Map.Entry<String, Double> e : songsID){
            jsonUris += "\"spotify:track:" + e.getKey() +"\",";
        }

        jsonUris = jsonUris.substring(0, jsonUris.length() -1) + "]}";

        final String jsonUrisFinal = jsonUris;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, SPOTIFY_URL + "me/player/play",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("MAINMONTRUC", "Plays song :" +response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("MAINMONTRUC", "Cannot play song : " + error.toString());
                    }
                }
        ) {

            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SpotifySingleton.get().getAccessToken());
                return headers;
            }

            @Override
            public byte[] getBody(){
                try {
                    JSONObject jsonObject = new JSONObject(jsonUrisFinal);
                    Log.e("BODYJSON", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("BODYJSON", "PROBLEM 1");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("BODYJSON", "PROBLEM 2 : ");
                    e.printStackTrace();
                }
                return null;
            }
        };

        queue_.add(putRequest);

    }

    public void playSong(final String trackURI){
        StringRequest putRequest = new StringRequest(Request.Method.PUT, SPOTIFY_URL + "me/player/play",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("MAINMONTRUC", "Plays song :" +response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("MAINMONTRUC", "Cannot play song : " + error.toString());
                    }
                }
        ) {

            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SpotifySingleton.get().getAccessToken());
                return headers;
            }

            @Override
            public byte[] getBody(){
                try {
                    JSONObject jsonObject = new JSONObject("{\"uris\": [\" " + trackURI +  " \"}");
                    Log.i("json", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        queue_.add(putRequest);

    }

    public void stopRunSongs(){
        switch (runningMode_){
            case WALK:
                goToNextMode(this.getWindow().getDecorView());
                break;
            case RUN_DISTANCE:
                goToNextMode(this.getWindow().getDecorView());
                break;
            case RUN_TIME:
                goToNextMode(this.getWindow().getDecorView());
                break;
            case INTERVAL:
                break;
            default:
                Log.e("MAINACTIVITY", "NO RUNNING MODE");
        }
    }

    private void stopSong(){
        StringRequest putRequest = new StringRequest(Request.Method.PUT, SPOTIFY_URL + "me/player/pause",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("MAINMONTRUC", "PAUSE");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("MAINMONTRUC", "Cannot pause");
                    }
                }
        ) {

            /** Passing some request headers* */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SpotifySingleton.get().getAccessToken());
                return headers;
            }

        };

        queue_.add(putRequest);

    }




    /**-----------REQUEST PLAYLIST AND PARSING-----------*/

    private void requestPlaylist(){
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SPOTIFY_URL + "playlists/0tWjZRwhX09MRKWBMAr5Zq/tracks?fields=items(track(id))", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MAIN", "Received playlist");
                        jsonPlaylist_ = response;
                        parse();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MAIN", "Cannot retrieve playlist");

                    }
                })

        {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + SpotifySingleton.get().getAccessToken());
                Log.e("TOKEN", SpotifySingleton.get().getAccessToken());
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue_.add(request);
    }

    private void parse(){
        playlist_ = new ArrayList<>();
        try {
            JSONArray array = jsonPlaylist_.getJSONArray("items");
            for(int i = 0 ; i < array.length() ; i++){
                JSONObject track = array.getJSONObject(i).getJSONObject("track");
                playlist_.add(track.get("id").toString());
            }
            retrieveTrackAnalysis();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void retrieveTrackAnalysis() {
        playlistBPM_ = new HashMap<>();
        final int[] count = {0};
        for (final String s : playlist_) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SPOTIFY_URL + "audio-analysis/" + s, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject track = response.getJSONObject("track");
                                //addToPlaylistBPM(track.getDouble("tempo"), play);
                                count[0] += 1;

                                Log.e("TEMPO", "tempo : " + playlistBPM_.putIfAbsent(s, track.getDouble("tempo")) + "s : " + s + " count : " + count[0]);

                                if (count[0] == playlist_.size()) {
                                    playlistWarmup();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("MAIN", "Cannot retrieve analysis");

                        }
                    })

            {
                /**
                 * Passing some request headers*
                 */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "Bearer " + SpotifySingleton.get().getAccessToken());
                    return headers;
                }
            };

            // Add the request to the RequestQueue.
            queue_.add(request);
        }

    }


    /**-----------OBSERVABLE-----------*/

    @Override
    public void update(Observable o, Object arg) {
        MockData m = (MockData) o;
        heartRate = m.getHeartBeat();
        slope = m.getLiveSlope();
        speed = m.getLiveSpeed();
        totalDistance = m.getTotalDistance();
        updateFields();
    }

    private void updateFields(){
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        ((TextView)findViewById(R.id.textview_main_distance)).setText(numberFormat.format(totalDistance) + " km");
        ((TextView)findViewById(R.id.textview_main_slope)).setText(numberFormat.format(slope) + " %");
        ((TextView)findViewById(R.id.textview_main_livespeed)).setText(numberFormat.format(speed) + " km/h");
        ((TextView)findViewById(R.id.textview_main_heartrate)).setText(heartRate + " bpm");
    }

    /**-----------TIMERS-----------*/

    private class StopRunPlayer extends TimerTask {
        @Override
        public void run() {
            stopRunSongs();
        }
    }

    private class CheckDistanceTimer extends TimerTask {
        @Override
        public void run() {
            if(distance_ <= totalDistance){
                stopRunSongs();}
        }
    }

    private class FastIntervalTimer extends TimerTask {
        @Override
        public void run() {
            playlistIntervalSlow();
        }
    }

    private class SlowIntervalTimer extends TimerTask {
        @Override
        public void run() {
            playlistIntervalFast();
        }
    }

    private class NextModeTimer extends TimerTask {
        @Override
        public void run() {
            goToNextMode();
        }
    }
}