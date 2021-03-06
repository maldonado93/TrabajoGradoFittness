package com.example.uer.trabajogradofittness.Nutricion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.ArrayList;

public class AdaptadorListaAlimentos extends RecyclerView.Adapter<AdaptadorListaAlimentos.MyViewHolder>{

    Context context;
    ArrayList<ListaAlimentos> alimentos;
    int cent;

    public AdaptadorListaAlimentos(Context context, ArrayList<ListaAlimentos> alimentos, int cent) {
        this.context = context;
        this.alimentos = alimentos;
        this.cent = cent;
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

        if(cent == 2){
            holder.tvCantidad.setVisibility(View.VISIBLE);
        }

        /*holder.item_alimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idAlimento = alimentos.get(holder.getAdapterPosition()).getId();

                Intent registro = new Intent(context, InformacionAlimento.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("idAlimento", idAlimento);
                context.startActivity(registro);
            }
        });*/

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvId.setText(String.valueOf(alimentos.get(i).getId()));
        myViewHolder.tvCantidad.setText(alimentos.get(i).getCantidad());
        myViewHolder.tvNombre.setText(alimentos.get(i).getNombre());
        myViewHolder.tvCalorias.setText(String.valueOf(alimentos.get(i).getCalorias()));
        myViewHolder.tvProteinas.setText(String.valueOf(alimentos.get(i).getProteinas()));
        myViewHolder.tvCarbohidratos.setText(String.valueOf(alimentos.get(i).getCarbohidratos()));
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout item_alimento;
        private TextView tvId;
        private TextView tvCantidad;
        private TextView tvNombre;
        private TextView tvCalorias;
        private TextView tvProteinas;
        private TextView tvCarbohidratos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_alimento = (ConstraintLayout) itemView.findViewById(R.id.item_alimento);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvCantidad = (TextView)itemView.findViewById(R.id.tvCantidad);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombre);
            tvCalorias = (TextView)itemView.findViewById(R.id.tvValCalorias);
            tvProteinas = (TextView)itemView.findViewById(R.id.tvValProteinas);
            tvCarbohidratos = (TextView)itemView.findViewById(R.id.tvValCarbohidratos);
        }
    }
}
