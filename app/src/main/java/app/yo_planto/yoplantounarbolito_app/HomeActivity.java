package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.classes.User;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.UserDatabase;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    //Request
    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    //preferencias
    SharedPreferences preference;
    String token;
    String user_id;

    //buttons
    Button button_log_out, button_your_tree, button_orphanage, button_ranking, button_games, button_profile, button_register_tree ;

    //datos de Usuario
    User user;
    UserDatabase user_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = new User();
        user_database = new UserDatabase();
        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token","");
        user_id = preference.getString("user_id","");
        url  = variables.getUrl();

        //metodos
        getUser();

        //buttons
        button_your_tree = findViewById(R.id.button_home_your_tree);
        button_orphanage = findViewById(R.id.button_home_orphanage);
        button_ranking = findViewById(R.id.button_home_ranking);
        button_games = findViewById(R.id.button_home_games);
        button_profile = findViewById(R.id.button_home_profile);
        button_register_tree = findViewById(R.id.button_home_register_tree);
        button_log_out = findViewById(R.id.button_home_log_out);

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
                /*Intent profileUser = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileUser);*/

                showUser();
            }
        });
        button_register_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerTree = new Intent(getApplicationContext(), RegisterTreeActivity.class);
                startActivity(registerTree);
            }
        });

        button_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }


    //cerrar sesion
    private void logOut(){
        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.POST, url + "/logout", null, new Response.Listener<JSONObject>() {
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
        editor.putString("user_id","");
        editor.commit();

        SharedPreferences preferencesTree= getSharedPreferences("preferenceTree", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTree = preferencesTree.edit();
        editor.putString("tree_id","");
        editorTree.commit();
    }

    //si tiene un arbol registrado
    private boolean ifHaveTree(){
        return true;
    }

   //cargar usuario

    private void showUser() {
       /* name = "nombre de usuario";
        email = "daniela@gmail.com";
        phone = "nombre de usuario";*/
        String message = "\n" + user.getName() + "\n" + "\n" + user.getEmail() + "\n" + "\n" + user.getPhone() + "\n";
        new MaterialAlertDialogBuilder(this)
                .setTitle("Mis Datos")
                .setMessage(message)
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                    }
                })
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Aborting mission...");
                    }
                }).show();
    }

    private void getUser() {

        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url +"/users/" + user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.setName(response.getString(user_database.getName()));
                    user.setEmail(response.getString(user_database.getEmail()));
                    user.setPhone(response.getString(user_database.getPhone()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                Toast.makeText(HomeActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
                Log.e("ErrorVolley", jsonError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                token = preference.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }

}