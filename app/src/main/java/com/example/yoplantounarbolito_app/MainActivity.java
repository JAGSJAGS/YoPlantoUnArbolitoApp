package com.example.yoplantounarbolito_app;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText name, email, phone;
    RequestQueue request;
    JsonObjectRequest JOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.editTextName);
        email = (EditText) findViewById(R.id.editTextEmail);
        phone = (EditText) findViewById(R.id.editTextPhone);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivity.this,"no se pudo registrar "+error,Toast.LENGTH_LONG).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(MainActivity.this, "Se registro correctamente", Toast.LENGTH_LONG).show();
        //progreso.hide();
    }

    private void storeUser() {

        request = Volley.newRequestQueue(this);
        String url = "https://calm-fjord-08371.herokuapp.com/api/users";
        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("points", "0");
        params.put("password", "password");
        params.put("password_confirmation", "password");
        JSONObject parameters = new JSONObject(params);
        url = url.replace(" ","%20");
        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters, this ,this);
        request.add(JOR);
    }

    public void OnclickRegister(View view) {
        storeUser();
    }
}