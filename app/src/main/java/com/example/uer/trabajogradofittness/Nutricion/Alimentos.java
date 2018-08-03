package com.example.uer.trabajogradofittness.Nutricion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Alimentos extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    View v;
    GlobalState gs;


    private String categoria;
    private AdaptadorListaAlimentos adaptadorAlimentos;
    private List<ListaAlimentos> listaAlimentos;
    ArrayList<String> listaOrden;

    TextView titulo;
    android.widget.SearchView svBuscar;
    Spinner spOrden;
    RecyclerView recyclerAlimentos;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_alimentos,container,false);


        titulo = (TextView) v.findViewById(R.id.tvTituloCategoria);
        titulo.setText(categoria);

        svBuscar = (android.widget.SearchView)v.findViewById(R.id.svBuscar);
        svBuscar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                adaptadorAlimentos.getFilter().filter(texto);

                return false;
            }
        });

        spOrden = (Spinner)v.findViewById(R.id.spOrden);

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listaOrden);
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

        recyclerAlimentos = (RecyclerView)v.findViewById(R.id.rvAlimentos);

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        if(getArguments() != null){
            categoria = getArguments().getString("categoria"," ");
        }

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        listaOrden = new ArrayList<>();

        listaOrden.add("Nombre");
        listaOrden.add("Calorias");
        listaOrden.add("Proteinas");
        listaOrden.add("Carbohidratos");



        listarAlimentos(categoria, "nombre");
    }


    private void listarAlimentos(String categoria, String orden){

        String url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/nutricion/listar_alimentosxcategoria.php?categoria="+categoria+"&orden="+orden;

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


            adaptadorAlimentos = new AdaptadorListaAlimentos(getContext(), listaAlimentos);
            recyclerAlimentos.setLayoutManager(new GridLayoutManager(getActivity(), 1));

            recyclerAlimentos.setAdapter(adaptadorAlimentos);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }



}
