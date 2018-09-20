package com.example.uer.trabajogradofittness.Rutina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.ArrayList;

public class AdaptadorListaEjercicios extends RecyclerView.Adapter<AdaptadorListaEjercicios.MyViewHolder> {

    Context context;
    ArrayList<ListaEjercicios> ejercicios;

    public AdaptadorListaEjercicios(Context context, ArrayList<ListaEjercicios> ejercicios) {
        this.context = context;
        this.ejercicios = ejercicios;
    }

    public void setFilter(ArrayList<ListaEjercicios> lista){
        ejercicios = new ArrayList<>();
        ejercicios.addAll(lista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorListaEjercicios.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_ejercicio,viewGroup,false);
        final AdaptadorListaEjercicios.MyViewHolder holder = new AdaptadorListaEjercicios.MyViewHolder(v);

        holder.item_ejercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idEjercicio = ejercicios.get(holder.getAdapterPosition()).getId();

                Bundle datos = new Bundle();
                datos.putString("idEjercicio", idEjercicio);

                Intent registro = new Intent(context, InformacionEjercicio.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("idEjercicio", idEjercicio);
                context.startActivity(registro);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaEjercicios.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvId.setText(ejercicios.get(i).getId());
        myViewHolder.tvNombre.setText(ejercicios.get(i).getNombre());
        //myViewHolder.ivImagen.setImageBitmap(ejercicios.get(i).getImagen());
    }

    @Override
    public int getItemCount() {
        return ejercicios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout item_ejercicio;
        private TextView tvId;
        private ImageView ivImagen;
        private TextView tvNombre;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_ejercicio = (ConstraintLayout)itemView.findViewById(R.id.item_ejercicio);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            //ivImagen = (ImageView) itemView.findViewById(R.id.ivImagen);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombres);

        }
    }
}
