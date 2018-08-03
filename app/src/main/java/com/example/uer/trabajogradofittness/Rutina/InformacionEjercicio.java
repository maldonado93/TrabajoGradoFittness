package com.example.uer.trabajogradofittness.Rutina;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class InformacionEjercicio extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;
    private String idEjercicio;

    TextView tvNombre;
    TextView tvDescripcion;
    ImageView ivImagen;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_informacion_ejercicio,container,false);

        tvNombre = v.findViewById(R.id.tvNombre);
        tvDescripcion = v.findViewById(R.id.tvDescripcion);
        ivImagen = v.findViewById(R.id.ivImagen);

        return v;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        if(getArguments() != null){
            idEjercicio= getArguments().getString("idEjercicio","");
        }

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        consultarEjercicio(idEjercicio);
    }


    private void consultarEjercicio(String id){

        String url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/ejercicios/consultar_ejercicio.php?id="+id;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("ejercicio");
        JSONObject jsonObject = null;

        try {
            jsonObject = datos.getJSONObject(0);

            tvNombre.setText(jsonObject.optString("nombre"));
            tvDescripcion.setText(jsonObject.optString("descripcion"));
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
