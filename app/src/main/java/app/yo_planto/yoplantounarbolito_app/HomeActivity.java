package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    SharedPreferences preference;
    String token;
    String user_id;

    //buttons
    Button button_log_out, button_your_tree, button_orphanage, button_ranking, button_games, button_profile, button_register_tree ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token","");
        user_id = preference.getString("user_id","");
        url  = variables.getUrl();

        //buttons
        button_your_tree = findViewById(R.id.button_home_your_tree);
        button_orphanage = findViewById(R.id.button_home_orphanage);
        button_ranking = findViewById(R.id.button_home_ranking);
        button_games = findViewById(R.id.button_home_games);
        button_profile = findViewById(R.id.button_home_profile);
        button_register_tree = findViewById(R.id.button_home_register_tree);
        //button_log_out = findViewById(R.id.button_log_out);

        button_your_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ifHaveTree() ){
                    Intent seeTree = new Intent(getApplicationContext(), SeeTreeActivity.class);
                    startActivity(seeTree);
                }
                else{
                    Intent registerTree = new Intent(getApplicationContext(), RegisterTreeActivity.class);
                    startActivity(registerTree);
                }
            }
        });
        button_orphanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orphanageTree = new Intent(getApplicationContext(), OrphanageActivity.class);
                startActivity(orphanageTree);
            }
        });

        button_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rankingUser = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(rankingUser);
            }
        });
        button_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gamesTree = new Intent(getApplicationContext(), GamesActivity.class);
                startActivity(gamesTree);
            }
        });
        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileUser = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileUser);
            }
        });
        button_register_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerTree = new Intent(getApplicationContext(), RegisterTreeActivity.class);
                startActivity(registerTree);
            }
        });
    }


    //cerrar sesion
    private void logOut(String url){
        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deletePreferences();
                Toast.makeText(HomeActivity.this,response+"",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(HomeActivity.this,"Erroraso: "+jsonError,Toast.LENGTH_LONG).show();
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

    //si tiene un arbol registrado
    private boolean ifHaveTree(){
        return true;
    }

}