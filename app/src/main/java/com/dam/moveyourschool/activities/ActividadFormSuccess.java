package com.dam.moveyourschool.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dam.moveyourschool.R;

public class ActividadFormSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_form_success);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ActividadFormSuccess.this, Actividades.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        },2000);
    }
}
