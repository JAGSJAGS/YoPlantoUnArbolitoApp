package app.yo_planto.yoplantounarbolito_app;

import static app.yo_planto.yoplantounarbolito_app.variables.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import app.yo_planto.yoplantounarbolito_app.validations.Validations;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register_user_activity extends AppCompatActivity {

    EditText name, email, phone, password, password_confirmation;
    TextView errors;

    RequestQueue request;
    JsonObjectRequest JOR;

    Validations validations = new Validations();

    Bundle send_data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_register_user);

        name = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_name_register_user);
        email = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_email_register_user);
        phone = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_phone_register_user);
        password = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_password_register_user);
        password_confirmation = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_confirmation_password_register_user);
        errors = findViewById(R.id.text_error);
        errors.setVisibility(View.GONE);
        send_data = new Bundle();
    }

    private void registerUser(String url) {
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
                try {
                    String id = response.getString("id");
                    send_data.putString("user_id",id);
                    loginUser(url_user_login);
                } catch (JSONException e) {
                    Toast.makeText(register_user_activity.this, "Se produjo un error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse network_response = error.networkResponse;
                String jsonError = new String(network_response.data);
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
        registerUser(url_user_register);
    }

    //Login
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
                    savePreferences(token, user_id);
                    Intent register_tree_activity = new Intent(getApplicationContext(), app.yo_planto.yoplantounarbolito_app.register_tree_activity.class);
                    register_tree_activity.putExtras(send_data);
                    startActivity(register_tree_activity);
                } catch (JSONException e) {
                    Toast.makeText(register_user_activity.this,"Se produjo un error",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse network_response = error.networkResponse;
                String json_error = new String(network_response.data);
                validations.validateDatas(json_error,errors);
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