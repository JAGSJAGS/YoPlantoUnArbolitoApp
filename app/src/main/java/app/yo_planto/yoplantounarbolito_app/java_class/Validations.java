package app.yo_planto.yoplantounarbolito_app.java_class;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

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
    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
