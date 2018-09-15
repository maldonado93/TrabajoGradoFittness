package com.example.uer.trabajogradofittness.MenuPrincipal;

import android.support.v4.app.FragmentManager;

import com.example.uer.trabajogradofittness.GlobalState;

public class NavigationManager {

    private static NavigationManager instancia;
    private FragmentManager fragmentManager;
    private Menu menu;
    GlobalState gs;

    public static NavigationManager getInstancia(Menu menu){
        if(instancia == null){
            instancia = new NavigationManager();
        }
        instancia.configurar(menu);

        return instancia;
    }

    private void configurar(Menu menu){
        menu = menu;
        fragmentManager = menu.getSupportFragmentManager();
    }

}
