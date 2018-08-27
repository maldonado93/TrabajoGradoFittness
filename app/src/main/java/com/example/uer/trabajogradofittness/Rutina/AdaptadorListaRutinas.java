package com.example.uer.trabajogradofittness.Rutina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorListaRutinas extends RecyclerView.Adapter<AdaptadorListaRutinas.MyViewHolder> {

    Context context;
    List<ListaRutinas> rutinas;

    public AdaptadorListaRutinas(Context context, List<ListaRutinas> rutinas) {
        this.context = context;
        this.rutinas = rutinas;
    }

    @NonNull
    @Override
    public AdaptadorListaRutinas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_rutina,viewGroup,false);
        final AdaptadorListaRutinas.MyViewHolder holder = new AdaptadorListaRutinas.MyViewHolder(v);

        holder.item_rutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idRutina = rutinas.get(holder.getAdapterPosition()).getId();
                String rutina = rutinas.get(holder.getAdapterPosition()).getRutina();

                Intent registro = new Intent(context, Ejercicios.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("categoria", "");
                registro.putExtra("idRutina", idRutina);
                registro.putExtra("rutina", rutina);
                context.startActivity(registro);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaRutinas.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvId.setText(rutinas.get(i).getId());
        myViewHolder.tvRutina.setText(rutinas.get(i).getRutina());
        myViewHolder.tvCategoria.setText(rutinas.get(i).getCategoria());
        myViewHolder.tvCantidad.setText(rutinas.get(i).getCantidad());
    }

    @Override
    public int getItemCount() {
        return rutinas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout item_rutina;
        private TextView tvId;
        private TextView tvRutina;
        private TextView tvCategoria;
        private TextView tvCantidad;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_rutina = (ConstraintLayout)itemView.findViewById(R.id.item_rutina);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            tvRutina = (TextView)itemView.findViewById(R.id.tvRutina);
            tvCategoria = (TextView)itemView.findViewById(R.id.tvCategoria);
            tvCantidad = (TextView)itemView.findViewById(R.id.tvCantidad);

        }
    }
}
