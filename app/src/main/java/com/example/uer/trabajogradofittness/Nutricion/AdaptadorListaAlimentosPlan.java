package com.example.uer.trabajogradofittness.Nutricion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

import java.util.ArrayList;

public class AdaptadorListaAlimentosPlan extends RecyclerView.Adapter<AdaptadorListaAlimentosPlan.MyViewHolder> {

    Context context;
    ArrayList<ListaAlimentosPlan> alimentos;

    GlobalState gs;

    public AdaptadorListaAlimentosPlan(Context context, ArrayList<ListaAlimentosPlan> alimentos) {
        this.context = context;
        this.alimentos = alimentos;
    }

    @NonNull
    @Override
    public AdaptadorListaAlimentosPlan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_alimentos_plan,viewGroup,false);
        final AdaptadorListaAlimentosPlan.MyViewHolder holder = new AdaptadorListaAlimentosPlan.MyViewHolder(v);

        gs = (GlobalState) v.getContext().getApplicationContext();

        holder.etSeleccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                int seleccion = alimentos.get(holder.getAdapterPosition()).getSeleccion();

                if(seleccion == 1){
                    int posicion = holder.getAdapterPosition();

                    int cant = alimentos.get(holder.getAdapterPosition()).getCantidad();

                    holder.cbSeleccion.setChecked(true);

                    int[] cantidades = gs.getCantidad();
                    cantidades[posicion] = cant;
                }
            }
        });

        holder.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                int posicion = holder.getAdapterPosition();

                int[] cantidades = gs.getCantidad();
                int cantPicker = numberPicker.getValue();

                holder.tvCalorias.setText(String.valueOf(alimentos.get(holder.getAdapterPosition()).getCalorias() * cantPicker));
                holder.tvProteinas.setText(String.valueOf(alimentos.get(holder.getAdapterPosition()).getProteinas() * cantPicker));
                holder.tvCarbohidratos.setText(String.valueOf(alimentos.get(holder.getAdapterPosition()).getCarbohidratos() * cantPicker));

                if(holder.cbSeleccion.isChecked()){
                    int cantAux = cantidades[posicion];

                    double[] calorias = gs.getCaloria();
                    double calAux = calorias[posicion];

                    double calPick = alimentos.get(holder.getAdapterPosition()).getCalorias() * cantPicker;

                    if(cantPicker > cantAux){
                        calAux += alimentos.get(holder.getAdapterPosition()).getCalorias() * (cantPicker - cantAux);
                    }
                    else{
                        calAux -= (alimentos.get(holder.getAdapterPosition()).getCalorias() * cantAux);
                        calAux += calPick;
                    }

                    calorias[posicion] = calAux;
                    gs.setCaloria(calorias);
                }

                cantidades[posicion] = cantPicker;
                gs.setCantidad(cantidades);
            }
        });

        holder.cbSeleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int idAlimento = alimentos.get(holder.getAdapterPosition()).getId();
                int seleccion = alimentos.get(holder.getAdapterPosition()).getSeleccion();

                int posicion = holder.getAdapterPosition();

                double[] caloriasMax = gs.getCaloriasMax();

                double[] calorias = gs.getCaloria();
                int[] cantidades = gs.getCantidad();

                double caloria = alimentos.get(holder.getAdapterPosition()).getCalorias() * cantidades[posicion];
                calorias[posicion] = caloria;

                int[] alimentos = gs.getAlimentos();
                String[] acciones = gs.getAccion();

                if (compoundButton.isChecked()) {
                    if(seleccion == 0) {
                        acciones[posicion] = "Agregar";
                    }
                    else{
                        acciones[posicion] = "Actualizar";
                    }

                    double sumCal = 0;
                    for(int i=0;i<calorias.length;i++){
                        sumCal += calorias[i];
                    }

                    if(sumCal <= caloriasMax[gs.getIndPlan()]){
                        alimentos[posicion] = idAlimento;

                    }
                    else{
                        //Toast.makeText(context, "Calorias: " , Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(context, "Calorias: "+ sumCal + " - "+caloriasMax[gs.getIndPlan()], Toast.LENGTH_SHORT).show();

                }
                else {
                    calorias[posicion] = 0;
                    if(seleccion == 0) {
                        for (int i = 0; i < alimentos.length; i++) {
                            if (alimentos[i] == idAlimento) {
                                alimentos[i] = 0;
                            }
                        }
                    }
                    else{
                        acciones[posicion] = "Eliminar";
                    }
                }
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvId.setText(String.valueOf(alimentos.get(i).getId()));
        myViewHolder.etSeleccion.setText(String.valueOf(alimentos.get(i).getSeleccion()));
        myViewHolder.numberPicker.setMinValue(1);
        myViewHolder.numberPicker.setMaxValue(20);
        myViewHolder.numberPicker.setValue(alimentos.get(i).getCantidad());
        myViewHolder.tvNombre.setText(alimentos.get(i).getNombre());
        myViewHolder.tvCategoria.setText(alimentos.get(i).getCategoria());
        myViewHolder.tvCalorias.setText(String.valueOf(alimentos.get(i).getCalorias()));
        myViewHolder.tvProteinas.setText(String.valueOf(alimentos.get(i).getProteinas()));
        myViewHolder.tvCarbohidratos.setText(String.valueOf(alimentos.get(i).getCarbohidratos()));
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout item_alimentos_plan;
        private TextView tvId;
        private EditText etSeleccion;
        private CheckBox cbSeleccion;
        private NumberPicker numberPicker;
        private TextView tvNombre;
        private TextView tvCategoria;
        private TextView tvCalorias;
        private TextView tvProteinas;
        private TextView tvCarbohidratos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_alimentos_plan = itemView.findViewById(R.id.item_alimentos_plan);
            tvId = itemView.findViewById(R.id.tvId);
            etSeleccion = itemView.findViewById(R.id.etSeleccion);
            cbSeleccion = itemView.findViewById(R.id.cbSeleccion);
            numberPicker = itemView.findViewById(R.id.numberPicker);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvCalorias = itemView.findViewById(R.id.tvCalorias);
            tvProteinas = itemView.findViewById(R.id.tvProteinas);
            tvCarbohidratos = itemView.findViewById(R.id.tvCarbohidratos);
        }
    }

}
