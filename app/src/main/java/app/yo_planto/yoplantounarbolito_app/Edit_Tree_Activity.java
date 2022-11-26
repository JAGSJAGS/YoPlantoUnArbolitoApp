package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Tree_Activity extends AppCompatActivity {

    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    SharedPreferences preference;
    String token, id;

    EditText edit_name_tree;
    TextView errors;

    Button button_edit_name;

    Validations validations = new Validations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tree);

        url = variables.getUrl();
        edit_name_tree = findViewById(R.id.edit_text_name_edit_tree);
        errors = findViewById(R.id.text_view_error_edit_tree);
        button_edit_name = findViewById(R.id.button_name_edit_tree);

        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token","");
        id = preference.getString("id","");

        button_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Edit_Tree_Activity.this, "Llego", Toast.LENGTH_SHORT).show();
                editTree();
            }
        });
    }

    private void editTree(){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("name", edit_name_tree.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.PATCH, url + "/trees/" +id , parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Edit_Tree_Activity.this, "Exitoso"+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                validations.errors(error, Edit_Tree_Activity.this);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }
}