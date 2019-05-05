package com.dam.moveyourschool.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadDetail;
import com.dam.moveyourschool.bean.Actividad;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class Actividad_Detail_General_Frag extends Fragment {
    private ExpandableTextView expDescripcion;
    private TextView tvTitulo;
    private TextView tvPrecio;

    public Actividad_Detail_General_Frag() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_actividad__detail__general_, container, false);
        expDescripcion = view.findViewById(R.id.expand_text_view);
        tvTitulo = view.findViewById(R.id.tvActividadTitulo);
        tvPrecio = view.findViewById(R.id.tvActividadPrecio);


        Actividad actividad = (((ActividadDetail) (getActivity())).getActividad());

        if (actividad.getTitulo() != null && !actividad.getTitulo().equals("")) {
            tvTitulo.setText(actividad.getTitulo());
        }

        if (actividad.getPrecio() % 1 == 0) {
            tvPrecio.setText(String.valueOf(((int) (actividad.getPrecio())) + " €"));
        }

        if (actividad.getDescripcion() != null && !actividad.getDescripcion().equals("")) {
            expDescripcion.setText(actividad.getDescripcion());
        }

        return view;
    }
}
