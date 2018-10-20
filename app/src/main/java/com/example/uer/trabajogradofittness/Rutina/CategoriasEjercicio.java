package com.example.uer.trabajogradofittness.Rutina;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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


public class CategoriasEjercicio extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    View v;
    GlobalState gs;

    ProgressBar progressBar;
    TextView titulo;

    private RecyclerView recyclerCategoriaEjercicios;
    private List<ListaCategorias> listaCategoriaEjercicios;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_categorias_ejercicios,container,false);

        progressBar = v.findViewById(R.id.progressBar);
        titulo = v.findViewById(R.id.tvTitulo);
        recyclerCategoriaEjercicios = (RecyclerView)v.findViewById(R.id.rvCategorias);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        //((Principal) getActivity()).getSupportActionBar().setTitle("Ejercicios");


        request = Volley.newRequestQueue(getActivity().getApplicationContext());
        consultarCategorias();
    }

    private void consultarCategorias(){

        String url = "http://"+gs.getIp()+"/ejercicio/listar_categorias.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("ejercicio");

        try {
            listaCategoriaEjercicios = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                listaCategoriaEjercicios.add(new ListaCategorias(jsonObject.optString("categoria")));
            }

            AdaptadorListaCategorias adaptador = new AdaptadorListaCategorias(getContext(), listaCategoriaEjercicios);
            recyclerCategoriaEjercicios.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            progressBar.setVisibility(View.GONE);
            recyclerCategoriaEjercicios.setAdapter(adaptador);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


}
