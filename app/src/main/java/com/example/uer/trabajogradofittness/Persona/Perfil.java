package com.example.uer.trabajogradofittness.Persona;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;
    ProgressDialog progress;
    ProgressBar progressBar;

    ScrollView scrollView;
    RelativeLayout relativeLayout;

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
    Spinner spTipoIdentificacion;
    EditText etIdentificacion;
    EditText etNombre;
    EditText etApellido;
    EditText etEmail;
    EditText etMovil;

    LinearLayout layout_condicion_fisica;
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

    String fecha;
    private String consulta = null;
    boolean existeIdentificacion;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_perfil, container, false);

        progressBar = v.findViewById(R.id.progressBar);

        scrollView = v.findViewById(R.id.scrollView);
        relativeLayout = v.findViewById(R.id.relativeLayout);

        ivImagen = v.findViewById(R.id.ivImagen);
        consultarImagen(gs.getFotoPerfil());

        fbBorrar = v.findViewById(R.id.fbBorrar);
        fbBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                dialogo1.setTitle("");
                dialogo1.setMessage("¿Desea eliminar su foto de perfil?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    eliminar_imagen();
                    String url = "http://piperomero1226.000webhostapp.com/persona/imagenes/";
                    if(gs.getGenero().compareTo("Femenino") == 0){
                        url +="foto_mujer.png";
                    }
                    else{
                        url +="foto_hombre.png";
                    }
                    gs.setFotoPerfil(url);
                    consultarImagen(url);
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();
                    }
                });
                dialogo1.show();
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
        tvNombre = v.findViewById(R.id.tvNombre);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvMovil = v.findViewById(R.id.tvMovil);
        tvLocalidad = v.findViewById(R.id.tvLocalidad);

        layout_campos = v.findViewById(R.id.layout_campos);
        spTipoIdentificacion = v.findViewById(R.id.spTipoIdentificacion);
        etIdentificacion = v.findViewById(R.id.etIdentificacion);
        etIdentificacion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String[] ident = tvIdentificacion.getText().toString().split(". ");
                if (etIdentificacion.getText().toString().compareTo(ident[1]) != 0) {
                    existeIdentificacion = false;
                    verificarIdentificacion();
                }
                else{
                    existeIdentificacion = false;
                    etIdentificacion.setTextColor(Color.BLACK);
                }
            }
        });

        etNombre = v.findViewById(R.id.etNombre);
        etApellido = v.findViewById(R.id.etApellido);
        etEmail = v.findViewById(R.id.etEmail);
        etMovil = v.findViewById(R.id.etMovil);

        layout_condicion_fisica = v.findViewById(R.id.layout_condicion_fisica);
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
                        boolean valida = validarDatos();
                        if(valida){
                            asignarDatos();
                            editarDatos(2);
                        }
                        else{
                            Toast.makeText(getContext(), "Complete todos los campos, por favor!", Toast.LENGTH_SHORT).show();
                        }

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

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(c.getTime());

        consultarPersona();
    }

    private void verificarIdentificacion(){
        consulta = "verificar_identificacion";

        String url = "http://"+gs.getIp()+"/persona/verificar_identificacion.php?identificacion="+ etIdentificacion.getText().toString();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
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

    private boolean validarDatos(){
        String identificacion = etIdentificacion.getText().toString();
        String nombres = etIdentificacion.getText().toString();
        String apellidos = etIdentificacion.getText().toString();
        String email = etIdentificacion.getText().toString();
        String movil = etIdentificacion.getText().toString();
        String peso = etIdentificacion.getText().toString();
        String estatura = etIdentificacion.getText().toString();

        if(identificacion.compareTo("") != 0 && nombres.compareTo("") != 0 && apellidos.compareTo("") != 0
                && email.compareTo("") != 0 && movil.compareTo("") != 0 && peso.compareTo("") != 0
                && estatura.compareTo("") != 0){
            return true;
        }
        else{
            return false;
        }
    }

    @SuppressLint("RestrictedApi")
    private void editarDatos(int accion){

        if(accion == 1){
            layout_datos.setVisibility(View.GONE);
            layout_campos.setVisibility(View.VISIBLE);
            layout_condicion_fisica.setVisibility(View.GONE);
            layout_campos_fisonomia.setVisibility(View.VISIBLE);
            fbEditar.setVisibility(View.GONE);
            fbConfirmar.setVisibility(View.VISIBLE);
        }
        else{
            layout_campos.setVisibility(View.GONE);
            layout_datos.setVisibility(View.VISIBLE);
            layout_campos_fisonomia.setVisibility(View.GONE);
            layout_condicion_fisica.setVisibility(View.VISIBLE);
            fbConfirmar.setVisibility(View.GONE);
            fbEditar.setVisibility(View.VISIBLE);
        }
    }

    private void asignarDatos(){
        if(spTipoIdentificacion.getSelectedItemPosition() == 0){
            tvIdentificacion.setText("CC. " + etIdentificacion.getText());
        }
        else{
            tvIdentificacion.setText("TI. " + etIdentificacion.getText());
        }
        tvNombre.setText(etNombre.getText() + " " + etApellido.getText());
        tvEmail.setText(etEmail.getText());
        tvMovil.setText(etMovil.getText());
        tvLocalidad.setText(spCiudad.getSelectedItem().toString()+", "+spDepartamento.getSelectedItem().toString());
        if(tvPeso.getText().toString().compareTo(etPeso.getText().toString()) != 0){
            tvPeso.setText(etPeso.getText());
        }
        else{
            etPeso.setText("0");
        }

        tvEstatura.setText(etEstatura.getText());

        guardarDatos();
    }

    private void consultarPersona(){
        consulta = "persona";
        String url = "http://"+gs.getIp()+"/persona/consultar_persona.php?idPersona="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void guardarDatos(){
        consulta = "guardar_datos";
        String url = "http://"+gs.getIp()+"/persona/guardar_datos.php?idPersona="+gs.getSesion_usuario()
                +"&tipoIdentificacion="+(spTipoIdentificacion.getSelectedItemPosition()+1)
                +"&identificacion="+etIdentificacion.getText()
                +"&nombres="+etNombre.getText()
                +"&apellidos="+etApellido.getText()
                +"&movil="+etMovil.getText()
                +"&email="+etEmail.getText()
                +"&ciudad="+spCiudad.getSelectedItem()
                +"&peso="+etPeso.getText()
                +"&estatura="+etEstatura.getText()
                +"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void guardarImagen(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + gs.getIp() + "/persona/guardar_imagen_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.compareTo("") != 0){
                            gs.setFotoPerfil(response);
                            Toast.makeText(getContext(), "Imagen actualizada!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Error al actualizar la imagen!", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                String imagen = convertirImagen();
                String idPersona = String.valueOf(gs.getSesion_usuario());

                Map<String, String> params = new Hashtable<String, String>();

                params.put("imagen", imagen);
                params.put("idPersona", idPersona);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void eliminar_imagen(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + gs.getIp() + "/persona/eliminar_imagen.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.compareTo("") != 0){
                            gs.setFotoPerfil(response);
                            Toast.makeText(getContext(), "Foto eliminada!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Error al actualizar la imagen!", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                String idPersona = String.valueOf(gs.getSesion_usuario());

                Map<String, String> params = new Hashtable<String, String>();

                params.put("genero", gs.getGenero());
                params.put("idPersona", idPersona);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private String convertirImagen() {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagen = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagen;
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

    public static int obtenerPosicionItem(Spinner spinner, String item) {
        int posicion = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                posicion = i;
            }
        }
        return posicion;
    }

    private void consultarImagen(String url){

        url = url.replace(" ", "%20");

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ivImagen.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray(consulta);
        ArrayList<String> listaConsulta = new ArrayList<String>();
        ArrayAdapter<CharSequence> adaptador = null;

        modeloPerfil = new ModeloPerfil();

        String url = "";
        try {
            if(consulta.compareTo("guardarImagen") != 0){
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);

                    if(consulta.compareTo("persona") == 0){
                        modeloPerfil.setId_tipo_identificacion(jsonObject.optString("tipo"));
                        modeloPerfil.setIdentificacion(jsonObject.optString("identificacion"));
                        modeloPerfil.setNombres(jsonObject.optString("nombres"));
                        modeloPerfil.setApellidos(jsonObject.optString("apellidos"));
                        modeloPerfil.setEmail(jsonObject.optString("email"));
                        if(jsonObject.optString("movil") != null){
                            modeloPerfil.setMovil(jsonObject.optString("movil"));
                        }
                        else{
                            modeloPerfil.setMovil(" ");
                        }
                        modeloPerfil.setLocalidad(jsonObject.optString("localidad"));
                        double peso = Double.parseDouble(jsonObject.optString("peso"));
                        modeloPerfil.setPeso(String.valueOf((int)peso));
                        modeloPerfil.setEstatura(jsonObject.optString("estatura"));

                    }
                    if(consulta.compareTo("tipo_identificacion") == 0){
                        listaConsulta.add(jsonObject.optString("tipo"));
                    }
                    if(consulta.compareTo("departamento") == 0){
                        listaConsulta.add(jsonObject.optString("departamento"));
                    }
                    if(consulta.compareTo("ciudad") == 0){
                        listaConsulta.add(jsonObject.optString("ciudad"));
                    }
                    if(consulta.compareTo("guardar_datos") == 0){
                        Toast.makeText(getContext(), "Datos actualizados!", Toast.LENGTH_SHORT).show();
                    }
                    if(consulta.compareTo("verificar_identificacion") == 0){
                        int id = jsonObject.optInt("id");
                        if(id != 0){
                            existeIdentificacion = true;
                            etIdentificacion.setTextColor(Color.RED);
                            Toast.makeText(getContext(), "La identificacion ya existe!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            existeIdentificacion = false;
                            etIdentificacion.setTextColor(Color.BLACK);
                        }
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
                if(modeloPerfil.getId_tipo_identificacion().compareTo("Cédula de ciudadanía") == 0){
                    tvIdentificacion.setText("CC. " + modeloPerfil.getIdentificacion());
                }
                else{
                    tvIdentificacion.setText("TI. " + modeloPerfil.getIdentificacion());
                }
                tvNombre.setText(modeloPerfil.getNombres() + " "+ modeloPerfil.getApellidos());
                tvEmail.setText(modeloPerfil.getEmail());
                tvLocalidad.setText(modeloPerfil.getLocalidad());
                tvMovil.setText(modeloPerfil.getMovil());

                etIdentificacion.setText(modeloPerfil.getIdentificacion());
                etNombre.setText(modeloPerfil.getNombres());
                etApellido.setText(modeloPerfil.getApellidos());
                etEmail.setText(modeloPerfil.getEmail());
                etMovil.setText(modeloPerfil.getMovil());

                tvPeso.setText(modeloPerfil.getPeso());
                tvEstatura.setText(modeloPerfil.getEstatura());

                etPeso.setText(modeloPerfil.getPeso());
                etEstatura.setText(modeloPerfil.getEstatura());


                cargarTiposIdentificacion();
            }
            else{
                if (consulta.compareTo("tipo_identificacion") == 0) {
                    adaptador = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listaConsulta);
                    spTipoIdentificacion.setAdapter(adaptador);
                    spCiudad.setSelection(obtenerPosicionItem(spDepartamento, modeloPerfil.getId_tipo_identificacion()));

                    cargarDepartamentos();
                }
                else{
                    if(consulta.compareTo("departamento") == 0){
                        spDepartamento.setAdapter(adapter);
                        String[] localidad = (tvLocalidad.getText().toString()).split(", ");
                        spDepartamento.setSelection(obtenerPosicionItem(spDepartamento, localidad[1]));
                    }
                    else{
                        if(consulta.compareTo("ciudad") == 0){
                            spCiudad.setAdapter(adapter);
                            String[] localidad = (tvLocalidad.getText().toString()).split(", ");
                            spCiudad.setSelection(obtenerPosicionItem(spCiudad, localidad[0]));
                        }
                    }
                }
            }

            scrollView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
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
