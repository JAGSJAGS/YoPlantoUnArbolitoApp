package com.example.yoplantounarbolito_app;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity {

    EditText name, email, phone, password, password_confirmation;
    RequestQueue request;
    JsonObjectRequest JOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        name = findViewById(R.id.editTextNameRegisterUser);
        email = findViewById(R.id.editTextEmailRegisterUser);
        phone = findViewById(R.id.editTextPhoneRegisterUser);
        password = findViewById(R.id.editTextPasswordRegisterUser);
        password_confirmation = findViewById(R.id.editTextConfirmationPasswordRegisterUser);
    }

    private void registerUser(String url){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("points", "0");
        params.put("password", password.getText().toString());
        params.put("password_confirmation", password_confirmation.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(RegisterUserActivity.this, "Se registro correctamente: "+response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(RegisterUserActivity.this,"error: "+jsonError,Toast.LENGTH_LONG).show();
                }
            }
        });
        request.add(JOR);
    }

    public void OnclickRegister(View view) {
        Toast.makeText(RegisterUserActivity.this,"Registrando",Toast.LENGTH_LONG).show();
        registerUser("https://calm-fjord-08371.herokuapp.com/api/users");
    }
}