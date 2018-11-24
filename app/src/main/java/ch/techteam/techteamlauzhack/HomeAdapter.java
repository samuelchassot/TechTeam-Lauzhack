package ch.techteam.techteamlauzhack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class HomeAdapter extends FragmentStatePagerAdapter {


    public  HomeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        //return RunTypeFragment.newRunTypeFragment(position, run_types.get(position == -1 ? run_types.size() -1 :position%run_types.size()));
        RunningMode[] runningModes = RunningMode.values();
        int l = runningModes.length;
        return RunTypeFragment.newRunTypeFragment(position, runningModes[position < 0 ? l -1 : position%l]);
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