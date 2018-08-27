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
import com.example.uer.trabajogradofittness.Principal;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rutinas extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;

    private AdaptadorListaRutinas adaptadorRutinas;
    private List<ListaRutinas> listaRutinas;

    RecyclerView recyclerRutinas;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rutinas, container, false);

        recyclerRutinas = (RecyclerView)v.findViewById(R.id.rvRutinas);

        return v;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        ((Principal) getActivity()).getSupportActionBar().setTitle("Mis rutinas");

        listarEjercicios();
    }


    private void listarEjercicios(){

        String url = "http://"+gs.getIp()+"/ejercicio/listar_rutinas.php?idPersona="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("rutina");

        try {
            listaRutinas  = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                listaRutinas.add(new ListaRutinas(jsonObject.optString("id"),
                                                jsonObject.optString("nombre"),
                                                jsonObject.optString("categoria"),
                                                jsonObject.optString("cantidad")));
            }


            adaptadorRutinas = new AdaptadorListaRutinas(getContext(), listaRutinas);
            recyclerRutinas.setLayoutManager(new GridLayoutManager(getActivity(), 1));

            recyclerRutinas.setAdapter(adaptadorRutinas);
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
