package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AdoptTreeActivity extends AppCompatActivity {

    RequestQueue request;
    JsonObjectRequest JOR;

    //mostrar arbolito
    String titleTree = "Tines que adoptar un arbolito" ,title_tree,lat_tree = "0.0", ln_tree="0.0", name_tree, avatar, path_photo, state;

    //mostrar user
    SharedPreferences preference;
    String token;
    String user_id;
    String name_t;
    String state_t;


    //View
    LinearLayout view_home, view_tree, view_options;
    ImageButton button_see_options, button_home, button_log_out;

    //Fragments
    FragmentTransaction transaction;
    Fragment fragment_home, fragment_options, fragment_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_tree);

        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token","");
        user_id = preference.getString("user_id","");

        getUser("https://calm-fjord-08371.herokuapp.com/api/users/" + user_id);

        getUsersTrees("https://calm-fjord-08371.herokuapp.com/api/tree_users/" + user_id);

        //buttons
        button_home = findViewById(R.id.button_home);
        button_see_options = findViewById(R.id.button_options);
        button_log_out = findViewById(R.id.button_log_out);

        //fragments
        fragment_home = new HomeFragment();
        fragment_options = new OptionsFragment();
        fragment_status = new StatusFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, fragment_home).commit();

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragments_container, fragment_home);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button_see_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragments_container, fragment_options);
                transaction.addToBackStack(null);
                transaction.commit();
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
                    name_t = response.getString("name");
                    lat_tree = response.getString("lat");
                    ln_tree = response.getString("lng");
                    avatar = response.getString("avatar");
                    path_photo = response.getString("photo");
                    state_t = response.getString("state");
                    title_tree = "Hola, mi nombre es " + name_tree + ",gracias por elegirme!!";
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