package ch.techteam.sensorsender;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;

import java.util.concurrent.ScheduledExecutorService;


public class UpdateSender extends WearableActivity implements SensorEventListener {
    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;
    private final String dataItemHeartURIString = "/data/sensor/heartbeat";
    private final String heartTag = "HEARTBEAT";

    private DataClient mDataClient;
    private SensorManager mSensorManager;
    private Sensor mHeartrateSensor;
    private ScheduledExecutorService mScheduler;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        setContentView(R.layout.activity_main);
    }

    public void startMesurement(){

        mHeartrateSensor = mSensorManager.getDefaultSensor(SENS_HEARTRATE);

        if (mHeartrateSensor != null) {
            mSensorManager.registerListener(this, mHeartrateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.d("HEARTRATE_SENSOR", "No Heartrate Sensor found");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            int heartbeat = (int)event.values[0];

            Log.d("HEART_RATE", heartbeat + "");

            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(dataItemHeartURIString);
            DataMap dataMap = putDataMapRequest.getDataMap();
            dataMap.putInt(heartTag, heartbeat);
            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
            TextView h = (TextView) findViewById(R.id.textview_heartbeat);
            h.setText(heartbeat + " bpm");


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
