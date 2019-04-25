package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetail extends BaseActivity {
    //Atributos Descripcion
    private TextView tvDesc;
    private TextView tvDir;
    private TextView tvLocalidad;

    //Atributos de valor
    private TextView tvTitular;
    private TextView tvInstitucion;
    private ExpandableTextView tvDescripcion;
    private TextView tvMailRes;
    private TextView tvDirRes;
    private TextView tvLocRes;
    private CircleImageView profileImg;
    private RequestManager glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inicialización de Glide
        glide = Glide.with(this);

        //Atributos de Valor
        tvTitular = findViewById(R.id.tvTitular);
        tvInstitucion = findViewById(R.id.tvinstitucion);
        tvDescripcion = findViewById(R.id.expand_text_view);
        tvMailRes = findViewById(R.id.tvMailRes);
        tvDirRes = findViewById(R.id.tvDirRes);
        tvLocRes = findViewById(R.id.tvLocRes);
        profileImg = findViewById(R.id.profile);

        //Atributos Descripcion
        tvDesc = findViewById(R.id.tvDescripcion);
        tvDir = findViewById(R.id.tvdir);
        tvLocalidad = findViewById(R.id.tvLocalidad);

        //Muestra todos los campos invisibilizados
        visibilidadFalsa();

        //Comprueba el usuario de Firebase e inicializa la consulta en la database si existe
        final FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        if (authUser != null) {

            new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getKey().equals(authUser.getUid())) {
                        visibilidadFalsa();
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        updateFields(usuario);
                    }
                }

                @Override
                public void nodoModificado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getKey().equals(authUser.getUid())) {
                        visibilidadFalsa();
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        updateFields(usuario);
                    }
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

    }

    //Invisibilida Campos
    public void visibilidadFalsa() {
        tvDescripcion.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);

        tvDirRes.setVisibility(View.GONE);
        tvDir.setVisibility(View.GONE);

        tvLocalidad.setVisibility(View.GONE);
        tvLocRes.setVisibility(View.GONE);
    }

    public void updateFields(Usuario usuario) {

        if (usuario.getTitular() != null) {
            tvTitular.setText(usuario.getTitular());
            tvTitular.setVisibility(View.VISIBLE);
        }

        if (usuario.getNombre() != null) {
            tvInstitucion.setText(usuario.getNombre());
            tvTitular.setVisibility(View.VISIBLE);
        }

        if (usuario.getDescripcion() != null) {
            tvDescripcion.setText(usuario.getDescripcion());
            tvDesc.setVisibility(View.VISIBLE);
            tvDescripcion.setVisibility(View.VISIBLE);
        }

        if (usuario.getDireccion() != null) {
            tvDirRes.setText(usuario.getDireccion());
            tvDirRes.setVisibility(View.VISIBLE);
            tvDir.setVisibility(View.VISIBLE);
        }

        if (usuario.getLocalidad() != null) {
            tvLocRes.setText(usuario.getLocalidad());
            tvLocalidad.setVisibility(View.VISIBLE);
            tvLocRes.setVisibility(View.VISIBLE);
        }

        if (usuario.getEmail() != null) {
            tvMailRes.setText(usuario.getEmail());
        }

        if(usuario.getUrlFoto() != null) {
            glide.load(UserDetail.this).load(usuario.getUrlFoto()).into(profileImg);
        }
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_user_detail;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }

    public void editProfile(View view) {
        startActivity(new Intent(this, UserEdit.class));
    }
}
