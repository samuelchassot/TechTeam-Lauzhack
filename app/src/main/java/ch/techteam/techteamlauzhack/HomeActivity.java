package ch.techteam.techteamlauzhack;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //List<String> run_types = new ArrayList<String>(Arrays.asList("Run Distance", "Run Time", "Intervals", "Walk"));
    HomeAdapter home_adapter;
    ViewPager home_view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_adapter = new HomeAdapter(getSupportFragmentManager());
        home_view_pager = (ViewPager) findViewById(R.id.viewPager_home_run_types);
        home_view_pager.setAdapter(home_adapter);
    }

}
