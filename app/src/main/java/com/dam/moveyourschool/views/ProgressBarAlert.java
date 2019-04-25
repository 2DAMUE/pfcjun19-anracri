package com.dam.moveyourschool.views;

import android.app.Dialog;
import android.content.Context;
import com.dam.moveyourschool.R;

public class ProgressBarAlert extends Dialog {

    public ProgressBarAlert(Context context) {
        super(context,  R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.progressbar);
    }
}
