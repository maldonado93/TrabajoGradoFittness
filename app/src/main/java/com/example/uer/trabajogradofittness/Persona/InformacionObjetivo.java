package com.example.uer.trabajogradofittness.Persona;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

public class InformacionObjetivo extends AppCompatActivity {

    GlobalState gs;

    ImageButton btnRegresar;
    ImageButton btnAdelante;

    RadioButton radioButton;
    RadioGroup rgroupobjetivo;
    RadioButton rbPerder;
    RadioButton rbMantener;
    RadioButton rbGanar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_objetivo);

        gs = (GlobalState)getApplication();

        rbPerder= findViewById(R.id.rbPerder);
        rbMantener= findViewById(R.id.rbMantener);
        rbGanar= findViewById(R.id.rbGanar);

        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar(view);
            }
        });

        btnAdelante = findViewById(R.id.btnSiguiente);
        btnAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String objetivo = "";
                if(rbPerder.isChecked()){
                    objetivo = "Perder";
                }
                if(rbMantener.isChecked()){
                    objetivo = "Mantener";
                }
                if(rbGanar.isChecked()){
                    objetivo = "Ganar";
                }
                if(objetivo.compareTo("") != 0){
                    gs.setObjetivo(objetivo);
                    adelante(view);
                }
                else{
                    Snackbar.make(view, "Seleccione su objetivo!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                }

            }
        });

        if (gs.getObjetivo().compareTo("Perder") == 0){
            rbPerder.setChecked(true);
        }
        if (gs.getObjetivo().compareTo("Mantener") == 0){
            rbMantener.setChecked(true);
        }
        if (gs.getObjetivo().compareTo("Ganar") == 0){
            rbGanar.setChecked(true);
        }
    }
    public void regresar(View view){
        Intent intent = new Intent(this, InformacionPersonal.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }


    public void adelante(View view) {

        Intent intent = new Intent(this, InformacionFisica.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
