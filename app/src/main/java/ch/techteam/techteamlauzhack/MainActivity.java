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


public class MainActivity extends AppCompatActivity {

    private StateMode stateMode_;
    private RunningMode runningMode_;
    private float meanHeartRate_;
    private float slowIntervalTime;
    private float fastIntervalTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

    }

    private void playlistDependingOnStateMode(){
        switch (stateMode_){
            case FOCUS:
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
}
