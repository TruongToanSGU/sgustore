package com.sgulab.thongtindaotao.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.sgulab.thongtindaotao.fragment.MarkTermFragment;
import com.sgulab.thongtindaotao.fragment.MarkTermPageFragment;
import com.sgulab.thongtindaotao.objects.MarkSubject;
import com.sgulab.thongtindaotao.objects.MarkTerm;

import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<MarkTerm> terms;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<MarkTerm> data) {
        super(fm);
        this.terms = data;
    }

    @Override
    public Fragment getItem(int position) {
        return MarkTermPageFragment.newInstance(terms.get(position));
    }

    @Override
    public int getCount() {
        return terms.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < getCount()) {
            return terms.get(position).getTermFullName();
        }
        return "";
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}
