package com.dam.moveyourschool.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Reserva;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterReservas extends RecyclerView.Adapter<AdapterReservas.HolderReserva> implements View.OnClickListener{

    private ArrayList<Reserva> listaReservas;
    private View.OnClickListener listener;
    private Context context;

    public AdapterReservas(ArrayList<Reserva> listaReservas, Context context) {
        this.listaReservas = listaReservas;
        this.context = context;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public HolderReserva onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_reservas, parent, false);
        v.setOnClickListener(this);
        HolderReserva holder = new HolderReserva(v);
        return holder;
    }


    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public static class HolderReserva extends RecyclerView.ViewHolder {
        ImageView imgReserva;
        TextView txtTituloReserva;
        TextView txtFecha;
        TextView txtHora;
        ImageView imgEstado;

        public HolderReserva(View itemView) {
            super(itemView);
            imgReserva = itemView.findViewById(R.id.imgRActividad);
            txtTituloReserva = itemView.findViewById(R.id.reservaTitulo);
            txtFecha = itemView.findViewById(R.id.reservaFecha);
            txtHora = itemView.findViewById(R.id.reservaHora);
            imgEstado = itemView.findViewById(R.id.imgEstado);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderReserva holder, int position) {

        holder.txtTituloReserva.setText(listaReservas.get(position).getTituloActiRef());
        holder.txtFecha.setText(listaReservas.get(position).getFecha());
        holder.txtHora.setText(listaReservas.get(position).getHora());

        if (listaReservas.get(position).getUrlFotoAct() == null){
            Glide.with(context).load(R.drawable.demoactivity).into(holder.imgReserva);
        }else{
            Glide.with(context).load(listaReservas.get(position).getUrlFotoAct()).into(holder.imgReserva);
        }

        String estado = listaReservas.get(position).getEstado();



        if(estado.equals("PENDIENTE")){
            Glide.with(context).load(R.drawable.ic_pendiente).into(holder.imgEstado);
        }else if(estado.equals("ACEPTADA")){
            Glide.with(context).load(R.drawable.ic_aceptado).into(holder.imgEstado);
        }else{
            Glide.with(context).load(R.drawable.ic_rechazado).into(holder.imgEstado);
        }

    }


    public void eliminarTodasLasReservas() {
        int size = listaReservas.size();
        listaReservas.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void agregarReservas(ArrayList<Reserva> alertas) {
        listaReservas.addAll(alertas);
        //notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }


    public void ordenarAZ() {
        Collections.sort(listaReservas, new Comparator<Reserva>() {
            @Override
            public int compare(Reserva res, Reserva t1) {
                return res.getTituloActiRef().compareToIgnoreCase(t1.getTituloActiRef());
            }
        });
        notifyDataSetChanged();
    }

    public void ordenarZA() {
        Collections.sort(listaReservas, new Comparator<Reserva>() {
            @Override
            public int compare(Reserva res, Reserva t1) {
                return (res.getTituloActiRef().compareToIgnoreCase(t1.getTituloActiRef())) * -1;
            }
        });
        notifyDataSetChanged();
    }

}
