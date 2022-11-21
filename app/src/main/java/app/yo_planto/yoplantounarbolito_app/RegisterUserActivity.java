package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity {

    EditText name, email, phone, password, password_confirmation;
    TextView errors;

    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    Validations validations = new Validations();

    //Bundle sendData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_register_user);
        url = variables.getUrl();

        name = findViewById(com.example.yoplantounarbolito_app.R.id.editTextNameRegisterUser);
        email = findViewById(com.example.yoplantounarbolito_app.R.id.editTextEmailRegisterUser);
        phone = findViewById(com.example.yoplantounarbolito_app.R.id.editTextPhoneRegisterUser);
        password = findViewById(com.example.yoplantounarbolito_app.R.id.editTextPasswordRegisterUser);
        password_confirmation = findViewById(com.example.yoplantounarbolito_app.R.id.editTextConfirmationPasswordRegisterUser);
        errors = findViewById(R.id.textError);
        errors.setVisibility(View.GONE);
        //sendData = new Bundle();
    }

    private void registerUser(){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("email", email.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("points", "0");
        params.put("password", password.getText().toString());
        params.put("password_confirmation", password_confirmation.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url + "/users", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.getString("id");
                    //sendData.putString("user_id",id);
                    loginUser();
                } catch (JSONException e) {
                    Toast.makeText(RegisterUserActivity.this, "Se produjo un error", Toast.LENGTH_LONG).show();
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

    public void OnclickRegister(View view) {
        errors.setVisibility(View.GONE);
        registerUser();
    }

    //Login
    private void loginUser(){
        request = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url+"/login", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("access_token");
                    String user_id = response.getString("user_id");
                    savePreferences(token, user_id);
                    Intent registerTreeActivity = new Intent(getApplicationContext(),RegisterTreeActivity.class);
                    //registerTreeActivity.putExtras(sendData);
                    startActivity(registerTreeActivity);
                } catch (JSONException e) {
                    Toast.makeText(RegisterUserActivity.this,"Se produjo un error",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                validations.validateDatas(jsonError,errors);
                Toast.makeText(RegisterUserActivity.this,"son errores"+jsonError,Toast.LENGTH_LONG).show();
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
}