package com.example.uer.trabajogradofittness;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.example.uer.trabajogradofittness.InformacionPersonal.Informacion;
import com.example.uer.trabajogradofittness.Nutricion.Nutricion;
import com.example.uer.trabajogradofittness.RegistroEntreno.RegistrosEntreno;
import com.example.uer.trabajogradofittness.Rutina.Rutina;


public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    String tituloFragment;

    GlobalState gs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        gs = (GlobalState) getApplication();

        //Inicializar fragment
        MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new MainFragment());
            ft.commit();
        }
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //drawer.closeDrawer(GravityCompat.START);
            dialogSalir();
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            tituloFragment = "Principal";
            MainFragment fragment = new MainFragment();
            cambiarFragment(fragment);
        } else if (id == R.id.nav_registros) {
            tituloFragment = "Registros de entrenos";
            RegistrosEntreno fragment = new RegistrosEntreno();
            cambiarFragment(fragment);

        } else if (id == R.id.nav_informacion) {
            tituloFragment = "Informacion personal";
            Informacion fragment = new Informacion();
            cambiarFragment(fragment);

        } else if (id == R.id.nav_rutina) {
            tituloFragment = "Rutinas";
            Rutina fragment = new Rutina();
            cambiarFragment(fragment);

        } else if (id == R.id.nav_nutricion) {
            tituloFragment = "Nutricion";
            Nutricion fragment = new Nutricion();
            cambiarFragment(fragment);

        } else if (id == R.id.nav_cuenta) {
            tituloFragment = "Cuenta";


        } else if (id == R.id.nav_salir) {
            dialogSalir();

        }

        Fragment switchTo = null;

        if(switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, switchTo);
            for(int i=0; i<getSupportFragmentManager().getBackStackEntryCount(); i++){
                getSupportFragmentManager().popBackStackImmediate();
            }
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cambiarFragment(Fragment frag){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag).addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void dialogSalir(){
        AlertDialog.Builder buider = new AlertDialog.Builder(Principal.this);
        View dView = getLayoutInflater().inflate(R.layout.dialog_salir, null);
        Button btnSalir = (Button)dView.findViewById(R.id.btnSalir);


        btnSalir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent salir = new Intent(Principal.this, Login.class);
                salir.addFlags(salir.FLAG_ACTIVITY_CLEAR_TOP | salir.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(salir);

            }
        });

        buider.setView(dView);
        AlertDialog dialog = buider.create();
        dialog.show();
    }

}
