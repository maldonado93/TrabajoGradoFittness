package com.example.uer.trabajogradofittness.Persona;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorListaRanking extends RecyclerView.Adapter<AdaptadorListaRanking.MyViewHolder>{

    Context context;
    List<ListaRanking> lista;

    public AdaptadorListaRanking(Context context, List<ListaRanking> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdaptadorListaRanking.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_ranking,viewGroup,false);
        final AdaptadorListaRanking.MyViewHolder holder = new AdaptadorListaRanking.MyViewHolder(v);

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaRanking.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvPosicion.setText(String.valueOf(lista.get(i).getPosicion()));
        myViewHolder.tvId.setText(String.valueOf(lista.get(i).getId()));
        myViewHolder.ivImagen.setImageBitmap(lista.get(i).getImagen());
        myViewHolder.tvNombre.setText(lista.get(i).getNombre());
        myViewHolder.ivInsignia.setBackground(lista.get(i).getInsignia());
        myViewHolder.tvNivel.setText(lista.get(i).getNivel());
        myViewHolder.tvPuntos.setText(lista.get(i).getPuntos());
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout item_ranking;
        private TextView tvPosicion;
        private TextView tvId;
        private ImageView ivImagen;
        private TextView tvNombre;
        private ImageView ivInsignia;
        private TextView tvNivel;
        private TextView tvPuntos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_ranking = (ConstraintLayout)itemView.findViewById(R.id.item_ranking);
            tvPosicion = (TextView)itemView.findViewById(R.id.tvPosicion);
            tvId = (TextView)itemView.findViewById(R.id.tvId);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            ivInsignia = itemView.findViewById(R.id.ivInsignia);
            tvNivel = itemView.findViewById(R.id.tvNivel);
            tvPuntos = itemView.findViewById(R.id.tvPuntos);
        }
    }
}
