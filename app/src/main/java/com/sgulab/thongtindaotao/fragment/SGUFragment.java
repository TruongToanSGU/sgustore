package com.sgulab.thongtindaotao.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SGUFragment extends Fragment {
    private String currentMSSV;

    protected AtomicBoolean isRequesting = new AtomicBoolean(false);

    protected ProgressDialog loadingDialog;

    public void onSearch(String title) {
        currentMSSV = title;
    }
    public void onShow(String title) {
        currentMSSV = title;
    }

    public String getCurrentMSSV() {
        return currentMSSV;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpLoading();
    }

    public void setCurrentMSSV(String currentMSSV) {
        this.currentMSSV = currentMSSV;
    }

    protected void setUpLoading() {
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setTitle("Loading");
        loadingDialog.setMessage("Wait while loading...");
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isRequesting.set(false);
            }
        });
    }

    protected void showLoading() {
        if (loadingDialog == null) return;;
        loadingDialog.show();
        isRequesting.set(true);
    }

    protected void hideLoading() {
        if (loadingDialog == null) return;;
        loadingDialog.dismiss();
    }
}
