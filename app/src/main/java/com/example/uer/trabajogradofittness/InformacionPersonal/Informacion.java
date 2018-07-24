package com.example.uer.trabajogradofittness.InformacionPersonal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Informacion extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {


    public Informacion() {
        // Required empty public constructor
    }

    TextView titulo;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        request = Volley.newRequestQueue(getContext());

        titulo = getActivity().findViewById(R.id.tvTitulo);

        return inflater.inflate(R.layout.fragment_informacion, container, false);
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


}
