package com.starsoft.traveldiary.fragments.fragAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.starsoft.traveldiary.fragments.FragFour;
import com.starsoft.traveldiary.fragments.FragOne;
import com.starsoft.traveldiary.fragments.FragThree;
import com.starsoft.traveldiary.fragments.FragTwo;

/**
 * Created by Aashish on 9/9/2016.
 */
public class FragAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = FragAdapter.class.getSimpleName();

    public FragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, ""+position);
        switch (position){
            case 0: return FragOne.getNewInstance(""+position);
            case 1: return FragTwo.getNewInstance(""+position);
            case 2: return FragThree.getNewInstance(""+position);
            case 3: return FragFour.getNewInstance(""+position);
            default:return FragOne.getNewInstance(""+position);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
