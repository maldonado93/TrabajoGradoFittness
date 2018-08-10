package com.example.uer.trabajogradofittness.Persona;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{


    View v;
    GlobalState gs;

    ModeloPerfil modeloPerfil;

    ImageView ivFoto;
    TextView tvIdentificacion;
    TextView tvNombre;
    TextView tvEmail;
    TextView tvMovil;
    TextView tvLocalidad;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_perfil, container, false);

        ivFoto = v.findViewById(R.id.ivFoto);
        tvIdentificacion = v.findViewById(R.id.tvIdentificacion);
        tvNombre = v.findViewById(R.id.tvNombre);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvMovil = v.findViewById(R.id.tvMovil);
        tvLocalidad = v.findViewById(R.id.tvLocalidad);

        return v;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        consultarPersona();
    }

    private void consultarPersona(){

        String url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/persona/consultar_persona.php?idPersona="+gs.getId_alumno();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }



    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("persona");
        JSONObject jsonObject = null;

        modeloPerfil = new ModeloPerfil();

        try {
            jsonObject = datos.getJSONObject(0);

            modeloPerfil.setId_tipo_identificacion(jsonObject.optString("id_tipo_identificacion"));
            modeloPerfil.setIdentificacion(jsonObject.optString("identificaion"));
            modeloPerfil.setNombre(jsonObject.optString("nombres")+" "
                                            +jsonObject.optString("apellidos"));
            modeloPerfil.setEmail(jsonObject.optString("email"));
            modeloPerfil.setMovil(jsonObject.optString("movil"));
            modeloPerfil.setDato(jsonObject.optString("foto"));
            modeloPerfil.setLocalidad(jsonObject.optString("localidad"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        tvIdentificacion.setText(modeloPerfil.getIdentificacion());
        tvNombre.setText(modeloPerfil.getNombre());
        tvEmail.setText(modeloPerfil.getEmail());
        tvLocalidad.setText(modeloPerfil.getLocalidad());
        tvIdentificacion.setText(modeloPerfil.getNombre());
        tvMovil.setText(modeloPerfil.getMovil());
        if(modeloPerfil.getFoto() != null){
            ivFoto.setImageBitmap(modeloPerfil.getFoto());
        }
        else{
            ivFoto.setImageResource(R.mipmap.foto_defecto_round);
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

}
