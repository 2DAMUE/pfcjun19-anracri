package com.dam.moveyourschool.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dam.moveyourschool.R;
import com.google.firebase.auth.FirebaseAuth;

public class Actividades extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("USER", FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
