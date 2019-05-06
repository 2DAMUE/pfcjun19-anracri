package com.dam.moveyourschool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.utils.SwipeRevealLayout;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.UsuarioHolder> implements View.OnClickListener {
    private ArrayList<Usuario> listaUsuarios;
    private View.OnClickListener listener;
    private RequestManager glide;
    private boolean primeraVez;
    private boolean lock;

    public AdapterUsuarios(ArrayList<Usuario> listaUsuarios, RequestManager glide, boolean lock){
        this.listaUsuarios = listaUsuarios;
        this.glide = glide;
        primeraVez = true;
        this.lock =  lock;
    }

    @NonNull
    @Override
    public UsuarioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_swipe_user, viewGroup, false);
        UsuarioHolder holder = new UsuarioHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioHolder usuarioHolder, int i) {

        //La primera vez añadimos el listener a los componentes
        if (primeraVez) {
            usuarioHolder.linearUsuarios.setOnClickListener(this);
            usuarioHolder.btnEliminar.setOnClickListener(this);
        }

        //Comprobamos si ha de ser Swipe o no el layout
        if (!lock) {
            usuarioHolder.swipeRevealLayout.dragLock(true);
            usuarioHolder.imgNext.setVisibility(View.GONE);
        } else {

            //Si el mensaje no está leido debemos mostrar el icono visualmente
            if (!listaUsuarios.get(i).isLeido()) {
                usuarioHolder.btnCheck.setVisibility(View.VISIBLE);
            }
        }

        //Añadimos la posicion en el tag para obtenerla en el callback desde la vista
        usuarioHolder.btnEliminar.setTag(i);
        usuarioHolder.linearUsuarios.setTag(i);

        //Asignamos valores a los componentes de la vista
        usuarioHolder.tvNombre.setText(listaUsuarios.get(i).getNombre());
        usuarioHolder.tvInstituciones.setText(listaUsuarios.get(i).getTitular());

        if (listaUsuarios.get(i).getUrlFoto() == null || listaUsuarios.get(i).equals("")) {
            glide.load(R.drawable.idea).into(usuarioHolder.imageView);
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
        private LinearLayout linearUsuarios;
        private ImageView btnEliminar;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageView imgNext;
        private Button btnCheck;

        public UsuarioHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvInstituciones = itemView.findViewById(R.id.tvInstituciones);
            imageView = itemView.findViewById(R.id.fotoUsuario);
            linearUsuarios = itemView.findViewById(R.id.linearUser);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            swipeRevealLayout = itemView.findViewById(R.id.swipeReveal);
            imgNext = itemView.findViewById(R.id.imgNext);
            btnCheck = itemView.findViewById(R.id.btnCheck);

        }
    }

    public void setonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
