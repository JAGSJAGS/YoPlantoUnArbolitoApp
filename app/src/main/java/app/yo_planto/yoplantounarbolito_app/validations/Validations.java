package app.yo_planto.yoplantounarbolito_app.validations;

import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Validations {

    public String validateDatas(String json_erros, TextView error){
        String obs = "";
        try {
            JSONObject root = new JSONObject(json_erros);
            obs = obs + root.getString("errors");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String res = obs.
                replaceAll("\\]", "").
                replaceAll("\\}\\}", " ").
                replaceAll("\\[", "").
                replaceAll("\\{", "\n").
                replaceAll("\\}", "\n").
                replaceAll(",", "\n").
                replaceAll("\"", "").
                replaceAll("errors:", "");

        error.setText(res);
        error.setVisibility(View.VISIBLE);
        return res;
    }
}
