package com.dam.moveyourschool.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dam.moveyourschool.R;

public class UserEdit extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int cargarLayout() {
        return R.layout.activity_user_edit;
    }

    @Override
    public boolean setDrawer() {
        return false;
    }
}
