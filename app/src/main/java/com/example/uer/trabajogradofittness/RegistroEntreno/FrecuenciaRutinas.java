package com.example.uer.trabajogradofittness.RegistroEntreno;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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
public class FrecuenciaRutinas extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    String consulta;
    int ultimoMes;
    int ultimoyear;
    ArrayList<String> listaHistorial;
    private List<ListaRegistros> listaRegistros;
    int[] promedios;

    TextView tvMensaje;
    LinearLayout layoutHistorial;
    Spinner spHistorial;

    BarChart graficaFrecuencias;

    String[] rutinas;
    ArrayList<BarEntry> columnas;
    BarDataSet barDataSet;
    BarData barData;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_frecuencia_rutinas, container, false);

        layoutHistorial = v.findViewById(R.id.layoutHistorial);
        spHistorial = v.findViewById(R.id.spHistorialEntrenos);

        spHistorial.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {
                        String[] fecha = (spHistorial.getSelectedItem().toString()).split("-");
                        int mes = Integer.parseInt(obtenerMes(fecha[0], 2));
                        int year = Integer.parseInt(fecha[1].trim());
                        generarDatos(mes, year);
                    }

                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        graficaFrecuencias = v.findViewById(R.id.graficaFrecuencias);
        graficaFrecuencias.getDescription().setEnabled(true);
        graficaFrecuencias.getDescription().setText("Promedios de frec. Cardiaca");
        graficaFrecuencias.setTouchEnabled(false);
        graficaFrecuencias.setPinchZoom(false);
        graficaFrecuencias.setScaleEnabled(false);

        generarGrafica(1);

        return v;
    }

    private void generarGrafica(int ind){


        columnas = new ArrayList<>();

        if(ind == 2){
            for(int i = 0;i<promedios.length;i++){
                columnas.add(new BarEntry(i, promedios[i]));
            }

        }
        else{
            columnas.add(new BarEntry(0, 10));
            rutinas = new String[]{"Pecho"};
        }

        barDataSet = new BarDataSet(columnas, "Rutinas");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        barData = new BarData(barDataSet);

        graficaFrecuencias.setData(barData);


        XAxis xAxis = graficaFrecuencias.getXAxis();
        xAxis.setValueFormatter(new Formato(rutinas));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        graficaFrecuencias.setVisibility(View.VISIBLE);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        generarHistorial();
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

    private void generarDatos(int mes, int year){
        Toast.makeText(getContext(),mes+" "+year, Toast.LENGTH_LONG).show();
        boolean existeCategoria;
        String fecha[] = null;
        ArrayList<String> categorias =  new ArrayList<>();
        int[] registros = new int[listaRegistros.size()];
        int[] nEntrenos = null;
        int[] sumaPromedios = null;

        for(int i = 0 ;i<listaRegistros.size(); i++) {
            fecha = listaRegistros.get(i).getFecha().split("-");
            if (Integer.parseInt(fecha[1]) == mes && Integer.parseInt(fecha[2]) == year) {
                registros[i] = 1;
                existeCategoria = false;
                if (i == 0) {
                    categorias.add(listaRegistros.get(i).getCategoria());
                } else {
                    for (int j = 0; j < categorias.size(); j++) {
                        if (categorias.get(j).compareTo(listaRegistros.get(i).getCategoria()) == 0) {
                            existeCategoria = true;
                        }
                    }
                    if (!existeCategoria) {
                        categorias.add(listaRegistros.get(i).getCategoria());
                    }
                }
            }
            else{
                registros[i] = 0;
            }
        }

        nEntrenos = new int[categorias.size()];
        sumaPromedios = new int[categorias.size()];
        promedios = new int[categorias.size()];
        rutinas = new String[categorias.size()];

        for(int i = 0 ;i<categorias.size(); i++) {
            rutinas[i] = categorias.get(i).toString();
            Log.i("Categorias: ", categorias.get(i).toString());
            nEntrenos[i] = 0;
            sumaPromedios[i] = 0;
            promedios[i] = 0;
        }

        for(int i=0 ;i<listaRegistros.size(); i++){
            if(registros[i] == 1) {
                for (int j = 0; j < categorias.size(); j++) {
                    if (listaRegistros.get(i).getCategoria().compareTo(categorias.get(j)) == 0) {
                        nEntrenos[j]++;
                        sumaPromedios[j] += Integer.parseInt(listaRegistros.get(i).getPromedioFrecuencia());
                    }
                }
            }
        }

        for(int i = 0; i<promedios.length; i++){
            promedios[i] = (int)(sumaPromedios[i]/nEntrenos[i]);
            Log.i("Promedios: ", ""+promedios[i]);
        }

        generarGrafica(2);

    }

    private class Formato implements IAxisValueFormatter {
        private String[] valores;
        public Formato(String[] valores){
            this.valores = valores;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return valores[(int)value];
        }
    }

    private void generarHistorial(){
        consulta = "historial";

        String url = "http://"+gs.getIp()+"/registro_entrenos/listar_fechas.php?idPersona="+ gs.getSesion_usuario() ;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarDatos(){
        consulta = "registros";

        String url = "http://" + gs.getIp() + "/registro_entrenos/listar_registros.php?idPersona="+gs.getSesion_usuario();
       url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    public void onResponse(JSONObject response) {

        listaRegistros = new ArrayList<>();
        listaHistorial = new ArrayList<String>();
        String[] fecha = null;
        String mes = "";
        int m = 0;
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
                        ultimoMes = Integer.parseInt(fecha[1]);
                        ultimoyear = Integer.parseInt(fecha[0]);
                        mes = obtenerMes(fecha[1], 1);
                        listaHistorial.add(mes + "- " + fecha[0]);
                        layoutHistorial.setVisibility(View.VISIBLE);
                        historial = true;
                    }

                    if (consulta == "registros") {

                        String idRegistro = jsonObject.optString("id");
                        String idRutina = jsonObject.optString("idRutina");
                        String rutina = jsonObject.optString("rutina");
                        String categoria = jsonObject.optString("categoria");
                        String promedioFrecuencia = jsonObject.optString("promedio_frecuencia");
                        String dia = obtenerDiaSemana(jsonObject.optString("fecha"));
                        String fech = jsonObject.optString("fecha");
                        String[] f = fech.split("-");
                        fech = (f[2] + "-" + f[1] + "-" + f[0]);
                        String hora = jsonObject.optString("hora");

                        String tiempo = jsonObject.optString("tiempo") + " min";

                        listaRegistros.add(new ListaRegistros(idRegistro,
                                idRutina,
                                rutina,
                                categoria,
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
            }
            else {
                tvMensaje.setVisibility(View.VISIBLE);
                layoutHistorial.setVisibility(View.GONE);
               //graficaFrecuencias.setVisibility(View.GONE);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(historial){
            spHistorial.setSelection(obtenerPosicionItem(spHistorial, mes+"- "+fecha[0] ));
            consultarDatos();
        }

        if(consulta.compareTo("registros") == 0){
            generarDatos(ultimoMes, ultimoyear);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }


}
