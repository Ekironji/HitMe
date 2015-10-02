package com.giovanniburresi.hitme;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.giovanniburresi.hitme.game.HitmeGame;

import java.io.FileDescriptor;
import java.io.FileInputStream;

import me.palazzetti.adktoolkit.AdkManager;
import me.palazzetti.adktoolkit.response.AdkMessage;

public class HitmeMainActivity extends Activity {

    private boolean IS_DEBUG = false;

    // ADK needed
    private AdkManager mAdkManager;
    private AdkReadTask mAdkReadTask;

    //Fragments
    private HitmeMainActivityFragment mMainFragment;

    // Game dynamics
    HitmeGame mHitmeGame = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitme_main);

        mAdkManager = new AdkManager((UsbManager) getSystemService(Context.USB_SERVICE));
        registerReceiver(mAdkManager.getUsbReceiver(), mAdkManager.getDetachedFilter());

        mMainFragment = new HitmeMainActivityFragment();


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mMainFragment)
                    .commit();
        }
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
                AdkMessage response = mAdkManager.read();
                System.out.println(response.getString());
                publishProgress(response.getString());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }
    }


    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String status = (String) msg.obj;
            int id   = msg.arg1;
            int arg2 = msg.arg2;

            if(id == 1)
                mMainFragment.setCenterTextView(status);
            if(id == 2)
                mMainFragment.setHighScore(arg2);
            if(id == 3)
                mMainFragment.setRightTextView(arg2);
            if(id == 4)
                mMainFragment.changeBackground(arg2);
        }
    };


    public void langButtonPressed(int lang){

        Log.i("HMActivity.buttonPres", "langButtonPressed " + lang);

        if(mHitmeGame == null || mHitmeGame.isTerminated()) {
            mHitmeGame = new HitmeGame(this, handler);
            mHitmeGame.start();
        }
        else{
            mHitmeGame.moveRecieved(lang);
        }



    }
}
