package com.example.uer.trabajogradofittness.RegistroEntreno;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrosEntreno extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    GlobalState gs;

    View v;

    ProgressDialog progress;
    ProgressBar progressBar;

    private List<ListaRegistros> listaRegistros;
    ArrayList<String> listaHistorial;

    String consulta;

    TextView tvMensaje;
    LinearLayout layoutFiltro;
    Spinner spHistorial;
    RecyclerView rvEntrenos;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_registros_entreno, container, false);

        progress = new ProgressDialog(getContext());
        progressBar = v.findViewById(R.id.progressBar);
        /*progress.setMessage("Cargando registros...");
        progress.show();*/

        tvMensaje = v.findViewById(R.id.tvMensaje);

        layoutFiltro= v.findViewById(R.id.layoutFiltro);
        spHistorial = (Spinner)v.findViewById(R.id.spHistorialEntrenos);

        spHistorial.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {
                        String[] fecha = (spHistorial.getSelectedItem().toString()).split("-");
                        int mes = Integer.parseInt(obtenerMes(fecha[0], 2));
                        int year = Integer.parseInt(fecha[1].trim());
                        consultarRegistros(mes, year);
                    }

                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        rvEntrenos = (RecyclerView) v.findViewById(R.id.rvEntrenos);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        //((Principal) getActivity()).getSupportActionBar().setTitle("Registros de entreno");

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        generarHistorial();
    }


    private void generarHistorial(){
        consulta = "historial";

        String url = "http://"+gs.getIp()+"/registro_entrenos/listar_fechas.php?idPersona="+ gs.getSesion_usuario() ;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    private void consultarRegistros(int mm, int yyyy){
        consulta = "registros";

        String url = "http://" + gs.getIp() + "/registro_entrenos/listar_registros.php?idPersona=" + gs.getSesion_usuario() + "&mes=" + mm + "&year=" + yyyy;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    public String obtenerDiaSemana(String fecha) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String[] f = fecha.split("-");
        Date dateFecha = dateFormat.parse(f[2]+"-"+f[1]+"-"+f[0]);

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateFecha);
        int dd = cal.get(Calendar.DAY_OF_WEEK);
        String dia = "";
        switch(dd){
            case 1: dia = "Dom";
                break;
            case 2: dia = "Lun";
                break;
            case 3: dia = "Mar";
                break;
            case 4: dia = "Mié";
                break;
            case 5: dia = "Jue";
                break;
            case 6: dia = "Vie";
                break;
            case 7: dia = "Sab";
                break;
        }
        return dia;
    }


    public String obtenerMes(String mes,int val){
        String mm = "";
        if(val == 1){
            switch(mes){
                case "01": mm = "Enero";
                    break;
                case "02": mm = "Febrero";
                    break;
                case "03": mm = "Marzo";
                    break;
                case "04": mm = "Abril";
                    break;
                case "05": mm = "Mayo";
                    break;
                case "06": mm = "Junio";
                    break;
                case "07": mm = "Julio";
                    break;
                case "08": mm = "Agosto";
                    break;
                case "09": mm = "Septiembre";
                    break;
                case "10": mm = "Octubre";
                    break;
                case "11": mm = "Noviembre";
                    break;
                case "12": mm = "Diciembre";
                    break;
            }
        }
        else{
            switch(mes){
                case "Enero": mm = "01";
                    break;
                case "Febrero": mm = "02";
                    break;
                case "Marzo": mm = "03";
                    break;
                case "Abril": mm = "04";
                    break;
                case "Mayo": mm = "05";
                    break;
                case "Junio": mm = "06";
                    break;
                case "Julio": mm = "07";
                    break;
                case "Agosto": mm = "08";
                    break;
                case "Septiembre": mm = "09";
                    break;
                case "Octubre": mm = "10";
                    break;
                case "Noviembre": mm = "11";
                    break;
                case "Diciembre": mm = "12";
                    break;
            }
        }
        return mm;
    }


    public static int obtenerPosicionItem(Spinner spinner, String fecha) {
        int posicion = 0;

        for (int i = 0; i < spinner.getCount(); i++) {

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fecha)) {
                posicion = i;
            }
        }
        return posicion;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResponse(JSONObject response) {

        listaRegistros = new ArrayList<>();
        listaHistorial = new ArrayList<String>();
        String[] fecha = null;
        String mes = "";
        boolean historial = false;

        JSONArray datos = response.optJSONArray("registro_entreno");
        JSONObject jsonObject = null;
        try {
            jsonObject = datos.getJSONObject(0);
            if(jsonObject.optString("id").compareTo("0") != 0) {
                for (int i = 0; i < datos.length(); i++) {
                    jsonObject = datos.getJSONObject(i);

                    if (consulta == "historial") {
                        fecha = jsonObject.optString("fecha").split("-");
                        mes = obtenerMes(fecha[1], 1);
                        listaHistorial.add(mes + "- " + fecha[0]);

                        historial = true;
                    }

                    if (consulta == "registros") {

                        String idRegistro = jsonObject.optString("id");
                        String idRutina = jsonObject.optString("idRutina");
                        String rutina = jsonObject.optString("rutina");
                        String orientacion = jsonObject.optString("orientacion");
                        String promedioFrecuencia = jsonObject.optString("promedio_frecuencia");
                        String dia = obtenerDiaSemana(jsonObject.optString("fecha"));
                        String fech = jsonObject.optString("fecha");
                        String[] f = fech.split("-");
                        fech = (f[2] + "-" + f[1] + "-" + f[0]);
                        String hora = jsonObject.optString("hora");

                        int tiempoEntreno = Integer.parseInt(jsonObject.optString("tiempo"));
                        int minutos = (int)Math.ceil(tiempoEntreno / 60);
                        int segundos = tiempoEntreno - (minutos * 60);

                        String tiempo = minutos + " min "+ segundos + " seg";

                        listaRegistros.add(new ListaRegistros(idRegistro,
                                idRutina,
                                rutina,
                                orientacion,
                                promedioFrecuencia,
                                dia,
                                fech,
                                hora,
                                tiempo));
                    }
                }
                if (consulta == "historial") {
                    ArrayAdapter<String> adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listaHistorial);
                    spHistorial.setAdapter(adapter);
                }

                if (consulta == "registros") {

                    AdaptadorListaRegistros adaptador = new AdaptadorListaRegistros(getContext(), listaRegistros);
                    rvEntrenos.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    rvEntrenos.setAdapter(adaptador);
                    layoutFiltro.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
            else {
                tvMensaje.setVisibility(View.VISIBLE);
                layoutFiltro.setVisibility(View.GONE);
                rvEntrenos.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(historial){
            final Calendar c = Calendar.getInstance();
            int yyyy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH)+1;

            if(mm != Integer.parseInt(fecha[1])){
                mm = Integer.parseInt(fecha[1]);
            }
            spHistorial.setSelection(obtenerPosicionItem(spHistorial, mes+"- "+fecha[0] ));

            consultarRegistros(mm, yyyy);
            //consultaBD(mm, yyyy);
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
        progress.hide();
    }


}
