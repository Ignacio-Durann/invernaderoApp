package Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ignacio.invernaderoapp.R;
import com.ignacio.invernaderoapp.adaptador;
import com.ignacio.invernaderoapp.adaptadorUsuarios;
import com.ignacio.invernaderoapp.conecxion;

import org.json.JSONArray;

public class calorFragment extends Fragment {
    private RecyclerView rvSensorCalor;
    String lista[][];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calor_fragment,container,false);
        rvSensorCalor = (RecyclerView) view.findViewById(R.id.rv_sensor_calor);
        rvSensorCalor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        llenarSensorCalor();
        return view;
    }
public calorFragment(){

}
    public static calorFragment getInstance(String param1, String param2) {
        calorFragment calorF = new calorFragment();
        return calorF;
    }
    private void llenarSensorCalor() {
        try {
            String url = Uri.parse(conecxion.URL + "sensort.php")
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
            lista = new String[response.length()][4];
            for (int i = 0; i < response.length(); i++) {
                lista[i][0] = response.getJSONObject(i).getString("id");
                lista[i][1] = response.getJSONObject(i).getString("user");;
                lista[i][2] = response.getJSONObject(i).getString("valor");
                lista[i][3] = response.getJSONObject(i).getString("fecha");

            }
            rvSensorCalor.setAdapter(new adaptador(lista));

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Inesperado " + e, Toast.LENGTH_SHORT).show();
        }
    }
}
