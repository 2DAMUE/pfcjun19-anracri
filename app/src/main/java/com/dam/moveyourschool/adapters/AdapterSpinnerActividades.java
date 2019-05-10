package com.dam.moveyourschool.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.bean.Actividad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdapterSpinnerActividades extends ArrayAdapter {

    ArrayList<Actividad> lista ;
    private Context context;

    public AdapterSpinnerActividades(@NonNull Context context, ArrayList<Actividad> lista) {
        super(context, R.layout.spinner_actividades);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    private static class ItemActividad{
        ImageView img;
        TextView txt1;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ItemActividad item = new ItemActividad();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_actividades, parent, false);
            item.img = convertView.findViewById(R.id.imgActivSpi);
            item.txt1 =  convertView.findViewById(R.id.txtTituloActividad);

            convertView.setTag(item);
        } else {
            item = (ItemActividad) convertView.getTag();
        }


        //Mapeamos la carpeta de imagenes asignando cada imagen a su playa

        if(lista.get(position).getUrlFoto() == null){
            Glide.with(context).load(R.drawable.demoactivity).into(item.img);
        }else{
            Glide.with(context).load(lista.get(position).getUrlFoto()).into(item.img);
        }



        item.txt1.setText(lista.get(position).getTitulo());



        return convertView;
    }




    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
