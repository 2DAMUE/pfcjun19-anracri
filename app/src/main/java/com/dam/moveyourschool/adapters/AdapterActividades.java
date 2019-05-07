package com.dam.moveyourschool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AdapterActividades extends RecyclerView.Adapter<AdapterActividades.HolderActividades> implements View.OnClickListener {
    private View.OnClickListener listener;
    private ArrayList<Actividad> listaActividades;
    private ArrayList<Actividad> listAuxiliar;
    private RequestManager glide;
    private String filtro;
    public static final String TODAS_LAS_ACTIVIDADES = "ELIMINAR FILTRO";
    private static final String FILTRO_ARTE = "Actividad Artística";
    private boolean primeraVez = true;

    public AdapterActividades(ArrayList<Actividad> listaActividades, RequestManager glide) {
        this.listaActividades = listaActividades;
        listAuxiliar = new ArrayList<>();
        filtro = TODAS_LAS_ACTIVIDADES;
        this.glide = glide;
    }

    @NonNull
    @Override
    public HolderActividades onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_actividad, viewGroup, false);
        view.setOnClickListener(this);
        HolderActividades holderActividades = new HolderActividades(view);
        return holderActividades;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderActividades holderActividades, int i) {
        if (listaActividades.get(i).getUrlFoto() != null && !listaActividades.get(i).getUrlFoto().equals("")) {
            glide.load(listaActividades.get(i).getUrlFoto()).into(holderActividades.imagen);
        } else {
            glide.load(R.drawable.imgexample).into(holderActividades.imagen);
        }

        holderActividades.tvTitulo.setText(listaActividades.get(i).getTitulo());
        holderActividades.tvLikes.setText(String.valueOf(listaActividades.get(i).getLikes()));

        if (listaActividades.get(i).getPrecio() % 1 == 0) {
            holderActividades.tvPrecio.setText(String.valueOf(((int) (listaActividades.get(i).getPrecio())) + " €"));
        } else {
            holderActividades.tvPrecio.setText(String.valueOf(listaActividades.get(i).getPrecio() + " €"));
        }
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }

    public static class HolderActividades extends RecyclerView.ViewHolder {
        private RoundedImageView imagen;
        private TextView tvTitulo;
        private TextView tvPrecio;
        private TextView tvLikes;

        public HolderActividades(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagesk);
            tvTitulo = itemView.findViewById(R.id.tvTituloActividad);
            tvPrecio = itemView.findViewById(R.id.tvPrecioActividad);
            tvLikes = itemView.findViewById(R.id.tvLikesActividad);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void filterByCategoria(String categoria) {
        this.filtro = categoria;

        if (primeraVez) {
            clonar();
        }

        if (filtro.equals(TODAS_LAS_ACTIVIDADES)) {
            recuperarOriginal();

        } else {
            filtrar(categoria);
            Log.e("AQI","A");
        }
        notifyDataSetChanged();
    }

    public void recuperarOriginal() {
        listaActividades.clear();

        for (int i = 0; i < listAuxiliar.size(); i++) {
            listaActividades.add(listAuxiliar.get(i));
        }
    }

    public void filtrar(String categoria) {
        listaActividades.clear();

        for (int i = 0; i < listAuxiliar.size(); i++) {
            Log.e("NO HAY", listAuxiliar.get(i).toString());
            if (listAuxiliar.get(i).getCategoria().equals(categoria)) {
                listaActividades.add(listAuxiliar.get(i));
            }
        }
    }

    public void clonar() {
        Iterator<Actividad> iterator = listaActividades.iterator();

        while(iterator.hasNext())
        {
            listAuxiliar.add((Actividad) iterator.next());
        }
        primeraVez = false;
    }
}
