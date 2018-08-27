package com.example.uer.trabajogradofittness.RegistroEntreno;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumenEntreno extends Fragment {

    private static String TAG = "ResumenEntreno";
    View v;

    private LineChart graficaPulsaciones;
    private PieChart graficaPorcentaje;

    TextView tvTiempo;
    TextView tvActividad;
    TextView tvDescanso;

    float cantSuperior;
    float cantInferior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_resumen_entreno, container, false);

        graficaPulsaciones = v.findViewById(R.id.graficaPulsaciones);
        graficaPorcentaje = v.findViewById(R.id.graficaPorcentaje);

        tvTiempo = v.findViewById(R.id.tvTiempo);
        tvActividad = v.findViewById(R.id.tvActividad);
        tvDescanso = v.findViewById(R.id.tvDescanso);

        initGraficaPulsaciones();
        initGraficaPorcentaje(v);

        return v;
    }

    private void initGraficaPulsaciones(){
        /*grafica.setOnChartGestureListener(ResumenEntreno.this);
        grafica.setOnChartValueSelectedListener(ResumenEntreno.this);*/

        graficaPulsaciones.setDragEnabled(true);
        graficaPulsaciones.setScaleEnabled(true);
        graficaPulsaciones.getDescription().setEnabled(true);
        graficaPulsaciones.getDescription().setText("Frecuencia cardiaca");

        ArrayList<Entry> valoresy = new ArrayList<>();
        int nRegistros = 5000;
        int numero= 170;
        int val = 0;
        int prom = 0;
        int ope = 0;
        cantSuperior = 0;
        cantInferior = 0;

        int[] valores = new int[nRegistros];

        for(int i = 0; i < nRegistros; i++){
            val = (int)(1 + (Math.random() * 2));
            ope = (int)Math.round(Math.random() * 1);
            if(ope == 0){
                numero -= val;
            }
            else{
                numero += val;
            }
            valores[i] = numero;
            valoresy.add(new Entry(i,numero));
            prom += numero;
        }
        prom = (int)(prom/nRegistros);

        for(int j = 0; j< nRegistros; j++){
            if(valores[j] > prom){
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

        cantSuperior = (cantSuperior/nRegistros*100);
        cantInferior = (cantInferior/nRegistros*100);

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

    private void initGraficaPorcentaje(View v){
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


}
