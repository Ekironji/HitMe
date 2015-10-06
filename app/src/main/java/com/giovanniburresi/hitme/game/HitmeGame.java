package com.giovanniburresi.hitme.game;

import android.util.Log;

import com.giovanniburresi.hitme.HitmeMainActivity;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Mario on 02/10/2015.
 */
public class HitmeGame extends Thread {

    private int COMBO_MULTIPLIER = 100;
    private int FAST_HIT_POINT = 200;
    private int SLOW_HIT_POINT = 100;
    private int AXIS_HIT_POINT = 100;
    private int DIRECTION_HIT_POINT = 200;

    private final int RIGHT_HIT = 1;
    private final int LEFT_HIT  = 2;
    private final int FRONT_HIT = 3;
    private final int UP_HIT    = 4;
//    private final int DOWN_HIT  = 5;
//    private final int BACK_HIT  = 6;

    public static final int STANDBY    = 0;
    public static final int PREMATCH   = 1;
    public static final int STARTMATCH = 2;
    public static final int GAMEOVER   = 3;

    private int timeLimit   = 20;

    private int UPDATE_TIME = 50; // msec
    private int FAST_GAP = 700;

    private int HIT_DURATION = 1000;

    private float MOVE_RATE = 0.05f;

    private int points = 0;

    // game vars
    private long startTime = -1;
    private long endTime   = -1;
    private long time      = -1;
    private int  remaningSeconds = 0;

    private int consecutiveHit = 0;

    private int  activeHit   =  0;
    private long lastHitTime = -1;

    private boolean isTerminated  = false;
    private boolean canReciveHits = false;


    private HitmeMainActivity mainActivity = null;
    private Handler mHandler = null;

    public HitmeGame(HitmeMainActivity mainActivity, Handler mHandler){
        this.mainActivity = mainActivity;
        this.mHandler = mHandler;
    }

    // timer funcs
    public void startTimer(){
        this.start();
    }

    public void resetTimer(int secs){
        timeLimit = secs;
    }

    public int getPoints(){
        return points;
    }

    @Override
    public void run(){

        sendFace("READY ?!");
        changeStatus(PREMATCH);
        for(int i=3; i>0; i--){
            sendFace("" + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sendFace("FIGHT !");
        changeStatus(STARTMATCH);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        canReciveHits = true;

        time = System.currentTimeMillis();
        startTime = time;
        endTime = startTime + timeLimit * 1000;
        remaningSeconds = (int) (endTime - time);

        while(time < endTime){

            sendTimeLeft();

            time = System.currentTimeMillis();
            remaningSeconds = (int) (endTime - time);

            //controllo se ci NON sono eventi attivi ne lancio un'altro
            if(activeHit == 0) {
                setHit(launchNewHit(MOVE_RATE));
                lastHitTime = time;
                Log.d("HitGame", "Hit added " + activeHit);
            } else { // altrimenti controllo da quanto tempo
                if(time - lastHitTime > HIT_DURATION){
                    Log.d("HitGame", "Hit removed");
                    points -= 50;
                    removeHit();
                }
            }

            // pause
            try {
                Thread.sleep(UPDATE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.d("HitGame", "Remaning time seconds: " + remaningSeconds / 1000);
        }

        gameOver();



    }

    public int moveRecieved(int move){

        if(!canReciveHits)
            return points;

        if(activeHit != 0){
            removeHit();
            points += SLOW_HIT_POINT;

            if(move == activeHit)
                points += FAST_HIT_POINT;

            // controllo se veloce
            if(System.currentTimeMillis() - lastHitTime < FAST_GAP)
                points += FAST_GAP;

            // controllo il destra sinistra
            if(activeHit == RIGHT_HIT)
                if(move == RIGHT_HIT)
                    points += DIRECTION_HIT_POINT;
                else if(move == LEFT_HIT)
                    points += AXIS_HIT_POINT;
            else if(activeHit == LEFT_HIT)
                if(move == LEFT_HIT)
                    points += DIRECTION_HIT_POINT;
                else if(move == RIGHT_HIT)
                    points += AXIS_HIT_POINT;
        }
        else
            points -= 50;

        sendPoints();
        Log.i("moveRecieved", "Points: " + points);
        return points;
    }

    public boolean isTerminated(){
        return isTerminated;
    }

    private int getRandomHit(int max){
        int rand = (int) ((Math.random() * max) + 1);
        if(rand > max)
            return max;
        return rand;
    }

    private int launchNewHit(float hitPerSeconds){
        double dice = (Math.random() * (1000 / UPDATE_TIME));
        double th = (1000 / UPDATE_TIME) * (hitPerSeconds * 2);

        if(dice < th){
            Log.v("launchNewHit", "OOOKKKKKK" + dice + " " + th);
            return getRandomHit(4);
        }
        else {
            Log.v("launchNewHit", "NOOO" + dice + " " + th );
            return 0;
        }
    }

    private void setHit(int hit){
        activeHit = hit;
        sendFace(activeHit);
    }

    private void removeHit(){
        activeHit = 0;
        sendFace(activeHit);
        sendPoints();
    }


    private void gameOver() {
        Log.w("gameOver", "GAME OVERRRR");
        sendFace("GAME OVER\n" + points);
        changeStatus(GAMEOVER);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendFace("HIT TO START");
        changeStatus(STANDBY);
        isTerminated = true;
        canReciveHits = false;
    }

    private void sendFace(String face){
        Message m = mHandler.obtainMessage();
        m.arg1 = 1;
        m.obj = face;
        mHandler.sendMessage(m);
    }

    private void sendFace(int face){
        Message m = mHandler.obtainMessage();
        m.arg1 = 1;
        m.obj = "( " + face + " )";
        mHandler.sendMessage(m);
    }

    private void sendPoints(){
        Message m = mHandler.obtainMessage();
        m.arg1 = 2;
        m.arg2 = points;
        mHandler.sendMessage(m);
    }

    private void sendTimeLeft(){
        Message m = mHandler.obtainMessage();
        m.arg1 = 3;
        m.arg2 = remaningSeconds;
        mHandler.sendMessage(m);
    }


    private void changeStatus(int status){
        Message m = mHandler.obtainMessage();
        m.arg1 = 4;
        m.arg2 = status;
        mHandler.sendMessage(m);
    }

}
