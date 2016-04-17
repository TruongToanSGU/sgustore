package com.sgulab.thongtindaotao.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.sgulab.thongtindaotao.fragment.WeekSchedulePageFragment;
import com.sgulab.thongtindaotao.models.ScheduleInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private HashMap<Integer, List<ScheduleInfo>> infoWeekList;
    private String[] maps;

    public ScheduleScreenSlidePagerAdapter(FragmentManager fm, HashMap<Integer, List<ScheduleInfo>> data, String[] maps) {
        super(fm);
        this.infoWeekList = data;
        this.maps = maps;
    }

    public void setInfoWeekList(HashMap<Integer, List<ScheduleInfo>> infoWeekList) {
        this.infoWeekList = infoWeekList;
    }

    @Override
    public Fragment getItem(int position) {
        return WeekSchedulePageFragment.newInstance(infoWeekList.get(position));
    }

    @Override
    public int getCount() {
        return infoWeekList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < getCount()) {
            return maps[position];
        }
        return "";
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    //    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}
