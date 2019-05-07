package com.dam.moveyourschool.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dam.moveyourschool.R;
import com.google.firebase.auth.FirebaseAuth;

public class NuevaReserva extends BaseActivity{


    String idEmpresa;
    String idUsu;

    TextView idU;
    TextView idE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idEmpresa = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idUsu = getIntent().getStringExtra("KEY_ID_USU");

        idU = findViewById(R.id.idU);
        idE = findViewById(R.id.idE);

        idU.setText(idUsu);
        idE.setText(idEmpresa);

    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_nueva_reserva;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }
}
