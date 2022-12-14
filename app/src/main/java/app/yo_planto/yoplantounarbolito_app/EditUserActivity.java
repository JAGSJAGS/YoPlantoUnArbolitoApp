package app.yo_planto.yoplantounarbolito_app;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.progressindicator.LinearProgressIndicator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    EditText email, phone, /*password, password_confirmation*/ age, organization, firstname, lastname;

    //request
    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    //validaciones
    Validations validations = new Validations();

    //interfaces
    User user;
    UserDatabase user_database;

    //preferencias
    Preferences preferences;

    LinearProgressIndicator linear_progres;
    Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        url = variables.getUrl();
        user = new User();
        user_database = new UserDatabase();
        preferences = new Preferences(EditUserActivity.this);
        linear_progres = findViewById(R.id.linear_progres_13);
        linear_progres.setVisibility(View.GONE);
        params = new HashMap<>();

        firstname = findViewById(R.id.editTextfirstnameEditUser);
        lastname = findViewById(R.id.editTextLastNameEditUser);
        email = findViewById(R.id.editTextEmailEditUser);
        age = findViewById(R.id.editTextAgeEditUser);
        organization = findViewById(R.id.editTextOrganizationEditUser);
        phone = findViewById(R.id.editTextPhoneEditUser);
        /*password = findViewById(R.id.editTextPasswordEditUser);
        password_confirmation = findViewById(R.id.editTextConfirmationPasswordEditUser);*/
    }

    private void editUser(){

        request = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.PATCH, url + "/auth/register", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent mainTree = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainTree);
                Toast.makeText(EditUserActivity.this,"Cambio exitoso",Toast.LENGTH_SHORT).show();
                finishAffinity();

                linear_progres.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                validations.errors(error,EditUserActivity.this);
                linear_progres.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + preferences.getToken());
                return headers;
            }
        };
        request.add(JOR);
    }

    public void OnclickEdiUser(View view) {
        if(!firstname.getText().toString().equals(""))
        params.put(user_database.getFirstname(), firstname.getText().toString());
        if(!lastname.getText().toString().equals(""))
        params.put(user_database.getLastname(), lastname.getText().toString());
        if(!email.getText().toString().equals(""))
        params.put(user_database.getEmail(), email.getText().toString());
        if(!age.getText().toString().equals(""))
        params.put(user_database.getAge(), age.getText().toString());
        if(!organization.getText().toString().equals(""))
        params.put(user_database.getOrganization(), organization.getText().toString());
        if(!phone.getText().toString().equals(""))
        params.put(user_database.getPhone(), phone.getText().toString());/*
        if(password.getText().toString() != "")
        params.put(user_database.getPassword(), password.getText().toString());
        if(password_confirmation.getText().toString() != "")
        params.put(user_database.getPassword_confirmation(), password_confirmation.getText().toString());*/

        linear_progres.setVisibility(View.VISIBLE);
        editUser();
    }
}