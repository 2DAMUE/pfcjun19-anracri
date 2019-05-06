package com.dam.moveyourschool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dam.moveyourschool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Actividad_Detail_Detalles_Frag extends Fragment {


    public Actividad_Detail_Detalles_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actividad__detail__detalles_, container, false);
    }

}
