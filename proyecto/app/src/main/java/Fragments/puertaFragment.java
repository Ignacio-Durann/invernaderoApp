package Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ignacio.invernaderoapp.MainActivity;
import com.ignacio.invernaderoapp.R;
import com.ignacio.invernaderoapp.conecxion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class puertaFragment extends Fragment {
    private Button btnPuertaAbrir, btnPuertaCerrar;
    private TextView tvPuertaMensaje;
    String c = "2", a = "1";


    public puertaFragment() {

    }

    public static puertaFragment getInstance(String param1, String param2) {
        puertaFragment puertaF = new puertaFragment();
        return puertaF;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.puerta_fragment, container, false);
        btnPuertaAbrir = (Button) view.findViewById(R.id.btn_puerta_abrir);
        btnPuertaCerrar = (Button) view.findViewById(R.id.btn_puerta_cerrar);
        tvPuertaMensaje = (TextView) view.findViewById(R.id.tv_puerta_mensaje);
        accionesBotones();
        checarEstadoPuerta();
        return view;
    }

    private void checarEstadoPuerta() {
        try {
            String url = Uri.parse(conecxion.URL + "sensorp.php")
                    .buildUpon()
                    .appendQueryParameter("valor", "1")
                    .build().toString();

            JsonArrayRequest peticion = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            respuetaEstado(response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getContext(), "Error de red: " + error, Toast.LENGTH_LONG).show();
                }
            }
            );

            RequestQueue cola = Volley.newRequestQueue(getContext());
            cola.add(peticion);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void respuetaEstado(JSONArray response) {
        //falta condicion
        try{
            if(response.getString(0).endsWith("1")) {
                tvPuertaMensaje.setText("Puerta Abierta");
            }else{
                tvPuertaMensaje.setText("Puerta Cerrada");
            }
            }catch (Exception e){
            Toast.makeText(getContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
        }
    }


    private void accionesBotones() {
        //boton de abrir
        btnPuertaAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                puertaAbre();
            }
        });
        //boton de cerrar
        btnPuertaCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                puertaCierra();
            }
        });
    }

    private void puertaCierra() {
        try {
            if(!tvPuertaMensaje.getText().toString().equals("Puerta Cerrada")){
                String url = Uri.parse(conecxion.URL + "sensorp.php")
                        .buildUpon()
                        .build().toString();
                StringRequest peticionAgrega = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        respuetaCierra(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error de conexion, intenta de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("valor", c);
                        return parametros;
                    }
                };
                RequestQueue cola = Volley.newRequestQueue(getContext());
                cola.add(peticionAgrega);
            }else{
                Toast.makeText(getContext(), "La puerta ya esta cerrada", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void respuetaCierra(String response) {
        try {
            JSONObject r = new JSONObject(response);
            if (r.getString("add").compareTo("y") == 0) {
                Toast.makeText(getContext(), "Cerrando Puerta", Toast.LENGTH_SHORT).show();
                tvPuertaMensaje.setText("Puerta Cerrada");
            } else {
                Toast.makeText(getContext(), "No se pudo agregar " + r, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
        }
    }

    private void puertaAbre() {
        try {
            if(!tvPuertaMensaje.getText().toString().equals("Puerta Abierta")){
                String url = Uri.parse(conecxion.URL + "sensorp.php")
                        .buildUpon()
                        .build().toString();
                StringRequest peticionAgrega = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        respuetaAbre(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error de conexion, intenta de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametros = new HashMap<>();
                        parametros.put("valor", a);
                        return parametros;
                    }
                };
                RequestQueue cola = Volley.newRequestQueue(getContext());
                cola.add(peticionAgrega);
            }else{
                Toast.makeText(getContext(), "La puerta ya esta abierta", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void respuetaAbre(String response) {
        try {
            JSONObject r = new JSONObject(response);
            if (r.getString("add").compareTo("y") == 0) {
                Toast.makeText(getContext(), "Abriendo Puerta", Toast.LENGTH_SHORT).show();
                tvPuertaMensaje.setText("Puerta Abierta");
            } else {
                Toast.makeText(getContext(), "No se pudo abrir " + r, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
        }
    }
}
