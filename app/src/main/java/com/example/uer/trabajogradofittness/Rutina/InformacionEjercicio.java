package com.example.uer.trabajogradofittness.Rutina;

import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InformacionEjercicio extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;

    ProgressBar progressBar;

    ConstraintLayout layout_informacion;

    private String idEjercicio;

    ImageButton btnRegresar;
    ImageView ivImagen;
    TextView tvNombre;
    TextView tvDescripcion;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_ejercicio);

        gs = (GlobalState) getApplication();

        progressBar = findViewById(R.id.progressBar);

        layout_informacion = findViewById(R.id.layout_informacion);

        Bundle datos = this.getIntent().getExtras();
        idEjercicio = datos.getString("idEjercicio");


        btnRegresar = (ImageButton) findViewById(R.id.btnRegresar1);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        ivImagen = (ImageView)findViewById(R.id.ivImagen);

        request = Volley.newRequestQueue(getApplicationContext());

        consultarEjercicio();
    }

    private void consultarImagen(String url){

        url = url.replace(" ", "%20");

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ivImagen.setImageBitmap(response);
                        progressBar.setVisibility(View.GONE);
                        layout_informacion.setVisibility(View.VISIBLE);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    private void consultarEjercicio(){

        String url = "http://"+gs.getIp()+"/ejercicio/consultar_ejercicio.php?id="+idEjercicio;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("ejercicio");
        JSONObject jsonObject = null;
        String url = "";
        try {
            jsonObject = datos.getJSONObject(0);

            tvNombre.setText(jsonObject.optString("nombre"));
            tvDescripcion.setText(jsonObject.optString("descripcion"));
            url = jsonObject.optString("imagen");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        consultarImagen(url);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
