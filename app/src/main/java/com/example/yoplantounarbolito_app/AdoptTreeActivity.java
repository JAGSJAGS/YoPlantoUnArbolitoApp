package com.example.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class AdoptTreeActivity extends AppCompatActivity {

    RequestQueue request;
    JsonObjectRequest JOR;




    //mostrar arbolito
    String name = "Tines que adoptar un arbolito" , lat, lng, avatar, path_photo, state;
    TextView textViewTitle;

    //mostrar user
    SharedPreferences preference;
    String token;
    String user_id;

    //View
    LinearLayout view_home, view_tree, view_options;
    Button button_see_tree, button_see_options, button_home, button_log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_tree);
        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token","");
        user_id = preference.getString("user_id","");

        textViewTitle = findViewById(R.id.textViewTitleArbolito);
        textViewTitle.setText(name);
        Toast.makeText(AdoptTreeActivity.this,"id en tree:" + user_id,Toast.LENGTH_LONG).show();
        getUsersTrees("https://calm-fjord-08371.herokuapp.com/api/tree_users/"+user_id);


        //View
        view_home = findViewById(R.id.view_home);
        view_tree = findViewById(R.id.view_see_tree);
        view_options = findViewById(R.id.view_options);
        view_home.setVisibility(View.VISIBLE);
        view_tree.setVisibility(View.GONE);
        view_options.setVisibility(View.GONE);

        //buttons
        button_home = findViewById(R.id.button_home);
        button_see_tree = findViewById(R.id.button_see_tree);
        button_see_options = findViewById(R.id.button_options);
        button_log_out = findViewById(R.id.button_log_out);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_home.setVisibility(View.VISIBLE);
                view_tree.setVisibility(View.GONE);
                view_options.setVisibility(View.GONE);
            }
        });

        button_see_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_home.setVisibility(View.GONE);
                view_tree.setVisibility(View.VISIBLE);
                view_options.setVisibility(View.GONE);
            }
        });

        button_see_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_home.setVisibility(View.GONE);
                view_tree.setVisibility(View.GONE);
                view_options.setVisibility(View.VISIBLE);
            }
        });

        button_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut("https://calm-fjord-08371.herokuapp.com/api/logout");
            }
        });
    }

    private void getUser(String url){
        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(AdoptTreeActivity.this,"respuesta: "+response,Toast.LENGTH_LONG).show();
                Log.i("neverita","neverita");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(AdoptTreeActivity.this,"Error: "+jsonError,Toast.LENGTH_LONG).show();
                    Log.i("ErrorVolley",jsonError);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }

    private void logOut(String url){
        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(AdoptTreeActivity.this,"respuesta: "+response,Toast.LENGTH_LONG).show();
                deletePreferences();
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(AdoptTreeActivity.this,"Error: "+jsonError,Toast.LENGTH_LONG).show();
                    Log.i("ErrorVolley",jsonError);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
                token = preference.getString("token","");
                Map<String, String> headers = new HashMap<>();
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }

    public void OnclickGetUser(View view) {
        getUser("https://calm-fjord-08371.herokuapp.com/api/users/1");
    }

    private void deletePreferences(){
        SharedPreferences preferences= getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token","");
        editor.commit();
    }


    private void getUsersTrees(String url){

        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name = response.getString("name");
                    textViewTitle.setText("Hola, mi nombre es " + name + ",gracias por elegirme!!");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(AdoptTreeActivity.this,"Error: "+jsonError,Toast.LENGTH_LONG).show();
                    Log.i("ErrorVolley",jsonError);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
                token = preference.getString("token","");
                Map<String, String> headers = new HashMap<>();
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }
}