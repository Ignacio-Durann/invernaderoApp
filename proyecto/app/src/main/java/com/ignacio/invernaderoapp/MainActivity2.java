package com.ignacio.invernaderoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import Fragments.calorFragment;
import Fragments.humedadFragment;
import Fragments.mainFragment;
import Fragments.movimientoFragment;
import Fragments.puertaFragment;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dlayout;
    ActionBarDrawerToggle aBarToogle;
    Toolbar tolbar;
    NavigationView navView;
    FragmentManager fManager;
    FragmentTransaction fTrabsaction;
    TextView tvDrawerHeader;
    LinearLayout NavUI;


    //funcionalidades
    SharedPreferences sesion;
    Bundle parametros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        controles();
        eventos();
        parametros = this.getIntent().getExtras();
        sesion= getSharedPreferences("sesion",0);
        ifoUsuario();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle(Html.fromHtml("<div align='center'>Invernadero</div>"));
    }

    private void ifoUsuario() {
        View datos = navView.getHeaderView(0);
        tvDrawerHeader = datos.findViewById(R.id.tv_drawer_header);
        tvDrawerHeader.setText("Usuario: "+sesion.getString("user",""));
    }


    private void eventos() {
        navView.setNavigationItemSelectedListener(this);

    }





    private void controles() {
        tolbar = findViewById(R.id.toolbar);
        setSupportActionBar(tolbar);
        dlayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.navigationView);
        aBarToogle = new ActionBarDrawerToggle(this,dlayout,tolbar,R.string.open,R.string.close);
        dlayout.addDrawerListener(aBarToogle);
        aBarToogle.setDrawerIndicatorEnabled(true);
        aBarToogle.syncState();
        //cargar main_fragment
        fManager = getSupportFragmentManager();
        fTrabsaction= fManager.beginTransaction();
        fTrabsaction.add(R.id.content_main, new mainFragment());
        fTrabsaction.commit();
        //controles normales


    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        dlayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.menu_inicio){
            fManager = getSupportFragmentManager();
            fTrabsaction= fManager.beginTransaction();
            fTrabsaction.replace(R.id.content_main, new mainFragment());
            fTrabsaction.commit();
        }if(item.getItemId() == R.id.sensor_calor){
            fManager = getSupportFragmentManager();
            fTrabsaction= fManager.beginTransaction();
            fTrabsaction.replace(R.id.content_main, new calorFragment());
            fTrabsaction.commit();
        }if(item.getItemId() == R.id.sen_humedad){
            fManager = getSupportFragmentManager();
            fTrabsaction= fManager.beginTransaction();
            fTrabsaction.replace(R.id.content_main, new humedadFragment());
            fTrabsaction.commit();
        }if(item.getItemId() == R.id.sen_movimiento){
            fManager = getSupportFragmentManager();
            fTrabsaction= fManager.beginTransaction();
            fTrabsaction.replace(R.id.content_main, new movimientoFragment());
            fTrabsaction.commit();
        }if(item.getItemId() == R.id.sen_puerta){
            fManager = getSupportFragmentManager();
            fTrabsaction= fManager.beginTransaction();
            fTrabsaction.replace(R.id.content_main, new puertaFragment());
            fTrabsaction.commit();
        }
        return false;
    }
}