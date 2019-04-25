package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dam.moveyourschool.R;

public class SignUpSuccess extends AppCompatActivity {
    private TextView tvCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        tvCorreo = findViewById(R.id.tvcorreo);
        String correo = getIntent().getStringExtra("KEY_MAIL");
        tvCorreo.setText(String.format(getString(R.string.TXT_CORREO_CONFIRMACION), correo));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SignUpSuccess.this, Actividades.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }, 3000);
    }
}
