package com.example.uer.trabajogradofittness.Nutricion;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanesNutricionales extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;

    AlertDialog dialog;

    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;
    LinearLayout linearLayout5;

    Button btnComida1;
    Button btnComida2;
    Button btnComida3;
    Button btnComida4;
    Button btnComida5;

    Button btnAgregar1;
    Button btnAgregar2;
    Button btnAgregar3;
    Button btnAgregar4;
    Button btnAgregar5;

    TextView tvCalorias1;
    TextView tvCalorias2;
    TextView tvCalorias3;
    TextView tvCalorias4;
    TextView tvCalorias5;

    TextView tvLimiteCal1;
    TextView tvLimiteCal2;
    TextView tvLimiteCal3;
    TextView tvLimiteCal4;
    TextView tvLimiteCal5;

    TextView tvAlimentos1;
    TextView tvAlimentos2;
    TextView tvAlimentos3;
    TextView tvAlimentos4;
    TextView tvAlimentos5;

    RecyclerView rvComida1;
    RecyclerView rvComida2;
    RecyclerView rvComida3;
    RecyclerView rvComida4;
    RecyclerView rvComida5;

    boolean plan1;
    boolean plan2;
    boolean plan3;
    boolean plan4;
    boolean plan5;

    String fecha;
    String consulta;
    String comida;

    ArrayList<ListaAlimentos> plan;
    int idPlanNutricional;
    float maxCalorias = 6000;
    int indAlimentosPlan;
    int seleccion;
    int cantidad;
    double[] auxCalorias = null;
    double[] caloriasPlan;
    int[] alimentosPlan;
    int[] cantidadesPlan;
    String[] accionesPlan;


    private AdaptadorListaAlimentosPlan adaptadorAlimentos;
    private ArrayList<ListaAlimentosPlan> listaAlimentos;

    private AdaptadorListaAlimentos adaptadorAlimentos1;
    private ArrayList<ListaAlimentos> listaAlimentos1;

    private AdaptadorListaAlimentos adaptadorAlimentos2;
    private ArrayList<ListaAlimentos> listaAlimentos2;

    private AdaptadorListaAlimentos adaptadorAlimentos3;
    private ArrayList<ListaAlimentos> listaAlimentos3;

    private AdaptadorListaAlimentos adaptadorAlimentos4;
    private ArrayList<ListaAlimentos> listaAlimentos4;

    private AdaptadorListaAlimentos adaptadorAlimentos5;
    private ArrayList<ListaAlimentos> listaAlimentos5;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_planes_nutricionales, container, false);

        plan1 = false;
        plan2 = false;
        plan3 = false;
        plan4 = false;
        plan5 = false;

        linearLayout1 = v.findViewById(R.id.linear_layout1);
        linearLayout2 = v.findViewById(R.id.linear_layout2);
        linearLayout3 = v.findViewById(R.id.linear_layout3);
        linearLayout4 = v.findViewById(R.id.linear_layout4);
        linearLayout5 = v.findViewById(R.id.linear_layout5);

        btnComida1 = v.findViewById(R.id.btnComida1);
        btnComida1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!plan1){
                    consultarPlanesNutricionales();
                    plan1 = true;
                    linearLayout1.setVisibility(View.VISIBLE);
                }
                else{
                    plan1 = false;
                    linearLayout1.setVisibility(View.GONE);
                }
            }
        });

        btnComida2 = v.findViewById(R.id.btnComida2);
        btnComida2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!plan2){
                    consultarPlanesNutricionales();
                    plan2 = true;
                    linearLayout2.setVisibility(View.VISIBLE);
                }
                else{
                    plan2 = false;
                    linearLayout2.setVisibility(View.GONE);
                }
            }
        });

        btnComida3 = v.findViewById(R.id.btnComida3);
        btnComida3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!plan3){
                    consultarPlanesNutricionales();
                    plan3 = true;
                    linearLayout3.setVisibility(View.VISIBLE);
                }
                else{
                    plan3 = false;
                    linearLayout3.setVisibility(View.GONE);
                }
            }
        });

        btnComida4 = v.findViewById(R.id.btnComida4);
        btnComida4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!plan4){
                    consultarPlanesNutricionales();
                    plan4 = true;
                    linearLayout4.setVisibility(View.VISIBLE);
                }
                else{
                    plan4 = false;
                    linearLayout4.setVisibility(View.GONE);
                }
            }
        });

        btnComida5 = v.findViewById(R.id.btnComida5);
        btnComida5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!plan5){
                    consultarPlanesNutricionales();
                    plan5 = true;
                    linearLayout5.setVisibility(View.VISIBLE);
                }
                else{
                    consultarPlanesNutricionales();
                    plan5 = false;
                    linearLayout5.setVisibility(View.GONE);
                }
            }
        });

        btnAgregar1 = v.findViewById(R.id.btnAgregar1);
        btnAgregar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.setIndPlan(0);
                comida = btnComida1.getText().toString();
                consultarAlimentosPlan();
            }
        });

        btnAgregar2 = v.findViewById(R.id.btnAgregar2);
        btnAgregar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.setIndPlan(1);
                comida = btnComida2.getText().toString();
                consultarAlimentosPlan();
            }
        });
        btnAgregar3 = v.findViewById(R.id.btnAgregar3);
        btnAgregar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.setIndPlan(2);
                comida = btnComida3.getText().toString();
                consultarAlimentosPlan();
            }
        });
        btnAgregar4 = v.findViewById(R.id.btnAgregar4);
        btnAgregar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.setIndPlan(3);
                comida = btnComida4.getText().toString();
                consultarAlimentosPlan();
            }
        });
        btnAgregar5 = v.findViewById(R.id.btnAgregar5);
        btnAgregar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gs.setIndPlan(4);
                comida = btnComida5.getText().toString();
                consultarAlimentosPlan();
            }
        });

        rvComida1 = v.findViewById(R.id.rvComida1);
        rvComida2 = v.findViewById(R.id.rvComida2);
        rvComida3 = v.findViewById(R.id.rvComida3);
        rvComida4 = v.findViewById(R.id.rvComida4);
        rvComida5 = v.findViewById(R.id.rvComida5);

        tvCalorias1 = v.findViewById(R.id.tvCalorias1);
        tvCalorias2 = v.findViewById(R.id.tvCalorias2);
        tvCalorias3 = v.findViewById(R.id.tvCalorias3);
        tvCalorias4 = v.findViewById(R.id.tvCalorias4);
        tvCalorias5 = v.findViewById(R.id.tvCalorias5);

        tvLimiteCal1 = v.findViewById(R.id.tvLimiteCal1);
        tvLimiteCal2 = v.findViewById(R.id.tvLimiteCal2);
        tvLimiteCal3 = v.findViewById(R.id.tvLimiteCal3);
        tvLimiteCal4 = v.findViewById(R.id.tvLimiteCal4);
        tvLimiteCal5 = v.findViewById(R.id.tvLimiteCal5);

        tvAlimentos1 = v.findViewById(R.id.tvAlimentos1);
        tvAlimentos2 = v.findViewById(R.id.tvAlimentos2);
        tvAlimentos3 = v.findViewById(R.id.tvAlimentos3);
        tvAlimentos4 = v.findViewById(R.id.tvAlimentos4);
        tvAlimentos5 = v.findViewById(R.id.tvAlimentos5);

        inicializarCalorias();
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fecha = sdf.format(c.getTime());

        consultarPlanNutricional();
    }

    private void inicializarCalorias(){
        double[] cal = new double[5];

        cal[0] = maxCalorias * 0.25;
        cal[1] = maxCalorias * 0.15;
        cal[2] = maxCalorias * 0.3;
        cal[3] = maxCalorias * 0.2;
        cal[4] = maxCalorias * 0.1;

        tvLimiteCal1.setText("- " + cal[0]);
        tvLimiteCal2.setText("- " + cal[1]);
        tvLimiteCal3.setText("- " + cal[2]);
        tvLimiteCal4.setText("- " + cal[3]);
        tvLimiteCal5.setText("- " + cal[4]);

        gs.setCaloriasMax(cal);
        gs.insCalorias(5);
    }

    private void generarListasPlanes(){

        if(listaAlimentos1.size() > 0){
            tvCalorias1.setText(""+ caloriasPlan[0]);
            tvCalorias1.setVisibility(View.VISIBLE);
            adaptadorAlimentos1 = new AdaptadorListaAlimentos(getContext(), listaAlimentos1, 2);
            rvComida1.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida1.setAdapter(adaptadorAlimentos1);
        }
        if(listaAlimentos2.size() > 0){
            tvCalorias2.setText(""+ caloriasPlan[1]);
            tvCalorias2.setVisibility(View.VISIBLE);
            adaptadorAlimentos2 = new AdaptadorListaAlimentos(getContext(), listaAlimentos2, 2);
            rvComida2.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida2.setAdapter(adaptadorAlimentos2);
        }

        if(listaAlimentos3.size() > 0){
            tvCalorias3.setText(""+ caloriasPlan[2]);
            tvCalorias3.setVisibility(View.VISIBLE);
            adaptadorAlimentos3 = new AdaptadorListaAlimentos(getContext(), listaAlimentos3, 2);
            rvComida3.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida3.setAdapter(adaptadorAlimentos3);
        }

        if(listaAlimentos4.size() > 0){
            tvCalorias4.setText(""+ caloriasPlan[3]);
            tvCalorias4.setVisibility(View.VISIBLE);
            adaptadorAlimentos4 = new AdaptadorListaAlimentos(getContext(), listaAlimentos4, 2);
            rvComida4.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida4.setAdapter(adaptadorAlimentos4);
        }

        if(listaAlimentos5.size() > 0){
            tvCalorias5.setText(""+ caloriasPlan[4]);
            tvCalorias5.setVisibility(View.VISIBLE);
            adaptadorAlimentos5 = new AdaptadorListaAlimentos(getContext(), listaAlimentos5, 2);
            rvComida5.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida5.setAdapter(adaptadorAlimentos5);
        }
    }

    private void agregarAlimentoLista(int id, String comida, int cantidad,String nombre, String calorias, String proteinas, String carbohidratos){
        float cal = Float.parseFloat(calorias) * cantidad;
        float prot = Float.parseFloat(proteinas) * cantidad;
        float carb = Float.parseFloat(carbohidratos) * cantidad;

        if(comida.compareTo("Desayuno") == 0){
            caloriasPlan[0] += Float.parseFloat(calorias) * cantidad;
            listaAlimentos1.add(new ListaAlimentos(id, "Cant: "+cantidad, nombre, cal, prot, carb));
        }
        if(comida.compareTo("Media mañana") == 0){
            caloriasPlan[1] += Float.parseFloat(calorias) * cantidad;
            listaAlimentos2.add(new ListaAlimentos(id, "Cant: "+cantidad, nombre, cal, prot, carb));
        }
        if(comida.compareTo("Almuerzo") == 0){
            caloriasPlan[2] += Float.parseFloat(calorias) * cantidad;
            listaAlimentos3.add(new ListaAlimentos(id, "Cant: "+cantidad, nombre, cal, prot, carb));
        }
        if(comida.compareTo("Media tarde") == 0){
            caloriasPlan[3] += Float.parseFloat(calorias) * cantidad;
            listaAlimentos4.add(new ListaAlimentos(id, "Cant: "+cantidad, nombre, cal, prot, carb));
        }
        if(comida.compareTo("Noche") == 0){
            caloriasPlan[4] += Float.parseFloat(calorias) * cantidad;
            listaAlimentos5.add(new ListaAlimentos(id, "Cant: "+cantidad, nombre, cal, prot, carb));
        }
    }

    private void mostrarDialog(){
        AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
        View dView = getLayoutInflater().inflate(R.layout.dialog_plan_nutricional, null);

        RecyclerView rvAlimentos = dView.findViewById(R.id.rvAlimentos);
        adaptadorAlimentos = new AdaptadorListaAlimentosPlan(getContext(), listaAlimentos);
        rvAlimentos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvAlimentos.setAdapter(adaptadorAlimentos);


        Button btnConfirmar = dView.findViewById(R.id.btnConfirmar);

        btnConfirmar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                double[] calorias = gs.getCaloria();
                double[] maxCal = gs.getCaloriasMax();

                double sumCal = 0;
                for(int i=0;i<calorias.length;i++){
                    sumCal += calorias[i];
                }

                if(sumCal <= maxCal[gs.getIndPlan()]){
                    caloriasPlan[gs.getIndPlan()] = sumCal;

                    indAlimentosPlan = 0;
                    cantidadesPlan = gs.getCantidad();
                    alimentosPlan = gs.getAlimentos();
                    accionesPlan = gs.getAccion();
                    if(idPlanNutricional != 0){
                        verificarAlimentos();
                    }
                    else{
                        registrarPlanNutricional();
                    }
                }
                else{
                    Toast.makeText(view.getContext(), "Calorias: "+ sumCal + " - "+ maxCal[gs.getIndPlan()], Toast.LENGTH_SHORT).show();
                }
            }
        });
        buider.setView(dView);
        dialog = buider.create();
        dialog.show();
    }


    public void verificarAlimentos(){
        if(alimentosPlan.length > 0 && indAlimentosPlan < alimentosPlan.length){
                if( alimentosPlan[indAlimentosPlan] != 0){
                    agregarAlimentosPlan();
                }
                else{
                    indAlimentosPlan++;
                    verificarAlimentos();
                }
        }
        else{
            consultarPlanesNutricionales();
            dialog.hide();
        }
    }

    private void registrarPlanNutricional(){

        consulta = "plan_agregado";

        String url = "http://"+gs.getIp()+"/nutricion/registrar_plan_nutricional.php?idPersona="+gs.getSesion_usuario()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    private void agregarAlimentosPlan(){

        consulta = "alimento_agregado";

        String url = "http://"+gs.getIp()+"/nutricion/registrar_alimento_plan_nutricional.php?idPlan="+idPlanNutricional+"&idAlimento="
                    +alimentosPlan[indAlimentosPlan]+"&cantidad="+cantidadesPlan[indAlimentosPlan]+"&accion="+accionesPlan[indAlimentosPlan];

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarPlanNutricional(){
        consulta = "plan_hoy";

        String url = "http://"+gs.getIp()+"/nutricion/consultar_plan_nutricional.php?idPersona="+gs.getSesion_usuario()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarPlanesNutricionales(){
        consulta = "plan_nutricional";

        String url = "http://"+gs.getIp()+"/nutricion/listar_planes_nutricionalesxpersona.php?idPersona="+gs.getSesion_usuario()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarAlimentosPlan(){
        auxCalorias = caloriasPlan;
        consulta = "plan_propuesto";

        String url = "http://"+gs.getIp()+"/nutricion/listar_plan_propuesto.php?comida="+comida+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void verificarPlan(int id){
        plan = new ArrayList<>();
        seleccion = 0;
        cantidad = 1;

        if(comida.compareTo("Desayuno") == 0){
            for(int j = 0; j<listaAlimentos1.size(); j++){
                if(listaAlimentos1.get(j).getId() == id){
                    plan = listaAlimentos1;
                    seleccion = 1;
                    String[] datos = listaAlimentos1.get(j).getCantidad().split(" ");
                    cantidad = Integer.parseInt(datos[1]);
                }
            }
        }
        else{
            if(comida.compareTo("Media mañana") == 0){
                for(int j = 0; j<listaAlimentos2.size(); j++){
                    if(listaAlimentos2.get(j).getId() == id){
                        plan = listaAlimentos2;
                        seleccion = 1;
                        String[] datos = listaAlimentos2.get(j).getCantidad().split(" ");
                        cantidad = Integer.parseInt(datos[1]);
                    }
                }
            }
            else{
                if(comida.compareTo("Almuerzo") == 0){
                    for(int j = 0; j<listaAlimentos3.size(); j++){
                        if(listaAlimentos3.get(j).getId() == id){
                            plan = listaAlimentos3;
                            seleccion = 1;
                            String[] datos = listaAlimentos3.get(j).getCantidad().split(" ");
                            cantidad = Integer.parseInt(datos[1]);
                        }
                    }
                }
                else{
                    if(comida.compareTo("Media tarde") == 0){
                        for(int j = 0; j<listaAlimentos4.size(); j++){
                            if(listaAlimentos4.get(j).getId() == id){
                                plan = listaAlimentos4;
                                seleccion = 1;
                                String[] datos = listaAlimentos4.get(j).getCantidad().split(" ");
                                cantidad = Integer.parseInt(datos[1]);
                            }
                        }
                    }
                    else {
                        for (int j = 0; j < listaAlimentos5.size(); j++) {
                            if (listaAlimentos5.get(j).getId() == id) {
                                plan = listaAlimentos5;
                                seleccion = 1;
                                String[] datos = listaAlimentos5.get(j).getCantidad().split(" ");
                                cantidad = Integer.parseInt(datos[1]);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray(consulta);


        if(consulta.compareTo("alimento_agregado") != 0) {
            listaAlimentos = new ArrayList<>();
        }
        if(consulta.compareTo("plan_nutricional") == 0) {
            plan = new ArrayList<>();

            listaAlimentos1 = new ArrayList<>();
            listaAlimentos2 = new ArrayList<>();
            listaAlimentos3 = new ArrayList<>();
            listaAlimentos4 = new ArrayList<>();
            listaAlimentos5 = new ArrayList<>();
            gs.insCalorias(5);
            caloriasPlan = gs.getCaloriasPlan();
        }
        try {
            if(datos != null || datos.length() > 1){
                for(int i=1; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);
                    if(consulta.compareTo("plan_hoy") == 0 || consulta.compareTo("plan_agregado") == 0){
                        idPlanNutricional = jsonObject.optInt("id");
                    }
                    if(consulta.compareTo("plan_nutricional") == 0){
                        if(idPlanNutricional != 0){
                            comida = jsonObject.optString("comida");
                            agregarAlimentoLista(jsonObject.optInt("id"),
                                    jsonObject.optString("comida"),
                                    jsonObject.optInt("cantidad"),
                                    jsonObject.optString("nombre"),
                                    jsonObject.optString("calorias"),
                                    jsonObject.optString("proteinas"),
                                    jsonObject.optString("carbohidratos"));
                        }
                    }
                    if(consulta.compareTo("plan_propuesto") == 0) {
                        verificarPlan(jsonObject.optInt("idAlimento"));

                        listaAlimentos.add(new ListaAlimentosPlan(jsonObject.optInt("id"),
                                cantidad,
                                jsonObject.optString("nombre"),
                                jsonObject.optString("categoria"),
                                Float.parseFloat(jsonObject.optString("calorias")),
                                Float.parseFloat(jsonObject.optString("proteinas")),
                                Float.parseFloat(jsonObject.optString("carbohidratos")),
                                seleccion));
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(consulta.compareTo("plan_hoy") == 0 && idPlanNutricional != 0) {
            consultarPlanesNutricionales();
        }
        else{
            if(consulta.compareTo("plan_nutricional") == 0 && datos.length() > 1) {
                generarListasPlanes();
            }
            else{
                if(consulta.compareTo("plan_propuesto") == 0 && datos.length() > 1) {

                    gs.insAlimentos(datos.length()-1);
                    gs.insCaloria(datos.length()-1);
                    gs.insCantidades(listaAlimentos.size());
                    int[] cant = gs.getCantidad();
                    for(int i=0;i<listaAlimentos.size();i++){
                        cant[i] = listaAlimentos.get(i).getCantidad();
                    }
                    gs.setCantidad(cant);
                    gs.insAccion(datos.length()-1);
                    mostrarDialog();
                }
                else{
                    if(consulta.compareTo("plan_agregado") == 0) {
                        verificarAlimentos();
                    }
                    else{
                        if(consulta.compareTo("alimento_agregado") == 0) {
                            indAlimentosPlan++;
                            if(indAlimentosPlan < alimentosPlan.length){
                                verificarAlimentos();
                            }
                            else{
                                consultarPlanesNutricionales();
                                dialog.hide();
                            }
                        }
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
}
