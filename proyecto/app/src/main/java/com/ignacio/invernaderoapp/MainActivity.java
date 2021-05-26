package com.ignacio.invernaderoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imLogo;
    EditText etUser, etPass;
    Button btnAccede;
    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controles();
        eventos();
        setTitle("Invernadero");
        sesion = getSharedPreferences("sesion", MODE_PRIVATE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle(Html.fromHtml("<p align=center>Invernadero</p>"));

    }
    private void eventos() {
        btnAccede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logearse();

            }
        });
    }

    private void logearse() {
        if (!etUser.getText().toString().isEmpty() && !etPass.getText().toString().isEmpty()) {

            String url = Uri.parse(conecxion.URL + "login.php")
                    .buildUpon()
                    .appendQueryParameter("user", etUser.getText().toString())
                    .appendQueryParameter("password", etPass.getText().toString())
                    .build().toString();

            JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            respuesta(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(MainActivity.this, "Error de red: " + error, Toast.LENGTH_LONG).show();
                }
            }
            );

            RequestQueue cola = Volley.newRequestQueue(this);
            cola.add(peticion);
        }else {
            Toast.makeText(this, "Hay algun campo vacio", Toast.LENGTH_SHORT).show();
        }
    }


    private void respuesta(JSONObject response) {
        try {
            if(response.getString("login").compareTo("s") == 0){
                String jwt = response.getString("token");
                SharedPreferences.Editor editor = sesion.edit();
                editor.putString("token", jwt);
                editor.putString("user", etUser.getText().toString());
                editor.apply();
                startActivity(new Intent(this,MainActivity2.class));
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error de usuario/contraseña", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }
    private void controles() {
        imLogo = findViewById(R.id.im_logo);
        etUser = findViewById(R.id.et_user);
        etPass = findViewById(R.id.et_pass);
        btnAccede = findViewById(R.id.btn_login);

    }



    }