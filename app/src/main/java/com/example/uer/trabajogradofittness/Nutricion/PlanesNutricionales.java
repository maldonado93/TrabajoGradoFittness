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
import android.widget.EditText;
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

    String consulta;
    ArrayList<String> planes;
    int idPlanNutricional;
    int[] alimentosPlan;
    int indAlimentosPlan;

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
                consultarAlimentosPlan(btnComida1.getText().toString());
            }
        });

        btnAgregar2 = v.findViewById(R.id.btnAgregar2);
        btnAgregar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarAlimentosPlan(btnComida2.getText().toString());
            }
        });
        btnAgregar3 = v.findViewById(R.id.btnAgregar3);
        btnAgregar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarAlimentosPlan(btnComida3.getText().toString());
            }
        });
        btnAgregar4 = v.findViewById(R.id.btnAgregar4);
        btnAgregar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarAlimentosPlan(btnComida4.getText().toString());
            }
        });
        btnAgregar5 = v.findViewById(R.id.btnAgregar5);
        btnAgregar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarAlimentosPlan(btnComida5.getText().toString());
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

        tvAlimentos1 = v.findViewById(R.id.tvAlimentos1);
        tvAlimentos2 = v.findViewById(R.id.tvAlimentos2);
        tvAlimentos3 = v.findViewById(R.id.tvAlimentos3);
        tvAlimentos4 = v.findViewById(R.id.tvAlimentos4);
        tvAlimentos5 = v.findViewById(R.id.tvAlimentos5);

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        consultarPlanesNutricionales();
    }

    private void mostrarPlanes(){

        if(listaAlimentos1.size() > 0){
            tvCalorias1.setVisibility(View.VISIBLE);
            adaptadorAlimentos1 = new AdaptadorListaAlimentos(getContext(), listaAlimentos1);
            rvComida1.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida1.setAdapter(adaptadorAlimentos1);

        }
        if(listaAlimentos2.size() > 0){
            tvCalorias2.setVisibility(View.VISIBLE);
            adaptadorAlimentos2 = new AdaptadorListaAlimentos(getContext(), listaAlimentos2);
            rvComida2.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida2.setAdapter(adaptadorAlimentos2);
        }
        if(listaAlimentos3.size() > 0){
            tvCalorias3.setVisibility(View.VISIBLE);
            adaptadorAlimentos3 = new AdaptadorListaAlimentos(getContext(), listaAlimentos3);
            rvComida3.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida3.setAdapter(adaptadorAlimentos3);
        }
        if(listaAlimentos4.size() > 0){
            tvCalorias4.setVisibility(View.VISIBLE);
            adaptadorAlimentos4 = new AdaptadorListaAlimentos(getContext(), listaAlimentos4);
            rvComida4.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida4.setAdapter(adaptadorAlimentos4);
        }
        if(listaAlimentos5.size() > 0){
            tvCalorias5.setVisibility(View.VISIBLE);
            adaptadorAlimentos5 = new AdaptadorListaAlimentos(getContext(), listaAlimentos5);
            rvComida5.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvComida5.setAdapter(adaptadorAlimentos5);
        }

    }

    private void agregarAlimentoLista(String id, String comida, String plan, String nombre, String calorias, String proteinas, String carbohidratos){
        if(comida.compareTo("Desayuno") == 0){
            listaAlimentos1.add(new ListaAlimentos(id, plan, nombre, calorias, proteinas, carbohidratos));
        }
        if(comida.compareTo("Media mañana") == 0){
            listaAlimentos2.add(new ListaAlimentos(id, plan, nombre, calorias, proteinas, carbohidratos));
        }
        if(comida.compareTo("Almuerzo") == 0){
            listaAlimentos3.add(new ListaAlimentos(id, plan, nombre, calorias, proteinas, carbohidratos));
        }
        if(comida.compareTo("Media tarde") == 0){
            listaAlimentos4.add(new ListaAlimentos(id, plan, nombre, calorias, proteinas, carbohidratos));
        }
        if(comida.compareTo("Noche") == 0){
            listaAlimentos5.add(new ListaAlimentos(id, plan, nombre, calorias, proteinas, carbohidratos));
        }
    }

    private void mostrarDialog(){
        AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
        View dView = getLayoutInflater().inflate(R.layout.dialog_plan_nutricional, null);

        EditText etCantidad = dView.findViewById(R.id.etCantidad);

        RecyclerView rvAlimentos = dView.findViewById(R.id.rvAlimentos);
        adaptadorAlimentos = new AdaptadorListaAlimentosPlan(getContext(), listaAlimentos);
        rvAlimentos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvAlimentos.setAdapter(adaptadorAlimentos);

        Button btnConfirmar = dView.findViewById(R.id.btnConfirmar);

        btnConfirmar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                indAlimentosPlan = 0;
                alimentosPlan = gs.getAlimentos();
                if(alimentosPlan.length > 0){
                    agregarAlimentosPlan();
                }


            }
        });

        buider.setView(dView);
        AlertDialog dialog = buider.create();
        dialog.show();
    }


    private void agregarAlimentosPlan(){

        consulta = "plan_agregado";

        String url = "http://"+gs.getIp()+"/nutricion/listar_planes_nutricionalesxpersona.php?&plan="+idPlanNutricional+"&idAlimento="+alimentosPlan[indAlimentosPlan];

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarPlanesNutricionales(){
        consulta = "plan_nutricional";

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(c.getTime());

        String url = "http://"+gs.getIp()+"/nutricion/listar_planes_nutricionalesxpersona.php?idPersona="+gs.getSesion_usuario()+"&fecha="+fecha;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void consultarAlimentosPlan(String comida){
        consulta = "plan_propuesto";

        String url = "http://"+gs.getIp()+"/nutricion/listar_plan_propuesto.php?comida="+comida;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray(consulta);
        listaAlimentos = new ArrayList<>();
        listaAlimentos1 = new ArrayList<>();
        listaAlimentos2 = new ArrayList<>();
        listaAlimentos3 = new ArrayList<>();
        listaAlimentos4 = new ArrayList<>();
        listaAlimentos5 = new ArrayList<>();

        if(consulta.compareTo("plan_nutricional") == 0) {
            planes = new ArrayList<>();
        }
        try {
            if(datos.length() > 0){
                for(int i=0; i<datos.length();i++) {
                    JSONObject jsonObject = null;
                    jsonObject = datos.getJSONObject(i);
                    if(consulta.compareTo("plan_nutricional") == 0){
                        idPlanNutricional = jsonObject.optInt("id");
                        if(idPlanNutricional != 0){
                            agregarAlimentoLista(jsonObject.optString("id"),
                                    jsonObject.optString("comida"),
                                    jsonObject.optString("plan"),
                                    jsonObject.optString("nombre"),
                                    jsonObject.optString("calorias"),
                                    jsonObject.optString("proteinas"),
                                    jsonObject.optString("carbohidratos"));
                        }

                    }
                    if(consulta.compareTo("plan_propuesto") == 0) {
                        listaAlimentos.add(new ListaAlimentosPlan(jsonObject.optInt("id"),
                                jsonObject.optString("nombre"),
                                jsonObject.optString("categoria"),
                                jsonObject.optString("calorias"),
                                jsonObject.optString("proteinas"),
                                jsonObject.optString("carbohidratos")));
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(consulta.compareTo("plan_nutricional") == 0) {
            mostrarPlanes();
            /*if(listaAlimentos1.size() == 0 && listaAlimentos2.size() == 0 && listaAlimentos3.size() == 0 && listaAlimentos4.size() == 0 &&
                    listaAlimentos5.size() == 0){
                idPlanNutricional = 0;
            }*/
        }
        if(consulta.compareTo("plan_propuesto") == 0) {

            gs.insAlimentos(datos.length());
            mostrarDialog();
        }
        if(consulta.compareTo("plan_agregado") == 0) {
            indAlimentosPlan++;
            if(indAlimentosPlan <= alimentosPlan.length){
                agregarAlimentosPlan();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
