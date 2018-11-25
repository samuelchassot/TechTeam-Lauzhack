package ch.techteam.techteamlauzhack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RunTypeFragment extends Fragment {

    private String title;
    private int position;
    private RunningMode mode;
    private Intent intent;
    private int time = 180;
    private double distance = 5.0;
    private int slowIntervalTime = 240;
    private int fastIntervalTime = 600;

    public static RunTypeFragment newRunTypeFragment(int position, RunningMode mode) {
        RunTypeFragment run_type = new RunTypeFragment();
        Bundle args = new Bundle();
        args.putString("title", mode.getTitle());
        args.putInt("position", position);
        args.putSerializable("mode", mode);
        run_type.setArguments(args);

        return run_type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", 0);
        title = getArguments().getString("title");
        mode = (RunningMode) getArguments().getSerializable("mode");
        intent = new Intent(this.getActivity(), MainActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run_type, container, false);
        Button b = view.findViewById(R.id.button_home_run);
        b.setText(title);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(mode){
                    case RUN_TIME:
                        intent.putExtra("time", time);
                        break;
                    case RUN_DISTANCE:
                        intent.putExtra("distance", distance);
                        break;
                    case WALK:
                        intent.putExtra("time", time);
                        break;
                    case INTERVAL:
                        intent.putExtra("slowIntervalTime", slowIntervalTime);
                        intent.putExtra("fastIntervalTime", fastIntervalTime);
                        break;
                }
                intent.putExtra("mode", mode);
                startActivity(intent);

            }
        });

        view.findViewById(R.id.button_home_run).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("mode", mode);
                startActivity(intent);
            }
        });

        return  view;
    }

}
