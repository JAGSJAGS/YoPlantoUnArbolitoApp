package com.example.yoplantounarbolito_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.validations.Validations;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterTreeActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText name;
    String avatar = "avatar1";
    Validations validations = new Validations();
    TextView errors;
    RequestQueue request;
    JsonObjectRequest JOR;

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;

    Bundle sendData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tree);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.editTextNameRegisterTree);
        errors = findViewById(R.id.textViewErrorsRegisterThree);
        errors.setVisibility(View.GONE);
        sendData = new Bundle();
    }

    private void registerTree(String url){
        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("lat", lat+"");
        params.put("lng", lng+"");
        params.put("avatar", avatar);
        params.put("path_photo", "null");
        params.put("state", "take care");
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.getString("id");
                    sendData.putString("tree_id",id);
                    registerTreeUser("https://calm-fjord-08371.herokuapp.com/api/tree_users", id);
                } catch (JSONException e) {
                    Toast.makeText(RegisterTreeActivity.this,"Se produjo un error",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                validations.validateDatas(jsonError,errors);
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

    private void registerTreeUser(String url, String tree_id){
        Bundle getDate = getIntent().getExtras();
        String id = getDate.getString("user_id");

        request = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        params.put("tree_id", tree_id);
        JSONObject parameters = new JSONObject(params);

        JOR = new JsonObjectRequest(Request.Method.POST, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent photoActivity = new Intent(getApplicationContext(),RegisterPhotoActivity.class);
                photoActivity.putExtras(sendData);
                startActivity(photoActivity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                validations.validateDatas(jsonError,errors);
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

    public void OnclickRegisterTree(View view) {
        errors.setVisibility(View.GONE);
        Toast.makeText(RegisterTreeActivity.this,"Registrando",Toast.LENGTH_LONG).show();
        registerTree("https://calm-fjord-08371.herokuapp.com/api/trees");
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
        lat = location.getLatitude();
        lng = location.getLongitude();
        agregarMarket(lat, lng);
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
        avatar = "avatar1";
        Toast.makeText(RegisterTreeActivity.this,avatar+" Seleccionado",Toast.LENGTH_SHORT).show();
    }

    public void OnclickSelectAvatar2(View view) {
        avatar = "avatar2";
        Toast.makeText(RegisterTreeActivity.this,avatar+" Seleccionado",Toast.LENGTH_SHORT).show();
    }
}