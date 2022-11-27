package app.yo_planto.yoplantounarbolito_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.Preference;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import app.yo_planto.yoplantounarbolito_app.classes.Tree;
import app.yo_planto.yoplantounarbolito_app.classes.TreeUser;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.TreeDatabase;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.TreeUserDatabase;
import app.yo_planto.yoplantounarbolito_app.java_class.Preferences;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterTreeActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Components
    EditText name;
    Validations validations = new Validations();


    //requests
    RequestQueue request;
    JsonObjectRequest JOR;
    Variables variables = new Variables();
    String url;

    //tree
    Tree tree;
    TreeDatabase tree_database;
    TreeUser tree_user;
    TreeUserDatabase tree_user_database;

    //Preferencias
    Preferences preferences;

    //Map
    private GoogleMap mMap;
    private Marker marcador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tree);
        tree = new Tree();
        tree_database = new TreeDatabase();
        tree_user = new TreeUser();
        tree_user_database = new TreeUserDatabase();
        preferences = new Preferences(RegisterTreeActivity.this);

        url = variables.getUrl();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_register_tree);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.editTextNameRegisterTree);
    }

    private void registerTree(){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", preferences.getUserId());
        params.put(tree_database.getName(), name.getText().toString());
        params.put(tree_database.getLat(), tree.getLat() + "");
        params.put(tree_database.getLng(), tree.getLng() + "");
        params.put(tree_database.getAvatar(), tree.getAvatar());
        params.put(tree_database.getPath_photo(), tree.getPath_photo());
        params.put(tree_database.getState(), tree.getState());
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url + "/trees", parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id_tree = response.getString("id");
                    preferences.savePreferencesTree(id_tree);
                    Intent photoActivity = new Intent(getApplicationContext(), RegisterPhotoActivity.class);
                    startActivity(photoActivity);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(RegisterTreeActivity.this,"Se produjo un error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                validations.errors(error, RegisterTreeActivity.this);
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

    public void OnclickRegisterTree(View view) {
        Toast.makeText(RegisterTreeActivity.this,"Registrando",Toast.LENGTH_LONG).show();
        registerTree();
    }

    //MAP LOGIC
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        miUbicacion();
    }

    private void agregarMarket(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas));
        mMap.animateCamera(miUbicacion);
    }

    private void actualizarUbicacion(Location location) {
        tree.setLat(location.getLatitude());
        tree.setLng(location.getLongitude());
        agregarMarket(tree.getLat(), tree.getLng());
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            actualizarUbicacion(location);
        }
    };

    private void miUbicacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0 , locationListener);
    }

    public void OnclickSelectAvatar1(View view) {
        tree.setAvatar("avatar1");
        Toast.makeText(RegisterTreeActivity.this,tree.getAvatar()+" Seleccionado",Toast.LENGTH_SHORT).show();
    }

    public void OnclickSelectAvatar2(View view) {
        tree.setAvatar("avatar2");
        Toast.makeText(RegisterTreeActivity.this,tree.getAvatar()+" Seleccionado",Toast.LENGTH_SHORT).show();
    }
}