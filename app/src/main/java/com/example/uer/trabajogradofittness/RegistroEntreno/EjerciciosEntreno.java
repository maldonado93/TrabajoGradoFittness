package com.example.uer.trabajogradofittness.RegistroEntreno;


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
import com.example.uer.trabajogradofittness.Rutina.AdaptadorListaEjercicios;
import com.example.uer.trabajogradofittness.Rutina.ListaEjercicios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EjerciciosEntreno extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;

    private AdaptadorListaEjercicios adaptadorEjercicios;
    private ArrayList<ListaEjercicios> listaEjercicios;

    RecyclerView recyclerEjercicios;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_ejercicios_entreno, container, false);

        recyclerEjercicios = (RecyclerView) v.findViewById(R.id.rvEjercicios);


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        listarEjercicios();
    }

    private void listarEjercicios(){

        String url = "http://"+gs.getIp()+"/ejercicio/listar_ejerciciosxrutina.php?idRutina="+gs.getId_rutina();

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
