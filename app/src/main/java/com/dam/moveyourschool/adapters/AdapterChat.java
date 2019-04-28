package com.dam.moveyourschool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Mensaje;
import com.dam.moveyourschool.bean.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterChat extends RecyclerView.Adapter {
    private Usuario userMe;
    private ArrayList<Mensaje> listaMensajes;
    private SimpleDateFormat dateFormat;

    public AdapterChat(ArrayList<Mensaje> listaMensajes, Usuario userMe) {
        this.listaMensajes = listaMensajes;
        this.userMe = userMe;
        dateFormat = new SimpleDateFormat("HH:mm");
    }

    @Override
    public int getItemViewType(int position) {
        //Si es mensaje recibido
        if (!listaMensajes.get(position).getUserSender().getUid().equals(userMe.getUid())) {
            return 1;
            //Si es mensaje enviado
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = null;

        if (i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_received, viewGroup, false);
            HolderReceive holderReceive = new HolderReceive(v);
            return holderReceive;
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sent, viewGroup, false);
            HolderSend holderSend = new HolderSend(v);
            return holderSend;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        HolderReceive holderReceive;
        HolderSend holderSend;

        String fecha = dateFormat.format(new Date());

        if (viewHolder.getItemViewType() == 1) {
            holderReceive = (HolderReceive) viewHolder;
            holderReceive.mensajeRemitente.setText(listaMensajes.get(i).getMensaje());
            holderReceive.nickRemitente.setText(listaMensajes.get(i).getUserSender().getNombre());
            holderReceive.tvHoraRecibo.setText(fecha);
        } else {
            holderSend = (HolderSend) viewHolder;
            holderSend.mensaje_envio.setText(listaMensajes.get(i).getMensaje());
            holderSend.tvHoraEnvio.setText(fecha);
        }
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public static class HolderSend extends RecyclerView.ViewHolder {
        private TextView mensaje_envio;
        private TextView tvHoraEnvio;

        public HolderSend(@NonNull View itemView) {
            super(itemView);
            mensaje_envio = itemView.findViewById(R.id.tvMensajeEnviar);
            tvHoraEnvio = itemView.findViewById(R.id.tvHoraEnvio);
        }
    }

    public static class HolderReceive extends RecyclerView.ViewHolder {
        private TextView nickRemitente;
        private TextView mensajeRemitente;
        private TextView tvHoraRecibo;

        public HolderReceive(@NonNull View itemView) {
            super(itemView);
            nickRemitente = itemView.findViewById(R.id.tvRemitente);
            mensajeRemitente = itemView.findViewById(R.id.tvMensajeRecibir);
            tvHoraRecibo = itemView.findViewById(R.id.tvHoraRecibo);
        }
    }
}
