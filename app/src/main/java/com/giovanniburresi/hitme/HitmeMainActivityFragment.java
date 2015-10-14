package com.giovanniburresi.hitme;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    // background
    private ImageView background = null;
    // textview
    private TextView scoreTextView = null;
    private TextView centerTextView = null;
    private TextView timeTextView = null;

    private ImageView arrowLeft = null;
    private ImageView arrowRight = null;
    private ImageView arrowDown = null;
    private ImageView arrowUp = null;
    private ImageView sacco = null;



    public HitmeMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_hitme_main_2, container, false);

        mainActivity = (HitmeMainActivity) getActivity();

        itaButton = (ImageButton) view.findViewById(R.id.language1);
        engButton = (ImageButton) view.findViewById(R.id.language2);
        romButton = (ImageButton) view.findViewById(R.id.language3);
        ita2Button = (ImageButton) view.findViewById(R.id.language4);

        // gui
        background = (ImageView) view.findViewById(R.id.background);
        arrowLeft = (ImageView) view.findViewById(R.id.arrowLeft);
        arrowRight = (ImageView) view.findViewById(R.id.arrowRight);
        arrowDown = (ImageView) view.findViewById(R.id.arrowDown);
        arrowUp = (ImageView) view.findViewById(R.id.arrowUp);
        sacco = (ImageView) view.findViewById(R.id.sacco);

        scoreTextView  = (TextView) view.findViewById( R.id.bestscore );
        centerTextView = (TextView) view.findViewById( R.id.centerText );
        timeTextView = (TextView) view.findViewById( R.id.timeTextView );

        itaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testButtonPressed(1);
            }
        });

        engButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testButtonPressed(2);
            }
        });

        romButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testButtonPressed(3);
            }
        });

        ita2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testButtonPressed(4);
            }
        });

        standby();

        return view;
    }

    // bottoni di test Sinistra destra su giu
    private void testButtonPressed(int lang){
        mainActivity.langButtonPressed(lang);
    }

    // etichetta alto sinistra
    public void setScoreLabel(int score){
        scoreTextView.setText("SCORE : " + score);
    }

    public void setScoreLabel(String s){
        scoreTextView.setText(s);
    }

    // etichetta centrale
    public void setCenterTextView(String s){
        centerTextView.setText(s);
    }


    // etichetta alto destra
    public void setRightTextView(int time){
        timeTextView.setText(time / 1000 + " seconds left");
    }

    public void setRightTextView(String s){
        timeTextView.setText(s);
    }



    // azioni per il cambio di stato
    public void changeStatus(int id){
        if(id == 0)
            standby();
        else if(id == 1)
            preMatch();
        else if(id == 2)
            startMatch();
        else if(id == 3)
            gameover();
    }

    public void standby(){
        Log.i("startMatch", "standby");
        arrowNo();
        hideSacco();
        background.setImageResource(R.drawable.intro);
        //setCenterTextView("hit me to start! \nHIGHSCORE " + mainActivity.getHighscore());
        setCenterTextView("");
        setRightTextView("");
        setScoreLabel("");
    }

    public void preMatch(){
        Log.i("startMatch", "preMatch");
        background.setImageResource(Character.getCountdown(Character.RYU, 3));
        hideSacco();
        //
    }

    public void startMatch(){
        Log.i("startMatch", "startMatch");
        background.setImageResource(R.drawable.sfondo_fight);
        arrowNo();
        showSacco();
    }

    public void gameover(){
        Log.i("startMatch", "gameover");
        arrowNo();
        hideSacco();
        background.setImageResource(R.drawable.end);
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
                arrowUp();
                break;
            case 4:
                arrowFront();
                break;
        }
    }

    public void arrowUp(){
        //old
        //background.setImageResource(Character.getFreccia(selectedCharacter, Character.TOP, 0));

        //new
        arrowRight.setVisibility(View.INVISIBLE);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowUp.setVisibility(View.VISIBLE);
        arrowDown.setVisibility(View.INVISIBLE);
    }

    public void arrowFront(){
        //new
        arrowRight.setVisibility(View.INVISIBLE);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowUp.setVisibility(View.INVISIBLE);
        arrowDown.setVisibility(View.VISIBLE);
    }

    public void arrowLeft(){
        //new
        arrowRight.setVisibility(View.INVISIBLE);
        arrowLeft.setVisibility(View.VISIBLE);
        arrowUp.setVisibility(View.INVISIBLE);
        arrowDown.setVisibility(View.INVISIBLE);
    }

    public void arrowRight(){
        //new
        arrowRight.setVisibility(View.VISIBLE);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowUp.setVisibility(View.INVISIBLE);
        arrowDown.setVisibility(View.INVISIBLE);
    }

    public void arrowNo(){
        //new
        arrowRight.setVisibility(View.INVISIBLE);
        arrowLeft.setVisibility(View.INVISIBLE);
        arrowUp.setVisibility(View.INVISIBLE);
        arrowDown.setVisibility(View.INVISIBLE);
        setNeutralFace();
    }


    private void setNeutralFace(){
        if(System.currentTimeMillis() - lastHitTime > 600)
            sacco.setImageResource(R.drawable.sacco_attesa);
    }

    private long lastHitTime = 0L;

    public void hitOk (int position){
        lastHitTime = System.currentTimeMillis();
        sacco.setImageResource(R.drawable.sacco_colpito);
    }

    public void hitMissed (int position){
        lastHitTime = System.currentTimeMillis();
        sacco.setImageResource(R.drawable.sacco_mancato_front);
    }


    private void showSacco(){
        sacco.setVisibility(View.VISIBLE);
    }

    private void hideSacco(){
        sacco.setVisibility(View.INVISIBLE);
    }




}
