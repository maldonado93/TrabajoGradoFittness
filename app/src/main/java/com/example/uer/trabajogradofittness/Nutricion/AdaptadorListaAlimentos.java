package com.example.uer.trabajogradofittness.Nutricion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaAlimentos extends RecyclerView.Adapter<AdaptadorListaAlimentos.MyViewHolder> implements Filterable{

    Context context;
    ArrayList<ListaAlimentos> alimentos;
    ArrayList<ListaAlimentos> alimentosFiltrados;



    public AdaptadorListaAlimentos(Context context, ArrayList<ListaAlimentos> alimentos) {
        this.context = context;
        this.alimentos = alimentos;
        alimentosFiltrados = new ArrayList<>(alimentos);
    }

    public void setFilter(ArrayList<ListaAlimentos> lista){
        alimentos = new ArrayList<>();
        alimentos.addAll(lista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_alimento,viewGroup,false);
        final MyViewHolder holder = new MyViewHolder(v);

        holder.item_alimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idAlimento = alimentos.get(holder.getAdapterPosition()).getId();

                Intent registro = new Intent(context, InformacionAlimento.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("idAlimento", idAlimento);
                context.startActivity(registro);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvId.setText(alimentos.get(i).getId());
        myViewHolder.tvNombre.setText(alimentos.get(i).getNombre());
        myViewHolder.tvCalorias.setText(alimentos.get(i).getValorCalorias());
        myViewHolder.tvProteinas.setText(alimentos.get(i).getValorProteinas());
        myViewHolder.tvCarbohidratos.setText(alimentos.get(i).getValorCarbohidratos());
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }




    @Override
    public Filter getFilter(){
        return alimentosFiltro;
    }

    private Filter alimentosFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ListaAlimentos> filtroAlimentos = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filtroAlimentos.addAll(alimentosFiltrados);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(ListaAlimentos item : alimentosFiltrados){
                    if(item.getNombre().toLowerCase().contains(filterPattern)){
                        filtroAlimentos.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtroAlimentos;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            alimentos.clear();
            alimentos.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout item_alimento;
        private TextView tvId;
        private TextView tvNombre;
        private TextView tvCalorias;
        private TextView tvProteinas;
        private TextView tvCarbohidratos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_alimento = (ConstraintLayout) itemView.findViewById(R.id.item_alimento);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombre);
            tvCalorias = (TextView)itemView.findViewById(R.id.tvValCalorias);
            tvProteinas = (TextView)itemView.findViewById(R.id.tvValProteinas);
            tvCarbohidratos = (TextView)itemView.findViewById(R.id.tvValCarbohidratos);
        }
    }
}
