package com.example.uer.trabajogradofittness;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.uer.trabajogradofittness.Nutricion.Nutricion;
import com.example.uer.trabajogradofittness.Persona.Perfil;
import com.example.uer.trabajogradofittness.RegistroEntreno.Inicio;
import com.example.uer.trabajogradofittness.RegistroEntreno.RegistrosEntreno;
import com.example.uer.trabajogradofittness.Rutina.CategoriasEjercicio;
import com.example.uer.trabajogradofittness.Rutina.Rutinas;


public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_ENABLE_BT = 1;
    NavigationView navigationView = null;
    Toolbar toolbar = null;

    GlobalState gs;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    Fragment fragment = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        gs = (GlobalState) getApplication();

        //Inicializar fragment


        if(gs.getFragmentActual() == null){
            fragment = new Inicio();
            addFragment();
        }
        else{
            verificarFragment();
            addFragment();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragment instanceof Inicio){
                dialogSalir();
            }
            else{
                gs.setFragmentActual("Inicio");
                verificarFragment();
                reemplazarFragment();
            }
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
            gs.setFragmentActual("Inicio");

        } else if (id == R.id.nav_registros) {
            gs.setFragmentActual("RegistrosEntreno");

        } else if (id == R.id.nav_informacion) {
            gs.setFragmentActual("Informacion");

        } else if (id == R.id.nav_rutinas) {
            gs.setFragmentActual("Rutinas");

        } else if (id == R.id.nav_ejercicios) {
            gs.setFragmentActual("Ejercicios");

        } else if (id == R.id.nav_nutricion) {
            gs.setFragmentActual("Nutricion");

        } else if (id == R.id.nav_cuenta) {

        } else if (id == R.id.nav_salir) {
            dialogSalir();

        }

        verificarFragment();
        reemplazarFragment();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void verificarFragment(){
        switch(gs.getFragmentActual()){
            case "Inicio": fragment = new Inicio();
                break;
            case "RegistrosEntreno": fragment = new RegistrosEntreno();
                break;
            case "Informacion": fragment = new Perfil();
                break;
            case "Rutinas": fragment = new Rutinas();
                break;
            case "Ejercicios": fragment = new CategoriasEjercicio();
                break;
            case "Nutricion": fragment = new Nutricion();
                break;

            default: gs.setFragmentActual("Inicio");
                fragment = new Inicio();
                break;
        }
    }

    private void addFragment(){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().toString()).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void reemplazarFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.fragment_container);
        if(!fragment.getClass().toString().equals(currentFragment.getTag()))
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, fragment, fragment.getClass().toString()) // add and tag the new fragment
                    .commit();
        }
    }



    private void dialogSalir(){
        AlertDialog.Builder buider = new AlertDialog.Builder(Principal.this);
        View dView = getLayoutInflater().inflate(R.layout.dialog_salir, null);
        Button btnSalir = (Button)dView.findViewById(R.id.btnSalir);

        btnSalir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                limpiarDatos();
                Intent salir = new Intent(Principal.this, Login.class);
                salir.addFlags(salir.FLAG_ACTIVITY_CLEAR_TOP | salir.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(salir);
            }
        });
        buider.setView(dView);
        AlertDialog dialog = buider.create();
        dialog.show();
    }

    private void limpiarDatos(){

    }

}
