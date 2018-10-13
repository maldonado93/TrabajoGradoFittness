package com.example.uer.trabajogradofittness;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Insignias {

    Context context;
    int nivel;


    public Insignias(Context context, int nivel) {
        this.context = context;
        this.nivel = nivel;
    }


    public Drawable getInsignia(){
        Drawable insignia = null;

        switch (nivel){
            case 0: insignia = context.getResources().getDrawable(R.mipmap.ic_insignia);
                break;
            case 1:
            case 10:
            case 19: insignia = context.getResources().getDrawable(R.mipmap.ic_bronce3);
                break;
            case 2:
            case 11:
            case 20: insignia = context.getResources().getDrawable(R.mipmap.ic_bronce2);
                break;
            case 3:
            case 12:
            case 21: insignia = context.getResources().getDrawable(R.mipmap.ic_bronce1);
                break;
            case 4:
            case 13:
            case 22: insignia = context.getResources().getDrawable(R.mipmap.ic_plata3);
                break;
            case 5:
            case 14:
            case 23: insignia = context.getResources().getDrawable(R.mipmap.ic_plata2);
                break;
            case 6:
            case 15:
            case 24: insignia = context.getResources().getDrawable(R.mipmap.ic_plata1);
                break;
            case 7:
            case 16:
            case 25: insignia = context.getResources().getDrawable(R.mipmap.ic_oro3);
                break;
            case 8:
            case 17:
            case 26: insignia = context.getResources().getDrawable(R.mipmap.ic_oro2);
                break;
            case 9:
            case 18:
            case 27: insignia = context.getResources().getDrawable(R.mipmap.ic_oro1);
                break;
        }
        return insignia;
    }
}
