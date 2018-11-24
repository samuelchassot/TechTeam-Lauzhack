package ch.techteam.techteamlauzhack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RunTypeFragment extends Fragment {

    private String title;
    private int position;

    public static RunTypeFragment newRunTypeFragment(int position, String title) {
        RunTypeFragment run_type = new RunTypeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("position", position);
        run_type.setArguments(args);

        return run_type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", 0);
        title = getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run_type, container, false);
        TextView textView_run_type = (TextView) view.findViewById(R.id.text_view_home_run_type);
        textView_run_type.setText(title);

        return  view;
    }
}
