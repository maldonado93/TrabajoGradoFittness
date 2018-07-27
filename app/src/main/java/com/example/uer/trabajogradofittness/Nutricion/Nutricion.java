package com.example.uer.trabajogradofittness.Nutricion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nutricion extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    public String ip = "192.168.1.6";
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


        titulo = v.findViewById(R.id.tvTitulo);

        recyclerCategoriaNutricion = (RecyclerView)v.findViewById(R.id.rvCategorias);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        request = Volley.newRequestQueue(getActivity().getApplicationContext());
        consultarCategorias();

        /*listaCategoriaNutricion.add(new CategoriaNutricion("Cereales"));
        listaCategoriaNutricion.add(new CategoriaNutricion("Carnes"));
        listaCategoriaNutricion.add(new CategoriaNutricion("Frutas"));*/
    }


    private void consultarCategorias(){

        String url = "http://"+ip+"/proyectoGrado/query_BD/nutricion/listar_categorias.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("alimentos");

        try {
            listaCategoriaNutricion = new ArrayList<>();
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                listaCategoriaNutricion.add(new CategoriaNutricion(jsonObject.optString("categoria")));
            }

            AdaptadorCategoriaNutricion adaptador = new AdaptadorCategoriaNutricion(getContext(), listaCategoriaNutricion);
            recyclerCategoriaNutricion.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            recyclerCategoriaNutricion.setAdapter(adaptador);
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
