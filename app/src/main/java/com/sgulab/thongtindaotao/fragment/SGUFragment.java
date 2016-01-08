package com.sgulab.thongtindaotao.fragment;

import android.support.v4.app.Fragment;

public abstract class SGUFragment extends Fragment {
    private String currentMSSV;

    public void onSearch(String title) {
        currentMSSV = title;
    }
    public void onShow(String title) {
        currentMSSV = title;
    }

    public String getCurrentMSSV() {
        return currentMSSV;
    }

    public void setCurrentMSSV(String currentMSSV) {
        this.currentMSSV = currentMSSV;
    }
}
