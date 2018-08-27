package com.example.uer.trabajogradofittness.Rutina;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.Nutricion.ListaAlimentos;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ejercicios extends AppCompatActivity implements SearchView.OnQueryTextListener, Response.Listener<JSONObject>, Response.ErrorListener{


    GlobalState gs;

    private String categoria;
    private String idRutina;
    private String rutina;

    private AdaptadorListaEjercicios adaptadorEjercicios;
    private ArrayList<ListaEjercicios> listaEjercicios;

    ImageButton btnRegresar;
    TextView tvVista;
    android.widget.SearchView svBuscar;
    TextView titulo;
    RecyclerView recyclerEjercicios;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        gs = (GlobalState) getApplication();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle datos = this.getIntent().getExtras();
        categoria = datos.getString("categoria");
        idRutina = datos.getString("idRutina");
        rutina = datos.getString("rutina");

        btnRegresar = (ImageButton) findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvVista = (TextView) findViewById(R.id.tvVista);

        svBuscar = (android.widget.SearchView)findViewById(R.id.svBuscar);
        svBuscar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                tvVista.setVisibility(View.GONE);
                if(texto != ""){
                    texto = texto.toLowerCase();
                    ArrayList<ListaEjercicios> listaFiltrada = new ArrayList<>();

                    for(ListaEjercicios lista: listaEjercicios){
                        String nombre = lista.getNombre().toLowerCase();
                        if(nombre.contains(texto)){
                            listaFiltrada.add(lista);
                        }
                    }
                    adaptadorEjercicios.setFilter(listaFiltrada);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        titulo = (TextView) findViewById(R.id.tvTitulo);
        if(categoria.compareTo("") != 0){
            titulo.setText(categoria);
        }
        else{
            titulo.setText(rutina);
        }

        recyclerEjercicios = (RecyclerView)findViewById(R.id.rvEjercicios);

        request = Volley.newRequestQueue(this);

        listarEjercicios();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_buscador, menu);
        MenuItem item = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String texto) {
        return false;
    }

    private void listarEjercicios(){

        String url = "";
        if(categoria.compareTo("") != 0){
            url = "http://"+gs.getIp()+"/ejercicio/listar_ejerciciosxcategoria.php?categoria="+categoria;
        }
        else{
            url = "http://"+gs.getIp()+"/ejercicio/listar_ejerciciosxrutina.php?idRutina="+idRutina;
        }

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("ejercicio");

        try {
            listaEjercicios = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);
                listaEjercicios.add(new ListaEjercicios(jsonObject.optString("id"),
                        jsonObject.optString("nombre")));
            }

            adaptadorEjercicios = new AdaptadorListaEjercicios(this, listaEjercicios);
            recyclerEjercicios.setLayoutManager(new GridLayoutManager(this, 1));

            recyclerEjercicios.setAdapter(adaptadorEjercicios);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
