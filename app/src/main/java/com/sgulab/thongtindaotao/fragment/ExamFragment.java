package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.adapters.ExamAdapter;
import com.sgulab.thongtindaotao.adapters.MarkAdapter;
import com.sgulab.thongtindaotao.models.ExamSchedule;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends SGUFragment {

    private WebView webView;
    private List<ExamSchedule> examScheduleList;
    private ListView examListView;
    private ExamAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webView);
        examListView = (ListView) view.findViewById(R.id.list_exam);

        examScheduleList = new ArrayList<>();
        adapter = new ExamAdapter(getContext(), examScheduleList);
        examListView.setAdapter(adapter);

        setUpLoading();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.loadUrl(getUrl());
        showLoading();
    }

    public String getUrl() {
        String url = null;
        if (sharedPreferences.getSharedPrefLoginUsingAccount()) {
            url = "http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemlichthi";
        }
        return url;
    }

    @Override
    public void onSearch(String title) {
        if (!sharedPreferences.getSharedPrefLoginUsingAccount()) {
            super.onSearch(title);
        }
        webView.loadUrl(getUrl());
        showLoading();
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
            webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
        }
    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        public void processHTML(String html)
        {
            if (!isRequesting.get()) return;

            Document document = Jsoup.parse(html);
            examScheduleList.clear();

            Elements elements = document.getElementById("ctl00_ContentPlaceHolder1_ctl00_gvXem").getElementsByTag("tbody").get(0).getElementsByTag("tr");

            for (int i = 1; i < elements.size(); i++) {
                Element element = elements.get(i);
                Elements cols = element.getElementsByTag("td");
                ExamSchedule schedule = new ExamSchedule();
                schedule.setSubjectId(cols.get(1).child(0).html());
                schedule.setSubjectName(cols.get(2).child(0).html());
                schedule.setDate(cols.get(6).child(0).html());
                schedule.setTime(cols.get(7).child(0).html());
                schedule.setDuration(Integer.parseInt(cols.get(8).child(0).html().trim()));
                schedule.setRoom(cols.get(9).child(0).html());
                examScheduleList.add(schedule);
            }

            Log.i("zzz", examScheduleList.size() + "");

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }
}
