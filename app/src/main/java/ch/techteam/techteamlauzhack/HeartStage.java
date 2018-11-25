package ch.techteam.techteamlauzhack;

import java.util.Observable;

public class HeartStage extends Observable {
    public enum Stage{
        High,
        Low,
        Middle;

        public static Stage fromBPM(int bpm){
            if(bpm < 100){
                return Low;
            }
            if(bpm >= 100 && bpm < 150){
                return Middle;
            }
            else{
                return High;
            }
        }
    }
    private Stage currentStage;
    private Stage nextStage;
    private int buffer;
    private final int threshhold = 5;


    public HeartStage(int currentBPM){
        currentStage = Stage.fromBPM(currentBPM);
        nextStage = null;
        buffer = 0;
    }

    public void update(int bpm){
        if(Stage.fromBPM(bpm) == nextStage && nextStage != currentStage){
            if(buffer > threshhold){
                currentStage = nextStage;
                setChanged();
                notifyObservers();
                buffer = 0;
            }
            else{
                buffer += 1;
            }
        }else{
            nextStage = Stage.fromBPM(bpm);
            buffer = 1;
        }
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
}
