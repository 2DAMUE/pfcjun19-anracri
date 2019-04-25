package com.dam.moveyourschool.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dam.moveyourschool.R;

public class CustomDialog extends Dialog {
    private int id;
    private Button btnErrorRegresar;
    private TextView tvErrorGenerico;

    public CustomDialog(@NonNull Context context, int id) {
        super(context);
        this.id = id;
        setContentView(R.layout.dialog_error);
        btnErrorRegresar = findViewById(R.id.btnErrorRegresar);
        tvErrorGenerico = findViewById(R.id.tvErrorGenerico);
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
}
