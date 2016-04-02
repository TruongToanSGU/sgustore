package com.sgulab.thongtindaotao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sgulab.thongtindaotao.utils.MySharedPrefer;

public class BaseFragment extends Fragment {

    protected MySharedPrefer sharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = MySharedPrefer.getInstance(getActivity().getApplication());
    }
}
