package com.example.uer.trabajogradofittness.Persona;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InformacionActividad extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    String consulta;
    String fecha;

    ProgressDialog progress;

    String idPersona;

    Button btnRegresar;

    RadioButton rbNovato;
    RadioButton rbIntermedio;
    RadioButton rbAvanzado;

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
                regresar();
            }
        });

        rbNovato= findViewById(R.id.rbNovato);
        rbIntermedio= findViewById(R.id.rbIntermedio);
        rbAvanzado= findViewById(R.id.rbAvanzado);

        rbNo= findViewById(R.id.rbNo);
        rbSi= findViewById(R.id.rbSi);


        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    verificarCampos(view, 2);
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(c.getTime());

        cargarDatos();
    }

    public void verificarCampos(View view, int ope){
        String actividad = "";
        String fumador = "";
        boolean registrar = true;

        if(rbNovato.isChecked()){
            actividad = "Novato";
        }
        if(rbIntermedio.isChecked()){
            actividad = "Intermedio";
        }
        if(rbAvanzado.isChecked()){
            actividad = "Avanzado";
        }

        if(rbNo.isChecked()){
            fumador = "No";
        }
        if(rbSi.isChecked()){
            fumador = "Si";
        }
        if (actividad.compareTo("") != 0) {
            gs.setNivelActividad(actividad);
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
        if (gs.getNivelActividad().compareTo("Novato") == 0){
            rbNovato.setChecked(true);
        }
        if (gs.getNivelActividad().compareTo("Intermedio") == 0){
            rbIntermedio.setChecked(true);
        }
        if (gs.getNivelActividad().compareTo("Avanzado") == 0){
            rbAvanzado.setChecked(true);
        }

        if (gs.getFumador().compareTo("No") == 0){
            rbNo.setChecked(true);
        }
        if (gs.getFumador().compareTo("Si") == 0){
            rbSi.setChecked(true);
        }
    }

    public void regresar() {
        Intent intent = new Intent(this, InformacionFisica.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        regresar();
    }

    private void limpiarDatos(){
        gs.setRegistro(0);
        gs.setTipoIdentificacion(0);
        gs.setIdentificacion("");
        gs.setNombres("");
        gs.setApellidos("");
        gs.setGenero("");
        gs.setFecha("");
        gs.setEmail("");
        gs.setEstatura(0);
        gs.setDepartamento(0);
        gs.setCiudad("");
        gs.setIdCiudad(0);
        gs.setUsuario("");
        gs.setPassword("");
        gs.setPeso(0);
        gs.setObjetivo("");
        gs.setNivelActividad("");
        gs.setFumador("");
    }

    private void registrarPersona(){
        consulta = "persona";
        String url = "http://"+gs.getIp()+"/persona/registrar_persona.php?tipoIdentificacion="+gs.getTipoIdentificacion()+"&identificacion="+gs.getIdentificacion()
                +"&nombres="+gs.getNombres()+"&apellidos="+gs.getApellidos()+"&movil="+gs.getMovil()+"&genero="+gs.getGenero()+"&fecha="+gs.getFecha()+"&email="+gs.getEmail()
                +"&estatura="+gs.getEstatura()+"&ciudad="+gs.getCiudad();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void generarRegistro(){
        consulta = "generar_registro";
        String url = "http://"+gs.getIp()+"/persona/generar_registro.php?idPersona="+idPersona+"&fecha="+fecha;

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
        String url = "http://"+gs.getIp()+"/persona/registrar_peso.php?idPersona="+idPersona+"&peso="+gs.getPeso()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarDatos(String nivel){
        consulta = "datos";
        String url = "http://"+gs.getIp()+"/persona/registrar_datos.php?idPersona="+idPersona+"&objetivo="+gs.getObjetivo()+"&nivel="+nivel+"&fuma="+gs.getFumador();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarGamificacion(int nivel){
        consulta = "gamificacion";
        String url = "http://"+gs.getIp()+"/persona/registrar_gamificacion.php?idPersona="+idPersona+"&nivel="+nivel+"&fecha="+fecha;

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
            generarRegistro();
        }
        else{
            if(consulta.compareTo("generar_registro") == 0){
                registrarUsuario();
            }
            else {
                if (consulta.compareTo("usuario") == 0) {
                    registrarPeso();
                } else {
                    if (consulta.compareTo("historico_peso") == 0) {
                        String nivel = "Novato";
                        if(gs.getNivelActividad().compareTo("Avanzado") == 0){
                            nivel = "Intermedio";
                        }
                        registrarDatos(nivel);
                    } else {
                        if (consulta.compareTo("datos") == 0) {
                            int nivel = 0;
                            if(gs.getNivelActividad().compareTo("Intermedio") == 0){
                                nivel = 7;
                            }
                            else{
                                if(gs.getNivelActividad().compareTo("Avanzado") == 0){
                                    nivel = 15;
                                }
                            }
                            registrarGamificacion(nivel);
                        }
                        else {
                            if (consulta.compareTo("gamificacion") == 0) {
                                progress.hide();
                                Toast.makeText(this, "Se ha registrado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                                limpiarDatos();
                                finish();
                            }
                        }
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
