package com.example.uer.trabajogradofittness.Persona;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

public class Registrar extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;

    ImageButton btnRegresar;
    ImageButton btnAdelante;
    ImageButton btnCalendario;

    EditText etFechaNato;
    static final int TIPO_DIALOGO = 0;
    static DatePickerDialog.OnDateSetListener fecha;

    RadioButton radioButton;
    RadioGroup rgroup;
    RadioButton rbMasculino;
    RadioButton rbFemenino;

    Spinner spDepartamento;
    Spinner spCiudad;

    ArrayList<String> listaConsulta;
    String consulta;

    int año;
    int mes;
    int dia;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        gs = (GlobalState)getApplication();
        request = Volley.newRequestQueue(getApplicationContext());

        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = rgroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                regresar(view);
            }
        });
        btnAdelante = findViewById(R.id.btnSiguiente);
        btnAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = rgroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                adelante(view);
            }
        });


        rgroup= findViewById(R.id.rgroup);

        etFechaNato = findViewById(R.id.etFechaNato);

        Calendar calendario = Calendar.getInstance();
        año = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH)+1 ;
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

        cargarDepartamentos();
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
        etFechaNato.setText(dia+"/"+mes+"/"+año);
    }

    public void adelante(View view) {
        Intent intent = new Intent(this, InformacionObjetivo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void regresar(View view) {

        Intent intent = new Intent(this, InformacionPersonal.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        JSONArray datos = response.optJSONArray(consulta);

        try {
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);

                if(consulta.compareTo("departamento") == 0){
                    listaConsulta.add(jsonObject.optString("departamento"));
                }
                if(consulta.compareTo("ciudad") == 0){
                    listaConsulta.add(jsonObject.optString("ciudad"));
                }

            }
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaConsulta);

            if(consulta.compareTo("departamento") == 0){
                spDepartamento.setAdapter(adapter);
            }
            if(consulta.compareTo("ciudad") == 0){
                spCiudad.setAdapter(adapter);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }





    @Override
    public void onErrorResponse(VolleyError error) {
        //progreso.hide();
        Toast.makeText(getApplicationContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

}
