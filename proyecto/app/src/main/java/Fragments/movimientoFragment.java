package Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ignacio.invernaderoapp.conecxion;

import org.json.JSONArray;

public class movimientoFragment extends Fragment {
    private RecyclerView rvSensorMovimiento;
    String lista[][];

    public movimientoFragment(){

    }
    public static movimientoFragment getInstance(String param1, String param2){
        movimientoFragment movimientoF = new movimientoFragment();
        return movimientoF;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movimiento_fragment,container,false);
        rvSensorMovimiento = (RecyclerView) view.findViewById(R.id.rv_sensor_movimiento);
        rvSensorMovimiento.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        llenarSensorMovimiento();
        return view;
    }

    private void llenarSensorMovimiento() {

        try {
            String url = Uri.parse(conecxion.URL + "sensora.php")
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
            rvSensorMovimiento.setAdapter(new adaptador(lista));

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error Inesperado " + e, Toast.LENGTH_SHORT).show();
        }
    }
}
