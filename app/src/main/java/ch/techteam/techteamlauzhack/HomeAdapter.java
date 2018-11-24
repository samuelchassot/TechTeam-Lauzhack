package ch.techteam.techteamlauzhack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class HomeAdapter extends FragmentStatePagerAdapter {

    List<String> run_types;

    public  HomeAdapter(FragmentManager fragmentManager, List<String> run_types) {
        super(fragmentManager);
        this.run_types = run_types;
    }

    @Override
    public Fragment getItem(int position) {
        return RunTypeFragment.newRunTypeFragment(position, run_types.get(position%run_types.size()));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Run type " + position;
    }
}