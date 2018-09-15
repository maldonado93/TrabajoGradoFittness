package com.example.uer.trabajogradofittness;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    String TAG = this.getClass().getName();

    GlobalState gs;

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

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gs = (GlobalState)getApplication();
        request = Volley.newRequestQueue(getApplicationContext());

        progress = new ProgressDialog(Login.this);
        progress.setMessage("Cargando...");


        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etUsuario.setText("");

        etPassword = (EditText)findViewById(R.id.etPassword);
        etPassword.setText("");

        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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

            progress.show();
            String url = "http://"+gs.getIp()+"/usuario/consultar_usuario1.php?usuario="+ usuario;

            url = url.replace(" ", "%20");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            request.add(jsonObjectRequest);
        }
        else{
            Snackbar.make(view, "Complete los campos, por favor!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
    }


    @Override
    public void onResponse(JSONObject response) {
        //progreso.hide();
        int tipoUsuario = 0;

        JSONArray datos = response.optJSONArray("usuario");
        JSONObject jsonObject = null;

        try {
            jsonObject = datos.getJSONObject(0);
            if(jsonObject != null){
                gs.setSesion_usuario(jsonObject.optInt("id_persona"));
                gs.setTipo_usuario(jsonObject.optInt("id_tipo_usuario"));
            }
            if(gs.getSesion_usuario() != 0){
                if(jsonObject.optString("password").compareTo(password) == 0 ){


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
                else{
                    Toast.makeText(this, "Contraseña erronea!",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Usuario no registrado!",Toast.LENGTH_SHORT).show();
            }
            progress.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //progreso.hide();
        Toast.makeText(getApplicationContext(), "Error de login "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
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

}
