package com.example.yoplantounarbolito_app;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {//implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText email, password;
    Button btnLogin;

    RequestQueue request;
    JsonObjectRequest JOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextEmailLogin);
        password = findViewById(R.id.editTextPasswordLogin);
    }

    public void OnclickRegister(View view) {
        Toast.makeText(LoginActivity.this,"buton apretado ",Toast.LENGTH_LONG).show();
        validateUser("https://calm-fjord-08371.herokuapp.com/api/login");
    }

    private void validateUser(String url){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        /*params.put("name", "amadeo2");
        params.put("email", "amadeo2@gmail.com");
        params.put("phone", "4564665");
        params.put("points", "0");
        params.put("password", "password");
        params.put("password_confirmation", "password");*/

        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(LoginActivity.this, "Response: "+ response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(LoginActivity.this,"error: "+jsonError,Toast.LENGTH_LONG).show();
                    Log.i("ErrorVolley",jsonError);
                }
            }
        });
        request.add(JOR);
    }
}