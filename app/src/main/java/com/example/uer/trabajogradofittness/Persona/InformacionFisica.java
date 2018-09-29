package com.example.uer.trabajogradofittness.Persona;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

public class InformacionFisica extends AppCompatActivity {

    GlobalState gs;

    ImageButton btnRegresar;
    ImageButton btnAdelante;

    EditText etEstatura;
    EditText etPeso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_fisica);

        gs = (GlobalState)getApplication();

        etEstatura = findViewById(R.id.etEstatura);
        etPeso = findViewById(R.id.etPeso);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar();
            }
        });

        btnAdelante = findViewById(R.id.btnSiguiente);
        btnAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String estatura = etEstatura.getText().toString().trim();
                String peso = etPeso.getText().toString().trim();
                if(estatura.compareTo("") != 0 && peso.compareTo("") != 0){
                    gs.setEstatura(Integer.parseInt(estatura));
                    gs.setPeso(Float.parseFloat(peso));

                    adelante();
                }
                else{
                    Snackbar.make(view, "Complete los campos, por favor!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                }
            }
        });

        if(gs.getEstatura() != 0){
            etEstatura.setText(String.valueOf(gs.getEstatura()));
            etPeso.setText(String.valueOf(gs.getPeso()));
        }
    }

    public void regresar() {
        Intent intent = new Intent(this, InformacionObjetivo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void adelante() {
        Intent intent = new Intent(this, InformacionActividad.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        regresar();
    }
}
