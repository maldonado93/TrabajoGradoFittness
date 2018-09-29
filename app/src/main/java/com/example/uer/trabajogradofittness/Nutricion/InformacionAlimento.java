package com.example.uer.trabajogradofittness.Nutricion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class InformacionAlimento extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    GlobalState gs;
    private int idAlimento;

    ImageButton btnRegresar;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_alimento);

        gs = (GlobalState) getApplication();

        btnRegresar = (ImageButton) findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvNombreAlimento = (TextView) findViewById(R.id.tvNombreAlimento);
        tvCategoria = (TextView) findViewById(R.id.tvCategoria);
        tvValCalorias = (TextView) findViewById(R.id.tvValCalorias);
        tvValProteinas = (TextView) findViewById(R.id.tvValProteinas);
        tvValCarbohidratos = (TextView) findViewById(R.id.tvValCarbohidratos);
        tvValFibra = (TextView) findViewById(R.id.tvValFibra);
        tvValLipidos = (TextView) findViewById(R.id.tvValLipidos);
        tvValFuente = (TextView) findViewById(R.id.tvValFuente);

        tvValSodio = (TextView) findViewById(R.id.tvValSodio);
        tvValCalcio = (TextView) findViewById(R.id.tvValCalcio);
        tvValHierro = (TextView) findViewById(R.id.tvValHierro);
        tvValFosforo = (TextView) findViewById(R.id.tvValFosforo);
        tvValFluor = (TextView) findViewById(R.id.tvValFluor);
        tvValSelenio = (TextView) findViewById(R.id.tvValSelenio);
        tvValPotasio = (TextView) findViewById(R.id.tvValPotasio);
        tvValMagnesio = (TextView) findViewById(R.id.tvValMagnesio);
        tvValZinc = (TextView) findViewById(R.id.tvValZinc);
        tvValLodo = (TextView) findViewById(R.id.tvValLodo);
        tvValCobre = (TextView) findViewById(R.id.tvValCobre);

        tvValVitA = (TextView) findViewById(R.id.tvValVitA);
        tvValVitE = (TextView) findViewById(R.id.tvValVitE);
        tvValVitB1 = (TextView) findViewById(R.id.tvValVitB1);
        tvValNiacina = (TextView) findViewById(R.id.tvValNiacina);
        tvValVitB12 = (TextView) findViewById(R.id.tvValVitB12);
        tvValVitD = (TextView) findViewById(R.id.tvValVitD);
        tvValVitC = (TextView) findViewById(R.id.tvValVitC);
        tvValVitB2 = (TextView) findViewById(R.id.tvValVitB2);
        tvValVitB6 = (TextView) findViewById(R.id.tvValVitB6);
        tvValAcFolico = (TextView) findViewById(R.id.tvValAcFolico);

        tvValAcgSatu = (TextView) findViewById(R.id.tvValAcgSatu);
        tvValC140 = (TextView) findViewById(R.id.tvValC140);
        tvValC160 = (TextView) findViewById(R.id.tvValC160);
        tvValC180 = (TextView) findViewById(R.id.tvValC180);
        tvValAcgMono = (TextView) findViewById(R.id.tvValAcgMono);
        tvValC161 = (TextView) findViewById(R.id.tvValC161);
        tvValC181 = (TextView) findViewById(R.id.tvValC181);
        tvValAcgPoli = (TextView) findViewById(R.id.tvValAcgPoli);
        tvValC182 = (TextView) findViewById(R.id.tvValC182);
        tvValC183 = (TextView) findViewById(R.id.tvValC183);
        tvValColesterol = (TextView) findViewById(R.id.tvValColesterol);
        tvValEtanol = (TextView) findViewById(R.id.tvValEtanol);

        request = Volley.newRequestQueue(this);



        Bundle datos = this.getIntent().getExtras();
        idAlimento = datos.getInt("idAlimento");

        consultarAlimento(idAlimento);

    }



    private void consultarAlimento(int id){

        String url = "http://"+gs.getIp()+"/nutricion/consultar_alimento.php?id="+id;

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
            if(valor == -2.00){
                val = "TR";
            }
            if(valor == -3.00){
                val = "-";
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
        Toast.makeText(this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
