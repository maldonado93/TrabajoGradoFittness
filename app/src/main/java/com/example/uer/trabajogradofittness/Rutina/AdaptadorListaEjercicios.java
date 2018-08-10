package com.example.uer.trabajogradofittness.Rutina;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaEjercicios extends RecyclerView.Adapter<AdaptadorListaEjercicios.MyViewHolder> {

    Context context;
    List<ListaEjercicios> ejercicios;
    List<ListaEjercicios> ejerciciosFiltrados;

    public AdaptadorListaEjercicios(Context context, List<ListaEjercicios> ejercicios) {
        this.context = context;
        this.ejercicios = ejercicios;
        ejerciciosFiltrados = new ArrayList<>(ejercicios);
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

                InformacionEjercicio fragment = new InformacionEjercicio();
                fragment.setArguments(datos);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null).commit();
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

    public Filter getFilter(){
        return ejerciciosFiltro;
    }

    private Filter ejerciciosFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ListaEjercicios> filtroEjercicios = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filtroEjercicios.addAll(ejerciciosFiltrados);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(ListaEjercicios item : ejerciciosFiltrados){
                    if(item.getNombre().toLowerCase().contains(filterPattern)){
                        filtroEjercicios.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtroEjercicios;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ejercicios.clear();
            ejercicios.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_ejercicio;
        private TextView tvId;
        private ImageView ivImagen;
        private TextView tvNombre;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_ejercicio = (LinearLayout)itemView.findViewById(R.id.item_ejercicio);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            //ivImagen = (ImageView) itemView.findViewById(R.id.ivImagen);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombre);

        }
    }
}
