package com.example.uer.trabajogradofittness.Nutricion;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Alimentos extends AppCompatActivity implements SearchView.OnQueryTextListener, Response.Listener<JSONObject>, Response.ErrorListener{


    GlobalState gs;

    ImageButton btnRegresar;
    TextView tvVista;
    android.widget.SearchView svBuscar;

    private String categoria;
    private AdaptadorListaAlimentos adaptadorAlimentos;
    private ArrayList<ListaAlimentos> listaAlimentos = null;
    ArrayList<String> listaOrden;

    TextView titulo;
    Spinner spOrden;
    RecyclerView recyclerAlimentos;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);

        gs = (GlobalState) getApplication();

        btnRegresar = (ImageButton) findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvVista = findViewById(R.id.tvVista);

        svBuscar = findViewById(R.id.svBuscar);
        svBuscar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvVista.setVisibility(View.GONE);
            }
        });

        svBuscar.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvVista.setVisibility(View.VISIBLE);
                return false;
            }
        });

        svBuscar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                if(texto != ""){
                    tvVista.setVisibility(View.GONE);
                    texto = texto.toLowerCase();
                    ArrayList<ListaAlimentos> listaFiltrada = new ArrayList<>();

                    for(ListaAlimentos lista: listaAlimentos){
                        String nombre = lista.getNombre().toLowerCase();
                        if(nombre.contains(texto)){
                            listaFiltrada.add(lista);
                        }
                    }
                    adaptadorAlimentos.setFilter(listaFiltrada);
                    return true;
                }
                else{
                    tvVista.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        });



        Bundle datos = this.getIntent().getExtras();
        categoria = datos.getString("categoria");



        titulo = (TextView) findViewById(R.id.tvTitulo);
        titulo.setText(categoria);


        spOrden = (Spinner)findViewById(R.id.spOrden);


        listaOrden = new ArrayList<>();
        listaOrden.add("Nombre");
        listaOrden.add("Calorias");
        listaOrden.add("Proteinas");
        listaOrden.add("Carbohidratos");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaOrden);
        spOrden.setAdapter(adapter);


        spOrden.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {
                        String orden = spOrden.getSelectedItem().toString().toLowerCase();
                        listarAlimentos(categoria, orden);
                    }

                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        recyclerAlimentos = (RecyclerView)findViewById(R.id.rvAlimentos);

        request = Volley.newRequestQueue(this);

        listarAlimentos(categoria, "nombre");
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
        if(texto != ""){
            texto = texto.toLowerCase();
            ArrayList<ListaAlimentos> listaFiltrada = new ArrayList<>();

            for(ListaAlimentos lista: listaAlimentos){
                String nombre = lista.getNombre().toLowerCase();
                if(nombre.contains(texto)){
                    listaFiltrada.add(lista);
                }
            }
            adaptadorAlimentos.setFilter(listaFiltrada);
            return true;
        }
        else{
            return false;
        }
    }



    private void listarAlimentos(String categoria, String orden){

        String url = "http://"+gs.getIp()+"/nutricion/listar_alimentosxcategoria.php?categoria="+categoria+"&orden="+orden;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private String verificarValor(String v){
        String val = v;
        double valor = Double.valueOf(v);

        if(valor == -1.00){
            val = "N";
        }
        else{
            if(valor == -2.00){
                val = "TR";
            }
        }
        return val;
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("alimento");

        try {
            listaAlimentos = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                listaAlimentos.add(new ListaAlimentos(jsonObject.optString("id"),
                        jsonObject.optString("nombre"),
                        verificarValor(jsonObject.optString("calorias")),
                        verificarValor(jsonObject.optString("proteinas")),
                        verificarValor(jsonObject.optString("carbohidratos"))));
            }


            adaptadorAlimentos = new AdaptadorListaAlimentos(this, listaAlimentos);
            recyclerAlimentos.setLayoutManager(new GridLayoutManager(this, 1));

            recyclerAlimentos.setAdapter(adaptadorAlimentos);
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
