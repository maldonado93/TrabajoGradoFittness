package com.example.uer.trabajogradofittness.Persona;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricoPesos extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    ProgressDialog progress;
    ProgressBar progressBar;

    String consulta;
    int indConsulta;

    private LineChart graficaPesos;
    ArrayList<String> registroPesos;

    ConstraintLayout layout_historico;

    TextView tvPeso;
    TextView tvKg;
    TextView tvPesoInicial;
    TextView tvPesoActual;
    TextView tvProgreso;
    EditText etPeso;
    Button btnActualizar;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_historico_pesos, container, false);

        progressBar = v.findViewById(R.id.progressBar);
        layout_historico = v.findViewById(R.id.layout_historico);

        tvPeso = v.findViewById(R.id.tvPeso);
        tvKg = v.findViewById(R.id.tvKg);
        tvPesoInicial = v.findViewById(R.id.tvPesoInicial);
        tvPesoActual = v.findViewById(R.id.tvPesoActual);
        tvProgreso = v.findViewById(R.id.tvProgreso);

        etPeso = v.findViewById(R.id.etPeso);
        btnActualizar = v.findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPeso.getText().toString() != ""){
                    registrarPeso(etPeso.getText().toString());
                }
            }
        });

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        registroPesos = new ArrayList<>();

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");

        consultarPesos();
    }

    private void initGrafica(){

        double pInicial = 0;
        double pActual = 0;
        double diferencia = 0;
        double porcentaje = 0;

        graficaPesos = v.findViewById(R.id.graficaPeso);

        graficaPesos.setDragEnabled(true);
        graficaPesos.setScaleEnabled(false);
        graficaPesos.getDescription().setEnabled(true);
        graficaPesos.getDescription().setText("");
        graficaPesos.setTouchEnabled(true);
        graficaPesos.setPinchZoom(false);

        ArrayList<Entry> valoresy = new ArrayList<>();
        float val = 0;
        int mes;
        String[] res;
        String[] fechas = new String[registroPesos.size()+1];


        for(int i = 0; i < registroPesos.size(); i++){

            res = registroPesos.get(i).split("-");
            val = Float.parseFloat(res[0]);
            mes = Integer.parseInt(res[2]);

            if(i == 0){
                pInicial = (double)val;
                valoresy.add(new Entry(i,val));
                fechas[i] = res[3] +"-"+ obtenerMes(mes-1)+"-"+res[1];
            }
            valoresy.add(new Entry(i+1,val));
            fechas[i+1] = res[3]+"-"+obtenerMes(mes)+"-"+res[1];
        }
        pActual = (double)val;

        diferencia = pActual - pInicial;
        if (diferencia < 0){
            porcentaje = (diferencia / pInicial) * (-100);
        }
        else{
            porcentaje = (diferencia / pInicial) * 100;
        }
        DecimalFormat df = new DecimalFormat("#.00");


        tvPesoInicial.setText((int)pInicial + " kg");
        tvPesoActual.setText((int)pActual + " kg");
        tvProgreso.setText((int)diferencia + " kg (" + df.format(porcentaje) + "%)");


        LineDataSet datos = new LineDataSet(valoresy, "Pesos");
        datos.setFillAlpha(110);
        datos.setValueTextSize(8f);
        datos.setDrawCircles(true);
        datos.setCircleRadius(0.3f);
        datos.setColor(Color.BLUE);

        XAxis xAxis = graficaPesos.getXAxis();
        xAxis.setValueFormatter(new Formato(fechas));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setTextSize(7f);

        YAxis yAxis = graficaPesos.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);
        yAxis.setDrawLabels(false);
        yAxis.setDrawLimitLinesBehindData(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(datos);

        verificarFecha();

        LineData lineData = new LineData(dataSets);
        graficaPesos.setData(lineData);
        graficaPesos.setVisibleXRangeMaximum(4);
        graficaPesos.animateX(1000, Easing.EasingOption.EaseOutSine);
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

    public String obtenerMes(int mes){
        String mm = "";

        switch(mes){
            case 1: mm = "Ene";
                break;
            case 2: mm = "Feb";
                break;
            case 3: mm = "Mar";
                break;
            case 4: mm = "Abr";
                break;
            case 5: mm = "May";
                break;
            case 6: mm = "Jun";
                break;
            case 7: mm = "Jul";
                break;
            case 8: mm = "Ago";
                break;
            case 9: mm = "Sep";
                break;
            case 10: mm = "Oct";
                break;
            case 11: mm = "Nov";
                break;
            case 12: mm = "Dic";
                break;
        }
        return mm;
    }

    private void verificarFecha(){
        final Calendar c = Calendar.getInstance();
        int mm = c.get(Calendar.MONTH)+1;
        int dd = c.get(Calendar.DAY_OF_WEEK)-1;

        String[] datos = registroPesos.get(registroPesos.size()-1).split("-");

        int mes = Integer.parseInt(datos[2]);
        int dia = Integer.parseInt(datos[3]);

        if(dia != dd || (dia == dd && mes != mm)){
            tvPeso.setVisibility(View.VISIBLE);
            tvKg.setVisibility(View.VISIBLE);
            etPeso.setVisibility(View.VISIBLE);
            btnActualizar.setVisibility(View.VISIBLE);
        }
    }

    private void consultarPesos(){
        consulta = "historico_peso";
        indConsulta = 1;

        String url = "http://"+gs.getIp()+"/persona/listar_pesos.php?idPersona="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarPeso(String peso){
        consulta = "historico_peso";
        indConsulta = 2;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(c.getTime());
        progress.show();

        String url = "http://"+gs.getIp()+"/persona/registrar_peso.php?idPersona="+gs.getSesion_usuario()+"&peso="+peso+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray datos = response.optJSONArray(consulta);
        registroPesos = new ArrayList<>();

        try {
            if(consulta == "historico_peso"){
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);
                    registroPesos.add(jsonObject.optString("peso")+ "-"+jsonObject.optString("fecha"));


                    progressBar.setVisibility(View.GONE);
                    layout_historico.setVisibility(View.VISIBLE);
                    tvPeso.setVisibility(View.GONE);
                    tvKg.setVisibility(View.GONE);
                    etPeso.setVisibility(View.GONE);
                    btnActualizar.setVisibility(View.GONE);
                }
                if(indConsulta == 2){
                    progress.hide();
                    etPeso.setText("");
                    Toast.makeText(getContext(), "Peso actualizado!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        initGrafica();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
