package com.ignacio.invernaderoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adaptador extends RecyclerView.Adapter<adaptador.ViewHolder> {
    String[][] datos;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId,tvUser,tvValor,tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvID);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvValor = itemView.findViewById(R.id.tvValor);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
        }
    }
    public adaptador(String[][] data){
        this.datos = data;
    }

    @NonNull
    @Override
    public adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_lista_sensores,parent,false);
        return new ViewHolder(vista);
    }


    @Override
    public void onBindViewHolder(@NonNull adaptador.ViewHolder holder, int position) {
        holder.tvId.setText(datos[position][0]);
        holder.tvUser.setText(datos[position][1]);
        holder.tvValor.setText(datos[position][2]);
        holder.tvFecha.setText(datos[position][3]);
    }


    @Override
    public int getItemCount() {
        return datos.length;
    }
}
