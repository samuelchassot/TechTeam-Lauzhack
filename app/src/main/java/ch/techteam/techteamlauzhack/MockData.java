package ch.techteam.techteamlauzhack;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.data.DataBufferObserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class MockData extends Observable {

    private int heartBeat;
    private double liveSpeed;
    private double liveSlope;
    private double totalDistance;

    private BufferedReader inHeart;
    private BufferedReader inSpeed;
    private BufferedReader inSlope;
    private BufferedReader inDist;


    public MockData(Context ctx){
        try {
//            inHeart = new BufferedReader(new FileReader("heartbeat.txt"));
            inHeart = new BufferedReader(new InputStreamReader(ctx.getAssets().open("heartbeat.txt")));
//            inSpeed = new BufferedReader(new FileReader("speed.txt"));
            inSpeed = new BufferedReader(new InputStreamReader(ctx.getAssets().open("speed.txt")));
//            inDist = new BufferedReader(new FileReader("distance.txt"));
            inDist = new BufferedReader(new InputStreamReader(ctx.getAssets().open("distance.txt")));
//            inSlope = new BufferedReader(new FileReader("slope.txt"));
            inSlope = new BufferedReader(new InputStreamReader(ctx.getAssets().open("slope.txt")));

        } catch (IOException e) {
            Log.e("MockDataIn", e.toString());
        }

    }

    public void run(){
        while(true) {
            if(!update()){
                break;
            }
            setChanged();
            notifyObservers();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean update(){
        try {
            if(inHeart.ready()) {
                heartBeat = Integer.valueOf(inHeart.readLine());
            }else{
                return false;
            }
            if(inDist.ready()) {
                totalDistance = Double.valueOf(inDist.readLine());
            }else{
                return false;
            }
            if(inSlope.ready()) {
                liveSlope = Double.valueOf(inSlope.readLine());
            }else{
                return false;
            }
            if(inSpeed.ready()) {
                liveSpeed = Double.valueOf(inSpeed.readLine());
            }else{
                return false;
            }
        } catch (IOException e) {
            Log.e("IO", "io exception \n" +  e.toString());
        }
        return true;
    }

    public int getHeartBeat() {
        return heartBeat;
    }

    public double getLiveSpeed() {
        return liveSpeed;
    }

    public double getLiveSlope() {
        return liveSlope;
    }

    public double getTotalDistance() {
        return totalDistance;
    }
}
