package com.example.uer.trabajogradofittness.Rutina;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorListaEjercicioRutina extends RecyclerView.Adapter<AdaptadorListaEjercicioRutina.MyViewHolder>{

    Context context;
    List<ListaEjercicioRutina> ejercicio;



    public AdaptadorListaEjercicioRutina(Context context, List<ListaEjercicioRutina> ejercicio) {
        this.context = context;
        this.ejercicio = ejercicio;
    }

    @NonNull
    @Override
    public AdaptadorListaEjercicioRutina.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_ejercicio_rutina,viewGroup,false);
        final AdaptadorListaEjercicioRutina.MyViewHolder holder = new AdaptadorListaEjercicioRutina.MyViewHolder(v);

        holder.item_ejercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idEjercicio = ejercicio.get(holder.getAdapterPosition()).getId();

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaEjercicioRutina.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvId.setText(ejercicio.get(i).getId());
        myViewHolder.tvNombre.setText(ejercicio.get(i).getNombre());
        myViewHolder.tvSerie.setText(ejercicio.get(i).getSeries());
    }

    @Override
    public int getItemCount() {
        return ejercicio.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout item_ejercicio;
        private TextView tvId;
        private TextView tvNombre;
        private TextView tvSerie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_ejercicio = itemView.findViewById(R.id.item_ejercicio_rutina);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombres);
            tvSerie = (TextView)itemView.findViewById(R.id.tvSerie);
        }
    }
}
