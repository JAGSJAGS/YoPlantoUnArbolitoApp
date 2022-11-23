package app.yo_planto.yoplantounarbolito_app.java_class;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Validations {

    public String validateDatas(String json_erros, Context context){
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
        showDialog(context, res);
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

    public void showDialog(Context context, String errors){
        new MaterialAlertDialogBuilder(context)
                .setTitle("Error")
                .setMessage(errors)
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Aborting mission...");
                    }
                }).show();
    }
}
