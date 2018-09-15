package com.example.uer.trabajogradofittness.Persona;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.Calendar;

public class InformacionPersonal extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;

    ArrayList<String> listaConsulta;
    String consulta;
    boolean carga = false;

    ImageButton btnSalir;
    ImageButton btnSiguiente;
    ImageButton btnCalendario;

    EditText etUsuario;
    EditText etPassword;
    EditText etIdentificacion;
    EditText etNombres;
    EditText etApellidos;
    EditText etEmail;
    EditText etFechaNato;

    static final int TIPO_DIALOGO = 0;
    static DatePickerDialog.OnDateSetListener fecha;

    RadioGroup rgroup;
    RadioButton rbMasculino;
    RadioButton rbFemenino;

    Spinner spTipoIdentificacion;
    Spinner spDepartamento;
    Spinner spCiudad;

    int año;
    int mes;
    int dia;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);

        gs = (GlobalState)getApplication();
        request = Volley.newRequestQueue(getApplicationContext());

        btnSalir = findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCampos(view);
            }
        });

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        spTipoIdentificacion = findViewById(R.id.spTipoIdentificacion);
        etIdentificacion = findViewById(R.id.etIdentificacion);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etEmail = findViewById(R.id.etEmail);

        rgroup= findViewById(R.id.rgroup);
        rbFemenino= findViewById(R.id.rbFemenino);
        rbMasculino= findViewById(R.id.rbMasculino);

        etFechaNato = findViewById(R.id.etFechaNato);

        Calendar calendario = Calendar.getInstance();
        año = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH) ;
        dia = calendario.get(Calendar.DAY_OF_MONTH);

        mostrarfecha();
        fecha = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH_YEAR, int DAY_OF_MONTH) {
                año = YEAR;
                mes = MONTH_YEAR;
                dia = DAY_OF_MONTH;
                mostrarfecha();
            }
        };

        btnCalendario = findViewById(R.id.btnCalendario);
        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarCalendario(view);
            }
        });

        spDepartamento = findViewById(R.id.spDepartamento);
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

        spCiudad = findViewById(R.id.spCiudad);

        cargarCampos();
        cargarTiposIdentificacion();
    }
    @Override

    protected Dialog onCreateDialog(int id){
        switch (id) {
            case 0:
                return new DatePickerDialog(this,fecha,dia, mes,año);
        }
        return null;
    }

    public void mostrarCalendario(View control){
        showDialog(TIPO_DIALOGO);
    }

    public void mostrarfecha() {
        etFechaNato.setText(dia+"/"+(mes+1)+"/"+año);
    }

    public void cargarCampos(){
        carga = true;

        etUsuario.setText(gs.getUsuario());
        etPassword.setText(gs.getPassword());
        etIdentificacion.setText(gs.getIdentificacion());
        etNombres.setText(gs.getNombres());
        etApellidos.setText(gs.getApellidos());
        etEmail.setText(gs.getEmail());

        String genero = gs.getGenero();
        if(genero.compareTo("Masculino") == 0){
            rbMasculino.setChecked(true);
        }
        else{
            rbFemenino.setChecked(true);
        }
        String[] fecha = gs.getFecha().split("-");
        etFechaNato.setText(fecha[2]+"/"+fecha[1]+"/"+fecha[0]);

    }

    public void verificarCampos(View view){
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        int tipoIdentificacion = (spTipoIdentificacion.getSelectedItemPosition()+1);
        String identificacion = etIdentificacion.getText().toString().trim();
        String nombres = etNombres.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String genero = "";
        if(rbMasculino.isChecked()){
            genero = "Masculino";
        }
        if(rbFemenino.isChecked()){
            genero = "Femenino";
        }
        String[] datos = etFechaNato.getText().toString().split("/");
        String fecha = datos[2]+"-"+datos[1]+"-"+datos[0];
        int departamento = (spDepartamento.getSelectedItemPosition()+1);
        String ciudad = spCiudad.getSelectedItem().toString();
        int idCiudad = (spCiudad.getSelectedItemPosition()+1);

        if(usuario.compareTo("") != 0 && password.compareTo("") != 0 && identificacion.compareTo("") != 0 && nombres.compareTo("") != 0 && apellidos.compareTo("") != 0
                && email.compareTo("") != 0 && genero.compareTo("") != 0 && fecha.compareTo("") != 0){

            gs.setRegistro(1);
            gs.setUsuario(usuario);
            gs.setPassword(password);
            gs.setTipoIdentificacion(tipoIdentificacion);
            gs.setIdentificacion(identificacion);
            gs.setNombres(nombres);
            gs.setApellidos(apellidos);
            gs.setEmail(email);
            gs.setGenero(genero);
            gs.setFecha(fecha);
            gs.setDepartamento(departamento);
            gs.setCiudad(ciudad);
            gs.setIdCiudad(idCiudad);

            siguiente(view);
        }
        else{
            Toast.makeText(this,"Complete los campos, por favor!", Toast.LENGTH_SHORT).show();
            Snackbar.make(view, "Complete los campos, por favor!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
    }

    public void siguiente(View view) {

        Intent intent = new Intent(this, InformacionObjetivo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void cargarTiposIdentificacion(){
        consulta = "tipo_identificacion";

        String url = "http://"+gs.getIp()+"/tipo_identificacion/listar_tipo_identificaciones.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void cargarDepartamentos(){
        consulta = "departamento";

        String url = "http://"+gs.getIp()+"/departamento/listar_departamentos.php";

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void cargarCiudades(int id){
        consulta = "ciudad";

        String url = "http://"+gs.getIp()+"/ciudad/listar_ciudades.php?idDepartamento="+id;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        listaConsulta = new ArrayList<>();
        ArrayAdapter<CharSequence> adaptador = null;
        JSONArray datos = response.optJSONArray(consulta);

        try {
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                if(consulta.compareTo("tipo_identificacion") == 0){
                    listaConsulta.add(jsonObject.optString("tipo"));
                }
                if(consulta.compareTo("departamento") == 0){
                    listaConsulta.add(jsonObject.optString("departamento"));
                }
                if(consulta.compareTo("ciudad") == 0){
                    listaConsulta.add(jsonObject.optString("ciudad"));
                }

            }
            adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaConsulta);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if(consulta.compareTo("tipo_identificacion") == 0){
            spTipoIdentificacion.setAdapter(adaptador);
            if(gs.getRegistro() == 1){
                spTipoIdentificacion.setSelection(gs.getTipoIdentificacion()-1);
            }
            cargarDepartamentos();
        }
        else{
            if(consulta.compareTo("departamento") == 0){
                spDepartamento.setAdapter(adaptador);
                if(gs.getRegistro() == 1){
                    spDepartamento.setSelection(gs.getDepartamento()-1);
                }
            }
            if(consulta.compareTo("ciudad") == 0){
                spCiudad.setAdapter(adaptador);
                if(carga){
                    spCiudad.setSelection(gs.getIdCiudad()-1);
                }
            }
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        //progreso.hide();
        Toast.makeText(getApplicationContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
