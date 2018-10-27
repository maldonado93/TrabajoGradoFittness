package com.example.uer.trabajogradofittness;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.MenuPrincipal.Menu;
import com.example.uer.trabajogradofittness.Persona.InformacionPersonal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    String TAG = this.getClass().getName();

    GlobalState gs;

    String consulta;
    String fecha;

    EditText teclado;
    EditText etUsuario;
    EditText etPassword;
    Button btnIngresar;
    Button btnRegistro;

    ProgressDialog progress;

    String usuario;
    String password;

    boolean salir = false;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new DecelerateInterpolator());

        getWindow().setEnterTransition(fadeIn);

        setContentView(R.layout.activity_login);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(c.getTime());

        gs = (GlobalState)getApplication();
        request = Volley.newRequestQueue(getApplicationContext());

        progress = new ProgressDialog(Login.this);
        progress.setMessage("Cargando...");
        progress.setCanceledOnTouchOutside(false);

        etUsuario = findViewById(R.id.etUsuario);
        etUsuario.setText("");

        etPassword = findViewById(R.id.etPassword);
        etPassword.setText("");

        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                consultarUsuario(view);
            }
        });


        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registrar(view);
            }
        });
    }

    private void iniciarSesion(){
        progress.hide();
        etUsuario.setText("");
        etPassword.setText("");
        Intent ingreso = null;

        if(gs.getTipo_usuario() == 1){
            ingreso = new Intent(Login.this, Menu.class);
        }
        else{
            ingreso = new Intent(Login.this, PrincipalInstructor.class);
        }

        ingreso.addFlags(ingreso.FLAG_ACTIVITY_CLEAR_TOP | ingreso.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(ingreso);
    }

    private void calcularRendimiento(int registros){
        double rendimiento = gs.getRendimiento();
        boolean correcto = true;
        if(gs.getNivelActividad().compareTo("Novato") == 0 || gs.getNivelActividad().compareTo("Intermedio") == 0 ){
            if(registros == 4){
                rendimiento += 0.01;
            }
            else{
                correcto = false;
            }
        }
        else{
            if(registros >= 4 && registros <= 6){
                rendimiento += 0.01;

            }
            else{
                correcto = false;
            }
        }

        if(!correcto){
            gs.setActualizaRendimiento("incumplido");
            rendimiento -= 0.01;
        }
        else{
            gs.setActualizaRendimiento("cumplido");
        }

        gs.setRendimiento(rendimiento);
    }

    private void consultarUsuario(View view){
        etUsuario = (EditText)findViewById(R.id.etUsuario);
        usuario = etUsuario.getText().toString().trim();

        etPassword = (EditText)findViewById(R.id.etPassword);
        password = etPassword.getText().toString().trim();

        if(usuario.compareTo("") != 0 && password.compareTo("") != 0){
            /*try {
                String passwdMd5 = this.toMd5(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }*/

            //String url = "http://"+gs.getIp()+"/usuario/consultar_usuario.php?usuario="+ usuario +"&password="+ password+"";

            consulta = "usuario";
            progress.show();
            String url = "http://"+gs.getIp()+"/usuario/consultar_usuario.php?usuario="+ usuario;

            url = url.replace(" ", "%20");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            request.add(jsonObjectRequest);
        }
        else{
            Toast.makeText(this, "Complete los campos, por favor!", Toast.LENGTH_SHORT).show();
        }
    }

    public void obtenerDatosUsuario(){

        consulta = "datos";
        String url = "http://"+gs.getIp()+"/usuario/consultar_datos_usuario.php?usuario="+ usuario+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    public void consultarEntrenos(){

        consulta = "registros";
        String url = "http://"+gs.getIp()+"/registro_entrenos/consultar_registros_semana.php?idPersona="+ gs.getSesion_usuario()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void actualizarRendimiento(){
        consulta = "rendimiento";
        String url = "http://"+gs.getIp()+"/persona/actualizar_rendimiento.php?idPersona="+ gs.getSesion_usuario()+"&rendimiento="+gs.getRendimiento()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void calcularEdad(String fecha){
        Date fechaActual = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String hoy = formato.format(fechaActual);
        String[] dat1 = fecha.split("-");
        String[] dat2 = hoy.split("-");

        int edad = Integer.parseInt(dat2[0]) - Integer.parseInt(dat1[0]);
        int mes = Integer.parseInt(dat2[1]) - Integer.parseInt(dat1[1]);
        if (mes < 0) {
            edad = edad - 1;
        } else if (mes == 0) {
            int dia = Integer.parseInt(dat2[2]) - Integer.parseInt(dat1[2]);
            if (dia > 0) {
                edad = edad - 1;
            }
        }
        gs.setEdad(edad);
    }

    public void Registrar(View v){

        Intent registro = new Intent(this, InformacionPersonal.class);
        registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(registro);
    }

    public String toMd5(String texto) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(texto.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        //ahora sb.toString(); es la contraseña cifrada
        return  sb.toString();
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "click");

        if(salir){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        salir = true;
        Log.d(TAG, "Salir: "+salir);
        Toast.makeText(Login.this,"Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                salir = false;
                Log.d(TAG, "Salir: "+salir);
            }
        },3000);
    }

    @Override
    public void onResponse(JSONObject response) {

        boolean usuarioValido = false;
        String url = "";
        int dias = 0;
        int diasRegistro = 0;
        int cantidadRegistros = 0;

        JSONArray datos = response.optJSONArray(consulta);
        JSONObject jsonObject = null;

        try {
            jsonObject = datos.getJSONObject(0);
            if(jsonObject != null){
                if(consulta.compareTo("usuario") == 0){
                    gs.setSesion_usuario(jsonObject.optInt("id_persona"));
                    gs.setTipo_usuario(jsonObject.optInt("id_tipo_usuario"));
                    usuario = jsonObject.optString("usuario");
                    if(gs.getSesion_usuario() != 0){
                        if(jsonObject.optString("password").compareTo(password) == 0 ){
                            gs.setUsuario(usuario);
                            gs.setPassword(password);
                            usuarioValido = true;
                        }
                        else{
                            progress.hide();
                            Toast.makeText(this, "Contraseña erronea!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        progress.hide();
                        Toast.makeText(this, "Usuario o email no registrado!",Toast.LENGTH_SHORT).show();
                    }
                }
                if(consulta.compareTo("datos") == 0){
                    gs.setFotoPerfil(jsonObject.optString("foto"));
                    gs.setNombres(jsonObject.optString("nombres"));
                    gs.setApellidos(jsonObject.optString("apellidos"));
                    gs.setGenero(jsonObject.optString("genero"));
                    gs.setNivel(jsonObject.optInt("nivel"));
                    gs.setPuntos(jsonObject.optInt("puntos"));
                    dias = jsonObject.optInt("dias");
                    diasRegistro = jsonObject.optInt("fecha_registro");

                    if(diasRegistro < 30){
                        gs.setNovato(true);
                    }
                    else{
                        gs.setNovato(false);
                    }
                    gs.setRendimiento(Double.parseDouble(jsonObject.optString("rendimiento")));
                    gs.setPeso(Float.parseFloat(jsonObject.optString("peso")));
                    gs.setNivelActividad(jsonObject.optString("nivel_actividad"));
                    gs.setFumador(jsonObject.optString("fuma"));
                    calcularEdad(jsonObject.optString("fecha_nacimiento"));
                    usuarioValido = true;
                }
                if(consulta.compareTo("registros") == 0){
                    cantidadRegistros = jsonObject.optInt("cantidad");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            progress.hide();
        }

        if(usuarioValido){
            if(consulta.compareTo("usuario") == 0){
                obtenerDatosUsuario();
            }
            else{
                if(consulta.compareTo("datos") == 0){
                    if(dias >= 7){
                        consultarEntrenos();
                    }
                    else{
                        iniciarSesion();
                    }
                }
                else{
                    if(consulta.compareTo("registros") == 0){
                        calcularRendimiento(cantidadRegistros);
                        actualizarRendimiento();
                    }
                    else{
                        if(consulta.compareTo("rendimiento") == 0){
                            iniciarSesion();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progress.hide();
        Toast.makeText(getApplicationContext(), "Error de login "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
