package com.dam.moveyourschool.utils;

import android.app.Activity;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class Constantes {
    private ArrayList<String> provincias;
    private static final String fichero = "provincias.txt";
    private BufferedReader reader;
    private  Activity activity;

    public Constantes(Activity activity) {
        this.activity = activity;
        loadProvincias();
    }

    private void loadProvincias() {
        AssetManager asset = activity.getAssets();
        provincias = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(asset.open(fichero)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            String linea = null;

            @Override
            public void run() {

                try {
                    provincias.add("Localidad");
                    while (  (linea = reader.readLine()) != null ) {
                        provincias.add(linea);
                    }
                    callBackGetProvincias(provincias);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public abstract void callBackGetProvincias(ArrayList<String> provincias);
}
