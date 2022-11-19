package app.yo_planto.yoplantounarbolito_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yoplantounarbolito_app.R;

public class StatusFragment extends Fragment {

    private static final String NAME_TREE = "ninguno";
    private static final String STATE = "inexistente";
    private static final String LAT_TREE = "0.0";
    private static final String LN_TREE = "0.0";
    private static final String AVATAR = "no hay";
    private static final String PATH_PHOTO = "no photo";

    String name_tree = "ninguno";
    String state = "inexistente";
    String lat_tree = "0.0";
    String ln_tree = "0.0";
    String avatar = "no hay";
    String path_photo = "no photo";

    TextView text_view_name_tree, text_view_state_tree;

    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static StatusFragment newInstance(String name_t, String state_t, String lat_t, String ln_t, String avatar_t, String path_photo_t) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(NAME_TREE, name_t);
        args.putString(STATE, state_t);
        args.putString(LAT_TREE, lat_t);
        args.putString(LN_TREE, ln_t);
        args.putString(AVATAR, avatar_t);
        args.putString(PATH_PHOTO, path_photo_t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name_tree = getArguments().getString(NAME_TREE);
            state = getArguments().getString(STATE);
            lat_tree = getArguments().getString(LAT_TREE);
            ln_tree = getArguments().getString(LN_TREE);
            avatar = getArguments().getString(AVATAR);
            path_photo = getArguments().getString(PATH_PHOTO);

            text_view_name_tree = getActivity().findViewById(R.id.text_view_name_tree);
            text_view_state_tree = getActivity().findViewById(R.id.text_view_state_tree);

            text_view_name_tree.setText(name_tree);
            text_view_state_tree.setText("Estado: " + state);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false);
    }
}