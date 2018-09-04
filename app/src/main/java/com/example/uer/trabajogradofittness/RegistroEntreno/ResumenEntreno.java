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

    private LineChart graficaPulsaciones;
    private PieChart graficaPorcentaje;

    TextView tvTiempo;
    TextView tvActividad;
    TextView tvDescanso;

    String consulta;

    float cantSuperior;
    float cantInferior;
    ArrayList<Integer> registroPulsaciones;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_resumen_entreno, container, false);

        graficaPulsaciones = v.findViewById(R.id.graficaPulsaciones);
        graficaPorcentaje = v.findViewById(R.id.graficaPorcentaje);

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

        consultarDatosEntreno();
    }

    private void initGraficaPulsaciones(){

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

        int entreno = (int)Math.floor((cantSuperior+cantInferior)/60);
        int actividad = (int)Math.floor(cantSuperior/60);
        int descanso = (int)Math.floor(cantInferior/60);

        tvTiempo.setText(entreno + " min");
        tvActividad.setText(actividad + " min");
        tvDescanso.setText(descanso + " min");

        cantSuperior = (cantSuperior/registroPulsaciones.size()*100);
        cantInferior = (cantInferior/registroPulsaciones.size()*100);

        LineDataSet datos = new LineDataSet(valoresy, "Pulsaciones/minuto");
        datos.setFillAlpha(110);
        datos.setDrawCircles(false);
        datos.setColor(Color.GREEN);

        LimitLine lineaPromedio = new LimitLine(prom,"Promedio: "+prom);
        lineaPromedio.setLineColor(Color.CYAN);
        lineaPromedio.setLineWidth(2f);
        lineaPromedio.enableDashedLine(10f, 10f, 0);
        lineaPromedio.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        lineaPromedio.setTextSize(8f);

        XAxis xAxis = graficaPulsaciones.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxis = graficaPulsaciones.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(lineaPromedio);
        yAxis.enableGridDashedLine(10f,10f,0);
        yAxis.setDrawLimitLinesBehindData(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(datos);

        LineData lineData = new LineData(dataSets);
        graficaPulsaciones.setData(lineData);

        graficaPulsaciones.setVisibleXRangeMinimum(60);
        graficaPulsaciones.setVisibleXRangeMaximum(600);
        graficaPulsaciones.animateX(1000, Easing.EasingOption.EaseOutSine);
    }

    private void initGraficaPorcentaje(){
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
            if(consulta == "datos_entreno"){
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);
                    registroPulsaciones.add(jsonObject.optInt("bpm"));
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        initGraficaPulsaciones();
        initGraficaPorcentaje();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
