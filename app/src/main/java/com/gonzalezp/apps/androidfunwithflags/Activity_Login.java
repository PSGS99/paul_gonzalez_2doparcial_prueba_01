package com.gonzalezp.apps.androidfunwithflags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Login extends AppCompatActivity {

    public Button boton;
    public Button boton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boton = findViewById(R.id.button_banderas);
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), psgs_MainActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

        boton2 = findViewById(R.id.button_admin);
        boton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), psgs_crudClients.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

    }
}