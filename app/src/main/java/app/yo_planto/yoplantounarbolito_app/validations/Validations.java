package app.yo_planto.yoplantounarbolito_app.validations;

import android.view.View;
import android.widget.TextView;

public class Validations {

    public String validateDatas(String jsonErros, TextView error){
        String res = jsonErros.replaceAll("\\]", "").replaceAll("\\}\\}", " ").replaceAll("\\[", "").
                replaceAll("\\{", "\n").replaceAll("\\}", "\n").
                replaceAll(",", "\n").replaceAll("\"", "").
                replaceAll("errors:", "");
        error.setText(res);
        error.setVisibility(View.VISIBLE);
        return res;
    }
}
