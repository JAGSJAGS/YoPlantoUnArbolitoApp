package app.yo_planto.yoplantounarbolito_app;

import static app.yo_planto.yoplantounarbolito_app.variables.url_save_photo;

import android.util.Base64;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.content.FileProvider;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import androidx.core.content.FileProvider;
import app.yo_planto.yoplantounarbolito_app.validations.Validations;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yoplantounarbolito_app.R;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//import static android.support.v4.content.FileProvider.getUriForFile;

public class register_photo_activity extends AppCompatActivity {
    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=200;

    Button boton_cargar;
    Button button_save_photo;
    ImageView imagen;
    Validations validations = new Validations();
    TextView errors;
    String path;
    String ruta_imagen;
    Bitmap bitmap;

    ProgressBar load_photo;
    TextView text_view_load_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.yoplantounarbolito_app.R.layout.activity_register_photo);

        imagen= findViewById(com.example.yoplantounarbolito_app.R.id.photo_tree);
        boton_cargar= findViewById(com.example.yoplantounarbolito_app.R.id.btn_cargar_img);
        button_save_photo = findViewById(com.example.yoplantounarbolito_app.R.id.button_save_photo);
        button_save_photo.setVisibility(View.GONE);
        errors = findViewById(com.example.yoplantounarbolito_app.R.id.text_view_errors_register_photo);
        errors.setVisibility(View.GONE);
        load_photo = findViewById(com.example.yoplantounarbolito_app.R.id.load_photo);
        load_photo.setVisibility(View.GONE);
        text_view_load_photo = findViewById(R.id.text_view_load_photo);
        text_view_load_photo.setVisibility(View.GONE);

    }

    public void onclick(View view) {
        cargar_imagen();
    }

    private void cargar_imagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alert_opciones=new AlertDialog.Builder(register_photo_activity.this);
        alert_opciones.setTitle("Seleccione una Opción");
        alert_opciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomar_fotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alert_opciones.show();
    }

    private void tomar_fotografia() {
        File file_imagen=new File(getFilesDir(),RUTA_IMAGEN);
        boolean is_creada=file_imagen.exists();
        String nombre_imagen="";
        if(is_creada==false){
            is_creada=file_imagen.mkdirs();
        }
        if(is_creada==true){
            nombre_imagen=(System.currentTimeMillis()/1000)+".jpg";
            path=Environment.getExternalStorageDirectory()+
                    File.separator+RUTA_IMAGEN+File.separator+nombre_imagen;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null){
                File imagen_archivo = null;

                try {
                    imagen_archivo = crear_imagen();
                }catch (IOException e){
                    Log.e("error", e.toString());
                }
                if(imagen_archivo != null){
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                    {
                        Uri foto_uri = FileProvider.getUriForFile(this, "com.example.yoplantounarbolito_app.fileprovider", imagen_archivo );
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, foto_uri);
                        startActivityForResult(intent,COD_FOTO);
                    }else
                    {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen_archivo ));
                    }
                }
            }
        }
    }

    private File crear_imagen() throws IOException{
        String nombre_imagen = "foto_";
        File directorio = getExternalFilesDir(path);
        File imagen = File.createTempFile(nombre_imagen, ".jpg", directorio);
        ruta_imagen = imagen.getAbsolutePath();
        return imagen;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri mi_path=data.getData();
                    imagen.setImageURI(mi_path);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(register_photo_activity.this.getContentResolver(), mi_path);
                        if (imagen.getDrawable() != null){
                            button_save_photo.setVisibility(View.VISIBLE);
                        }else{
                            button_save_photo.setVisibility(View.GONE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(register_photo_activity.this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    bitmap= BitmapFactory.decodeFile(ruta_imagen);
                    imagen.setImageBitmap(bitmap);
                    if (imagen.getDrawable() != null){
                        button_save_photo.setVisibility(View.VISIBLE);
                    }else{
                        button_save_photo.setVisibility(View.GONE);
                    }

                    break;
            }
        }
    }

    private void save_photo(String url){
        RequestQueue request;
        JsonObjectRequest JOR;
        request = Volley.newRequestQueue(this);
        String img = img_to_string(bitmap);
        Map<String, String> params = new HashMap<>();
        params.put("photo",img);
        JSONObject parameters = new JSONObject(params);
        JOR = new JsonObjectRequest(Request.Method.PUT, url, parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                load_photo.setVisibility(View.GONE);
                text_view_load_photo.setVisibility(View.GONE);
                finishAffinity ();
                Intent main_activity = new Intent(getApplicationContext(), app.yo_planto.yoplantounarbolito_app.main_activity.class);
                startActivity(main_activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                load_photo.setVisibility(View.GONE);
                text_view_load_photo.setVisibility(View.GONE);
                errors.setText("No se pudo registrar, seleccione otra foto o intente despues");
                errors.setVisibility(View.VISIBLE);
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

    private String img_to_string(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagen_byte = array.toByteArray();
        String imagen_string = Base64.encodeToString(imagen_byte, Base64.DEFAULT);
        Log.i("imagen:", imagen_string);
        return imagen_string;
    }

    public void on_click_save_photo(View view) {
        errors.setVisibility(View.GONE);
        load_photo.setVisibility(View.VISIBLE);
        text_view_load_photo.setVisibility(View.VISIBLE);
        Bundle get_date = getIntent().getExtras();
        String id = get_date.getString("tree_id");
        Toast.makeText(register_photo_activity.this,"Id: "+id,Toast.LENGTH_LONG).show();
        save_photo(url_save_photo +id);
    }
}