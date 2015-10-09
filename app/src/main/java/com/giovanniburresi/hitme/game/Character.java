package com.giovanniburresi.hitme.game;

import com.giovanniburresi.hitme.R;

/**
 * Created by Mario on 09/10/2015.
 */
public class Character {

    public final static int RYU       = 1;
    public final static int WOLVERINE = 2;

    public final static int CENTER = 0;
    public final static int RIGHT  = 2;
    public final static int TOP    = 4;
    public final static int LEFT   = 6;

    public final static int HIT    = 0;
    public final static int MISS   = 1;


    private static int frecceRyu[] = {R.drawable.ryu_colpo_centro_1,
            R.drawable.ryu_colpo_centro_2,
            R.drawable.ryu_colpo_dx_1,
            R.drawable.ryu_colpo_dx_2,
            R.drawable.ryu_colpo_sopra_1,
            R.drawable.ryu_colpo_sopra_2,
            R.drawable.ryu_colpo_sx_1,
            R.drawable.ryu_colpo_sx_2};

    private static int hitMissRyu[] = {R.drawable.ryu_centro_colpito,
            R.drawable.ryu_centro_errore,
            R.drawable.ryu_dx_colpito,
            R.drawable.ryu_dx_errore,
            R.drawable.ryu_sopra_colpito,
            R.drawable.ryu_sopra_errore,
            R.drawable.ryu_sx_colpito,
            R.drawable.ryu_sx_errore};

    private static int countdownRyu[] = {R.drawable.ryu_start,
            R.drawable.ryu_ready_1,
            R.drawable.ryu_ready_2,
            R.drawable.ryu_ready_3
            };



    private int frecceWolverine[] = {R.drawable.ryu_colpo_centro_1,
            R.drawable.ryu_colpo_centro_2,
            R.drawable.ryu_colpo_dx_1,
            R.drawable.ryu_colpo_dx_2,
            R.drawable.ryu_colpo_sopra_1,
            R.drawable.ryu_colpo_sopra_2,
            R.drawable.ryu_colpo_sx_1,
            R.drawable.ryu_colpo_sx_2};

    private int hitMissRyuWolverine[] = {R.drawable.ryu_centro_colpito,
            R.drawable.ryu_centro_errore,
            R.drawable.ryu_dx_colpito,
            R.drawable.ryu_dx_errore,
            R.drawable.ryu_sopra_colpito,
            R.drawable.ryu_sopra_errore,
            R.drawable.ryu_sx_colpito,
            R.drawable.ryu_sx_errore};

    private int countdownWolverine[] = {R.drawable.ryu_start,
            R.drawable.ryu_ready_1,
            R.drawable.ryu_ready_2,
            R.drawable.ryu_ready_3
    };


    public static int  getFreccia(int character, int position, int frame){
        return frecceRyu[position + frame];
    }

    public static int  getHitMiss(int character, int position, int missed){
        return hitMissRyu[position + missed];
    }

    public static int getCountdown(int character, int instant){
        return countdownRyu[instant];
    }

}
