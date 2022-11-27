package app.yo_planto.yoplantounarbolito_app;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.yo_planto.yoplantounarbolito_app.classes.Ranking;
import app.yo_planto.yoplantounarbolito_app.java_class.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {

    //request
    RequestQueue request;
    JsonArrayRequestCustom JOR;

    //url
    Variables variables;
    String url;

    //preferencias
    Preferences preference;

    ArrayList<Ranking> list_item_ranking;
    RecyclerView recyclerView;

    Validations validations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        variables = new Variables();
        validations = new Validations();
        url = variables.getUrl();
        preference = new Preferences(RankingActivity.this);
        recyclerView = findViewById(R.id.recicler_ranking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list_item_ranking = new ArrayList<>();
        getRanking();

    }

    private void getRanking() {
        request = Volley.newRequestQueue(this);
        JOR = new JsonArrayRequestCustom(Request.Method.GET, url + "/users/?include=trees&-sort=points&limit=50", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int j = 0;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String name = "", points = "", name_tree = "", avatar = "";

                        JSONObject user = response.getJSONObject(i);

                        if (user.has("name") && user.has("points") ) {
                            name = user.getString("name");
                            points = user.getString("points");
                            if (user.has("trees")){
                                JSONArray array_tree = user.getJSONArray("trees");
                                if(!array_tree.isNull(0)){
                                    JSONObject tree = array_tree.getJSONObject(0);
                                    if(tree.has("name") && tree.has("avatar")){
                                        name_tree = tree.getString("name");
                                        avatar = tree.getString("avatar");
                                        j += 1;
                                        list_item_ranking.add(new Ranking(j,name, name_tree, points, avatar));
                                        AdapterDates adapter_dates = new AdapterDates(list_item_ranking);
                                        recyclerView.setAdapter(adapter_dates);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(RankingActivity.this,"ocurrio un error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                validations.errors(error, RankingActivity.this);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Authorization", "Bearer " + preference.getToken());
                return headers;
            }
        };
        request.add(JOR);
    }
}