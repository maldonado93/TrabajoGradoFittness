package com.example.uer.trabajogradofittness.Rutina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class InformacionEjercicio extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    ModeloEjercicio modeloEjercicio;

    private String idEjercicio;

    ImageButton btnRegresar;
    TextView tvId;
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

        Bundle datos = this.getIntent().getExtras();
        idEjercicio = datos.getString("id");

        btnRegresar = (ImageButton) findViewById(R.id.btnRegresar1);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tvId = (TextView)findViewById(R.id.tvId);
        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        ivImagen = (ImageView)findViewById(R.id.ivImagen);




        request = Volley.newRequestQueue(getApplicationContext());

        consultarEjercicio();
    }

    private void consultarEjercicio(){

        String url = "http://"+gs.getIp()+"/ejercicios/consultar_ejercicio.php?id="+idEjercicio;

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
        Toast.makeText(this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
