package com.dam.moveyourschool.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.dam.moveyourschool.R;
import com.dam.moveyourschool.activities.ActividadForm;
import com.dam.moveyourschool.fragments.Actividad_Form_Step_4;

public class CustomDialog extends Dialog {
    private int id;
    private Button btnErrorRegresar;
    private TextView tvErrorGenerico;
    private ImageView imgError;

    public CustomDialog(@NonNull final Context context, int id) {
        super(context);
        this.id = id;
        setContentView(R.layout.dialog_error);
        btnErrorRegresar = findViewById(R.id.btnErrorRegresar);
        tvErrorGenerico = findViewById(R.id.tvErrorGenerico);
        imgError = findViewById(R.id.imgError);
        setCancelable(false);
        mostrarDialogoError();

        btnErrorRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    //Metodo que personaliza el XML con el error oportuno para cada situación y lo muestra al usuario.
    public void mostrarDialogoError(){
        tvErrorGenerico.setText(String.format(super.getContext().getString(id)));
    }

    public void setImg(Drawable drawable) {
        imgError.setImageDrawable(drawable);
    }

    public void setButtonStyle(String text, int color) {
        btnErrorRegresar.setText(text);
        btnErrorRegresar.setBackgroundColor(color);
    }
}
