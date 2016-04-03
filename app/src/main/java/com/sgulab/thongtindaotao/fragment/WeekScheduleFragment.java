package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.adapters.ScheduleScreenSlidePagerAdapter;
import com.sgulab.thongtindaotao.models.MarkTerm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeekScheduleFragment extends SGUFragment implements AdapterView.OnItemSelectedListener {

    private WebView webView;
    private AtomicInteger step = new AtomicInteger();

    private ViewPager mPager;
    private ScheduleScreenSlidePagerAdapter mPagerAdapter;
    private Spinner spinner;
    private List<MarkTerm> terms;
    private ArrayList<CharSequence> hks;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webView);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        setUpLoading();

        terms = new ArrayList<>();
        String[] dayOfWeeks = new String[] {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};
        for (int i = 0; i < 7; i++) {
            MarkTerm term = new MarkTerm();
            term.setTermFullName(dayOfWeeks[i]);
            terms.add(term);
        }
        mPagerAdapter = new ScheduleScreenSlidePagerAdapter(getFragmentManager(), terms);

        mPager.setAdapter(mPagerAdapter);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        step.set(0);
        webView.loadUrl(getUrl());
        showLoading();

        hks = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.week_schedule_spinner_item, hks);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    public String getUrl() {
        String url;
        if (sharedPreferences.getSharedPrefLoginUsingAccount()) {
            url = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=thoikhoabieu";
        } else {
            url = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=thoikhoabieui&id=" + getCurrentMSSV();
        }
        return url;
    }

    @Override
    public void onSearch(String title) {
//        if (!sharedPreferences.getSharedPrefLoginUsingAccount()) {
//            super.onSearch(title);
//        }
//        step.set(0);
//        webView.loadUrl(getUrl());
//        showLoading();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        step.set(0);
        showLoading();
        webView.loadUrl("javascript:(function(){" +
                "document.getElementById('ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK').selectedIndex = " + position + ";" +
                "})()");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            if (!isRequesting.get()) return;

            switch (step.getAndIncrement()) {
                case 0:
                    webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
                    break;
            }
        }
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void processHTML(String html)
        {
            if (!isRequesting.get()) return;

            hks.clear();
            final Document document = Jsoup.parse(html);
            Element selector = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK");
            for (Element option : selector.children()) {
                hks.add(option.html());
            }

            Elements items = document.getElementsByAttribute("onmouseover");
            Log.i("zzz", "Items: " + items.size());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    spinnerAdapter.notifyDataSetChanged();
                    mPagerAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
