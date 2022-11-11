package com.example.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterTreeActivity extends AppCompatActivity {

    EditText name, geolocalitation, avatar, photo, state;
    RequestQueue request;
    JsonObjectRequest JOR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tree);
        name = findViewById(R.id.editTextNameRegisterTree);
        geolocalitation = findViewById(R.id.editTextGeolocalitationRegisterTree);
        avatar = findViewById(R.id.editTextAvatarRegisterTree);
        photo = findViewById(R.id.editTextPhotoRegisterTree);
        state = findViewById(R.id.editTextStateRegisterTree);
    }

    private void registerTree(String url){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("geolocation", geolocalitation.getText().toString());
        params.put("avatar", avatar.getText().toString());
        params.put("photo", photo.getText().toString());
        params.put("state", state.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(RegisterTreeActivity.this, "Se registro correctamente: "+response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(RegisterTreeActivity.this,"error: "+jsonError,Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type", "application/vnd.api+json");
                return headers;
            }
        };
        request.add(JOR);
    }

    public void OnclickRegisterTree(View view) {
        Toast.makeText(RegisterTreeActivity.this,"Registrando",Toast.LENGTH_LONG).show();
        registerTree("https://calm-fjord-08371.herokuapp.com/api/trees");
    }
}