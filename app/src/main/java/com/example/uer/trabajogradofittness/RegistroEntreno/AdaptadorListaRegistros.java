package com.example.uer.trabajogradofittness.RegistroEntreno;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorListaRegistros extends RecyclerView.Adapter<AdaptadorListaRegistros.MyViewHolder>{

    Context context;
    List<ListaRegistros> registros;

    public AdaptadorListaRegistros(Context context, List<ListaRegistros> registros) {
        this.context = context;
        this.registros = registros;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_registro_entreno,viewGroup,false);
        final AdaptadorListaRegistros.MyViewHolder holder = new AdaptadorListaRegistros.MyViewHolder(v);

        holder.item_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idRegistro = registros.get(holder.getAdapterPosition()).getIdRegistro();
                String idRutina = registros.get(holder.getAdapterPosition()).getIdRutina();
                String orientacion = registros.get(holder.getAdapterPosition()).getCategoria();

                Intent registro = new Intent(context, DetallesEntreno.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("idRegistro", idRegistro);
                registro.putExtra("idRutina", idRutina);
                registro.putExtra("orientacion", orientacion);
                context.startActivity(registro);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvIdRegistro.setText(registros.get(i).getIdRegistro());
        myViewHolder.tvIdRutina.setText(registros.get(i).getIdRutina());
        myViewHolder.tvRutina.setText(registros.get(i).getRutina());
        myViewHolder.tvCategoria.setText(registros.get(i).getCategoria());
        myViewHolder.tvDia.setText(registros.get(i).getDia());
        myViewHolder.tvFecha.setText(registros.get(i).getFecha());
        myViewHolder.tvHora.setText(registros.get(i).getHora());
        myViewHolder.tvTiempo.setText(registros.get(i).getTiempo());
    }


    @Override
    public int getItemCount() {
        return registros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout item_registro;
        private TextView tvIdRegistro;
        private TextView tvIdRutina;
        private TextView tvRutina;
        private TextView tvCategoria;
        private TextView tvDia;
        private TextView tvFecha;
        private TextView tvHora;
        private TextView tvTiempo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_registro = (ConstraintLayout)itemView.findViewById(R.id.item_registro);
            tvIdRegistro = (TextView)itemView.findViewById(R.id.tvIdRegistro);
            tvIdRutina = (TextView)itemView.findViewById(R.id.tvIdRutina);
            tvRutina = (TextView)itemView.findViewById(R.id.tvRutina);
            tvCategoria = (TextView)itemView.findViewById(R.id.tvCategoria);
            tvDia = (TextView)itemView.findViewById(R.id.tvDia);
            tvFecha = (TextView)itemView.findViewById(R.id.tvFecha);
            tvHora = (TextView)itemView.findViewById(R.id.tvHora);
            tvTiempo = (TextView)itemView.findViewById(R.id.tvTiempo);


        }
    }
}
