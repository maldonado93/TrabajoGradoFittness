package com.example.uer.trabajogradofittness.Persona;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{


    View v;
    GlobalState gs;
    ProgressDialog progress;

    ModeloPerfil modeloPerfil;

    ImageView ivImagen;
    Bitmap bitmap;

    LinearLayout layout_datos;
    TextView tvTipoIdentificacion;
    TextView tvIdentificacion;
    TextView tvNombre;
    TextView tvFecha;
    TextView tvEmail;
    TextView tvMovil;
    TextView tvLocalidad;

    LinearLayout layout_campos;
    EditText etNombre;
    EditText etEmail;
    EditText etMovil;

    LinearLayout layout_fisonomia;
    TextView tvEdad;
    TextView tvPeso;
    TextView tvEstatura;

    LinearLayout layout_campos_fisonomia;
    EditText etPeso;
    EditText etEstatura;

    TextView division1;

    Spinner spDepartamento;
    Spinner spCiudad;

    FloatingActionButton fbBorrar;
    FloatingActionButton fbCambiarImagen;

    FloatingActionButton fbEditar;
    FloatingActionButton fbConfirmar;

    private String consulta = null;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_perfil, container, false);

        ivImagen = v.findViewById(R.id.ivImagen);

        fbBorrar = v.findViewById(R.id.fbBorrar);
        fbBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fbCambiarImagen = v.findViewById(R.id.fbCambiarImagen);
        fbCambiarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });

        layout_datos = v.findViewById(R.id.layout_datos);
        tvIdentificacion = v.findViewById(R.id.tvIdentificacion);
        tvNombre = v.findViewById(R.id.tvNombres);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvMovil = v.findViewById(R.id.tvMovil);
        tvLocalidad = v.findViewById(R.id.tvLocalidad);

        layout_campos = v.findViewById(R.id.layout_campos);
        etNombre = v.findViewById(R.id.etNombre);
        etEmail = v.findViewById(R.id.etEmail);
        etMovil = v.findViewById(R.id.etMovil);

        layout_fisonomia = v.findViewById(R.id.layout_fisonomia);
        tvPeso = v.findViewById(R.id.tvPeso);
        tvEstatura = v.findViewById(R.id.tvEstatura);

        layout_campos_fisonomia = v.findViewById(R.id.layout_campos_fisonomia);
        etPeso = v.findViewById(R.id.etPeso);
        etEstatura = v.findViewById(R.id.etEstatura);

        division1 = v.findViewById(R.id.division);

        spDepartamento = v.findViewById(R.id.spDepartamento);
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
        spCiudad = v.findViewById(R.id.spCiudad);

        fbEditar = v.findViewById(R.id.fbEditar);
        fbEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarDatos(1);
            }
        });

        fbConfirmar = v.findViewById(R.id.fbConfirmar);
        fbConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                //dialogo1.setTitle("Importante");
                dialog.setMessage("¿Aplicar los cambios realizados?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        guardarDatos();
                        editarDatos(2);
                    }
                });
                dialog.setNeutralButton("Descartar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editarDatos(2);
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog ventana = dialog.create();
                ventana.show();

            }
        });

        return v;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        //((Principal) getActivity()).getSupportActionBar().setTitle("Mi informacion");

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando informacion...");
        progress.show();

        consultarPersona();
    }

    private void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                ivImagen.setImageBitmap(bitmap);
                guardarImagen();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void editarDatos(int accion){

        if(accion == 1){
            layout_datos.setVisibility(View.GONE);
            layout_campos.setVisibility(View.VISIBLE);
            layout_fisonomia.setVisibility(View.GONE);
            layout_campos_fisonomia.setVisibility(View.VISIBLE);
            fbEditar.setVisibility(View.GONE);
            fbConfirmar.setVisibility(View.VISIBLE);
        }
        else{
            layout_campos.setVisibility(View.GONE);
            layout_datos.setVisibility(View.VISIBLE);
            layout_campos_fisonomia.setVisibility(View.GONE);
            layout_fisonomia.setVisibility(View.VISIBLE);
            fbConfirmar.setVisibility(View.GONE);
            fbEditar.setVisibility(View.VISIBLE);
        }
    }


    private void guardarDatos(){
        tvNombre.setText(etNombre.getText());
        tvEmail.setText(etEmail.getText());
        tvMovil.setText(etMovil.getText());
        tvLocalidad.setText(spCiudad.getSelectedItem().toString()+", "+spDepartamento.getSelectedItem().toString());
        tvPeso.setText(etPeso.getText());
        tvEstatura.setText(etEstatura.getText());
    }

    private void consultarPersona(){
        consulta = "persona";
        String url = "http://"+gs.getIp()+"/persona/consultar_persona.php?idPersona="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void guardarImagen(){
        consulta = "guardar_imagen";
        String idPersona = String.valueOf(gs.getSesion_usuario());

        String imagen = convertirImagen(bitmap);

        String url = "http://"+gs.getIp()+"/persona/guardar_imagen.php?idPersona="+idPersona+"&imagen="+imagen;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private String convertirImagen(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagen = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagen;
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


    public static int obtenerPosicionItem(Spinner spinner, String item) {
        int posicion = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                posicion = i;
            }
        }
        return posicion;
    }



    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray(consulta);
        ArrayList<String> listaConsulta = new ArrayList<String>();

        modeloPerfil = new ModeloPerfil();

        try {
            if(consulta.compareTo("guardarImagen") != 0){
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);

                    if(consulta.compareTo("persona") == 0){
                        modeloPerfil.setId_tipo_identificacion(jsonObject.optString("id_tipo_identificacion"));
                        modeloPerfil.setIdentificacion(jsonObject.optString("identificacion"));
                        modeloPerfil.setNombre(jsonObject.optString("nombres")+" "
                                +jsonObject.optString("apellidos"));
                        modeloPerfil.setEmail(jsonObject.optString("email"));
                        if(jsonObject.optString("movil") != null){
                            modeloPerfil.setMovil(jsonObject.optString("movil"));
                        }
                        else{
                            modeloPerfil.setMovil(" ");
                        }
                        modeloPerfil.setDato(jsonObject.optString("foto"));
                        modeloPerfil.setLocalidad(jsonObject.optString("localidad"));

                        modeloPerfil.setPeso(jsonObject.optString("peso"));
                        modeloPerfil.setEstatura(jsonObject.optString("estatura"));

                    }
                    if(consulta.compareTo("departamento") == 0){
                        listaConsulta.add(jsonObject.optString("departamento"));
                    }
                    if(consulta.compareTo("ciudad") == 0){
                        listaConsulta.add(jsonObject.optString("ciudad"));
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if(consulta.compareTo("guardarImagen") != 0){
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listaConsulta);

            if(consulta.compareTo("persona") == 0){
                if(modeloPerfil.getFoto() != null){
                    ivImagen.setImageBitmap(modeloPerfil.getFoto());
                }
                else{
                    ivImagen.setImageResource(R.mipmap.foto_defecto_round);
                }
                tvIdentificacion.setText(modeloPerfil.getIdentificacion());
                tvNombre.setText(modeloPerfil.getNombre());
                tvEmail.setText(modeloPerfil.getEmail());
                tvLocalidad.setText(modeloPerfil.getLocalidad());
                tvMovil.setText(modeloPerfil.getMovil());

                etNombre.setText(modeloPerfil.getNombre());
                etEmail.setText(modeloPerfil.getEmail());
                etMovil.setText(modeloPerfil.getMovil());

                tvPeso.setText(modeloPerfil.getPeso());
                tvEstatura.setText(modeloPerfil.getEstatura());

                etPeso.setText(modeloPerfil.getPeso());
                etEstatura.setText(modeloPerfil.getEstatura().toString());
                cargarDepartamentos();
            }
            if(consulta.compareTo("departamento") == 0){
                spDepartamento.setAdapter(adapter);
                String[] localidad = (tvLocalidad.getText().toString()).split(", ");
                spDepartamento.setSelection(obtenerPosicionItem(spDepartamento, localidad[1]));
            }
            if(consulta.compareTo("ciudad") == 0){
                spCiudad.setAdapter(adapter);
                String[] localidad = (tvLocalidad.getText().toString()).split(", ");
                spCiudad.setSelection(obtenerPosicionItem(spCiudad, localidad[0]));
            }
            progress.hide();
        }
        else{
            Toast.makeText(getContext(), "Imagen actualizada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progress.hide();
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

}
