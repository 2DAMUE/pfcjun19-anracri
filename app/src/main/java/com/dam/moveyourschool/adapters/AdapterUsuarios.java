package com.dam.moveyourschool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.UsuarioHolder> implements View.OnClickListener {
    private ArrayList<Usuario> listaUsuarios;
    private View.OnClickListener listener;
    private RequestManager glide;

    public AdapterUsuarios(ArrayList<Usuario> listaUsuarios, RequestManager glide){
        this.listaUsuarios = listaUsuarios;
        this.glide = glide;
    }

    @NonNull
    @Override
    public UsuarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usuario, viewGroup, false);
        v.setOnClickListener(this);
        UsuarioHolder holder = new UsuarioHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioHolder usuarioHolder, int i) {

        usuarioHolder.tvNombre.setText(listaUsuarios.get(i).getNombre());
        usuarioHolder.tvInstituciones.setText(listaUsuarios.get(i).getTitular());

        if (listaUsuarios.get(i).getUrlFoto() == null) {
            glide.load(R.drawable.ic_edificio).into(usuarioHolder.imageView);
        } else {
            glide.load(listaUsuarios.get(i).getUrlFoto()).into(usuarioHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }

    }

    public static class UsuarioHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvInstituciones;
        private CircleImageView imageView;

        public UsuarioHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvInstituciones = itemView.findViewById(R.id.tvInstituciones);
            imageView = itemView.findViewById(R.id.fotoUsuario);
        }
    }

    public void setonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
