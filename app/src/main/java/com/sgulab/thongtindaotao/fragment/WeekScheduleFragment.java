package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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
import com.sgulab.thongtindaotao.models.ScheduleInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WeekScheduleFragment extends SGUFragment {

    private WebView webView;
    private AtomicInteger step = new AtomicInteger();

    private ViewPager mPager;
    private Spinner spinner;
    private Spinner spinnerWeek;
    private HashMap<Integer, List<ScheduleInfo>> lists;
    private ArrayList<CharSequence> hks;
    private ArrayList<CharSequence> weeks;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ArrayAdapter<CharSequence> spinnerWeekAdapter;
    private String[] dayOfWeeks;
    private AtomicInteger lastIdx;
    private AtomicInteger lastWeekIdx;

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
        spinnerWeek = (Spinner) view.findViewById(R.id.spinner_week);

        setUpLoading();

        lists = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            lists.put(i, new ArrayList<ScheduleInfo>());
        }
        dayOfWeeks = new String[] {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        lastIdx = new AtomicInteger(0);
        lastWeekIdx = new AtomicInteger(-1);
        step.set(0);
        webView.loadUrl(getUrl());
        showLoading();

        hks = new ArrayList<>();
        weeks = new ArrayList<>();

        spinnerAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.week_schedule_spinner_item, hks);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lastIdx.get() == position) return;
                showLoading();
                webView.loadUrl("javascript:(function(){" +
                        "selector = document.getElementById('ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK');" +
                        "selector.selectedIndex = " + position + ";" +
                        "selector.onchange();" +
                        "})()");
                ScheduleScreenSlidePagerAdapter mPagerAdapter = new ScheduleScreenSlidePagerAdapter(getFragmentManager(), new HashMap<Integer, List<ScheduleInfo>>(), dayOfWeeks);
                mPager.setAdapter(mPagerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWeekAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.week_schedule_spinner_item2, weeks);
        spinnerWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeek.setAdapter(spinnerWeekAdapter);
        spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lastWeekIdx.get() == position) return;
                showLoading();
                webView.loadUrl("javascript:(function(){" +
                        "selector = document.getElementById('ctl00_ContentPlaceHolder1_ctl00_ddlTuan');" +
                        "selector.selectedIndex = " + position + ";" +
                        "selector.onchange();" +
                        "})()");
                ScheduleScreenSlidePagerAdapter mPagerAdapter = new ScheduleScreenSlidePagerAdapter(getFragmentManager(), new HashMap<Integer, List<ScheduleInfo>>(), dayOfWeeks);
                mPager.setAdapter(mPagerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String getUrl() {
        String url;
        if (sharedPreferences.getSharedPrefLoginUsingAccount()) {
            url = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=thoikhoabieu";
        } else {
            url = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=thoikhoabieu&id=" + getCurrentMSSV();
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

    private class HelloWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url)
        {
            if (!isRequesting.get()) return;

            webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
        }
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void processHTML(String html)
        {
            if (!isRequesting.get()) return;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hks.clear();
                    weeks.clear();
                    spinnerAdapter.notifyDataSetChanged();
                    spinnerWeekAdapter.notifyDataSetChanged();
                    for (int i = 0; i < 7; i++) {
                        lists.put(i, new ArrayList<ScheduleInfo>());
                    }
                }
            });

            final Document document = Jsoup.parse(html);
            Element selector = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK");
            for (int i = 0; i < selector.children().size(); i++) {
                Element option = selector.child(i);
                hks.add(option.html());
                if (option.hasAttr("selected")) {
                    lastIdx.set(i);
                }
            }
            Element selectorWeek = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ddlTuan");
            for (int i = 0; i < selectorWeek.children().size(); i++) {
                Element option = selectorWeek.child(i);
                weeks.add(option.html());
                if (option.hasAttr("selected")) {
                    lastWeekIdx.set(i);
                }
            }

            Elements items = document.getElementsByAttribute("onmouseover");
            for (Element item :  items) {
                String data = item.attr("onmouseover");
                data = data.replace("ddrivetip('", "");
                String[] parts = data.split("','");
                ScheduleInfo scheduleInfo = new ScheduleInfo();
                scheduleInfo.setClassName(parts[0]);
                scheduleInfo.setName(parts[1]);
                scheduleInfo.setId(parts[2].split(" ")[0]);
                String gString = parts[2].split(" ")[2];
                if (gString.startsWith("0")) {
                    gString = gString.substring(1);
                }
                scheduleInfo.setGroup(Integer.parseInt(gString));
                scheduleInfo.setDayOfWeek(Integer.parseInt(parts[3]) - 2);
                scheduleInfo.setSessionDuration(Integer.parseInt(parts[4]));
                scheduleInfo.setRoom(parts[5]);
                scheduleInfo.setSessionBegin(Integer.parseInt(parts[6]));
                scheduleInfo.setTeacher(parts[8]);
                scheduleInfo.setDateBegin(parts[9]);
                scheduleInfo.setDateEnd(parts[10]);

                lists.get(scheduleInfo.getDayOfWeek()).add(scheduleInfo);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    spinnerAdapter.notifyDataSetChanged();
                    spinnerWeekAdapter.notifyDataSetChanged();
                    spinner.setSelection(lastIdx.get());
                    spinnerWeek.setSelection(lastWeekIdx.get());
                    ScheduleScreenSlidePagerAdapter mPagerAdapter = new ScheduleScreenSlidePagerAdapter(getFragmentManager(), lists, dayOfWeeks);
                    mPager.setAdapter(mPagerAdapter);
                }
            });
        }
    }
}
