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
import app.yo_planto.yoplantounarbolito_app.classes.User;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.UserDatabase;
import app.yo_planto.yoplantounarbolito_app.java_class.Preferences;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {//implements Response.Listener<JSONObject>,Response.ErrorListener {

    //request
    RequestQueue request;
    JsonObjectRequest JOR;
    String url;
    Validations validations = new Validations();

    //interface
    UserDatabase user_database;

    //layouts
    TextView errors;
    EditText email, password;

    //Preferences
    Preferences preferences;



    Variables variables = new Variables();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_login);
        url = variables.getUrl();
        email = findViewById(com.example.yoplantounarbolito_app.R.id.editTextEmailLogin);
        password = findViewById(com.example.yoplantounarbolito_app.R.id.editTextPasswordLogin);
        user_database = new UserDatabase();
        preferences = new Preferences(LoginActivity.this);
    }

    public void OnclickLogin(View view) {
        loginUser();
    }

    private void loginUser(){
        request = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put(user_database.getEmail(), email.getText().toString());
        params.put(user_database.getPassword(), password.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url + "/login", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(LoginActivity.this,"id en login:" + response,Toast.LENGTH_LONG).show();
                try {
                    String token = response.getString("access_token");
                    String user_id = response.getString("user_id");
                    preferences.savePreferencesUser(token, user_id);
                    Intent homeTreeActivity = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(homeTreeActivity);
                    finishAffinity();
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"Se produjo un errorsss",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                validations.validateDatas(jsonError, LoginActivity.this);
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

    public void OnclickGoToRegisterActivity(View view) {
        Intent registerUser = new Intent(getApplicationContext(),RegisterUserActivity.class);
        startActivity(registerUser);
        //finish();
    }
}