package com.giovanniburresi.hitme;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.usb.UsbManager;

import com.giovanniburresi.hitme.game.HitmeGame;
import com.giovanniburresi.hitme.sounds.Sounds;

import java.util.ArrayList;

import me.palazzetti.adktoolkit.AdkManager;
import me.palazzetti.adktoolkit.response.AdkMessage;

public class HitmeMainActivity extends Activity {

    private boolean IS_DEBUG = true;

    // ADK needed
    private AdkManager mAdkManager;
    private AdkReadTask mAdkReadTask;

    //Fragments
    private HitmeMainActivityFragment mMainFragment;

    // Game dynamics
    HitmeGame mHitmeGame = null;
    ArrayList<HitmeGame> mGames = null;

    Sounds mSounds = null;

    int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitme_main);

        mAdkManager = new AdkManager((UsbManager) getSystemService(Context.USB_SERVICE));
        registerReceiver(mAdkManager.getUsbReceiver(), mAdkManager.getDetachedFilter());

        mMainFragment = new HitmeMainActivityFragment();

        mGames = new ArrayList<HitmeGame>();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mMainFragment)
                    .commit();
        }

        mSounds = new Sounds(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdkManager.open();
        if(IS_DEBUG) {
            mAdkReadTask = new AdkReadTask();
            mAdkReadTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdkManager.close();
        if(IS_DEBUG) {
            mAdkReadTask.pause();
            mAdkReadTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAdkManager.getUsbReceiver());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hitme_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
	 * We put the readSerial() method in an AsyncTask to run the
	 * continuous read task out of the UI main thread
	 */
    private class AdkReadTask extends AsyncTask<Void, String, Void> {

        private boolean running = true;

        public void pause(){
            running = false;
        }

        protected Void doInBackground(Void... params) {
//	    	Log.i("ADK demo bi", "start adkreadtask");
            while(running) {
                AdkMessage response = null;
                try {
                    response = mAdkManager.read();
                    publishProgress(response.getString());
                }
                catch (Exception e) {
                    Log.w("doInBackGround","No adk available");
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            if(progress[0].equals("d")){
                langButtonPressed(1);
            }
            else if(progress[0].equals("s")){
                langButtonPressed(2);
            }
            else if(progress[0].equals("u") || progress[0].equals("g")){
                langButtonPressed(3);
            }
            else if(progress[0].equals("i") || progress[0].equals("a")){
                langButtonPressed(4);
            }
        }
    }


    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String status = (String) msg.obj;
            int id   = msg.arg1;
            int arg2 = msg.arg2;

            if(id == 1) // etichetta centrale
                mMainFragment.setCenterTextView(status);
            else if(id == 2) // etichetta punteggio
                mMainFragment.setScoreLabel(arg2);
            else if(id == 3) // etichetta tempo
                mMainFragment.setRightTextView(arg2);

            else if(id == 4) { // cambio sati
                mMainFragment.changeStatus(arg2);

                switch (arg2) {
                    case HitmeGame.STANDBY:
                        mMainFragment.standby();
                        break;
                    case HitmeGame.PREMATCH:
                        mMainFragment.preMatch();
                        break;
                    case HitmeGame.STARTMATCH:
                        break;
                    case HitmeGame.GAMEOVER:
                        break;
                }
            }
            else if(id == 5){  // new posizione
                mMainFragment.arrow(arg2);
            }
            else if(id == 6) { // notifyHit
                mMainFragment.hitOk(arg2);
                mSounds.playSbarbyHitSound();
            }
            else if(id == 7) { // notifyMiss
                mMainFragment.hitMissed(arg2);
                mSounds.playSbarbyMissSound();
            }
        }
    };


    public void langButtonPressed(int lang){
        if(mHitmeGame == null || mHitmeGame.isTerminated()) {
            mHitmeGame = new HitmeGame(this, handler);
            mGames.add(mHitmeGame);
            mHitmeGame.start();
        }
        else {
            mHitmeGame.moveRecieved(lang);
        }
    }

    public int getHighscore(){
        int max = 0;

        for(HitmeGame h : mGames){
            if(h.getPoints() > max){
                max = h.getPoints();
            }
        }

        return max;
    }
}
