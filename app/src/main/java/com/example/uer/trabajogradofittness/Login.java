package com.example.uer.trabajogradofittness;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.uer.trabajogradofittness.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    public String ip = "192.168.1.6";
    private EditText etUsuario;
    private EditText etPassword;
    private Button btnIngresar;

    ProgressDialog progreso;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);

        request = Volley.newRequestQueue(getApplicationContext());

        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                consultarUsuario();

            }
        });
    }



    private void consultarUsuario(){
        etUsuario = (EditText)findViewById(R.id.etUsuario);
        String usuario = etUsuario.getText().toString().trim();

        etPassword = (EditText)findViewById(R.id.etPassword);
        String password = etPassword.getText().toString().trim();

        /*try {
            String passwdMd5 = this.toMd5(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

        /*progreso = new ProgressDialog(getApplicationContext());
        progreso.setMessage("Registrando...");
        progreso.show();*/

        String url = "http://"+ip+"/proyectoGrado/query_BD/usuario/consultar_usuario.php?usuario="+ usuario +"&password="+ password+"";

        url = url.replace(" ", "%20");

        /*jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);*/

        Intent registro = new Intent(Login.this, Principal.class);
        registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(registro);
    }



    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Bienvenido!", Toast.LENGTH_SHORT).show();
        //progreso.hide();

        Usuario usuario = new Usuario();

        JSONArray datos = response.optJSONArray("usuario");
        JSONObject jsonObject = null;

        try {
            jsonObject = datos.getJSONObject(0);

            usuario.setId_usuario(jsonObject.optInt("ID_USUARIO"));
            usuario.setId_tipo_usuario(jsonObject.optInt("ID_TIPO_USUARIO"));
            usuario.setId_persona(jsonObject.optInt("ID_PERSONA"));

            int id = usuario.getId_usuario();
            int idPersona = usuario.getId_persona();
            Toast.makeText(this, "id:"+ id + "Persona: "+idPersona, Toast.LENGTH_LONG).show();
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


    public void Registrar(View v) throws NoSuchAlgorithmException {

        Intent registro = new Intent(Login.this, RegistroPersona.class);
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
}
