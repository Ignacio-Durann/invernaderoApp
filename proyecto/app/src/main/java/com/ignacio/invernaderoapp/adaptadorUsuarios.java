package com.ignacio.invernaderoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adaptadorUsuarios extends RecyclerView.Adapter<adaptadorUsuarios.ViewHolder> {
    String datos[][];

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvContra,tvUsua,tvTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsua = itemView.findViewById(R.id.a_l_u_user);
            tvContra = itemView.findViewById(R.id.a_l_u_pass);
            tvTipo = itemView.findViewById(R.id.a_l_u_tipo);
        }
    }

    public adaptadorUsuarios(String[][] data){this.datos = data;}

    @NonNull
    @Override
    public adaptadorUsuarios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_lista_usuarios,parent,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorUsuarios.ViewHolder holder, int position) {
        holder.tvUsua.setText(datos[position][0]);
        holder.tvContra.setText(datos[position][1]);
        holder.tvTipo.setText(datos[position][2]);
    }

    @Override
    public int getItemCount() {
        return datos.length;
    }

}
