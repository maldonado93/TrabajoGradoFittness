package com.example.uer.trabajogradofittness.MenuPrincipal;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.Insignias;
import com.example.uer.trabajogradofittness.Nutricion.Nutricion;
import com.example.uer.trabajogradofittness.Nutricion.PlanesNutricionales;
import com.example.uer.trabajogradofittness.Persona.Cuenta;
import com.example.uer.trabajogradofittness.Persona.HistoricoPesos;
import com.example.uer.trabajogradofittness.Persona.Perfil;
import com.example.uer.trabajogradofittness.Persona.Ranking;
import com.example.uer.trabajogradofittness.R;
import com.example.uer.trabajogradofittness.RegistroEntreno.FrecuenciaRutinas;
import com.example.uer.trabajogradofittness.RegistroEntreno.Inicio;
import com.example.uer.trabajogradofittness.RegistroEntreno.RegistrosEntreno;
import com.example.uer.trabajogradofittness.Rutina.CategoriasEjercicio;
import com.example.uer.trabajogradofittness.Rutina.Rutinas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Menu extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String tituloActividad;
    private String[] items;

    ImageView ivImagen;
    ImageView ivInsignia;
    TextView tvNombre;
    TextView tvNivel;
    ProgressBar prPuntos;
    TextView tvPuntos;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter adaptador;
    private List<String> listTitulos;
    private Map<String, List<String>> listChild;
    private NavigationManager manager;

    Fragment fragment = null;

    GlobalState gs;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        gs = (GlobalState) getApplication();
        request = Volley.newRequestQueue(getApplicationContext());

        drawerLayout = findViewById(R.id.drawer_layout);
        tituloActividad = getTitle().toString();
        expandableListView = findViewById(R.id.navList);
        manager = NavigationManager.getInstancia(this);

        initItems();

        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header_menu, null, false);

        ivImagen = listHeaderView.findViewById(R.id.ivImagen);
        ivInsignia = listHeaderView.findViewById(R.id.ivInsignia);
        tvNombre = listHeaderView.findViewById(R.id.tvNombre);
        tvNivel = listHeaderView.findViewById(R.id.tvNivel);
        prPuntos = listHeaderView.findViewById(R.id.prPuntos);
        tvPuntos = listHeaderView.findViewById(R.id.tvPuntos);

        consultarImagen(gs.getFotoPerfil());

        tvNombre.setText(gs.getNombres()+" "+gs.getApellidos());
        tvNivel.setText(gs.getNivelActividad());
        prPuntos.setProgress(250, true);
        tvPuntos.setText(gs.getPuntos()+ "/" + prPuntos.getMax());

        Insignias insignias = new Insignias(getApplicationContext(), gs.getNivel());
        ivInsignia.setBackground(insignias.getInsignia());

        expandableListView.addHeaderView(listHeaderView);

        genData();

        addDrawersItem();
        setupDrawer();
        if(savedInstanceState == null){
            if(gs.getFragmentActual() == null){
                fragment = new Inicio();
                addFragment();
            }
            else{
                verificarFragment();
                addFragment();
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void consultarImagen(String url){

        url = url.replace(" ", "%20");

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ivImagen.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDrawerOpened(View drawerView) {

                consultarImagen(gs.getFotoPerfil());
                tvNivel.setText(gs.getNivelActividad());
                prPuntos.setProgress(gs.getPuntos(), true);
                tvPuntos.setText(gs.getPuntos() + "/" + 1000);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void addDrawersItem() {
        adaptador = new AdaptadorListaMenu(this, listTitulos, listChild);
        expandableListView.setAdapter(adaptador);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
               //getSupportActionBar().setTitle(listChild.get(groupPosition).toString());

            }

        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //getSupportActionBar().setTitle("Dev");
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                String seleccion = ((List)(listChild.get(listTitulos.get(groupPosition)))).get(childPosition).toString();

                getSupportActionBar().setTitle(seleccion);
                gs.setFragmentActual(seleccion);

                verificarFragment();
                reemplazarFragment();

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void initItems() {
        items = new String[]{"Inicio", "Informacion Personal", "Datos de entrenos", "Rutinas", "Nutricion"};
    }

    private void genData() {
        List<String> listaMenu = Arrays.asList("Inicio", "Mi información", "Datos de entrenos", "Rutina", "Nutrición");

        List<String> itemsInicio = Arrays.asList("Iniciar entreno", "Calcular frecuencia en reposo");
        List<String> itemsInformacion = Arrays.asList("Perfil", "Ranking", "Histórico de peso", "Mi cuenta");
        List<String> itemsDatos = Arrays.asList("Registro de entrenos", "Promedio de frecuencia por rutina");
        List<String> itemsRutina = Arrays.asList("Mis rutinas", "Ejercicios");
        List<String> itemsNutricion = Arrays.asList("Plan nutricional", "Alimentos");

        listChild = new TreeMap<>();
        listChild.put(listaMenu.get(4), itemsNutricion);
        listChild.put(listaMenu.get(3), itemsRutina);
        listChild.put(listaMenu.get(2), itemsDatos);
        listChild.put(listaMenu.get(1), itemsInformacion);
        listChild.put(listaMenu.get(0), itemsInicio);
        listTitulos = new ArrayList<>(listChild.keySet());
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void verificarFragment(){
        switch(gs.getFragmentActual()){
            case "Inicio": fragment = new Inicio();
                break;
            case "Registro de entrenos": fragment = new RegistrosEntreno();
                break;
            case "Promedio de frecuencia por rutina": fragment = new FrecuenciaRutinas();
                break;
            case "Perfil": fragment = new Perfil();
                break;
            case "Histórico de peso": fragment = new HistoricoPesos();
                break;
            case "Ranking": fragment = new Ranking();
                break;
            case "Mi cuenta": fragment = new Cuenta();
                break;
            case "Mis rutinas": fragment = new Rutinas();
                break;
            case "Ejercicios": fragment = new CategoriasEjercicio();
                break;
            case "Plan nutricional": fragment = new PlanesNutricionales();
                break;
            case "Alimentos": fragment = new Nutricion();
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

    private void limpiarDatos(){
        gs.setSesion_usuario(0);
        gs.setUsuario("");
        gs.setPassword("");
        gs.setNombres("");
        gs.setApellidos("");
        gs.setGenero("");
        gs.setPeso(0);
        gs.setNivelActividad("");
        gs.setFumador("");
        gs.setEdad(0);
        gs.setAlimentos(null);
        gs.setCantidad(null);
        gs.setAccion(null);
        gs.setCaloriasPlan(null);
    }

    private void dialogSalir(){
        AlertDialog.Builder buider = new AlertDialog.Builder(Menu.this);
        View dView = getLayoutInflater().inflate(R.layout.dialog_salir, null);
        Button btnSalir = (Button)dView.findViewById(R.id.btnSalir);

        btnSalir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                limpiarDatos();
                finish();
            }
        });
        buider.setView(dView);
        AlertDialog dialog = buider.create();
        dialog.show();
    }
}
