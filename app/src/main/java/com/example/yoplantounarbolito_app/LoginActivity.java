package com.example.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.validations.Validations;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {//implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText email, password;
    RequestQueue request;
    JsonObjectRequest JOR;
    Validations validations = new Validations();
    TextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextEmailLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        errors = findViewById(R.id.textViewErrorsLogin);
        errors.setVisibility(View.GONE);
    }

    public void OnclickLogin(View view) {
        errors.setVisibility(View.GONE);
        loginUser("https://calm-fjord-08371.herokuapp.com/api/login");
    }

    private void loginUser(String url){
        request = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    String user_id = response.getString("user_id");
                    Toast.makeText(LoginActivity.this,"id en login:" + user_id,Toast.LENGTH_LONG).show();
                    savePreferences(token, user_id);
                    Intent adoptTreeActivity = new Intent(getApplicationContext(),AdoptTreeActivity.class);
                    startActivity(adoptTreeActivity);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"Se produjo un error",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                validations.validateDatas(jsonError,errors);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type", "application/vnd.api+json");
                return headers;
            }
        };
        request.add(JOR);
    }

    private void savePreferences(String token, String user_id){
        SharedPreferences preferences= getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.putString("user_id", user_id);
        editor.commit();
    }

    public void OnclickGoToRegisterActivity(View view) {
        Intent registerUser = new Intent(getApplicationContext(),RegisterUserActivity.class);
        startActivity(registerUser);
        //finish();
    }
}