package app.yo_planto.yoplantounarbolito_app;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.classes.User;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.UserDatabase;
import app.yo_planto.yoplantounarbolito_app.java_class.Preferences;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity {

    EditText email, phone, password, password_confirmation, age, organization, firstname, lastname;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_register_user);
        url = variables.getUrl();
        user = new User();
        user_database = new UserDatabase();
        preferences = new Preferences(RegisterUserActivity.this);
        linear_progres = findViewById(R.id.linear_progres_3);
        linear_progres.setVisibility(View.GONE);

        firstname = findViewById(R.id.editTextFirstNameRegisterUser);
        lastname = findViewById(R.id.editTextLastNameRegisterUser);
        email = findViewById(R.id.editTextEmailRegisterUser);
        age = findViewById(R.id.editTextAgeRegisterUser);
        organization = findViewById(R.id.editTextOrganizationRegisterUser);
        phone = findViewById(R.id.editTextPhoneRegisterUser);
        password = findViewById(R.id.editTextPasswordRegisterUser);
        password_confirmation = findViewById(R.id.editTextConfirmationPasswordRegisterUser);
    }

    private void registerUser(){

        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put(user_database.getFirstname(), firstname.getText().toString());
        params.put(user_database.getLastname(), lastname.getText().toString());
        params.put(user_database.getEmail(), email.getText().toString());
        params.put(user_database.getAge(), age.getText().toString());
        params.put(user_database.getOrganization(), organization.getText().toString());
        params.put(user_database.getPhone(), phone.getText().toString());
        params.put(user_database.getPoints(), "0");
        params.put(user_database.getPassword(), password.getText().toString());
        params.put(user_database.getPassword_confirmation(), password_confirmation.getText().toString());
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url + "/auth/register", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("accessToken");
                    JSONObject user = response.getJSONObject("user");
                    String user_id = user.getString("id");
                    preferences.savePreferencesUser(token, user_id);
                    //Toast.makeText(RegisterUserActivity.this,"id:" + user_id,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(RegisterUserActivity.this,"token:" + token,Toast.LENGTH_SHORT).show();
                    Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainActivity);
                    finishAffinity();

                } catch (JSONException e) {
                    Toast.makeText(RegisterUserActivity.this,"Se produjo un error",Toast.LENGTH_SHORT).show();
                }
                linear_progres.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                validations.errors(error,RegisterUserActivity.this);
                linear_progres.setVisibility(View.GONE);
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
        linear_progres.setVisibility(View.VISIBLE);
        registerUser();
    }
}