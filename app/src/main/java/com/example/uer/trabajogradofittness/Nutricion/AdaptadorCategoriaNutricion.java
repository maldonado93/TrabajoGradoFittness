package com.example.uer.trabajogradofittness.Nutricion;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorCategoriaNutricion extends RecyclerView.Adapter<AdaptadorCategoriaNutricion.MyViewHolder>{

    Context context;
    List<CategoriaNutricion> categoria;

    public AdaptadorCategoriaNutricion(Context context, List<CategoriaNutricion> categoria) {
        this.context = context;
        this.categoria = categoria;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_categoria_nutricion,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvCategoria.setText(categoria.get(i).getCategoria());
        //myViewHolder.imagen.setImageResource(categoria.get(i).getImagen());
    }

    @Override
    public int getItemCount() {
        return categoria.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCategoria;
        private ImageView imagen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategoria = (TextView)itemView.findViewById(R.id.tvCategoria);
            //imagen = (ImageView)itemView.findViewById(R.id.ivImagen);

        }
    }


}
