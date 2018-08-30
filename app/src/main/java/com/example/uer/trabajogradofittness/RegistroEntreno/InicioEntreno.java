package com.example.uer.trabajogradofittness.RegistroEntreno;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.uer.trabajogradofittness.Rutina.AdaptadorListaEjercicioRutina;
import com.example.uer.trabajogradofittness.Rutina.ListaEjercicioRutina;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioEntreno extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    Cronometro cronometro = null;

    Drawable d;
    TextView tvEntreno;
    Button btnRutina;
    TextView tvResultado;
    TextView tvHoras;
    TextView tvMinutos;
    TextView tvSegundos;
    Button btnIniciar;

    private LineChart graficaPulsaciones;

    RecyclerView recyclerEjercicios;

    private String consulta;
    int idRutina;
    int[] tiempo;
    boolean estadoEntreno;
    int tiempoTotal;
    double pulsaciones = 210;

    int numero= 170;
    int val = 0;
    int ope = 0;

    private AdaptadorListaEjercicioRutina adaptadorEjercicios;
    private List<ListaEjercicioRutina> listaEjercicios;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_inicio_entreno, container, false);

        estadoEntreno = false;

        tvHoras = v.findViewById(R.id.tvHoras);
        tvMinutos = v.findViewById(R.id.tvMinutos);
        tvSegundos = v.findViewById(R.id.tvSegundos);

        tvEntreno = v.findViewById(R.id.tvEntreno);
        tvResultado = v.findViewById(R.id.tvResultado);
        btnRutina = v.findViewById(R.id.btnRutina);
        btnIniciar = v.findViewById(R.id.btnIniciar);

        btnRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarEjercicios();
            }
        });


        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estadoEntreno){
                    d = getResources().getDrawable(R.drawable.ic_play);
                    btnIniciar.setBackgroundDrawable(d);
                    cronometro = null;
                    estadoEntreno = false;

                    //registrarEntreno();
                }
                else{
                    if(idRutina != 0){
                        d = getResources().getDrawable(R.drawable.ic_stop);
                        btnIniciar.setBackgroundDrawable(d);
                        estadoEntreno = true;
                        graficaPulsaciones.setVisibility(View.VISIBLE);

                        cronometro = new Cronometro();
                        cronometro.execute();
                    }
                    else{
                        Snackbar.make(view, "No puedes iniciar el entreno!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        graficaPulsaciones = v.findViewById(R.id.graficaPulsaciones);

        initGraficaPulsaciones();

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        consultarRutina();
    }

    private void initGraficaPulsaciones(){
        graficaPulsaciones.setDragEnabled(false);
        graficaPulsaciones.setScaleEnabled(false);
        graficaPulsaciones.getDescription().setEnabled(true);
        graficaPulsaciones.getDescription().setText("Frecuencia cardiaca");

        graficaPulsaciones.setTouchEnabled(false);
        graficaPulsaciones.setDrawGridBackground(false);
        graficaPulsaciones.setPinchZoom(false);
        graficaPulsaciones.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        graficaPulsaciones.setData(data);

        Legend l = graficaPulsaciones.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xAxis = graficaPulsaciones.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        YAxis yAxis = graficaPulsaciones.getAxisLeft();
        yAxis.setEnabled(false);

        graficaPulsaciones.getAxisLeft().setDrawGridLines(false);
        graficaPulsaciones.getXAxis().setDrawGridLines(false);
        graficaPulsaciones.setDrawBorders(false);

        graficaPulsaciones.setVisibility(View.INVISIBLE);
    }


    private void dialogRutina(){
            AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
            View dView = getLayoutInflater().inflate(R.layout.dialog_ejercicios_rutina, null);

            recyclerEjercicios = dView.findViewById(R.id.rvEjercicios);

            buider.setView(dView);
            AlertDialog dialog = buider.create();
            dialog.show();
    }

    private void consultarRutina(){
        consulta = "rutina";

        String url = "http://"+gs.getIp()+"/ejercicio/rutinaProgramada.php?idPersona="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarEntreno(){
        consulta = "registro";
        String url = "http://"+gs.getIp()+"/registro_entrenos/registrar_entreno.php?idRutina=2&idPersona="+gs.getSesion_usuario()+"&tiempo="+tiempoTotal+"&pulsaciones="+pulsaciones;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void listarEjercicios(){
        consulta = "ejercicio";
        String url = "http://"+gs.getIp()+"/ejercicio/listar_ejerciciosxrutina.php?idRutina="+idRutina;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "Datos dinamicos");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setDrawValues(true);
        set.setDrawCircles(false);
        set.setColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray datos = response.optJSONArray(consulta);

        try {

            if(consulta == "rutina"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
                idRutina = jsonObject.optInt("id");

                if(idRutina != 0){
                    btnRutina.setText(jsonObject.optString("nombre")+ "- "+ jsonObject.optString("categoria"));
                    tvEntreno.setVisibility(View.VISIBLE);
                    btnRutina.setVisibility(View.VISIBLE);
                    tvResultado.setVisibility(View.GONE);
                }
                else{
                    tvEntreno.setVisibility(View.GONE);
                    btnRutina.setVisibility(View.GONE);
                    tvResultado.setVisibility(View.VISIBLE);
                }
            }
            if(consulta == "registro"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
            }
            if(consulta == "ejercicio"){
                listaEjercicios = new ArrayList<>();
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);
                    listaEjercicios.add(new ListaEjercicioRutina(jsonObject.optString("id"),
                                                                 jsonObject.optString("nombre"),
                                                                "4*12"));
                }

                adaptadorEjercicios = new AdaptadorListaEjercicioRutina(getContext(), listaEjercicios);
                dialogRutina();

                recyclerEjercicios.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                recyclerEjercicios.setAdapter(adaptadorEjercicios);


            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }


    private class Cronometro extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvHoras.setText("00");
            tvMinutos.setText("00");
            tvSegundos.setText("00");

            tiempo = new int[3];
            tiempo[0] = 0;
            tiempo[1] = 0;
            tiempo[2] = 0;

            tiempoTotal = 0;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try{
                while(estadoEntreno){

                    Thread.sleep(1000);
                    tiempoTotal++;
                    if(tiempo[2] < 59){
                        tiempo[2]++;
                    }
                    else{
                        tiempo[2] = 0;
                        if(tiempo[1] < 59){
                            tiempo[1]++;
                        }else{
                            tiempo[1] = 0;
                            tiempo[0]++;
                        }
                    }

                    /*LineData data = graficaPulsaciones.getData();

                    if(data != null){
                        ILineDataSet set = data.getDataSetByIndex(0);

                        if(set == null){
                            set = createSet();
                            data.addDataSet(set);
                        }
                        val = (int)(1 + (Math.random() * 2));
                        ope = (int)Math.round(Math.random() * 1);
                        if(ope == 0){
                            numero -= val;
                        }
                        else{
                            numero += val;
                        }

                        data.addEntry(new Entry(set.getEntryCount(), numero),0);
                        data.notifyDataChanged();
                        graficaPulsaciones.setMaxVisibleValueCount(250);
                        graficaPulsaciones.moveViewToX(data.getEntryCount());
                    }*/
                    publishProgress(tiempo[0], tiempo[1], tiempo[2]);
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            return true;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            String hora = String.valueOf(values[0]);
            String minutos = String.valueOf(values[1]);
            String segundos = String.valueOf(values[2]);
            if(values[0]< 10){
                hora = "0"+values[0];
            }
            if(values[1]< 10){
                minutos = "0"+values[1];
            }
            if(values[2]< 10){
                segundos = "0"+values[2];
            }
            tvHoras.setText(hora);
            tvMinutos.setText(minutos);
            tvSegundos.setText(segundos);
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            //super.onPostExecute(resultado);
            if(resultado){
                Snackbar.make(getView(), "Registrando datos...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cronometro=null;
            estadoEntreno = false;
        }

    }

}
