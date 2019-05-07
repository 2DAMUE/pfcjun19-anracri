package com.dam.moveyourschool.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.bean.Actividad;
import com.dam.moveyourschool.services.FireBaseStorage;
import com.dam.moveyourschool.utils.BitmapToUri;
import com.dam.moveyourschool.views.CustomDialog;
import com.dam.moveyourschool.views.ProgressBarAlert;
import com.mvc.imagepicker.ImagePicker;

public class Actividad_Form_Step2_Frag extends Fragment {
    private TextInputEditText etxTitulo;
    private TextInputEditText etxDescripcion;
    private TextInputEditText etxPrecio;
    private TextInputEditText etxMaxPersonas;
    private TextInputEditText etxMinPersonas;
    private ImageView imgEdit;
    private String urlFoto;
    private ProgressBarAlert progressBarAlert;

    public Actividad_Form_Step2_Frag() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ImagePicker.setMinQuality(600, 600);

        View contenedor = inflater.inflate(R.layout.fragment_actividad__form__step2_, container, false);
        etxTitulo = contenedor.findViewById(R.id.etxNombreActividad);
        etxMaxPersonas = contenedor.findViewById(R.id.etxMaximoPersonas);
        etxMinPersonas = contenedor.findViewById(R.id.etxMinPersonas);
        etxDescripcion = contenedor.findViewById(R.id.etxDescripcion);
        etxPrecio = contenedor.findViewById(R.id.etxPrecio);
        imgEdit = contenedor.findViewById(R.id.imgEdit);
        progressBarAlert = new ProgressBarAlert(getActivity());

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });

        return contenedor;
    }

    public void onPickImage() {
        ImagePicker.pickImage(this, getString(R.string.ESCOGER_IMAGEN));
    }

    public void setImageResult(String url) {
        progressBarAlert.cancel();
        Glide.with(this).load(url).into(imgEdit);
    }

    public boolean registrar() {
        Actividad actividad = null;
        boolean success = false;
        boolean exceptions = true;

        if (checkFields(etxTitulo) || checkFields(etxDescripcion)
                || checkFields(etxPrecio)) {

            new CustomDialog(getActivity(), R.string.EXCEPT_DEBE_RELLENAR_TITULO_DESCRIPCION_PRECIO).show();

        } else {

            try {
                Double.parseDouble(etxPrecio.getText().toString());

                if (!etxMinPersonas.getText().toString().isEmpty()) {
                    Integer.parseInt(etxMinPersonas.getText().toString());
                }

                if (!etxMaxPersonas.getText().toString().isEmpty()) {
                    Integer.parseInt(etxMaxPersonas.getText().toString());
                }

                exceptions = false;

            } catch (NumberFormatException e) {
                new CustomDialog(getActivity(), R.string.EXCEPT_CAMPOS_NUMERICOS).show();
            }

            if (!exceptions) {
                actividad = new Actividad();
                actividad.setTitulo(etxTitulo.getText().toString());
                actividad.setPrecio(Double.parseDouble(etxPrecio.getText().toString()));

                if (!etxMaxPersonas.getText().toString().isEmpty()) {
                    actividad.setMaxPlazas(Integer.parseInt(etxMaxPersonas.getText().toString()));
                }

                if (!etxMinPersonas.getText().toString().isEmpty()) {
                    actividad.setMinPlazas(Integer.parseInt(etxMinPersonas.getText().toString()));
                }

                actividad.setDescripcion(etxDescripcion.getText().toString());

                if (urlFoto != null) {
                    actividad.setUrlFoto(urlFoto);
                }
            }

            if (!exceptions) {
                success = true;
                actividad.setCategoria(((ActividadForm) (getActivity())).getActividad().getCategoria());
                ((ActividadForm) (getActivity())).setActividad(actividad);
            }
        }

        return success;
    }

    private boolean checkFields(EditText field) {
        if (field.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressBarAlert.show();
        Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);

        if (bitmap != null) {
            Uri uri = BitmapToUri.getImageUri(getActivity(), bitmap);

            new FireBaseStorage() {
                @Override
                public void getUrl(String url) {
                    urlFoto = url;
                    setImageResult(url);
                }
            }.uploadPhoto(uri);
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }
}
