package com.example.uer.trabajogradofittness.Persona;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

public class InformacionActividad extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    String consulta;

    ProgressDialog progress;

    String idPersona;

    ImageButton btnRegresar;

    RadioButton rbNoActivo;
    RadioButton rbLigeramente;
    RadioButton rbActivo;
    RadioButton rbMuyActivo;

    RadioButton rbNo;
    RadioButton rbSi;


    Button btnRegistrar;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_actividad);

        gs = (GlobalState)getApplication();

        request = Volley.newRequestQueue(getApplicationContext());

        progress = new ProgressDialog(InformacionActividad.this);
        progress.setMessage("Registrando datos...");

        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCampos(view, 1);
                regresar(view);
            }
        });

        rbNoActivo= findViewById(R.id.rbNoActivo);
        rbLigeramente= findViewById(R.id.rbLigeramente);
        rbActivo= findViewById(R.id.rbActivo);
        rbMuyActivo= findViewById(R.id.rbMuyActivo);

        rbNo= findViewById(R.id.rbNo);
        rbSi= findViewById(R.id.rbSi);


        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    verificarCampos(view, 2);
            }
        });

        cargarDatos();
    }

    public void verificarCampos(View view, int ope){
        String actividad = "";
        String fumador = "";
        boolean registrar = true;

        if(rbNoActivo.isChecked()){
            actividad = "No activo";
        }
        if(rbLigeramente.isChecked()){
            actividad = "Ligeramente activo";
        }
        if(rbActivo.isChecked()){
            actividad = "Activo";
        }
        if(rbMuyActivo.isChecked()){
            actividad = "Muy activo";
        }

        if(rbNo.isChecked()){
            fumador = "No";
        }
        if(rbSi.isChecked()){
            fumador = "Si";
        }
        if (actividad.compareTo("") != 0) {
            gs.setActividad(actividad);
        }
        else{
            registrar = false;
        }
        if (fumador.compareTo("") != 0) {
            gs.setFumador(fumador);
        }
        else{
            registrar = false;
        }
        if(ope == 2) {
            if (registrar) {
                progress.show();
                registrarPersona();
            }
            else{
                Snackbar.make(view, "Seleccione las casillas, por favor!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }
    }


    public void cargarDatos(){
        if (gs.getActividad().compareTo("No activo") == 0){
            rbNoActivo.setChecked(true);
        }
        if (gs.getActividad().compareTo("Ligeramente activo") == 0){
            rbLigeramente.setChecked(true);
        }
        if (gs.getActividad().compareTo("Activo") == 0){
            rbActivo.setChecked(true);
        }
        if (gs.getActividad().compareTo("Muy activo") == 0){
            rbMuyActivo.setChecked(true);
        }

        if (gs.getFumador().compareTo("No") == 0){
            rbNo.setChecked(true);
        }
        if (gs.getFumador().compareTo("Si") == 0){
            rbSi.setChecked(true);
        }
    }


    public void regresar(View view) {
        Intent intent = new Intent(this, InformacionFisica.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void registrarPersona(){
        consulta = "persona";
        String url = "http://"+gs.getIp()+"/persona/registrar_persona.php?tipoIdentificacion="+gs.getTipoIdentificacion()+"&identificacion="+gs.getIdentificacion()
                +"&nombres="+gs.getNombres()+"&apellidos="+gs.getApellidos()+"&genero="+gs.getGenero()+"&fecha="+gs.getFecha()+"&email="+gs.getEmail()
                +"&estatura="+gs.getEstatura()+"&ciudad="+gs.getCiudad();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarUsuario(){
        consulta = "usuario";
        String url = "http://"+gs.getIp()+"/usuario/registrar_usuario.php?idPersona="+idPersona+"&usuario="+gs.getUsuario()+"&password="+gs.getPassword();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarPeso(){
        consulta = "historico_peso";
        String url = "http://"+gs.getIp()+"/persona/registrar_peso.php?idPersona="+idPersona+"&peso="+gs.getPeso();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarDatos(){
        consulta = "datos";
        String url = "http://"+gs.getIp()+"/persona/registrar_datos.php?idPersona="+idPersona+"&peso="+gs.getPeso()+"&descripcion="+gs.getObjetivo()+"&fuma="+gs.getFumador();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray(consulta);

        try {
            JSONObject jsonObject = null;
            jsonObject = datos.getJSONObject(0);
            if(consulta.compareTo("persona") == 0){
                idPersona = jsonObject.optString("id");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if(consulta.compareTo("persona") == 0){
            registrarUsuario();
        }
        else{
            if(consulta.compareTo("usuario") == 0){
                registrarPeso();
            }
            else{
                if(consulta.compareTo("historico_peso") == 0){
                    registrarDatos();
                }
                else{
                    if(consulta.compareTo("datos") == 0){
                        progress.hide();
                        finish();
                    }
                }
            }
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        progress.show();
        Toast.makeText(getApplicationContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());

    }

}
