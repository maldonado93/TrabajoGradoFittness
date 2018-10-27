package com.example.uer.trabajogradofittness.RegistroEntreno;


import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.androidplot.xy.SimpleXYSeries;
import com.example.uer.trabajogradofittness.Bluetooth.ConnectThread;
import com.example.uer.trabajogradofittness.Bluetooth.DataHandler;
import com.example.uer.trabajogradofittness.Bluetooth.H7ConnectThread;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.Insignias;
import com.example.uer.trabajogradofittness.R;
import com.example.uer.trabajogradofittness.Rutina.AdaptadorListaEjercicioRutina;
import com.example.uer.trabajogradofittness.Rutina.ListaEjercicioRutina;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.google.android.gms.internal.zzahf.runOnUiThread;
import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inicio extends Fragment implements OnItemSelectedListener, Observer, Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    AlertDialog dialogResultado;
    View viewResultado;

    AlertDialog dialogRutina;
    View viewRutina;

    AlertDialog dialogRendimiento;
    View viewRendimiento;

    ImageView ivInsignia;
    TextView tvNivel;
    ProgressBar prPuntos;
    TextView tvPuntos;
    TextView tvTiempo;
    TextView tvPuntosTiempo;
    TextView tvTotal;
    TextView tvRendimiento;
    TextView tvNuevoNivel;

    Cronometro cronometro = null;
    Resultados resultados = null;

    Drawable d;
    TextView tvEntreno;
    TextView tvDispositivos;
    Spinner spDispositivos;
    Button btnRutina;
    TextView tvResultado;
    TextView tvHoras;
    TextView tvMinutos;
    TextView tvSegundos;
    Button btnIniciar;
    TextView tvBpm;

    Button btnEscanear;

    LineChart graficaPulsaciones;

    private String consulta;
    int idRutina;
    String orientacion;
    int idRegistroEntreno;

    int tiempoEstimadoMin;
    int tiempoEstimadoMax;
    int tiempoTotal;

    boolean estadoEntreno;
    int[] tiempo;

    int puntosNivel = 1000;
    boolean subeNivel;
    boolean subeNivelActividad;
    int puntosTiempo;
    int puntosTotal;
    int nivel;
    int puntos;
    int sumaPuntos;

    boolean hiloResultados;
    int puntosTiempoHilo;
    int puntosTotalHilo;
    int puntosHilo;
    int[] datosResultados;

    ArrayList<String> registroPulsaciones;
    ArrayList<String> registroFrecuenciaReposo;
    int indPulsaciones;
    float frecuenciaReposo;
    int contReposo;

    private int MAX_SIZE = 60; //graph max size

    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> pairedDevices = new ArrayList<>();
    boolean bluetooth;
    boolean menuBool = false; //display or not the disconnect option
    boolean h7 = false; //Was the BTLE tested
    boolean normal = false; //Was the BT tested

    private AdaptadorListaEjercicioRutina adaptadorEjercicios;
    private List<ListaEjercicioRutina> listaEjercicios;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_inicio_entreno, container, false);

        registroPulsaciones = new ArrayList<>();
        registroFrecuenciaReposo = new ArrayList<>();

        tvDispositivos = v.findViewById(R.id.tvDispositivos);
        spDispositivos = v.findViewById(R.id.spDispositivos);

        tvHoras = v.findViewById(R.id.tvHoras);
        tvMinutos = v.findViewById(R.id.tvMinutos);
        tvSegundos = v.findViewById(R.id.tvSegundos);

        tvEntreno = v.findViewById(R.id.tvEntreno);
        tvResultado = v.findViewById(R.id.tvResultado);
        btnRutina = v.findViewById(R.id.btnRutina);
        btnIniciar = v.findViewById(R.id.btnIniciar);

        tvBpm = v.findViewById(R.id.tvBpm);

        btnEscanear = v.findViewById(R.id.btnEscanear);

        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarBluetooth();
            }
        });

        btnRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRutina();
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if(estadoEntreno){
                    android.app.AlertDialog.Builder dialogo1 = new android.app.AlertDialog.Builder(getContext());
                    dialogo1.setTitle("");
                    dialogo1.setMessage("¿Desea terminar el entreno?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            d = getResources().getDrawable(R.drawable.ic_play);
                            btnIniciar.setBackgroundDrawable(d);
                            cronometro = null;
                            estadoEntreno = false;
                            spDispositivos.setEnabled(true);
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            dialogo1.cancel();
                        }
                    });
                    dialogo1.show();
                }
                else{
                    tiempoTotal = 2500;
                    dialog_resultados();
                    /*if(mBluetoothAdapter != null){
                        if (mBluetoothAdapter.isEnabled()){
                            if(spDispositivos.getSelectedItemPosition() != 0){
                                if(h7 || normal){
                                    if(idRutina != 0){
                                        d = getResources().getDrawable(R.drawable.ic_stop);
                                        btnIniciar.setBackgroundDrawable(d);
                                        btnEscanear.setVisibility(View.GONE);
                                        spDispositivos.setEnabled(false);
                                        registrarEntreno();
                                    }
                                    else{
                                        Snackbar.make(view, "No tienes alguna rutina para realizar el entreno!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }
                                else{
                                    Snackbar.make(view, "No se ha establecido conexion!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                            else{
                                Snackbar.make(view, "Debe seleccionar un dispositivo!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                        else{
                            btnEscanear.setVisibility(View.VISIBLE);
                            Snackbar.make(view, "Debe encender el bluetooth para realizar el entreno!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    else{
                        verificarBluetooth();
                    }*/
                }
            }
        });

        graficaPulsaciones = v.findViewById(R.id.graficaPeso);

        setupChart();
        setupAxes();
        setupData();
        setLegend();

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        estadoEntreno = false;
        h7 = false;
        normal = false;

        if(gs.getActualizaRendimiento().compareTo("") != 0){
            dialogRendimiento();
        }

        consultarRutina();
    }

    private void calcularTiemposEspera(){
        tiempoEstimadoMax = 0;
        tiempoEstimadoMin = 0;

        String nivelActividad = gs.getNivelActividad();
        String categoria = "";
        String[] series = null;
        int cantSeries = 0;
        int tiempo = 0;

        for(int i = 0; i < listaEjercicios.size(); i++)
        {
            categoria = listaEjercicios.get(i).getCategoria();

            if(categoria.compareTo("Cardio") != 0){
                series = listaEjercicios.get(i).getSeries().split(" * ");
                cantSeries = Integer.parseInt(series[0]);

                if(orientacion.compareTo("Metabólica") == 0){
                    tiempoEstimadoMin += 90 * cantSeries;
                    tiempoEstimadoMax += 120 * cantSeries;
                }
                else{
                    if((nivelActividad.compareTo("Novato") == 0) || (nivelActividad.compareTo("Intermedio") == 0)){

                        if(orientacion.compareTo("Estructural") == 0){
                            tiempoEstimadoMin += 120 * cantSeries;
                            tiempoEstimadoMax += 180 * cantSeries;

                        }
                        if(orientacion.compareTo("Neural") == 0){
                            tiempoEstimadoMin += 180 * cantSeries;
                            tiempoEstimadoMax += 240 * cantSeries;
                        }
                    }
                    else{
                        if(orientacion.compareTo("Estructural") == 0){
                            tiempoEstimadoMin += 120 * cantSeries;
                            tiempoEstimadoMax += 240 * cantSeries;
                        }
                        if(orientacion.compareTo("Neural") == 0){
                            tiempoEstimadoMin += 240 * cantSeries;
                            tiempoEstimadoMax += 360 * cantSeries;
                        }
                    }
                }
            }
            else{
                series = listaEjercicios.get(i).getSeries().split(" ");
                tiempo = Integer.parseInt(series[0]) * 60;

                tiempoEstimadoMin += tiempo;
                tiempoEstimadoMax += tiempo;
            }
        }
    }

    private void obtenerFrecuenciaReposo(){
        frecuenciaReposo = 0;
        for(int i = 0; i<registroFrecuenciaReposo.size();i++){
            frecuenciaReposo += Float.parseFloat(registroFrecuenciaReposo.get(i));
        }
        frecuenciaReposo = frecuenciaReposo / registroFrecuenciaReposo.size();

        calcularFrecEsfuerzo((int)frecuenciaReposo);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void verificarBluetooth(){
        Log.i("Main Activity", "Starting Polar HR monitor main activity");
        DataHandler.getInstance().addObserver(this);

        //Verify if device is to old for BTLE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {

            Log.i("Main Activity", "old device H7 disabled");
            h7 = false;
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter != null){
            if (!mBluetoothAdapter.isEnabled()) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle(R.string.bluetooth)
                        .setMessage(R.string.bluetoothOff)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                            public void onClick(DialogInterface dialog, int which) {
                                mBluetoothAdapter.enable();
                                bluetooth = true;
                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                listarDispositivos();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                bluetooth= false;
                            }
                        })
                        .show();
            } else {
                bluetooth = true;
                listarDispositivos();
            }
        }

        // Create Graph

        if (graficaPulsaciones.getLineData() == null) {
            Number[] series1Numbers = {};
            DataHandler.getInstance().setSeries1(new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Heart Rate"));
        }

        DataHandler.getInstance().setNewValue(false);

    }

    private void setupChart() {
        // disable description text
        graficaPulsaciones.getDescription().setEnabled(false);
        // enable touch gestures
        graficaPulsaciones.setTouchEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        graficaPulsaciones.setPinchZoom(false);
        // enable scaling
        graficaPulsaciones.setScaleEnabled(false);
        graficaPulsaciones.setDrawGridBackground(true);
        // set an alternative background color
        graficaPulsaciones.setBackgroundColor(Color.WHITE);
    }

    private void setupAxes() {

        XAxis xl = graficaPulsaciones.getXAxis();
        xl.setTextColor(Color.DKGRAY);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);

        YAxis leftAxis = graficaPulsaciones.getAxisLeft();
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(50f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = graficaPulsaciones.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setupData() {
        LineData data = new LineData();
        data.setValueTextColor(Color.DKGRAY);

        // Agregar data vacia.
        graficaPulsaciones.setData(data);
    }

    private void setLegend() {
        Legend l = graficaPulsaciones.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextColor(Color.DKGRAY);
    }

    private void calcularFrecEsfuerzo(int frecuenciaReposo){
        int FCmax = 0;
        int FCrep = frecuenciaReposo;
        String nivelActividad = gs.getNivelActividad();
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

        YAxis yAxis = graficaPulsaciones.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(frecuenciaMaxima);
        yAxis.addLimitLine(frecuenciaReservaMin);
        yAxis.addLimitLine(frecuenciaReservaMax);
        yAxis.enableGridDashedLine(10f,10f,0);
        yAxis.setDrawLimitLinesBehindData(true);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Pulsaciones/minuto");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.GREEN);
        set.setDrawCircles(true);
        set.setCircleRadius(0.1f);
        set.setLineWidth(2f);
        // To show values of each point
        set.setDrawValues(true);

        return set;
    }

    private void addEntry(String bpm) {
        LineData data = graficaPulsaciones.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            tvBpm.setText(bpm + " BPM");

            int dato = Integer.parseInt(bpm);

            if(estadoEntreno){
                registroPulsaciones.add(bpm);
                registrarDatosEntreno(dato);
            }

            data.addEntry(new Entry(set.getEntryCount(), dato), 0);

            data.notifyDataChanged();
            graficaPulsaciones.notifyDataSetChanged();
            graficaPulsaciones.setVisibleXRangeMaximum(20);
            graficaPulsaciones.setMaxVisibleValueCount(Integer.parseInt(DataHandler.getInstance().getLastValue())+5);

            graficaPulsaciones.moveViewToX(data.getEntryCount());
            if(contReposo < 10){
                registroFrecuenciaReposo.add(bpm);
                contReposo++;
            }
            else{
                if(contReposo == 10){
                    obtenerFrecuenciaReposo();
                    contReposo++;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onItemSelected(AdapterView<?> arg0, View arg1, int indice,
                               long arg3) {
        normal = false;
        menuBool = false;
        if(bluetooth){
            if (indice != 0) {
                //Actual work
                DataHandler.getInstance().setID(indice);
                if (!h7 && ((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1]).getName().contains("H7") && DataHandler.getInstance().getReader() == null) {

                    Log.i("Main Activity", "Conexion polar");
                    DataHandler.getInstance().setH7(new H7ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], this));
                    h7 = true;
                    spDispositivos.setEnabled(false);
                    contReposo = 0;
                    graficaPulsaciones.setVisibility(View.VISIBLE);
                    tvBpm.setVisibility(View.VISIBLE);
                } else if (!normal && DataHandler.getInstance().getH7() == null) {

                    Log.i("Main Activity", "Conexion normal");
                    DataHandler.getInstance().setReader(new ConnectThread((BluetoothDevice) pairedDevices.toArray()[indice - 1], this));
                    DataHandler.getInstance().getReader().start();
                    contReposo = 0;
                    spDispositivos.setEnabled(false);
                    graficaPulsaciones.setVisibility(View.VISIBLE);
                    tvBpm.setVisibility(View.VISIBLE);
                    normal = true;
                }
                menuBool = true;
            }
            else{
                spDispositivos.setEnabled(true);
                graficaPulsaciones.setVisibility(View.GONE);
                tvBpm.setVisibility(View.GONE);
            }
        }
        else{
            spDispositivos.setEnabled(true);
            verificarBluetooth();
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    /**
     * SE LLAMA CUANDO LA CONEXION BLUETOOTH FALLE
     */
    public void connectionError() {

        Log.w("Main Activity", "Connection error occured");
        if (menuBool) {//did not manually tried to disconnect
            Log.d("Main Activity", "in the app");
            menuBool = false;
            final Inicio ac = this;
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getContext(), getString(R.string.couldnotconnect), Toast.LENGTH_SHORT).show();
                    if (DataHandler.getInstance().getID() < spDispositivos.getCount())
                        spDispositivos.setSelection(DataHandler.getInstance().getID());

                    if (!h7) {

                        Log.w("Main Activity", "Reanudando conexion con polar");
                        DataHandler.getInstance().setReader(null);
                        DataHandler.getInstance().setH7(new H7ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], ac));
                        h7 = true;
                    } else if (!normal) {
                        Log.w("Main Activity", "Reanudando conexion normal");
                        DataHandler.getInstance().setH7(null);
                        DataHandler.getInstance().setReader(new ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], ac));
                        DataHandler.getInstance().getReader().start();
                        normal = true;
                    }
                }
            });
        }
    }

    public void update(Observable observable, Object data) {
        lecturaBpm();
    }

    public void lecturaBpm() {

        runOnUiThread(new Runnable() {
            public void run() {

                addEntry(DataHandler.getInstance().getLastValue());


                /*if (DataHandler.getInstance().getLastIntValue() != 0) {
                    DataHandler.getInstance().getSeries1().addLast(0, DataHandler.getInstance().getLastIntValue());
                    if (DataHandler.getInstance().getSeries1().size() > MAX_SIZE)
                        DataHandler.getInstance().getSeries1().removeFirst();//Previene que la grafica se sobrecargue de datos
                }*/
            }
        });
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        h7 = false;
        normal = false;
        DataHandler.getInstance().deleteObserver(this);
    }

    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void listarDispositivos() {
        Log.d("Inicio", "Listando dispositivos Bluetooth");
        if (bluetooth) {
            //Discover bluetooth devices
            final List<String> list = new ArrayList<>();
            list.add("Seleccione");
            pairedDevices.clear();
            pairedDevices.addAll(mBluetoothAdapter.getBondedDevices());
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    list.add(device.getName() + "\n" + device.getAddress());
                }
            }
            if (!h7) {
                Log.d("Inicio", "Listando dispositivos");
                final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
                    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                        if (!list.contains(device.getName() + "\n" + device.getAddress())) {
                            Log.d("Inicio", "Agregando " + device.getName());
                            list.add(device.getName() + "\n" + device.getAddress());
                            pairedDevices.add(device);
                        }
                    }
                };

                Thread scannerBTLE = new Thread() {
                    public void run() {
                        Log.d("Inicio", "Iniciando escaneo");
                        mBluetoothAdapter.startLeScan(leScanCallback);
                        try {
                            sleep(5000);
                            Log.d("Inicio", "Deteniendo escaneo");
                            mBluetoothAdapter.stopLeScan(leScanCallback);
                        } catch (InterruptedException e) {
                            Log.e("Inicio", "Error de escaneo");
                        }
                    }
                };

                scannerBTLE.start();
            }

            //Populate drop down

            tvDispositivos.setVisibility(View.VISIBLE);
            spDispositivos.setVisibility(View.VISIBLE);

            if(list.size() == 0) {
                btnEscanear.setVisibility(View.VISIBLE);
                graficaPulsaciones.setVisibility(View.GONE);
                tvBpm.setVisibility(View.GONE);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDispositivos.setAdapter(null);
            spDispositivos.setOnItemSelectedListener(this);
            spDispositivos.setAdapter(dataAdapter);

            if (DataHandler.getInstance().getID() != 0 && DataHandler.getInstance().getID() < spDispositivos.getCount())
                spDispositivos.setSelection(DataHandler.getInstance().getID());
        }
    }

    private void dialogRendimiento(){
        AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
        viewRendimiento = getLayoutInflater().inflate(R.layout.dialog_actualiza_rendimiento, null);

        TextView tvCumplido = viewRendimiento.findViewById(R.id.tvCumplido);
        TextView tvIncumplido = viewRendimiento.findViewById(R.id.tvIncumplido);

        if(gs.getActualizaRendimiento().compareTo("cumplido") == 0){
            tvCumplido.setVisibility(View.VISIBLE);
        }
        else{
            tvIncumplido.setVisibility(View.VISIBLE);
        }

        Button btnContinuar = viewRendimiento.findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRendimiento.hide();
            }
        });

        buider.setView(viewRendimiento);
        dialogRendimiento = buider.create();
        dialogRendimiento.show();
    }

    private void dialogRutina(){
        AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
        viewRutina = getLayoutInflater().inflate(R.layout.dialog_ejercicios_rutina, null);

        RecyclerView recyclerEjercicios = viewRutina.findViewById(R.id.rvEjercicios);

        recyclerEjercicios.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerEjercicios.setAdapter(adaptadorEjercicios);

        TextView tvCarga = viewRutina.findViewById(R.id.tvCarga);

        if(gs.isNovato()){
            tvCarga.setText("Auto- Carga");
        }
        else{
            if(orientacion.compareTo("Metabólica") == 0){
                tvCarga.setText("50-60% 1RM con reducción del 15 al 20%");
            }
            else{
                if(orientacion.compareTo("Estructural") == 0){
                    tvCarga.setText("60-70% 1RM con reducción del 15 al 20%");
                }
                else{
                    tvCarga.setText("70-80% 1RM con reducción del 15 al 20%");
                }
            }
        }

        String descanso = "";

        TextView tvDescanso = viewRutina.findViewById(R.id.tvDescanso);
        if(orientacion.compareTo("Metabólica") == 0){
            descanso = "- De 30 a 60 segundos";
        }
        else {
            if (gs.getNivelActividad().compareTo("Novato") == 0 || gs.getNivelActividad().compareTo("Intermedio") == 0) {
                if (orientacion.compareTo("Estructural") == 0) {
                    descanso = "- 1 a 2 minutos";
                }
                else{
                    descanso = "- 2 a 3 minutos";
                }
            }
            else{
                if (orientacion.compareTo("Estructural") == 0) {
                    descanso = "- 1 a 3 minutos";
                }
                else {
                    descanso = "- 3 a 5 minutos";
                }
            }
        }
        tvDescanso.setText(descanso);

        int tiempo;
        int minutos;
        int segundos;
        String t;

        TextView tvTiempoMin = viewRutina.findViewById(R.id.tvTiempoMin);

        tiempo = tiempoEstimadoMin;
        minutos = (int) Math.ceil(tiempo / 60);
        segundos = tiempo - (minutos * 60);

        if(segundos != 0){
            t = "- Mínimo: " + minutos + " min " + segundos + " seg";
        }
        else{
            t ="- Mínimo: " + minutos + " min ";
        }

        tvTiempoMin.setText(t);

        TextView tvTiempoMax = viewRutina.findViewById(R.id.tvTiempoMax);

        tiempo = tiempoEstimadoMax;
        minutos = (int) Math.ceil(tiempo / 60);
        segundos = tiempo - (minutos * 60);

        if(segundos != 0){
            t = "- Máximo: " + minutos + " min " + segundos + " seg";
        }
        else{
            t ="- Máximo: " + minutos + " min ";
        }

        tvTiempoMax.setText(t);

        Button btnAceptar = viewRutina.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRutina.hide();
            }
        });

        buider.setView(viewRutina);
        dialogRutina = buider.create();
        dialogRutina.show();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dialog_resultados(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        viewResultado = getLayoutInflater().inflate(R.layout.dialog_resultado_entreno, null);

        int puntosEntreno = 100;
        nivel = gs.getNivel();
        puntos = gs.getPuntos();

        ivInsignia = viewResultado.findViewById(R.id.ivInsignia);
        tvNivel = viewResultado.findViewById(R.id.tvNivel);
        prPuntos = viewResultado.findViewById(R.id.prPuntos);
        tvPuntos = viewResultado.findViewById(R.id.tvPuntos);
        tvTiempo = viewResultado.findViewById(R.id.tvTiempo);
        tvPuntosTiempo = viewResultado.findViewById(R.id.tvPuntosTiempo);
        tvTotal = viewResultado.findViewById(R.id.tvTotal);
        tvRendimiento = viewResultado.findViewById(R.id.tvRendimiento);
        Button btnContinuar = viewResultado.findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResultado.hide();
            }
        });

        Insignias insignias = new Insignias(getContext(), nivel);
        Drawable insignia = insignias.getInsignia();
        ivInsignia.setBackground(insignia);
        tvNivel.setText(gs.getNivelActividad());

        calcularPuntosEntreno(puntosEntreno);

        if(subeNivel){
            tvNuevoNivel = viewResultado.findViewById(R.id.tvNuevoNivel);
            tvNuevoNivel.setVisibility(View.VISIBLE);
        }

        prPuntos.setProgress(puntos, true);
        tvPuntos.setText(puntos + "/" + puntosNivel);

        builder.setView(viewResultado);
        builder.setCancelable(false);
        dialogResultado = builder.create();
        dialogResultado.show();

        //actualizarEntreno();

        hiloResultados = true;
        resultados = new Resultados();
        resultados.execute();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calcularPuntosEntreno(int puntosEntreno){

        subeNivel = false;
        int tiempo = tiempoTotal;
        int minutos = (int) Math.ceil(tiempo / 60);
        int segundos = tiempo - (minutos * 60);
        tvTiempo.setText(minutos + " min " + segundos + " seg");

        tvPuntosTiempo.setText("+0");
        tvRendimiento.setText((int)(gs.getRendimiento() * 100) + "%");
        tvTotal.setText("+0");

        double calculoTiempo = 1;
        if(tiempo < tiempoEstimadoMin){
            calculoTiempo = tiempo/((double)tiempoEstimadoMin);
        }
        else{
            if(tiempo > tiempoEstimadoMax){
                calculoTiempo = ((double)tiempoEstimadoMax) / tiempo;
            }
        }

        puntosTiempo = (int)(puntosEntreno * calculoTiempo);

        puntosTotal = (int)(puntosTiempo * gs.getRendimiento());

        sumaPuntos = puntos + puntosTotal;

        if(sumaPuntos > puntosNivel){
            subeNivel = true;
            sumaPuntos = sumaPuntos - puntosNivel;
        }
        gs.setPuntos(sumaPuntos);
    }


    private void consultarRutina(){
        consulta = "rutina";

        String url = "http://"+gs.getIp()+"/ejercicio/rutinaProgramada.php?idPersona="+gs.getSesion_usuario();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void rutinaRealizada(){
        consulta = "rutina";

        String url = "http://"+gs.getIp()+"/ejercicio/actualizarRutinaRealizada.php?idRutina="+idRutina;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarEntreno(){

        consulta = "registro_entreno";

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] fecha = sdf.format(c.getTime()).split(" ");

        String url = "http://"+gs.getIp()+"/registro_entrenos/registrar_entreno.php?idRutina="+idRutina+"&idPersona="+gs.getSesion_usuario()+"&hora="+fecha[1]+"&tiempo="+tiempoTotal+"&frecuencia="+frecuenciaReposo+"&fecha="+fecha[0];

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarDatosEntreno(int bpm){
        consulta = "datos_entreno";
        String url = "http://"+gs.getIp()+"/registro_entrenos/registrar_datos_entreno.php?idEntreno="+idRegistroEntreno+"&bpm="+bpm;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);

    }

    private void actualizarEntreno() {
        consulta = "registro_actualizado";
        int promedio = 0;
        for(int i = 0 ;i< registroPulsaciones.size();i++){
            promedio += Integer.parseInt(registroPulsaciones.get(i));
        }

        promedio = (int) promedio / registroPulsaciones.size();

        String url = "http://" + gs.getIp() + "/registro_entrenos/actualizar_entreno.php?idEntreno=" + idRegistroEntreno + "&tiempo=" + tiempoTotal+"&promedio="+promedio+"&puntos="+puntosTotal;

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

    private void actualizarGamificacion(){
        consulta = "gamificacion";
        String url = "http://"+gs.getIp()+"/persona/actualizar_gamificacion.php?idPersona="+gs.getSesion_usuario()+"&nivel="+gs.getNivel()+"&puntos="+gs.getPuntos();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void actualizarCondicionFisica(){
        consulta = "condicion_fisica";
        String url = "http://"+gs.getIp()+"/persona/actualizar_condicion_fisica.php?idPersona="+gs.getSesion_usuario()+"&nivel="+gs.getNivelActividad();

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResponse(JSONObject response) {
        int resultado = 0;

        JSONArray datos = response.optJSONArray(consulta);

        try {
            if(consulta == "rutina"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
                idRutina = jsonObject.optInt("id");

                if(idRutina != 0){
                    orientacion = jsonObject.optString("orientacion");
                    btnRutina.setText(jsonObject.optString("nombre")+ "- "+ jsonObject.optString("orientacion"));
                    tvEntreno.setVisibility(View.VISIBLE);
                    btnRutina.setVisibility(View.VISIBLE);
                    tvResultado.setVisibility(View.GONE);
                }
                else{
                    tvEntreno.setVisibility(View.INVISIBLE);
                    btnRutina.setVisibility(View.INVISIBLE);
                    tvResultado.setVisibility(View.VISIBLE);
                }
            }
            if(consulta == "registro_entreno"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
                idRegistroEntreno = jsonObject.optInt("id");
                if(idRegistroEntreno == 0){
                    Toast.makeText(getContext(), "Error al registrar el entreno", Toast.LENGTH_SHORT).show();
                }
            }
            if(consulta == "datos_entreno"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
                resultado = jsonObject.optInt("id");
                if(resultado == 0){
                    Toast.makeText(getContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }
            if(consulta == "ejercicio"){
                listaEjercicios = new ArrayList<>();
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);

                    String categoria = jsonObject.optString("categoria");
                    if(categoria.compareTo("Cardio") != 0){
                        listaEjercicios.add(new ListaEjercicioRutina(jsonObject.optString("id"),
                                jsonObject.optString("categoria"),
                                jsonObject.optString("nombre"),
                                "Serie/Repeticion",
                                jsonObject.optString("series")+" * "+
                                        jsonObject.optString("repeticiones")));
                    }
                    else{
                        listaEjercicios.add(new ListaEjercicioRutina(jsonObject.optString("id"),
                                jsonObject.optString("categoria"),
                                jsonObject.optString("nombre"),
                                "Tiempo",
                                jsonObject.optString("tiempo")+" min"));
                    }
                }
                adaptadorEjercicios = new AdaptadorListaEjercicioRutina(getContext(), listaEjercicios);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(consulta.compareTo("rutina") == 0 && idRutina != 0)
        {
            listarEjercicios();
        }
        else{
            if(consulta.compareTo("ejercicio") == 0 && idRutina != 0)
            {
                calcularTiemposEspera();
            }
        }
        if(consulta.compareTo("registro_entreno") == 0 && idRegistroEntreno != 0){
            subeNivelActividad = false;
            indPulsaciones = 0;
            estadoEntreno = true;
            cronometro = new Cronometro();
            cronometro.execute();
        }
        else{
            if(consulta.compareTo("registro_actualizado") == 0){
                actualizarGamificacion();
            }
            else{
                if(consulta.compareTo("gamificacion") == 0){
                    if(subeNivelActividad){
                        actualizarCondicionFisica();
                    }
                }
            }
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
                    sleep(1000);
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

        @TargetApi(Build.VERSION_CODES.N)
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if(resultado){
                    dialog_resultados();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cronometro=null;
            estadoEntreno = false;
        }

    }

    private class Resultados extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            puntosTiempoHilo = 0;
            puntosTotalHilo = 0;
            puntosHilo = puntos;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected Boolean doInBackground(Void... voids) {

            try{
                boolean pTiempo = true;
                boolean pTotal = false;
                boolean pNivel = false;
                boolean pPuntos = false;

                sleep(1000);
                while(hiloResultados){
                    sleep(20);
                    if(pTiempo){
                        if(puntosTiempoHilo < puntosTiempo){
                            puntosTiempoHilo++;
                        }
                        else{
                            pTiempo = false;
                            pTotal = true;
                            sleep(500);
                        }
                    }
                    else{
                        if(pTotal){
                            if(puntosTotalHilo < puntosTotal){
                                puntosTotalHilo++;
                            }
                            else{
                                pTotal = false;
                                pPuntos = true;
                                sleep(500);
                            }
                        }
                        else{
                            if(pNivel){
                                nivel++;
                                gs.setNivel(nivel);
                                Insignias insignias = new Insignias(getContext(), nivel);
                                Drawable insignia = insignias.getInsignia();
                                ivInsignia.setBackground(insignia);

                                if(nivel == 10){
                                    subeNivelActividad = true;
                                    tvNivel.setText("Intermedio");
                                    gs.setNivelActividad("Intermedio");
                                }
                                else{
                                    if(nivel == 19){
                                        subeNivelActividad = true;
                                        tvNivel.setText("Avanzado");
                                        gs.setNivelActividad("Avanzado");
                                    }
                                }

                                pNivel = false;
                                pPuntos = true;
                                subeNivel = false;
                                sleep(500);
                            }
                            else{
                                if(pPuntos){
                                    if(subeNivel){
                                        if(puntosHilo < puntosNivel){
                                            puntosHilo++;
                                        }
                                        else{
                                            puntosHilo = 0;
                                            pPuntos = false;
                                            pNivel = true;
                                            sleep(250);
                                        }
                                    }
                                    else{
                                        if(puntosHilo < sumaPuntos){
                                            puntosHilo++;
                                        }
                                        else{
                                            pPuntos = false;
                                            hiloResultados = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    publishProgress(puntosTiempoHilo, puntosTotalHilo, puntosHilo);
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            int puntosTiempo = values[0];
            int puntosTotal = values[1];
            int puntos = values[2];

            tvPuntosTiempo.setText("+" + puntosTiempo);
            tvTotal.setText("+" + puntosTotal);
            prPuntos.setProgress(puntos, true);
            tvPuntos.setText(puntos + "/" + puntosNivel);
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if(resultado){
                resultados = null;
                hiloResultados = false;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            resultados = null;
            hiloResultados = false;
        }
    }
}
