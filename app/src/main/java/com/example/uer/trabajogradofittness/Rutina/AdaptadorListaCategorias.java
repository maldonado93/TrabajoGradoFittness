package com.example.uer.trabajogradofittness.Rutina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.Nutricion.InformacionAlimento;
import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorListaCategorias extends RecyclerView.Adapter<AdaptadorListaCategorias.MyViewHolder> {
    Context context;
    List<ListaCategorias> categoria;

    public AdaptadorListaCategorias(Context context, List<ListaCategorias> categoria) {
        this.context = context;
        this.categoria = categoria;
    }

    @NonNull
    @Override
    public AdaptadorListaCategorias.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_categoria_ejercicios,viewGroup,false);
        final AdaptadorListaCategorias.MyViewHolder holder = new AdaptadorListaCategorias.MyViewHolder(v);

        holder.item_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreCategoria = categoria.get(holder.getAdapterPosition()).getCategoria();

                Intent registro = new Intent(context, Ejercicios.class);
                registro.addFlags(registro.FLAG_ACTIVITY_CLEAR_TOP | registro.FLAG_ACTIVITY_SINGLE_TOP);
                registro.putExtra("categoria", nombreCategoria);
                registro.putExtra("idRutina", "");
                registro.putExtra("rutina", "");
                context.startActivity(registro);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaCategorias.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvCategoria.setText(categoria.get(i).getCategoria());
        //myViewHolder.imagen.setImageResource(categoria.get(i).getImagen());
    }

    @Override
    public int getItemCount() {
        return categoria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_categoria;
        private TextView tvCategoria;
        private ImageView imagen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_categoria = (LinearLayout)itemView.findViewById(R.id.item_categoria_ejercicios);
            tvCategoria = (TextView)itemView.findViewById(R.id.tvCategoria);
            //imagen = (ImageView)itemView.findViewById(R.id.ivImagen);

        }
    }
}
