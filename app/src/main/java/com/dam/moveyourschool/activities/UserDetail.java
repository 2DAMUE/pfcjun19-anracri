package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class UserDetail extends BaseActivity {
    private TextView tvTitular;
    private TextView tvInstitucion;
    private ExpandableTextView tvDescripcion;
    private TextView tvMailRes;
    private TextView tvDirRes;
    private TextView tvLocRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvTitular = findViewById(R.id.tvTitular);
        tvInstitucion = findViewById(R.id.tvinstitucion);
        tvDescripcion = findViewById(R.id.expand_text_view);
        tvMailRes = findViewById(R.id.tvMailRes);
        tvDirRes = findViewById(R.id.tvDirRes);
        tvLocRes = findViewById(R.id.tvLocRes);

        final FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        if (authUser != null) {

            new FireDBUsuarios() {
                @Override
                public void nodoAgregado(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getKey().equals(authUser.getUid())) {

                            Usuario usuario = dataSnapshot.getValue(Usuario.class);

                            if (usuario.getTitular() != null) {
                                tvTitular.setText(usuario.getTitular());
                            }

                            if (usuario.getNombre() != null) {
                                tvInstitucion.setText(usuario.getNombre());
                            }

                            if (usuario.getDescripcion() != null) {
                                tvDescripcion.setText(usuario.getDescripcion());
                            }

                            if (usuario.getLocalidad() != null) {
                                tvLocRes.setText(usuario.getLocalidad());
                            }

                            if (usuario.getEmail() != null) {
                                tvMailRes.setText(usuario.getEmail());
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




        // sample code snippet to set the text content on the ExpandableTextView


// IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        //tvDescripcion.setText(getString(R.string.TXT_DESC));

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
