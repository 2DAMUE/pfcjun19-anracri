package com.dam.moveyourschool.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Usuario;
import com.dam.moveyourschool.services.FireDBUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class Actividades extends BaseActivity {
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_actividades;
    }

    @Override
    public boolean setDrawer() {
        return true;
    }
}
