package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.*;
import static app.yo_planto.yoplantounarbolito_app.variables.*;

public class main_activity extends AppCompatActivity {

    RequestQueue request;
    JsonObjectRequest JOR;
    String token;
    SharedPreferences preference;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(validaPermisos()){
            authenticated(url_user_authenticated);
        }
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA, ACCESS_FINE_LOCATION},100);
        }
        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(main_activity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION},100);
            }
        });
        dialogo.show();
    }


    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results) {
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
        if(request_code==100){
            if(grant_results.length==3 && grant_results[0]==PackageManager.PERMISSION_GRANTED
                    && grant_results[1]==PackageManager.PERMISSION_GRANTED
                    && grant_results[2]==PackageManager.PERMISSION_GRANTED){
                authenticated(url_user_authenticated);
            }else{
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"Si","No"};
        final AlertDialog.Builder alert_opciones = new AlertDialog.Builder(main_activity.this);
        alert_opciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alert_opciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alert_opciones.show();
    }
    private void authenticated(String url){
        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent register_user = new Intent(getApplicationContext(), adopt_tree_activity.class);
                startActivity(register_user);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent login = new Intent(getApplicationContext(), login_activity.class);
                startActivity(login);
                finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
                token = preference.getString("token","");
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }
}