package com.example.uer.trabajogradofittness.Persona;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.Insignias;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class Ranking extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    ProgressDialog progress;
    ProgressBar progressBar;

    ConstraintLayout layout_ranking;

    private List<ListaRanking> listaRanking;
    ArrayList listaPersonas;
    String consulta;
    String[] datosPersonas;
    int cantidadPersonas;

    ImageView ivImagen1;
    ImageView ivImagen2;
    ImageView ivImagen3;

    ImageView ivInsignia1;
    ImageView ivInsignia2;
    ImageView ivInsignia3;

    TextView tvNivel1;
    TextView tvNivel2;
    TextView tvNivel3;

    TextView tvPuntos1;
    TextView tvPuntos2;
    TextView tvPuntos3;

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

        progressBar = v.findViewById(R.id.progressBar);

        layout_ranking = v.findViewById(R.id.layout_ranking);

        ivImagen1 = v.findViewById(R.id.ivImagen1);
        ivImagen2 = v.findViewById(R.id.ivImagen2);
        ivImagen3 = v.findViewById(R.id.ivImagen3);

        ivInsignia1 = v.findViewById(R.id.ivInsignia1);
        ivInsignia2 = v.findViewById(R.id.ivInsignia2);
        ivInsignia3 = v.findViewById(R.id.ivInsignia3);

        tvNivel1 = v.findViewById(R.id.tvNivel1);
        tvNivel2 = v.findViewById(R.id.tvNivel2);
        tvNivel3 = v.findViewById(R.id.tvNivel3);

        tvPuntos1 = v.findViewById(R.id.tvPuntos1);
        tvPuntos2 = v.findViewById(R.id.tvPuntos2);
        tvPuntos3 = v.findViewById(R.id.tvPuntos3);

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

        listarPersonas();
    }

    private void generarRanking() throws InterruptedException {
        cantidadPersonas = 0;
        String datos[];
        for(int i=0; i<datosPersonas.length;i++){
            datos = datosPersonas[i].split("-");
            consultarImagen(datos[1]);
            sleep(200);
        }

    }

    private void consultarImagen(String url){

        url = url.replace(" ", "%20");

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Bitmap response) {
                            String[] datos = datosPersonas[cantidadPersonas].split("-");
                            Insignias insignias = new Insignias(getContext(), Integer.parseInt(datos[4]));
                            Drawable insignia = insignias.getInsignia();
                            if(cantidadPersonas == 0){
                                ivImagen1.setImageBitmap(response);
                                ivInsignia1.setBackground(insignia);
                                tvNivel1.setText(datos[3]);
                                tvPuntos1.setText(datos[5]);
                                tvNombre1.setText(datos[2]);
                            }
                            else{
                                if(cantidadPersonas == 1){
                                    ivImagen2.setImageBitmap(response);
                                    ivInsignia2.setBackground(insignia);
                                    tvNivel2.setText(datos[3]);
                                    tvPuntos2.setText(datos[5]);
                                    tvNombre2.setText(datos[2]);
                                }
                                else{
                                    if(cantidadPersonas == 2){
                                        ivImagen3.setImageBitmap(response);
                                        ivInsignia3.setBackground(insignia);
                                        tvNivel3.setText(datos[3]);
                                        tvPuntos3.setText(datos[5]);
                                        tvNombre3.setText(datos[2]);
                                    }
                                    else{
                                        listaRanking.add(new ListaRanking((cantidadPersonas+1),
                                                response,
                                                datos[2],
                                                insignia,
                                                datos[3],
                                                datos[5]));
                                    }
                                }
                            }
                            cantidadPersonas++;
                            if(cantidadPersonas == datosPersonas.length){
                                AdaptadorListaRanking adaptador = new AdaptadorListaRanking(getContext(), listaRanking);
                                rvRanking.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                                rvRanking.setAdapter(adaptador);
                                layout_ranking.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
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
        cantidadPersonas = 0;

        JSONArray datos = response.optJSONArray("ranking");
        JSONObject jsonObject = null;
        try {
            datosPersonas = new String[datos.length()];
            for (int i = 0; i < datos.length(); i++) {
                jsonObject = datos.getJSONObject(i);

                int id = jsonObject.optInt("id");
                String foto = jsonObject.optString("foto");
                String nombre = jsonObject.optString("nombres") + " " + jsonObject.optString("apellidos");
                String nivelActividad = jsonObject.optString("nivel_actividad");
                String nivel = jsonObject.optString("nivel");
                String puntos = "Puntos: " + jsonObject.optString("puntos");

                datosPersonas[i] = id + "-" + foto + "-" + nombre + "-" + nivelActividad + "-" + nivel + "-" + puntos;
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            generarRanking();
        } catch (InterruptedException e) {
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
