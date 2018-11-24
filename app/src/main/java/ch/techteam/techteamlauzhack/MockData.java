package ch.techteam.techteamlauzhack;

import android.util.Log;

import com.google.android.gms.common.data.DataBufferObserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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


    public MockData(){
        try {
            inHeart = new BufferedReader(new FileReader("heartbeat.txt"));
            inSpeed = new BufferedReader(new FileReader("speed.txt"));
            inDist = new BufferedReader(new FileReader("distance.txt"));
            inSlope = new BufferedReader(new FileReader("slope.txt"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void run(){
        update();
        setChanged();
        notifyObservers();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update(){
        try {
            if(inHeart.ready()) {
                heartBeat = Integer.valueOf(inHeart.readLine());
            }
            if(inDist.ready()) {
                totalDistance = Double.valueOf(inDist.readLine());
            }
            if(inSlope.ready()) {
                liveSlope = Double.valueOf(inSlope.readLine());
            }
            if(inSpeed.ready()) {
                liveSpeed = Double.valueOf(inSpeed.readLine());
            }
        } catch (IOException e) {
            Log.e("IO", "io exception \n" +  e.toString());
        }
    }
}
