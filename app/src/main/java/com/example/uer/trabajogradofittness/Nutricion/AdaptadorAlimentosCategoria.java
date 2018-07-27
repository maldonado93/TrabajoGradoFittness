package com.example.uer.trabajogradofittness.Nutricion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.List;

public class AdaptadorAlimentosCategoria extends RecyclerView.Adapter<AdaptadorAlimentosCategoria.MyViewHolder>{

    Context context;
    List<AlimentosCategoria> alimentos;

    public AdaptadorAlimentosCategoria(Context context, List<AlimentosCategoria> alimentos) {
        this.context = context;
        this.alimentos = alimentos;
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
                /*String nombreCategoria = categoria.get(holder.getAdapterPosition()).getCategoria();
                //Toast.makeText(context,"Categoria: "+nombreCategoria, Toast.LENGTH_SHORT).show();

                Bundle datos = new Bundle();
                datos.putString("categoria", nombreCategoria);

                Alimentos fragment = new Alimentos();
                fragment.setArguments(datos);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("fragment_nutricion").commit();
*/
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvNombre.setText(alimentos.get(i).getNombre());
        myViewHolder.tvCalorias.setText(alimentos.get(i).getValorCalorias());
        myViewHolder.tvProteinas.setText(alimentos.get(i).getValorProteinas());
        myViewHolder.tvCarbohidratos.setText(alimentos.get(i).getValorCarbohidratos());
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_alimento;
        private TextView tvNombre;
        private TextView tvCalorias;
        private TextView tvProteinas;
        private TextView tvCarbohidratos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_alimento = (LinearLayout)itemView.findViewById(R.id.item_alimento);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombre);
            tvCalorias = (TextView)itemView.findViewById(R.id.tvValCalorias);
            tvProteinas = (TextView)itemView.findViewById(R.id.tvValProteinas);
            tvCarbohidratos = (TextView)itemView.findViewById(R.id.tvValCarbohidratos);
        }
    }
}
