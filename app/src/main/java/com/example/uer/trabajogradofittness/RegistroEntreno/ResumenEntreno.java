package com.example.uer.trabajogradofittness.RegistroEntreno;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumenEntreno extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private static String TAG = "ResumenEntreno";
    GlobalState gs;
    View v;

    String consulta;
    int frecuenciaReposo;
    int promedioFrecuencias;
    String orientacionRutina;

    ProgressBar progressBar;

    ConstraintLayout layout_resumen;

    private LineChart graficaPulsaciones;
    private PieChart graficaPorcentaje;

    TextView tvTiempo;
    TextView tvActividad;
    TextView tvDescanso;

    int cantSuperior;
    int cantInferior;
    ArrayList<Integer> registroPulsaciones;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_resumen_entreno, container, false);

        layout_resumen = v.findViewById(R.id.layout_resumen);
        progressBar = v.findViewById(R.id.progressBar);

        tvTiempo = v.findViewById(R.id.tvTiempo);
        tvActividad = v.findViewById(R.id.tvActividad);
        tvDescanso = v.findViewById(R.id.tvDescanso);

        return v;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        registroPulsaciones = new ArrayList<>();

        consultarFrecuencias();
    }

    private void initGraficaPulsaciones(){

        graficaPulsaciones = v.findViewById(R.id.graficaPeso);

        graficaPulsaciones.setDragEnabled(true);
        graficaPulsaciones.setScaleEnabled(true);
        graficaPulsaciones.getDescription().setEnabled(true);
        graficaPulsaciones.getDescription().setText("Frecuencia cardiaca");

        ArrayList<Entry> valoresy = new ArrayList<>();
        int val = 0;
        int prom = 0;
        cantSuperior = 0;
        cantInferior = 0;

        for(int i = 0; i < registroPulsaciones.size(); i++){
            val = registroPulsaciones.get(i);
            valoresy.add(new Entry(i,val));
            prom += val;
        }
        prom = (int)(prom/registroPulsaciones.size());

        for(int j = 0; j< registroPulsaciones.size(); j++){
            if(registroPulsaciones.get(j) > prom){
                cantSuperior++;
            }
            else{
                cantInferior++;
            }
        }

        int tiempoEntreno = registroPulsaciones.size();
        int minutos = (int)Math.ceil(tiempoEntreno / 60);
        int segundos = tiempoEntreno - (minutos * 60);

        String entreno = minutos + " min "+ segundos + " seg";

        int tiempoActividad = cantSuperior;
        minutos = (int)Math.ceil(tiempoActividad / 60);
        segundos = tiempoActividad - (minutos * 60);

        String actividad = minutos + " min "+ segundos + " seg";

        int tiempoDescanso = cantInferior;
        minutos = (int)Math.ceil(tiempoDescanso / 60);
        segundos = tiempoDescanso - (minutos * 60);

        String descanso = minutos + " min "+ segundos + " seg";

        tvTiempo.setText(entreno);
        tvActividad.setText(actividad);
        tvDescanso.setText(descanso);

        cantSuperior = (int)((float)cantSuperior/registroPulsaciones.size()*100);
        cantInferior = (int)((float)cantInferior/registroPulsaciones.size()*100);

        LineDataSet datos = new LineDataSet(valoresy, "Pulsaciones/minuto");
        datos.setFillAlpha(110);
        datos.setDrawCircles(false);
        datos.setColor(Color.GREEN);

        calcularFrecEsfuerzo(prom);

        XAxis xAxis = graficaPulsaciones.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(datos);

        LineData lineData = new LineData(dataSets);
        graficaPulsaciones.setData(lineData);

        graficaPulsaciones.setVisibleXRangeMinimum(60);
        graficaPulsaciones.setVisibleXRangeMaximum(600);
        graficaPulsaciones.animateX(1000, Easing.EasingOption.EaseOutSine);
    }

    private void calcularFrecEsfuerzo(int promedio){
        int FCmax = 0;
        int FCrep = frecuenciaReposo;
        String nivelActividad = gs.getNivelActividad();
        String orientacion = gs.getOrientacion();
        int edad = gs.getEdad();
        float peso = gs.getPeso();
        int CF;

        int FCRMin;
        int FCRMax;

        double FCEsfMin = 0;
        double FCEsfMax = 0;

        if(gs.getFumador().compareTo("Si") == 0){
            CF = 1;
        }
        else{
            CF = 0;
        }
        String genero = gs.getGenero();

        if(genero.compareTo("Femenino") == 0){
            FCmax= (int)Math.round(204.8 - (0.718 * edad) + (0.162 * FCrep) - (0.105 * peso) - (6.2 * CF));
        }
        else{
            FCmax= (int)Math.round(203.9 - (0.812 * edad) + (0.276* FCrep) - (0.084 * peso) - (4.5*CF));
        }

        if((nivelActividad.compareTo("Novato") == 0) || (nivelActividad.compareTo("Intermedio") == 0)){
            if(orientacion.compareTo("Metabólica") == 0){
                FCEsfMin = 0.5;
                FCEsfMax = 0.6;
            }
            if(orientacion.compareTo("Estructural") == 0){
                FCEsfMin = 0.6;
                FCEsfMax = 0.7;
            }
            if(orientacion.compareTo("Neural") == 0){
                FCEsfMin = 0.7;
                FCEsfMax = 0.8;
            }
        }
        else{
            if(orientacion.compareTo("Metabólica") == 0){
                FCEsfMin = 0.5;
                FCEsfMax = 0.6;
            }
            if(orientacion.compareTo("Estructural") == 0){
                FCEsfMin = 0.7;
                FCEsfMax = 0.75;
            }
            if(orientacion.compareTo("Neural") == 0){
                FCEsfMin = 0.8;
                FCEsfMax = 0.9;
            }
        }

        FCRMin = (int)((FCmax - FCrep) * FCEsfMin + FCrep);
        FCRMax = (int)((FCmax - FCrep) * FCEsfMax + FCrep);


        LimitLine frecuenciaMaxima = new LimitLine(FCmax,"Frecuencia máxima: "+FCmax);
        frecuenciaMaxima.setLineColor(Color.RED);
        frecuenciaMaxima.setLineWidth(1.5f);
        frecuenciaMaxima.enableDashedLine(10f, 1.5f, 0);
        frecuenciaMaxima.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        frecuenciaMaxima.setTextSize(8f);

        LimitLine frecuenciaReservaMin = new LimitLine(FCRMin,"Frecuencia reserva minima: "+FCRMin);
        frecuenciaReservaMin.setLineColor(Color.RED);
        frecuenciaReservaMin.setLineWidth(1.5f);
        frecuenciaReservaMin.enableDashedLine(10f, 1.5f, 0);
        frecuenciaReservaMin.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        frecuenciaReservaMin.setTextSize(8f);

        LimitLine frecuenciaReservaMax = new LimitLine(FCRMax,"Frecuencia reserva máxima: "+FCRMax);
        frecuenciaReservaMax.setLineColor(Color.RED);
        frecuenciaReservaMax.setLineWidth(1.5f);
        frecuenciaReservaMax.enableDashedLine(10f, 1.5f, 0);
        frecuenciaReservaMax.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        frecuenciaReservaMax.setTextSize(8f);

        LimitLine lineaPromedio = new LimitLine(promedio,"Promedio: "+promedio);
        lineaPromedio.setLineColor(Color.CYAN);
        lineaPromedio.setLineWidth(2f);
        lineaPromedio.enableDashedLine(10f, 10f, 0);
        lineaPromedio.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        lineaPromedio.setTextSize(8f);

        YAxis yAxis = graficaPulsaciones.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(frecuenciaMaxima);
        yAxis.addLimitLine(frecuenciaReservaMin);
        yAxis.addLimitLine(frecuenciaReservaMax);
        yAxis.addLimitLine(lineaPromedio);
        yAxis.enableGridDashedLine(10f,10f,0);
        yAxis.setDrawLimitLinesBehindData(true);
    }

    private void initGraficaPorcentaje(){
        graficaPorcentaje = v.findViewById(R.id.graficaPorcentaje);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)graficaPorcentaje.getLayoutParams();
        params.setMargins(0,0,0, 0);
        graficaPorcentaje.setLayoutParams(params);

        ArrayList<PieEntry> porcentajes = new ArrayList<>();

        porcentajes.add(new PieEntry(cantSuperior,"Actividad"));
        porcentajes.add(new PieEntry(cantInferior,"Descanso"));

        PieDataSet dataSet = new PieDataSet(porcentajes, "");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        graficaPorcentaje.setUsePercentValues(true);
        graficaPorcentaje.getDescription().setEnabled(false);
        graficaPorcentaje.setDrawEntryLabels(false);
        graficaPorcentaje.setDrawHoleEnabled(true);
        graficaPorcentaje.setMaxAngle(180);
        graficaPorcentaje.setRotationAngle(180);
        graficaPorcentaje.setCenterTextOffset(0, 0);
        graficaPorcentaje.setRotationEnabled(false);

        graficaPorcentaje.setData(data);

        graficaPorcentaje.animateY(1000, Easing.EasingOption.EaseInSine);

        Legend l = graficaPorcentaje.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);


    }

    private void consultarFrecuencias(){
        consulta = "frecuencias";

        String url = "http://"+gs.getIp()+"/registro_entrenos/consultar_frecuencias.php?idRegistro="+gs.getId_registro_entreno();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarDatosEntreno(){
        consulta = "datos_entreno";

        String url = "http://"+gs.getIp()+"/registro_entrenos/listar_datos_entreno.php?idRegistro="+gs.getId_registro_entreno();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        int resultado = 0;

        JSONArray datos = response.optJSONArray(consulta);

        try {
            for(int i=0; i<datos.length();i++) {
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(i);
                if(consulta.compareTo("frecuencias") == 0) {
                    frecuenciaReposo = jsonObject.optInt("frecuencia_reposo");
                    promedioFrecuencias = jsonObject.optInt("promedio_frecuencia");
                }
                if(consulta.compareTo("datos_entreno") == 0) {
                    registroPulsaciones.add(jsonObject.optInt("bpm"));
                }

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(consulta.compareTo("frecuencias") == 0) {
            consultarDatosEntreno();
        }
        else{
            if(consulta.compareTo("datos_entreno") == 0){
                progressBar.setVisibility(View.GONE);
                layout_resumen.setVisibility(View.VISIBLE);
                initGraficaPulsaciones();
                initGraficaPorcentaje();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
