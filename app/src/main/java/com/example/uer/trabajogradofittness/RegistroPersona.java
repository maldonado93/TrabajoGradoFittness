package com.example.uer.trabajogradofittness;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistroPersona extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    public String ip = "192.168.0.27";

    private Spinner spTipoIdentificacion;
    private EditText etIdentificacion;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etEmail;
    private EditText etTelefono;
    private EditText etMovil;
    private Spinner spDepartamento;
    private Spinner spCiudad;
    private Button btnRegistrar;

    public String consulta;

    public boolean ident;
    public boolean depto;
    public boolean ciu;
    ArrayList<String> listaConsulta;


    ProgressDialog progreso;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_persona);

        ident = false;
        depto = false;
        ciu = false;

        spTipoIdentificacion = (Spinner) findViewById(R.id.spTipoIdentificacion);
        etIdentificacion = (EditText) findViewById(R.id.etIdentificacion);
        etNombres = (EditText) findViewById(R.id.etNombres);
        etApellidos = (EditText) findViewById(R.id.etApellidos);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etMovil = (EditText) findViewById(R.id.etMovil);
        spDepartamento = (Spinner) findViewById(R.id.spDepartamento);
        spCiudad = (Spinner) findViewById(R.id.spCiudad);

        request = Volley.newRequestQueue(getApplicationContext());

        spDepartamento.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {
                        cargarCiudades(posicion+1);
                    }

                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);


        btnRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registrarUsuario();

            }
        });

        cargarIdentificaciones();

    }

    private void cargarIdentificaciones(){
        consulta = "tipos_identificaciones";

        String url = "http://"+ip+"/proyectoGrado/query_BD/tipo_identificaciones/listar_tipo_identificaciones.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void cargarDepartamentos(){
        consulta = "departamentos";

        String url = "http://"+ip+"/proyectoGrado/query_BD/departamentos/listar_departamentos.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void cargarCiudades(int id){
        consulta = "ciudades";

        String url = "http://"+ip+"/proyectoGrado/query_BD/ciudades/listar_ciudades.php?idDepartamento="+id;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarUsuario(){
        consulta = "registroUsuario";

        long tipoIdentificacion = spTipoIdentificacion.getSelectedItemId();

        etIdentificacion = (EditText) findViewById(R.id.etIdentificacion);
        String identificacion = etIdentificacion.getText().toString().trim();

        etNombres = (EditText) findViewById(R.id.etNombres);
        String nombres = etNombres.getText().toString().trim();

        etApellidos = (EditText) findViewById(R.id.etApellidos);
        String apellidos = etApellidos.getText().toString().trim();


        etEmail = (EditText) findViewById(R.id.etEmail);
        String email = etEmail.getText().toString().trim();

        etTelefono = (EditText) findViewById(R.id.etTelefono);
        String telefono = etTelefono.getText().toString().trim();

        etMovil = (EditText) findViewById(R.id.etMovil);
        String movil = etMovil.getText().toString().trim();

        spDepartamento = (Spinner) findViewById(R.id.spDepartamento);
        long departamento = spDepartamento.getSelectedItemId();

        spCiudad = (Spinner) findViewById(R.id.spCiudad);
        long ciudad = spCiudad.getSelectedItemId();

        /*progreso = new ProgressDialog(getApplicationContext());
        progreso.setMessage("Consultando...");
        progreso.show();*/

        String url = "http://"+ip+"/proyectoGrado/query_BD/usuario/registrar_usuario.php?usuario="+ nombres +"&password="+ apellidos +"";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onResponse(JSONObject response) {
        listaConsulta = new ArrayList<String>();
        ident = false;
        depto = false;

        if(consulta.compareTo("registroUsuario") == 0) {
            Toast.makeText(getApplicationContext(), "Usuario registrado!", Toast.LENGTH_SHORT).show();
        }
        else {
            JSONArray datos = response.optJSONArray(consulta);

            try {
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);

                    if(consulta.compareTo("tipos_identificaciones") == 0) {
                        listaConsulta.add(jsonObject.optString("tipo"));
                    }
                    if(consulta.compareTo("departamentos") == 0){
                        listaConsulta.add(jsonObject.optString("departamento"));
                    }
                    if(consulta.compareTo("ciudades") == 0){
                        listaConsulta.add(jsonObject.optString("ciudad"));
                    }

                }
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaConsulta);

                if(consulta.compareTo("tipos_identificaciones") == 0) {
                    spTipoIdentificacion.setAdapter(adapter);
                    ident = true;
                }
                if(consulta.compareTo("departamentos") == 0){
                    spDepartamento.setAdapter(adapter);
                }
                if(consulta.compareTo("ciudades") == 0){
                    spCiudad.setAdapter(adapter);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(ident){
            cargarDepartamentos();
        }
        //progreso.hide();
    }





    @Override
    public void onErrorResponse(VolleyError error) {
        //progreso.hide();
        Toast.makeText(getApplicationContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

}
