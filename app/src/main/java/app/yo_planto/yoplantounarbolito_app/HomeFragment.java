package app.yo_planto.yoplantounarbolito_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yoplantounarbolito_app.R;

public class HomeFragment extends Fragment {

    TextView textViewTitle;
    ImageButton imagen_tree_button;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        String title_tree = "Tines que adoptar un arbolito";

        textViewTitle = (TextView) rootView.findViewById(R.id.textViewTitleArbolito);
        textViewTitle.setText(title_tree);

        imagen_tree_button = (ImageButton) rootView.findViewById(R.id.image_tree_button);
        imagen_tree_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seeTree = new Intent(getActivity().getApplicationContext(),SeeTreeActivity.class);
                startActivity(seeTree);
            }
        });

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}