package com.example.uer.trabajogradofittness.Rutina;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Ejercicios extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{


    View v;
    GlobalState gs;
    private String consulta;
    private String categoria;
    private String idRutina;
    private AdaptadorListaEjercicios adaptadorEjercicios;
    private List<ListaEjercicios> listaEjercicios;

    TextView titulo;
    android.widget.SearchView svBuscar;
    RecyclerView recyclerEjercicios;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ejercicios,container,false);


        titulo = (TextView) v.findViewById(R.id.tvTituloCategoria);

        svBuscar = (android.widget.SearchView)v.findViewById(R.id.svBuscar);
        svBuscar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                adaptadorEjercicios.getFilter().filter(texto);

                return false;
            }
        });

        recyclerEjercicios = (RecyclerView)v.findViewById(R.id.rvEjercicios);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        if(getArguments() != null){
            categoria =  getArguments().getString("categoria","");
            idRutina =  getArguments().getString("idRutina", "");
        }

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        listarEjercicios();

    }


    private void listarEjercicios(){

        String url = "";
        if(categoria != ""){
            consulta = "ejercicio";
            url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/ejercicio/listar_ejerciciosxcategoria.php?categoria="+categoria;
        }
        else{
            consulta = "rutina";
            url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/ejercicio/listar_ejerciciosxrutina.php?idRutina="+idRutina;
        }

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray(consulta);

        try {
            listaEjercicios = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);
                listaEjercicios.add(new ListaEjercicios(jsonObject.optString("id"),
                                                        jsonObject.optString("nombre")));
                categoria = jsonObject.optString("categoria");
            }
            titulo.setText(categoria);

            adaptadorEjercicios = new AdaptadorListaEjercicios(getContext(), listaEjercicios);
            recyclerEjercicios.setLayoutManager(new GridLayoutManager(getActivity(), 1));

            recyclerEjercicios.setAdapter(adaptadorEjercicios);
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
