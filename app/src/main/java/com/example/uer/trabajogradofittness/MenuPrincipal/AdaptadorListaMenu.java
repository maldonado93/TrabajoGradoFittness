package com.example.uer.trabajogradofittness.MenuPrincipal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uer.trabajogradofittness.R;

import java.util.List;
import java.util.Map;

public class AdaptadorListaMenu extends BaseExpandableListAdapter{

    private Context context;
    private List<String> listaTitulo;
    private Map<String, List<String>> listItem;

    public AdaptadorListaMenu(Context context, List<String> listaTitulo, Map<String, List<String>> listItem) {
        this.context = context;
        this.listaTitulo = listaTitulo;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return listaTitulo.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItem.get(listaTitulo.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listaTitulo.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(listaTitulo.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String titulo = (String) getGroup(groupPosition);
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_group, null);
        }
        TextView tvTitulo = view.findViewById(R.id.listTitle);
        tvTitulo.setTypeface(null, Typeface.BOLD);
        tvTitulo.setText(titulo);


        asignarIcono(titulo, view);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        String titulo = (String) getChild(groupPosition,childPosition);
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        }
        TextView tvTitulo = view.findViewById(R.id.expandableListItem);
        tvTitulo.setText(titulo);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void asignarIcono(String titulo, View view){

        ImageView icono = view.findViewById(R.id.icono);
        switch (titulo){
            case "Entreno" : icono.setImageResource(R.drawable.ic_account);
                        break;
            case "Mi información" : icono.setImageResource(R.drawable.ic_info);
                break;
            case "Datos de entrenos" : icono.setImageResource(R.drawable.ic_storage);
                break;
            case "Rutina" : icono.setImageResource(R.drawable.ic_fitness);
                break;
            case "Nutrición" : icono.setImageResource(R.drawable.ic_local_dining);
                break;
            case "Salir" : icono.setImageResource(R.drawable.ic_exit_to_app);
                break;
        }
    }
}
