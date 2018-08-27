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
public class InformacionEjercicioFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;
    ModeloEjercicio modeloEjercicio;

    private String idEjercicio;

    TextView tvId;
    ImageView ivImagen;
    TextView tvNombre;
    TextView tvDescripcion;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_informacion_ejercicio,container,false);

        tvId = v.findViewById(R.id.tvId);
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

        String url = "http://"+gs.getIp()+"/ejercicios/consultar_ejercicio.php?id="+id;

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

            modeloEjercicio.setId(jsonObject.optString("id"));
            modeloEjercicio.setDato(jsonObject.optString("imagen"));
            modeloEjercicio.setNombre(jsonObject.optString("nombre"));
            modeloEjercicio.setCategoria(jsonObject.optString("categoria"));
            modeloEjercicio.setDescripcion(jsonObject.optString("descripcion"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        tvId.setText(modeloEjercicio.getId());
        tvNombre.setText(modeloEjercicio.getNombre());
        tvDescripcion.setText(modeloEjercicio.getDescripcion());
        tvId.setText(modeloEjercicio.getId());
        if(modeloEjercicio.getImagen() != null){
            ivImagen.setImageBitmap(modeloEjercicio.getImagen());
        }
        else{
            ivImagen.setImageResource(R.mipmap.foto_defecto_round);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
