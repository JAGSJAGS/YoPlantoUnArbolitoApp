package app.yo_planto.yoplantounarbolito_app;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.classes.Action;
import app.yo_planto.yoplantounarbolito_app.classes.User;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.ActionDatabase;
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

public class TreeCareActivity extends AppCompatActivity {

    //Request
    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables;
    String url;

    //botones
    Button button_regar, button_limpiar, button_abonar, button_establecido;

    //preferencias
    Preferences preferences;

    //User
    User user;
    UserDatabase user_database;

    //Action
    Action action;
    ActionDatabase action_database;

    //validaciones
    Validations validations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_care);
        action = new Action();
        action_database = new ActionDatabase();
        validations = new Validations();
        variables = new Variables();
        url = variables.getUrl();
        preferences = new Preferences(TreeCareActivity.this);
        Toast.makeText(TreeCareActivity.this, "Id de arbol en preferencia:"+preferences.getTreeId(), Toast.LENGTH_SHORT).show();
        Toast.makeText(TreeCareActivity.this, "Id de user en preferencia:"+preferences.getUserId(), Toast.LENGTH_SHORT).show();

        button_regar = findViewById(R.id.button_regar);
        button_limpiar = findViewById(R.id.button_limpiar);
        button_abonar = findViewById(R.id.button_abonar);
        button_establecido = findViewById(R.id.button_establecido);

        button_regar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TreeCareActivity.this,"funciona el boton",Toast.LENGTH_LONG).show();
                waterTree();
            }
        });
        button_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TreeCareActivity.this,"funciona el boton",Toast.LENGTH_LONG).show();
                cleanTree();
            }
        });
        button_abonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TreeCareActivity.this,"funciona el boton",Toast.LENGTH_LONG).show();
                fertiliceTree();
            }
        });
        button_establecido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TreeCareActivity.this,"funciona el boton",Toast.LENGTH_LONG).show();
                greepTree();
            }
        });
    }

    private void waterTree(){
        action.setName("Regar");
        action();

    }
    private void cleanTree(){
        action.setName("Limpieza");
        action();

    }
    private void fertiliceTree(){
        action.setName("Abono");
        action();
    }
    private void greepTree(){
        action.setName("Agarre");
        action();
    }

    private void action(){

        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put(action_database.getName(), action.getName());
        params.put(action_database.getUser_id(), preferences.getUserId());
        params.put(action_database.getTree_id(), preferences.getTreeId());
        params.put(action_database.getLat(), action.getLat() + "");
        params.put(action_database.getLng(), action.getLng() + "");
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url  + "/actions", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(TreeCareActivity.this, "Correcto:"+preferences.getTreeId(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                validations.errors(error,TreeCareActivity.this);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + preferences.getToken());
                return headers;
            }
        };
        request.add(JOR);
    }
}