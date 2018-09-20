package com.example.uer.trabajogradofittness.Nutricion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
        holder.cbSeleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int idAlimento = alimentos.get(holder.getAdapterPosition()).getId();

                int posicion = holder.getAdapterPosition();
                int[] alimentos = gs.getAlimentos();
                if(compoundButton.isChecked()){
                    alimentos[posicion] = idAlimento;
                    Toast.makeText(context, "Posicion: "+posicion+ " id: "+ idAlimento,Toast.LENGTH_LONG).show();
                }
                else{
                    for(int i = 0;i<alimentos.length;i++){
                        if(alimentos[i] == idAlimento){
                            alimentos[i] = 0;
                        }
                    }
                }
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvId.setText(String.valueOf(alimentos.get(i).getId()));
        myViewHolder.etCantidad.setText(alimentos.get(i).getCantidad());
        myViewHolder.tvNombre.setText(alimentos.get(i).getNombre());
        myViewHolder.tvCategoria.setText(alimentos.get(i).getCategoria());
        myViewHolder.tvCalorias.setText(alimentos.get(i).getCalorias());
        myViewHolder.tvProteinas.setText(alimentos.get(i).getProteinas());
        myViewHolder.tvCarbohidratos.setText(alimentos.get(i).getCarbohidratos());
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout item_alimentos_plan;
        private TextView tvId;
        private CheckBox cbSeleccion;
        private EditText etCantidad;
        private TextView tvNombre;
        private TextView tvCategoria;
        private TextView tvCalorias;
        private TextView tvProteinas;
        private TextView tvCarbohidratos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_alimentos_plan = itemView.findViewById(R.id.item_alimentos_plan);
            tvId = itemView.findViewById(R.id.tvId);
            cbSeleccion = itemView.findViewById(R.id.cbSeleccion);
            etCantidad = itemView.findViewById(R.id.etCantidad);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvCalorias = itemView.findViewById(R.id.tvCalorias);
            tvProteinas = itemView.findViewById(R.id.tvProteinas);
            tvCarbohidratos = itemView.findViewById(R.id.tvCarbohidratos);
        }
    }

}
