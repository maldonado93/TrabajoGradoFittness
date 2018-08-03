package com.example.uer.trabajogradofittness.Nutricion;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.uer.trabajogradofittness.Login;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformacionAlimento extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;
    private String idAlimento;

    TextView tvNombreAlimento;
    TextView tvCategoria;
    TextView tvValCalorias;
    TextView tvValProteinas;
    TextView tvValCarbohidratos;
    TextView tvValFibra;
    TextView tvValLipidos;
    TextView tvValFuente;

    TextView tvValSodio;
    TextView tvValCalcio;
    TextView tvValHierro;
    TextView tvValFosforo;
    TextView tvValFluor;
    TextView tvValSelenio;
    TextView tvValPotasio;
    TextView tvValMagnesio;
    TextView tvValZinc;
    TextView tvValLodo;
    TextView tvValCobre;

    TextView tvValVitA;
    TextView tvValVitE;
    TextView tvValVitB1;
    TextView tvValNiacina;
    TextView tvValVitB12;
    TextView tvValVitD;
    TextView tvValVitC;
    TextView tvValVitB2;
    TextView tvValVitB6;
    TextView tvValAcFolico;

    TextView tvValAcgSatu;
    TextView tvValC140;
    TextView tvValC160;
    TextView tvValC180;
    TextView tvValAcgMono;
    TextView tvValC161;
    TextView tvValC181;
    TextView tvValAcgPoli;
    TextView tvValC182;
    TextView tvValC183;
    TextView tvValColesterol;
    TextView tvValEtanol;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_informacion_alimento,container,false);

        tvNombreAlimento = (TextView) v.findViewById(R.id.tvNombreAlimento);
        tvCategoria = (TextView) v.findViewById(R.id.tvCategoria);
        tvValCalorias = (TextView) v.findViewById(R.id.tvValCalorias);
        tvValProteinas = (TextView) v.findViewById(R.id.tvValProteinas);
        tvValCarbohidratos = (TextView) v.findViewById(R.id.tvValCarbohidratos);
        tvValFibra = (TextView) v.findViewById(R.id.tvValFibra);
        tvValLipidos = (TextView) v.findViewById(R.id.tvValLipidos);
        tvValFuente = (TextView) v.findViewById(R.id.tvValFuente);

        tvValSodio = (TextView) v.findViewById(R.id.tvValSodio);
        tvValCalcio = (TextView) v.findViewById(R.id.tvValCalcio);
        tvValHierro = (TextView) v.findViewById(R.id.tvValHierro);
        tvValFosforo = (TextView) v.findViewById(R.id.tvValFosforo);
        tvValFluor = (TextView) v.findViewById(R.id.tvValFluor);
        tvValSelenio = (TextView) v.findViewById(R.id.tvValSelenio);
        tvValPotasio = (TextView) v.findViewById(R.id.tvValPotasio);
        tvValMagnesio = (TextView) v.findViewById(R.id.tvValMagnesio);
        tvValZinc = (TextView) v.findViewById(R.id.tvValZinc);
        tvValLodo = (TextView) v.findViewById(R.id.tvValLodo);
        tvValCobre = (TextView) v.findViewById(R.id.tvValCobre);

        tvValVitA = (TextView) v.findViewById(R.id.tvValVitA);
        tvValVitE = (TextView) v.findViewById(R.id.tvValVitE);
        tvValVitB1 = (TextView) v.findViewById(R.id.tvValVitB1);
        tvValNiacina = (TextView) v.findViewById(R.id.tvValNiacina);
        tvValVitB12 = (TextView) v.findViewById(R.id.tvValVitB12);
        tvValVitD = (TextView) v.findViewById(R.id.tvValVitD);
        tvValVitC = (TextView) v.findViewById(R.id.tvValVitC);
        tvValVitB2 = (TextView) v.findViewById(R.id.tvValVitB2);
        tvValVitB6 = (TextView) v.findViewById(R.id.tvValVitB6);
        tvValAcFolico = (TextView) v.findViewById(R.id.tvValAcFolico);

        tvValAcgSatu = (TextView) v.findViewById(R.id.tvValAcgSatu);
        tvValC140 = (TextView) v.findViewById(R.id.tvValC140);
        tvValC160 = (TextView) v.findViewById(R.id.tvValC160);
        tvValC180 = (TextView) v.findViewById(R.id.tvValC180);
        tvValAcgMono = (TextView) v.findViewById(R.id.tvValAcgMono);
        tvValC161 = (TextView) v.findViewById(R.id.tvValC161);
        tvValC181 = (TextView) v.findViewById(R.id.tvValC181);
        tvValAcgPoli = (TextView) v.findViewById(R.id.tvValAcgPoli);
        tvValC182 = (TextView) v.findViewById(R.id.tvValC182);
        tvValC183 = (TextView) v.findViewById(R.id.tvValC183);
        tvValColesterol = (TextView) v.findViewById(R.id.tvValColesterol);
        tvValEtanol = (TextView) v.findViewById(R.id.tvValEtanol);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        if(getArguments() != null){
            idAlimento= getArguments().getString("idAlimento","");
        }

        request = Volley.newRequestQueue(getActivity().getApplicationContext());

        consultarAlimento(idAlimento);
    }


    private void consultarAlimento(String id){

        String url = "http://"+gs.getIp()+"/proyectoGrado/query_BD/nutricion/consultar_alimento.php?id="+id;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private String verificarValor(String v){
        String val = v;
        if(val.compareTo("-") != 0 && !val.contains("/")){
            double valor = Double.valueOf(v);

            if(valor == -1.00){
                val = "N";
            }
            else{
                if(valor == -2.00){
                    val = "TR";
                }
            }
        }
        return val;
    }


    @Override
    public void onResponse(JSONObject response) {
        JSONArray datos = response.optJSONArray("alimento");
        JSONObject jsonObject = null;

        try {
            jsonObject = datos.getJSONObject(0);

            tvNombreAlimento.setText(jsonObject.optString("nombre"));
            tvCategoria.setText(jsonObject.optString("categoria"));
            tvValFuente.setText(verificarValor(jsonObject.optString("fuente")));
            tvValCalorias.setText(verificarValor(jsonObject.optString("calorias")));
            tvValProteinas.setText(verificarValor(jsonObject.optString("proteinas")));
            tvValCarbohidratos.setText(verificarValor(jsonObject.optString("carbohidratos")));
            tvValFibra.setText(verificarValor(jsonObject.optString("fibra_alimentaria")));
            tvValLipidos.setText(verificarValor(jsonObject.optString("lipidos")));

            tvValAcgSatu.setText(verificarValor(jsonObject.optString("acg_saturados")));
            tvValC140.setText(verificarValor(jsonObject.optString("c14:0")));
            tvValC160.setText(verificarValor(jsonObject.optString("c16:0")));
            tvValC180.setText(verificarValor(jsonObject.optString("c18:0")));
            tvValAcgMono.setText(verificarValor(jsonObject.optString("acg_monoinsaturados")));
            tvValC161.setText(verificarValor(jsonObject.optString("c16:1")));
            tvValC181.setText(verificarValor(jsonObject.optString("c18:1")));
            tvValAcgPoli.setText(verificarValor(jsonObject.optString("acg_poliinsaturados")));
            tvValC182.setText(verificarValor(jsonObject.optString("c18:2")));
            tvValC183.setText(verificarValor(jsonObject.optString("c18:3")));
            tvValColesterol.setText(verificarValor(jsonObject.optString("colesterol")));
            tvValEtanol.setText(verificarValor(jsonObject.optString("etanol")));

            tvValSodio.setText(verificarValor(jsonObject.optString("sodio")));
            tvValPotasio.setText(verificarValor(jsonObject.optString("potasio")));
            tvValCalcio.setText(verificarValor(jsonObject.optString("calcio")));
            tvValMagnesio.setText(verificarValor(jsonObject.optString("magnesio")));
            tvValHierro.setText(verificarValor(jsonObject.optString("hierro")));
            tvValZinc.setText(verificarValor(jsonObject.optString("zinc")));
            tvValFosforo.setText(verificarValor(jsonObject.optString("fosforo")));
            tvValLodo.setText(verificarValor(jsonObject.optString("lodo")));
            tvValFluor.setText(verificarValor(jsonObject.optString("fluor")));
            tvValCobre.setText(verificarValor(jsonObject.optString("cobre")));
            tvValSelenio.setText(verificarValor(jsonObject.optString("selenio")));

            tvValVitA.setText(verificarValor(jsonObject.optString("vitA")));
            tvValVitD.setText(verificarValor(jsonObject.optString("vitD")));
            tvValVitE.setText(verificarValor(jsonObject.optString("vitE")));
            tvValVitC.setText(verificarValor(jsonObject.optString("vitC")));
            tvValVitB1.setText(verificarValor(jsonObject.optString("vitB1")));
            tvValVitB2.setText(verificarValor(jsonObject.optString("vitB2")));
            tvValNiacina.setText(verificarValor(jsonObject.optString("niacina")));
            tvValVitB6.setText(verificarValor(jsonObject.optString("vitB6")));
            tvValVitB12.setText(verificarValor(jsonObject.optString("vitB12")));
            tvValAcFolico.setText(verificarValor(jsonObject.optString("ac_folico")));
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
}
