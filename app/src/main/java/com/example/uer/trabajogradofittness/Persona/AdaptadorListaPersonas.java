package com.example.uer.trabajogradofittness.Persona;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class AdaptadorListaPersonas extends RecyclerView.Adapter<AdaptadorListaPersonas.MyViewHolder> {

    Context context;
    List<ListaPersonas> personas;
    List<ListaPersonas> personasFiltrados;

    public AdaptadorListaPersonas(Context context, List<ListaPersonas> personas) {
        this.context = context;
        this.personas = personas;
        personasFiltrados = new ArrayList<>(personas);
    }

    @NonNull
    @Override
    public AdaptadorListaPersonas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_persona,viewGroup,false);
        final AdaptadorListaPersonas.MyViewHolder holder = new AdaptadorListaPersonas.MyViewHolder(v);

        holder.item_persona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int idPersona = Integer.parseInt(personas.get(holder.getAdapterPosition()).getId());

                Intent registro = new Intent(context, InformacionPersona.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("idPersona", idPersona);
                context.startActivity(registro);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaPersonas.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvId.setText(personas.get(i).getId());
        myViewHolder.tvNombre.setText(personas.get(i).getNombre());
        //myViewHolder.ivImagen.setImageBitmap(ejercicios.get(i).getImagen());
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public Filter getFilter(){
        return personasFiltro;
    }

    private Filter personasFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ListaPersonas> filtroPersonas = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filtroPersonas.addAll(personasFiltrados);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(ListaPersonas item : personasFiltrados){
                    if(item.getNombre().toLowerCase().contains(filterPattern)){
                        filtroPersonas.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtroPersonas;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            personas.clear();
            personas.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_persona;
        private TextView tvId;
        private ImageView ivImagen;
        private TextView tvNombre;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_persona = (LinearLayout) itemView.findViewById(R.id.item_persona);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            //ivImagen = (ImageView) itemView.findViewById(R.id.ivImagen);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
        }
    }
}
