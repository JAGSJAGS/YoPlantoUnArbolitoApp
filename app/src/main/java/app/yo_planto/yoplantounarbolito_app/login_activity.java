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
import app.yo_planto.yoplantounarbolito_app.validations.Validations;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login_activity extends AppCompatActivity {//implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText email, password;
    RequestQueue request;
    JsonObjectRequest JOR;
    Validations validations = new Validations();
    TextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_login);
        email = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_email_login);
        password = findViewById(com.example.yoplantounarbolito_app.R.id.edit_text_password_login);
        errors = findViewById(R.id.text_view_errors_login);
        errors.setVisibility(View.GONE);
    }

    public void OnclickLogin(View view) {
        errors.setVisibility(View.GONE);
        loginUser(url_user_login);
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
                    Toast.makeText(login_activity.this,"id en login:" + user_id,Toast.LENGTH_LONG).show();
                    savePreferences(token, user_id);
                    Intent adoptTreeActivity = new Intent(getApplicationContext(), adopt_tree_activity.class);
                    startActivity(adoptTreeActivity);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(login_activity.this,"Se produjo un error",Toast.LENGTH_LONG).show();
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

    private void savePreferences(String token, String user_id){
        SharedPreferences preferences= getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.putString("user_id", user_id);
        editor.commit();
    }

    public void OnclickGoToRegisterActivity(View view) {
        Intent register_user = new Intent(getApplicationContext(), register_user_activity.class);
        startActivity(register_user);
        //finish();
    }
}