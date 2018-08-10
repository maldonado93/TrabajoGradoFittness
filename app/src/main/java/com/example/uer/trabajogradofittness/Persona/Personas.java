package com.example.uer.trabajogradofittness.Persona;


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
import com.example.uer.trabajogradofittness.PrincipalInstructor;
import com.example.uer.trabajogradofittness.R;
import com.example.uer.trabajogradofittness.Rutina.AdaptadorListaEjercicios;
import com.example.uer.trabajogradofittness.Rutina.ListaEjercicios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Personas extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;

    private AdaptadorListaPersonas adaptadorPersonas;
    private List<ListaPersonas> listaPersonas;

    TextView titulo;
    android.widget.SearchView svBuscar;
    RecyclerView recyclerPersonas;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_personas,container,false);

        ((PrincipalInstructor) getActivity()).getSupportActionBar().setTitle("Mis alumnos");

        titulo = (TextView) v.findViewById(R.id.tvTitulo);

        svBuscar = (android.widget.SearchView)v.findViewById(R.id.svBuscar);
        svBuscar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                adaptadorPersonas.getFilter().filter(texto);

                return false;
            }
        });

        recyclerPersonas = (RecyclerView)v.findViewById(R.id.rvPersonas);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        listarPersonas();
    }

    private void listarPersonas(){

        String url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/instructor/listar_alumnos.php?idInstructor="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }



    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("entrena");

        try {
            listaPersonas = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                listaPersonas.add(new ListaPersonas(jsonObject.optString("id"),
                                                jsonObject.optString("nombres")+" "
                                                        +jsonObject.optString("apellidos")));
            }


            adaptadorPersonas = new AdaptadorListaPersonas(getContext(), listaPersonas);
            recyclerPersonas.setLayoutManager(new GridLayoutManager(getActivity(), 1));

            recyclerPersonas.setAdapter(adaptadorPersonas);
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
