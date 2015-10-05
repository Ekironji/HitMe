package com.giovanniburresi.hitme.sounds;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.giovanniburresi.hitme.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mario on 05/10/2015.
 */
public class Sounds {

    private int[] voiceGio = {
            R.raw.g_01,
            R.raw.g_02,
            R.raw.g_03,
            R.raw.g_04,
            R.raw.g_05,
            R.raw.g_06,
            R.raw.g_07,
            R.raw.g_08,
            R.raw.g_09,
            R.raw.g_10,
            R.raw.g_11,
            R.raw.g_12,
            R.raw.g_13,
            R.raw.g_14,
            R.raw.g_15,
            R.raw.g_16,
            R.raw.g_17,
            R.raw.g_18,
            R.raw.g_19
    };

    private int[] voiceRizzo = {
            R.raw.r_01,
            R.raw.r_02,
            R.raw.r_03,
            R.raw.r_04,
            R.raw.r_05,
            R.raw.r_06,
            R.raw.r_07,
            R.raw.r_08,
            R.raw.r_09,
            R.raw.r_10
    };

    private int[] voiceSbarby = {
            R.raw.s_01,
            R.raw.s_02,
            R.raw.s_03,
            R.raw.s_04,
            R.raw.s_05,
            R.raw.s_06,
            R.raw.s_07,
            R.raw.s_08,
            R.raw.s_09,
            R.raw.s_10,
            R.raw.s_11,
            R.raw.s_12,
            R.raw.s_13
    };

    private int[] voiceX = {
            R.raw.x_01,
            R.raw.x_02,
            R.raw.x_03,
            R.raw.x_04
    };

    private Context mContext;

    private SoundPool soundPoolGio, soundPoolRizzo, soundPoolSbarby, soundPoolX;
    private SoundPool.Builder builder;
    private int soundID;
    boolean plays[]  = {false, false, false, false};
    boolean loaded[] = {false, false, false, false};
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    ArrayList<Integer> mGioSounds = null;
    ArrayList<Integer> mRizzoSounds = null;
    ArrayList<Integer> mSbarbySounds = null;
    ArrayList<Integer> mXSounds = null;

    public Sounds(Context mContext) {
        this.mContext = mContext;

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Hardware buttons setting to adjust the media sound
        // Activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundPoolGio = new SoundPool(19, AudioManager.STREAM_MUSIC, 0);
        soundPoolRizzo = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPoolSbarby = new SoundPool(13, AudioManager.STREAM_MUSIC, 0);
        soundPoolX = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        mGioSounds = new ArrayList<>();
        mRizzoSounds = new ArrayList<>();
        mSbarbySounds = new ArrayList<>();
        mXSounds = new ArrayList<>();

        soundPoolGio.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded[0] = true;
            }
        });


        soundPoolGio.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded[0] = true;
            }
        });

/*
        for (int i = 0; i < 19; i++)
            mGioSounds.add(new SoundPool(1, audioManager.STREAM_MUSIC, 0));

        for (int i = 0; i < 10; i++)
            mRizzoSounds.add(new SoundPool(1, audioManager.STREAM_MUSIC, 0));

        for (int i = 0; i < 13; i++)
            mSbarbySounds.add(new SoundPool(1, audioManager.STREAM_MUSIC, 0));

        for (int i = 0; i < 4; i++)
            mXSounds.add(new SoundPool(1, audioManager.STREAM_MUSIC, 0));*/

        for (int i = 0; i < voiceGio.length; i++)
            mGioSounds.add(soundPoolGio.load(mContext, voiceGio[i], 1));

        for (int i = 0; i < voiceRizzo.length; i++)
            mRizzoSounds.add(soundPoolRizzo.load(mContext, voiceRizzo[i], 1));

        for (int i = 0; i < voiceSbarby.length; i++)
            mSbarbySounds.add(soundPoolSbarby.load(mContext, voiceSbarby[i], 1));

        for (int i = 0; i < voiceX.length; i++)
            mXSounds.add(soundPoolX.load(mContext, voiceX[i], 1));

    }

    public void playGioSound() {
        int s = (int)(Math.random() * voiceGio.length);
        soundPoolGio.play(mGioSounds.get(s), 0.8f, 0.8f, 0, 0, 1.0f);
    }


    public void playSbarbySound() {
        int s = (int)(Math.random() * voiceGio.length);
        soundPoolSbarby.play(mSbarbySounds.get(s), 0.8f, 0.8f, 0, 0, 1.0f);
    }
}
