package com.example.uer.trabajogradofittness.Nutricion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nutricion extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    View v;
    private RecyclerView recyclerCategoriaNutricion;
    private List<CategoriaNutricion> listaCategoriaNutricion;
    TextView titulo;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_nutricion,container,false);

        request = Volley.newRequestQueue(getContext());
        titulo = getActivity().findViewById(R.id.tvTitulo);


        recyclerCategoriaNutricion = (RecyclerView)v.findViewById(R.id.rvCategorias);
        AdaptadorCategoriaNutricion adaptador = new AdaptadorCategoriaNutricion(getContext(), listaCategoriaNutricion);
        recyclerCategoriaNutricion.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCategoriaNutricion.setAdapter(adaptador);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaCategoriaNutricion = new ArrayList<>();
        listaCategoriaNutricion.add(new CategoriaNutricion("Cereales"));
        listaCategoriaNutricion.add(new CategoriaNutricion("Carnes"));
        listaCategoriaNutricion.add(new CategoriaNutricion("Frutas"));
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


}
