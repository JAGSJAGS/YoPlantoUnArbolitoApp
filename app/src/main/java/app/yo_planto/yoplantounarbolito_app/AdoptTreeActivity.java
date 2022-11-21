package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdoptTreeActivity extends AppCompatActivity {

    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    //mostrar arbolito
    String titleTree = "Tines que adoptar un arbolito" ,lat_tree = "0.0", ln_tree="0.0", name_tree, avatar, path_photo, state;
    TextView textViewTitle;

    //mostrar user
    SharedPreferences preference;
    String token;
    String user_id;
    TextView text_view_name_tree, text_view_state_tree;

    //View
    LinearLayout view_home, view_tree, view_options;
    ImageButton imagen_tree_button, button_see_options, button_home, button_log_out;
    Button button_edit_tree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_tree);

        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token","");
        user_id = preference.getString("user_id","");
        url  = variables.getUrl();

        textViewTitle = findViewById(R.id.textViewTitleArbolito);
        textViewTitle.setText(titleTree);
        text_view_name_tree = findViewById(R.id.text_view_name_tree);
        text_view_state_tree = findViewById(R.id.text_view_state_tree);

        getUser();
        getUsersTrees();



        //View
        view_home = findViewById(R.id.view_home);
        view_tree = findViewById(R.id.view_see_tree);
        view_options = findViewById(R.id.view_options);
        view_home.setVisibility(View.VISIBLE);
        view_tree.setVisibility(View.GONE);
        view_options.setVisibility(View.GONE);

        //buttons
        button_home = findViewById(R.id.button_home);
        imagen_tree_button = findViewById(R.id.image_tree_button);
        button_see_options = findViewById(R.id.button_options);
        button_log_out = findViewById(R.id.button_log_out);
        button_edit_tree = findViewById(R.id.button_edit_tree);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_home.setVisibility(View.VISIBLE);
                view_tree.setVisibility(View.GONE);
                view_options.setVisibility(View.GONE);
            }
        });

        imagen_tree_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seeTree = new Intent(getApplicationContext(),SeeTreeActivity.class);
                startActivity(seeTree);
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
                logOut(url + "/logout");
            }
        });

        button_edit_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_Tree = new Intent( AdoptTreeActivity.this, Edit_Tree_Activity.class);
                startActivity(edit_Tree);
            }
        });
    }

    private void getUser(){
        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url + "/users/" + user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                deletePreferences();
                Toast.makeText(AdoptTreeActivity.this,response+"",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AdoptTreeActivity.this,"Erroraso: "+jsonError,Toast.LENGTH_LONG).show();
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

    private void deletePreferences(){
        SharedPreferences preferences= getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token","");
        editor.commit();
    }

    private void getUsersTrees(){

        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url + "/tree_users/" + user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    name_tree = response.getString("name");
                    lat_tree = response.getString("lat");
                    ln_tree = response.getString("lng");
                    avatar = response.getString("avatar");
                    path_photo = response.getString("photo");
                    state = response.getString("state");
                    textViewTitle.setText("Hola, mi nombre es " + name_tree + ",gracias por elegirme!!");
                    text_view_name_tree.setText(name_tree);
                    text_view_state_tree.setText("Estado: " + state);


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