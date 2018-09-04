package com.example.uer.trabajogradofittness.RegistroEntreno;


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
import android.support.design.widget.Snackbar;
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
import com.example.uer.trabajogradofittness.R;
import com.example.uer.trabajogradofittness.Rutina.AdaptadorListaEjercicioRutina;
import com.example.uer.trabajogradofittness.Rutina.ListaEjercicioRutina;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.google.android.gms.internal.zzahf.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inicio extends Fragment implements OnItemSelectedListener, Observer, Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    View v;

    Cronometro cronometro = null;

    Drawable d;
    TextView tvEntreno;
    TextView tvDispositivos;
    Button btnRutina;
    TextView tvResultado;
    TextView tvHoras;
    TextView tvMinutos;
    TextView tvSegundos;
    Button btnIniciar;
    Button btnEscanear;

    LineChart graficaPulsaciones;

    RecyclerView recyclerEjercicios;

    private String consulta;
    int idRutina;
    int idRegistroEntreno;
    int[] tiempo;
    int tiempoTotal;
    boolean estadoEntreno;

    ArrayList<String> registroPulsaciones;
    int indPulsaciones;



    private FirebaseAnalytics mFirebaseAnalytics;
    private int MAX_SIZE = 60; //graph max size
    boolean bluetooth;
    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> pairedDevices = new ArrayList<>();
    boolean menuBool = false; //display or not the disconnect option
    boolean h7 = false; //Was the BTLE tested
    boolean normal = false; //Was the BT tested
    private Spinner spDispositivos;

    private AdaptadorListaEjercicioRutina adaptadorEjercicios;
    private List<ListaEjercicioRutina> listaEjercicios;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_inicio_entreno, container, false);

        estadoEntreno = false;
        registroPulsaciones = new ArrayList<>();

        tvDispositivos = v.findViewById(R.id.tvDispositivos);
        spDispositivos = v.findViewById(R.id.spDispositivos);

        tvHoras = v.findViewById(R.id.tvHoras);
        tvMinutos = v.findViewById(R.id.tvMinutos);
        tvSegundos = v.findViewById(R.id.tvSegundos);

        tvEntreno = v.findViewById(R.id.tvEntreno);
        tvResultado = v.findViewById(R.id.tvResultado);
        btnRutina = v.findViewById(R.id.btnRutina);
        btnIniciar = v.findViewById(R.id.btnIniciar);

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
                    spDispositivos.setEnabled(true);


                    //registrarEntreno();
                }
                else{
                    if(mBluetoothAdapter != null){
                        if (mBluetoothAdapter.isEnabled()){
                            if(spDispositivos.getSelectedItemPosition() != 0){
                                if(idRutina != 0){
                                    d = getResources().getDrawable(R.drawable.ic_stop);
                                    btnIniciar.setBackgroundDrawable(d);
                                    estadoEntreno = true;
                                    graficaPulsaciones.setVisibility(View.VISIBLE);
                                    spDispositivos.setEnabled(false);
                                    registrarEntreno();

                                    cronometro = new Cronometro();
                                    cronometro.execute();
                                }
                                else{
                                    Snackbar.make(view, "No tienes alguna rutina para realizar el entreno!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                            else{
                                Snackbar.make(view, "Debe seleccionar un dispositivo!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                        else{
                            Snackbar.make(view, "Debe encender el bluetooth para realizar el entreno!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    else{
                        verificarBluetooth();
                    }
                }
            }
        });


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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void verificarBluetooth(){
        Log.i("Main Activity", "Starting Polar HR monitor main activity");
        DataHandler.getInstance().addObserver(this);

        AdRequest adRequestBanner = new AdRequest.Builder().build();

        //Verify if device is to old for BTLE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {

            Log.i("Main Activity", "old device H7 disabled");
            h7 = true;
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (DataHandler.getInstance().newValue) {

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
                                        Thread.sleep(2000);
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
                    listarDispositivos();
                }
            }

            // Create Graph
            graficaPulsaciones = v.findViewById(R.id.graficaPulsaciones);

            if (graficaPulsaciones.getLineData() == null) {
                Number[] series1Numbers = {};
                DataHandler.getInstance().setSeries1(new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Heart Rate"));
            }

            DataHandler.getInstance().setNewValue(false);

        } else {
            listarDispositivos();
            graficaPulsaciones = v.findViewById(R.id.graficaPulsaciones);

        }

        setupChart();
        setupAxes();
        setupData();
        setLegend();
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

            TextView rpm = (TextView) v.findViewById(R.id.rpm);
            rpm.setText(bpm);

            int dato = Integer.parseInt(bpm);

            if(estadoEntreno){
                registroPulsaciones.add(bpm);
            }

            data.addEntry(new Entry(set.getEntryCount(), dato), 0);

            data.notifyDataChanged();
            graficaPulsaciones.notifyDataSetChanged();

            graficaPulsaciones.setVisibleXRangeMaximum(15);
            graficaPulsaciones.setMaxVisibleValueCount(Integer.parseInt(DataHandler.getInstance().getLastValue())+5);

            graficaPulsaciones.moveViewToX(data.getEntryCount());
        }
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int indice,
                               long arg3) {
        normal = false;
        menuBool = false;
        if (indice != 0) {
            //Actual work
            DataHandler.getInstance().setID(indice);
            if (!h7 && ((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1]).getName().contains("H7") && DataHandler.getInstance().getReader() == null) {

                Log.i("Main Activity", "Starting h7");
                DataHandler.getInstance().setH7(new H7ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], this));
                h7 = true;
                graficaPulsaciones.setVisibility(View.VISIBLE);
            } else if (!normal && DataHandler.getInstance().getH7() == null) {

                Log.i("Main Activity", "Starting normal");
                DataHandler.getInstance().setReader(new ConnectThread((BluetoothDevice) pairedDevices.toArray()[indice - 1], this));
                DataHandler.getInstance().getReader().start();
                normal = true;
            }
            menuBool = true;
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
                    //TextView rpm = (TextView) findViewById(R.id.rpm);
                    //rpm.setText("0 BMP");
                    if (DataHandler.getInstance().getID() < spDispositivos.getCount())
                        spDispositivos.setSelection(DataHandler.getInstance().getID());

                    if (!h7) {

                        Log.w("Main Activity", "starting H7 after error");
                        DataHandler.getInstance().setReader(null);
                        DataHandler.getInstance().setH7(new H7ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], ac));
                        h7 = true;
                    } else if (!normal) {
                        Log.w("Main Activity", "Starting normal after error");
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
                //menuBool=true;

                addEntry(DataHandler.getInstance().getLastValue());

                if (DataHandler.getInstance().getLastIntValue() != 0) {
                    DataHandler.getInstance().getSeries1().addLast(0, DataHandler.getInstance().getLastIntValue());
                    if (DataHandler.getInstance().getSeries1().size() > MAX_SIZE)
                        DataHandler.getInstance().getSeries1().removeFirst();//Previene que la grafica se sobrecargue de datos

                }
            }
        });
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        DataHandler.getInstance().deleteObserver(this);
    }

    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void listarDispositivos() {
        Log.d("Main Activity", "Listando dispositivos Bluetooth");
        if (bluetooth) {
            //Discover bluetooth devices
            final List<String> list = new ArrayList<>();
            list.add("Seleccione");
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
                Log.d("Main Activity", "Listando dispositivos");
                final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
                    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                        if (!list.contains(device.getName() + "\n" + device.getAddress())) {
                            Log.d("Main Activity", "Agregando " + device.getName());
                            list.add(device.getName() + "\n" + device.getAddress());
                            pairedDevices.add(device);
                        }
                    }
                };


                Thread scannerBTLE = new Thread() {
                    public void run() {
                        Log.d("Main Activity", "Iniciando escaneo");
                        mBluetoothAdapter.startLeScan(leScanCallback);
                        try {
                            Thread.sleep(5000);
                            Log.d("Main Activity", "Deteniendo escaneo");
                            mBluetoothAdapter.stopLeScan(leScanCallback);
                        } catch (InterruptedException e) {
                            Log.e("Main Activity", "Error de escaneo");
                        }
                    }
                };

                scannerBTLE.start();
            }

            //Populate drop down

            tvDispositivos.setVisibility(View.VISIBLE);
            spDispositivos.setVisibility(View.VISIBLE);

            if(list.size() != 0){
                btnEscanear.setVisibility(View.GONE);
            }
            else{
                btnEscanear.setVisibility(View.VISIBLE);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spDispositivos.setOnItemSelectedListener(this);
            spDispositivos.setAdapter(dataAdapter);

            if (DataHandler.getInstance().getID() != 0 && DataHandler.getInstance().getID() < spDispositivos.getCount())
                spDispositivos.setSelection(DataHandler.getInstance().getID());
        }
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

        /*db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        String[] fechaActual = df.format(Calendar.getInstance().getTime()).split(", ");

        values.put(RegistroEntreno.ID_RUTINA, idRutina);
        values.put(RegistroEntreno.ID_PERSONA, gs.getSesion_usuario());
        values.put(RegistroEntreno.FECHA, fechaActual[0]);
        values.put(RegistroEntreno.HORA, fechaActual[1]);

        idRegistroEntreno = db.insert(RegistroEntreno.TABLA_REGISTRO_ENTRENO, RegistroEntreno.ID, values);

        Toast.makeText(getContext(), "Id: "+idRegistroEntreno, Toast.LENGTH_SHORT ).show();

        try {
            conn.BD_backup();

        } catch (IOException e) {
            e.printStackTrace();
        }
        db.close();*/


        consulta = "registro_entreno";
        String url = "http://"+gs.getIp()+"/registro_entrenos/registrar_entreno.php?idRutina="+idRutina+"&idPersona="+gs.getSesion_usuario()+"&tiempo="+tiempoTotal;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void registrarDatosEntreno(int nDato){

        if(nDato < registroPulsaciones.size()){
            Snackbar.make(getView(), "Registrando datos...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            consulta = "datos_entreno";
            String url = "http://"+gs.getIp()+"/registro_entrenos/registrar_datos_entreno.php?idEntreno="+idRegistroEntreno+"&bpm="+registroPulsaciones.get(nDato);

            url = url.replace(" ", "%20");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            request.add(jsonObjectRequest);
        }
        else{
            actualizarTiempoEntreno();
        }



    }

    private void actualizarTiempoEntreno() {
        consulta = "registro_entreno";
        String url = "http://" + gs.getIp() + "/registro_entrenos/actualizar_tiempo.php?idEntreno=" + idRegistroEntreno + "&tiempo=" + tiempoTotal;

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


    @Override
    public void onResume() {
        super.onResume();
    }


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
            if(consulta == "registro_entreno"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
                idRegistroEntreno = jsonObject.optInt("id");
            }
            if(consulta == "datos_entreno"){
                JSONObject jsonObject = null;
                jsonObject = datos.getJSONObject(0);
                resultado = jsonObject.optInt("id");
                if(resultado == 0){
                    Toast.makeText(null, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
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

        if(consulta.compareTo("datos_entreno") == 0){
            indPulsaciones++;
            registrarDatosEntreno(indPulsaciones);
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

                indPulsaciones = 0;
                registrarDatosEntreno(indPulsaciones);
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
