package com.giovanniburresi.hitme;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giovanniburresi.hitme.game.*;
import com.giovanniburresi.hitme.game.Character;

/**
 * A placeholder fragment containing a simple view.
 */
public class HitmeMainActivityFragment extends Fragment {

    private HitmeMainActivity mainActivity = null;

    private ImageButton itaButton = null;
    private ImageButton engButton = null;
    private ImageButton romButton = null;
    private ImageButton ita2Button = null;

    private ImageView background = null;

    private TextView scoreTextView = null;
    private TextView centerTextView = null;
    private TextView timeTextView = null;


    public HitmeMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_hitme_main, container, false);

        mainActivity = (HitmeMainActivity) getActivity();

        itaButton = (ImageButton) view.findViewById(R.id.language1);
        engButton = (ImageButton) view.findViewById(R.id.language2);
        romButton = (ImageButton) view.findViewById(R.id.language3);
        ita2Button = (ImageButton) view.findViewById(R.id.language4);

        background = (ImageView) view.findViewById(R.id.background);

        scoreTextView  = (TextView) view.findViewById( R.id.bestscore );
        centerTextView = (TextView) view.findViewById( R.id.centerText );
        timeTextView = (TextView) view.findViewById( R.id.timeTextView );

        itaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed(1);
            }
        });

        engButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed(2);
            }
        });

        romButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed(3);
            }
        });

        ita2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed(4);
            }
        });

        return view;
    }


    private void buttonPressed(int lang){
        mainActivity.langButtonPressed(lang);
    }

    public void setScoreLabel(int score){
        scoreTextView.setText("SCORE : " + score);
    }

    public void setScoreLabel(String s){
        scoreTextView.setText(s);
    }

    public void setCenterTextView(String s){
        centerTextView.setText(s);
    }

    public void setRightTextView(int time){
        timeTextView.setText(time / 1000 + " seconds left");
    }

    public void setRightTextView(String s){
        timeTextView.setText(s);
    }



    public void standby(){
        Log.i("startMatch", "standby");
        background.setImageResource(R.drawable.main);
        setCenterTextView("hit me to start! \nHIGHSCORE " + mainActivity.getHighscore());
        setRightTextView("");
        setScoreLabel("");
    }

    public void preMatch(){
        Log.i("startMatch", "preMatch");
        background.setImageResource(Character.getCountdown(Character.RYU, 3));
        //
    }

    public void startMatch(){
        Log.i("startMatch", "startMatch");
        arrowNo();
    }

    public void gameover(){
        Log.i("startMatch", "gameover");
        background.setImageResource(R.drawable.winner);
    }

    public void changeBackground(int id){
        if(id == 0)
            standby();
        else if(id == 1)
            preMatch();
        else if(id == 2)
            startMatch();
        else if(id == 3)
            gameover();

    }


    int selectedCharacter = Character.RYU;

    // Animations
    public void arrow(int arrow){
        switch(arrow){
            case 0:
                arrowNo();
                break;
            case 1:
                arrowRight();
                break;
            case 2:
                arrowLeft();
                break;
            case 3:
                arrowTop();
                break;
            case 4:
                arrowFront();
                break;
        }
    }

    public void arrowTop(){
        background.setImageResource(Character.getFreccia(selectedCharacter, Character.TOP, 0));
    }

    public void arrowFront(){
        background.setImageResource(Character.getFreccia(selectedCharacter, Character.CENTER, 0));
    }

    public void arrowLeft(){
        background.setImageResource(Character.getFreccia(selectedCharacter, Character.LEFT, 0));
    }

    public void arrowRight(){
        background.setImageResource(Character.getFreccia(selectedCharacter, Character.RIGHT, 0));
    }

    public void arrowNo(){
        background.setImageResource(Character.getCountdown(selectedCharacter, 0));
    }


    public void hitOk (int position){
        background.setImageResource(Character.getHitMiss(selectedCharacter, Character.CENTER, 0));
    }

    public void hitMissed (int position){
        background.setImageResource(Character.getHitMiss(selectedCharacter, Character.CENTER, 1));
    }



}
