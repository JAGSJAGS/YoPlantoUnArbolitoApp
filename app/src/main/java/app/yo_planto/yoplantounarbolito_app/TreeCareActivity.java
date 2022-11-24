package app.yo_planto.yoplantounarbolito_app;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.yoplantounarbolito_app.R;

public class TreeCareActivity extends AppCompatActivity {

    //botones
    Button button_regar, button_limpiar, button_abonar, button_establecido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_care);

        button_regar = findViewById(R.id.button_regar);
        button_limpiar = findViewById(R.id.button_limpiar);
        button_abonar = findViewById(R.id.button_abonar);
        button_establecido = findViewById(R.id.button_establecido);

        button_regar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        button_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        button_abonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        button_establecido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void regarTree(){

    }
}