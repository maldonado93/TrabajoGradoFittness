package com.example.uer.trabajogradofittness.Persona;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uer.trabajogradofittness.GlobalState;
import com.example.uer.trabajogradofittness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cuenta extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    View v;
    GlobalState gs;

    EditText etPassword;
    EditText etNewPassword;
    Button btnConfirmar;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_cuenta, container, false);

        etPassword = v.findViewById(R.id.etPassword);
        etNewPassword = v.findViewById(R.id.etNewPassword);
        btnConfirmar = v.findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarPass();
            }
        });

        return v;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs = (GlobalState) getActivity().getApplication();

        request = Volley.newRequestQueue(getActivity().getApplicationContext());
    }
    private void verificarPass(){
        String pass = etPassword.getText().toString();
        String nPass = etNewPassword.getText().toString();
        if(pass.compareTo("") != 0 && nPass.compareTo("") != 0){
            if(pass.compareTo(gs.getPassword()) == 0){
                    gs.setPassword(nPass);
                    etPassword.setText("");
                    etNewPassword.setText("");
                    guardarPass(nPass);
            }
            else{
                Toast.makeText(getContext(), "Contraseña actual erronea!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(), "Campos vacios!", Toast.LENGTH_SHORT).show();

        }
    }

    private void guardarPass(String password){
        String url = "http://"+gs.getIp()+"/usuario/actualizar_password.php?idPersona="+ gs.getSesion_usuario()+"&password="+password;

        url = url.replace(" ", "%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {

        int tipoUsuario = 0;
        boolean usuarioValido = false;

        JSONArray datos = response.optJSONArray("usuario");
        JSONObject jsonObject = null;

        try {
            jsonObject = datos.getJSONObject(0);
            if(jsonObject.optInt("id") != 0){
                Toast.makeText(getContext(),"Contraseña actualizada!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(),"Error al actualizar!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }
}
