package Fragments;

import android.annotation.SuppressLint;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ignacio.invernaderoapp.R;
import com.ignacio.invernaderoapp.adaptadorUsuarios;
import com.ignacio.invernaderoapp.conecxion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class mainFragment extends Fragment {
    private EditText etUsuario, etContra;
    private Button btnAgregar;
    private RecyclerView rvTodosUsuarios;
    private Spinner spTipo;

    SharedPreferences sesion;
    String lista[][];

    public mainFragment() {
    }

    public static mainFragment getInstance(String param1, String param2) {
        mainFragment mainF = new mainFragment();
        return mainF;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        etUsuario = (EditText) view.findViewById(R.id.et_main_usuario);
        etContra = (EditText) view.findViewById(R.id.et_main_contrasena);
        spTipo = (Spinner) view.findViewById(R.id.main_spinner);
        btnAgregar = (Button) view.findViewById(R.id.btn_main_agregar);
        rvTodosUsuarios = (RecyclerView) view.findViewById(R.id.rv_main_todos_usuarios);
        rvTodosUsuarios.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        llenar();
        acciones();
        //opciones del spinner
        String[] datos = new String[] {"A", "U", "V"};
        ArrayAdapter<String> adapterSpiner = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datos);
        spTipo.setAdapter(adapterSpiner);

        return view;
    }

    private void acciones() {
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               agregarUsuario();
            }
        });
    }

    private void agregarUsuario() {
        try {
           if ((!etUsuario.getText().toString().isEmpty() ) && (!etContra.getText().toString().isEmpty())) {
                String url = Uri.parse(conecxion.URL + "agregaUser.php")
                        .buildUpon()
                        .build().toString();
                StringRequest peticionAgrega = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        agregaRespueta(response);
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
                        parametros.put("user", etUsuario.getText().toString());
                        parametros.put("password", etContra.getText().toString());
                        parametros.put("tipo", spTipo.getSelectedItem().toString());
                        return parametros;
                    }
                };
                RequestQueue cola = Volley.newRequestQueue(getContext());
                cola.add(peticionAgrega);
            } else {
                Toast.makeText(getContext(), "Ningun campo debe estar vácio", Toast.LENGTH_SHORT).show();
            }
       }catch (Exception e){
            Toast.makeText(getContext(), "Error "+e, Toast.LENGTH_LONG).show();
        }
    }

    private void agregaRespueta(String response) {
        try{
            JSONObject res = new JSONObject(response);
            if(res.getString("add").compareTo("y")==0){
                Toast.makeText(getContext(), "Usuario Agregado", Toast.LENGTH_SHORT).show();
                llenar();
                etUsuario.setText("");
                etContra.setText("");
            }else{
                Toast.makeText(getContext(), "No se pudo agregar el usuario", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(getContext(), "Error inesperado", Toast.LENGTH_SHORT).show();
        }
    }


    private void llenar() {
        try {
            String url = Uri.parse(conecxion.URL + "agregaUser.php")
                    .buildUpon()
                    .build().toString();
            JsonArrayRequest peticion = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    llenarRespuesta(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error de red " + error, Toast.LENGTH_LONG).show();

                }
            });
            RequestQueue cola = Volley.newRequestQueue(getContext());
            cola.add(peticion);
        }catch(Exception e){
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void llenarRespuesta(JSONArray response) {
        try {
            lista = new String[response.length()][3];
            for (int i = 0; i < response.length(); i++) {
                lista[i][0] = response.getJSONObject(i).getString("user");
                lista[i][1] = "********";
                lista[i][2] = response.getJSONObject(i).getString("tipo");

            }
            rvTodosUsuarios.setAdapter(new adaptadorUsuarios(lista));

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Inesperado " + e, Toast.LENGTH_SHORT).show();
        }
    }


}
