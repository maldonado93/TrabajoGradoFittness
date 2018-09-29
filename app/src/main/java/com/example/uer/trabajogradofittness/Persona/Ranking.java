package com.example.uer.trabajogradofittness.Persona;


import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Ranking extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    ProgressDialog progress;

    private List<ListaRanking> listaRanking;
    String consulta;

    TextView tvNivel1;
    TextView tvNivel2;
    TextView tvNivel3;

    TextView tvNombre1;
    TextView tvNombre2;
    TextView tvNombre3;

    RecyclerView rvRanking;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_ranking, container, false);

        tvNivel1 = v.findViewById(R.id.tvNivel1);
        tvNivel2 = v.findViewById(R.id.tvNivel2);
        tvNivel3 = v.findViewById(R.id.tvNivel3);

        tvNombre1 = v.findViewById(R.id.tvNombre1);
        tvNombre2 = v.findViewById(R.id.tvNombre2);
        tvNombre3 = v.findViewById(R.id.tvNombre3);

        rvRanking = v.findViewById(R.id.rvRanking);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando información...");
        progress.show();

        listarPersonas();
    }

    private void listarPersonas(){

        String url = "http://" + gs.getIp() + "/persona/lista_ranking.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {

        listaRanking = new ArrayList<>();

        JSONArray datos = response.optJSONArray("ranking");
        JSONObject jsonObject = null;
        try {
            for (int i = 0; i < datos.length(); i++) {
                jsonObject = datos.getJSONObject(i);
                int id = jsonObject.optInt("id");
                String nombre = jsonObject.optString("nombres")+" "+ jsonObject.optString("apellidos");
                String nivel = "Lv: "+ jsonObject.optString("nivel");
                String puntos = "Puntos: " + jsonObject.optString("puntos");
                if(i == 0){
                    tvNivel1.setText(nivel);
                    tvNombre1.setText(nombre);
                }
                else{
                    if(i == 1){
                        tvNivel2.setText(nivel);
                        tvNombre2.setText(nombre);
                    }
                    else{
                        if(i == 2){
                            tvNivel3.setText(nivel);
                            tvNombre3.setText(nombre);
                        }
                        else{
                            listaRanking.add(new ListaRanking((i+1),
                                    id,
                                    nombre,
                                    nivel,
                                    puntos));
                        }
                    }
                }
            }

            AdaptadorListaRanking adaptador = new AdaptadorListaRanking(getContext(), listaRanking);
            rvRanking.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            rvRanking.setAdapter(adaptador);
            progress.hide();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
        progress.hide();
    }
}
