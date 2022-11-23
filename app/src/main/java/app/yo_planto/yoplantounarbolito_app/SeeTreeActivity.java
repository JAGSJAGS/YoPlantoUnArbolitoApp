package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.classes.Tree;
import app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz.TreeDatabase;
import app.yo_planto.yoplantounarbolito_app.java_class.Validations;
import app.yo_planto.yoplantounarbolito_app.java_class.Variables;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SeeTreeActivity extends AppCompatActivity implements OnMapReadyCallback {

    //request
    RequestQueue request;
    JsonObjectRequest JOR;

    //url
    Variables variables = new Variables();
    String url;

    //preferencias
    SharedPreferences preference;
    String token;
    String user_id;

    //mostrarArbolito
    TreeDatabase tree_database;
    Tree tree;
    ImageView imagen_view_imagen_avatar, imagen_view_photo_trees;
    TextView text_view_name_tree, text_view_state_tree;

    //Map
    private GoogleMap mMap;
    private Marker marcador;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_see_tree);

        tree_database = new TreeDatabase();
        tree = new Tree();

        url = variables.getUrl();

        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token", "");
        user_id = preference.getString("user_id", "");

        text_view_name_tree = findViewById(R.id.text_view_see_tree_name_tree_);
        text_view_state_tree = findViewById(R.id.text_view_see_tree_estate_tree);
        imagen_view_imagen_avatar = findViewById(R.id.imagen_view_see_tree_imagen_avatar);
        imagen_view_photo_trees = findViewById(R.id.imagen_view_see_tree_photo_tree);

        getUsersTrees();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);

    }

    private void getUsersTrees() {

        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url +"/tree_users/" + user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tree.setAvatar(response.getString(tree_database.getAvatar()));
                    tree.setName(response.getString(tree_database.getAvatar()));
                    tree.setLat(response.getString(tree_database.getLat()));
                    tree.setLng(response.getString(tree_database.getLng()));
                    tree.setPath_photo(response.getString(tree_database.getPath_photo()));
                    tree.setState(response.getString(tree_database.getState()));
                    text_view_name_tree.setText("Nombre Arbol");
                    text_view_state_tree.setText("Estado del arbol");

                    //guarda la foto en un bitmap
                    //Bitmap bitmap = Validations.convert(tree.getPath_photo());
                    //see_tree_photo_trees.setImageBitmap(bitmap);

                    //carga las variables al mapa
                    //mapFragment.getMapAsync(SeeTreeActivity.this);

                    showAvatar(tree.getAvatar());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String jsonError = new String(networkResponse.data);
                Toast.makeText(SeeTreeActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
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

    private void showAvatar(String avatar) {
        if(avatar.equals("avatar1")) {
            imagen_view_imagen_avatar.setImageResource(R.mipmap.avatar_one_happy_600);
        }
        else  {
            imagen_view_photo_trees.setImageResource(R.mipmap.yo_planto_un_arbolito);
        }
    }

    //Map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = Double.parseDouble(tree.getLat());
        double lng = Double.parseDouble(tree.getLng());
        mMap = googleMap;
        LatLng mi_arbolito = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(mi_arbolito).title("Mi Arbolito"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( mi_arbolito,16));
    }
}