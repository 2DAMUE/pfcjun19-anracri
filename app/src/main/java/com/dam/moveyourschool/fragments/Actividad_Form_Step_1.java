package com.dam.moveyourschool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;

public class Actividad_Form_Step_1 extends Fragment {
    private LinearLayout linearArte;
    private LinearLayout linearCiudad;
    private LinearLayout linearDeporte;
    private LinearLayout linearFabrica;
    private LinearLayout linearFiesta;
    private LinearLayout linearReligion;
    private LinearLayout linearJuego;
    private LinearLayout linearMonumento;
    private LinearLayout linearMuseo;
    private LinearLayout linearMusica;
    private LinearLayout linearNaturaleza;
    private LinearLayout linearTecnologia;
    private LinearLayout linearTaller;
    private LinearLayout linearTeatro;
    private LinearLayout linearOtros;





    public Actividad_Form_Step_1() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contenedor = inflater.inflate(R.layout.fragment_actividad__form__step_1, container, false);
        linearArte = contenedor.findViewById(R.id.linearArte);
        linearCiudad = contenedor.findViewById(R.id.linearCiudad);
        linearDeporte = contenedor.findViewById(R.id.linearDeporte);
        linearFabrica = contenedor.findViewById(R.id.linearFabrica);
        linearFiesta = contenedor.findViewById(R.id.linearFiesta);
        linearJuego = contenedor.findViewById(R.id.linearJuego);
        linearMonumento = contenedor.findViewById(R.id.linearMonumental);
        linearMuseo = contenedor.findViewById(R.id.linearMuseo);
        linearMusica = contenedor.findViewById(R.id.linearMusica);
        linearNaturaleza = contenedor.findViewById(R.id.linearNaturaleza);
        linearOtros = contenedor.findViewById(R.id.linearOtros);
        linearReligion = contenedor.findViewById(R.id.linearReligion);
        linearTaller = contenedor.findViewById(R.id.linearTaller);
        linearTeatro = contenedor.findViewById(R.id.linearTeatro);
        linearTecnologia = contenedor.findViewById(R.id.linearTecnologia);


        return contenedor;
    }
}
