package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.adapters.AdapterUsuarios;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;

public class UsuariosList extends BaseActivity {
    private RecyclerView recyclerUsuarios;
    private LinearLayoutManager lm;
    private AdapterUsuarios adapterUsuarios;
    private ArrayList<Usuario> listaUsuarios;
    private RequestManager glide;
    private FireDBUsuarios databaseServiceUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicialización del ArrayList con los usuarios
        this.listaUsuarios = new ArrayList<>();

        //Inicialización del Request Manager de Glide para gestionar las imagenes del usuario
        glide = Glide.with(this);

        //Inicialización del RecyclerView y sus atributos y conectores
        lm = new LinearLayoutManager(this);
        adapterUsuarios = new AdapterUsuarios(listaUsuarios, glide);
        recyclerUsuarios = findViewById(R.id.recyclerView);
        recyclerUsuarios.setLayoutManager(lm);
        recyclerUsuarios.setAdapter(adapterUsuarios);
        recyclerUsuarios.setHasFixedSize(true);

        //Inicialización del Listener del Recycler View
        startRecyclerListener();

        //Inicialización del servicio de la base de datos de usuarios
        loadDatabase();
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_usuarios_list;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }

    private void loadDatabase() {
        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        databaseServiceUsuarios = new FireDBUsuarios() {
            @Override
            public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (userAuth != null){
                    if (!userAuth.getUid().equals(dataSnapshot.getKey())) {

                        Usuario aux = dataSnapshot.getValue(Usuario.class);

                        if (aux.getNombre() != null && aux.getTitular() != null) {
                            listaUsuarios.add(dataSnapshot.getValue(Usuario.class));
                            adapterUsuarios.notifyItemInserted(listaUsuarios.size() - 1);
                        }
                    }
                }
            }

            @Override
            public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void nodoEliminado(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void nodoMovido(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void nodoCancelado(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void startRecyclerListener() {
        adapterUsuarios.setonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = recyclerUsuarios.getChildAdapterPosition(view);
                startActivity(new Intent(UsuariosList.this, ChatWindow.class)
                        .putExtra(getString(R.string.KEY_USER), listaUsuarios.get(recyclerUsuarios.getChildAdapterPosition(view))));
                //startActivity(new Intent(UsuariosList.this, UserDetail.class));
            }
        });
    }
}
