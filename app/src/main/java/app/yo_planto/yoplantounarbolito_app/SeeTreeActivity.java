package app.yo_planto.yoplantounarbolito_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.yo_planto.yoplantounarbolito_app.validations.Validations;
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

    RequestQueue request;
    JsonObjectRequest JOR;

    //mostrearArbolito
    String titleTree = "Tines que adoptar un arbolito", lat_tree = "0.0", ln_tree = "0.0", name_tree, avatar = "avatar", photo, state;

    ImageView see_tree_imagen_avatar, see_tree_photo_trees;

    //mostrar user
    SharedPreferences preference;
    String token;
    String user_id;
    TextView text_view_name_tree, text_view_state_tree;

    //Map
    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    SupportMapFragment mapFragment;

    //Load
    ProgressBar load_bar;
    TextView load_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_see_tree);

        preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
        token = preference.getString("token", "");
        user_id = preference.getString("user_id", "");

        text_view_name_tree = findViewById(R.id.see_tree_name_tree_);
        text_view_state_tree = findViewById(R.id.see_tree_estate_tree);
        see_tree_imagen_avatar = findViewById(R.id.see_tree_imagen_avatar);
        see_tree_photo_trees = findViewById(R.id.see_tree_photo_tree);
        load_bar = findViewById(R.id.progressBar3);
        load_text = findViewById(R.id.msg_load_3);

        getUsersTrees("https://calm-fjord-08371.herokuapp.com/api/tree_users/" + user_id);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);

    }

    private void getUsersTrees(String url) {

        request = Volley.newRequestQueue(this);

        JOR = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    avatar = response.getString("avatar");
                    name_tree = response.getString("name");
                    lat_tree = response.getString("lat");
                    ln_tree = response.getString("lng");
                    photo = response.getString("photo");
                    state = response.getString("state");
                    text_view_name_tree.setText(name_tree);
                    text_view_state_tree.setText("Estado: " + state);
                    System.out.println("photos:" + photo);

                    Bitmap bitmap = Validations.convert(photo);

                    see_tree_photo_trees.setImageBitmap(bitmap);

                    mapFragment.getMapAsync(SeeTreeActivity.this);

                    load_bar.setVisibility(View.GONE);
                    load_text.setVisibility(View.GONE);


                    showAvatar(avatar);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Toast.makeText(SeeTreeActivity.this, "Error: " + jsonError, Toast.LENGTH_SHORT).show();
                    Log.i("ErrorVolley", jsonError);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                preference = getSharedPreferences("preferenceLogin", Context.MODE_PRIVATE);
                token = preference.getString("token", "");
                Map<String, String> headers = new HashMap<>();
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        request.add(JOR);
    }

    private void showAvatar(String avatar) {
        if(avatar.equals("avatar1")) {
            see_tree_imagen_avatar.setImageResource(R.mipmap.avatar_one_happy_600);
        }
        else  {
            see_tree_imagen_avatar.setImageResource(R.mipmap.yo_planto_un_arbolito);
        }
    }

    //Map
    @Override
    public void onMapReady(GoogleMap googleMap) {

        lat = Double.parseDouble(lat_tree);
        lng = Double.parseDouble(ln_tree);
        mMap = googleMap;
        LatLng mi_arbolito = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(mi_arbolito).title("Mi Arbolito"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( mi_arbolito,16));
    }
}