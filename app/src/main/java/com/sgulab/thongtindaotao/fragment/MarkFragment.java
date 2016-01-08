package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.adapters.MarkAdapter;
import com.sgulab.thongtindaotao.objects.MarkSubject;
import com.sgulab.thongtindaotao.objects.MarkSubjectDetail;
import com.sgulab.thongtindaotao.utils.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MarkFragment extends SGUFragment {

    private WebView webView;
    private ExpandableListView listMark;
    private MarkAdapter adapter;
    private ArrayList<MarkSubject> groups;
    private ArrayList<Object> childs;
    private AtomicInteger step = new AtomicInteger();

    private Fragment loadingFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webView);
        listMark = (ExpandableListView) view.findViewById(R.id.list_diem);

        loadingFragment = getFragmentManager().findFragmentByTag("SimpleLoading");
        groups = new ArrayList<>();
        childs = new ArrayList<>();
        adapter = new MarkAdapter(getActivity(), groups, childs);
        listMark.setAdapter(adapter);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        step.set(0);
        getFragmentManager().beginTransaction().show(loadingFragment).commitAllowingStateLoss();
        webView.loadUrl("http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + getCurrentMSSV());
    }

    @Override
    public void onSearch(String title) {
        super.onSearch(title);
        step.set(0);
        getFragmentManager().beginTransaction().show(loadingFragment).commitAllowingStateLoss();
        webView.loadUrl("http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + getCurrentMSSV());
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
            switch (step.getAndIncrement()) {
                case 0:
                    webView.loadUrl("javascript:(function(){" +
                            "l=document.getElementById('ctl00_ContentPlaceHolder1_ctl00_lnkChangeview2');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                    break;
                case 1:
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
            groups.clear();
            childs.clear();
            Document document = Jsoup.parse(html);
            Elements diems = document.getElementsByClass("row-diem");
            for (int i = 0; i < diems.size(); i++) {
                Element e = diems.get(i);
                MarkSubject s = new MarkSubject();
                s.setName(e.child(2).child(0).html());
                try {
                    s.setFinalMark(Float.parseFloat(e.child(8).child(0).html()));
                } catch (Exception ex) {
                    s.setFinalMark(0);
                }
                String classification = e.child(9).child(0).html();
                if (classification.contains("&nbsp;")) classification = "-";
                s.setClassification(classification);
                groups.add(s);

                MarkSubjectDetail detail = new MarkSubjectDetail();
                detail.setClassification(s.getClassification());
                detail.setName(s.getName());
                detail.setFinalMark(s.getFinalMark());
                detail.setId(e.child(1).child(0).html());
                try {
                    detail.setTc(Integer.parseInt(e.child(3).child(0).html()));
                } catch (Exception ex) {
                    detail.setTc(0);
                }
                try {
                    detail.setPercentProcess(Integer.parseInt(e.child(4).child(0).html()));
                } catch (Exception ex) {
                    detail.setPercentProcess(0);
                }
                try {
                    detail.setPercentExam(Integer.parseInt(e.child(5).child(0).html()));
                } catch (Exception ex) {
                    detail.setPercentExam(0);
                }
                try {
                    detail.setMarkProcess(Float.parseFloat(e.child(6).child(0).html()));
                } catch (Exception ex) {
                    detail.setMarkProcess(0);
                }
                try {
                    detail.setMarkExam(Float.parseFloat(e.child(7).child(0).html()));
                } catch (Exception ex) {
                    detail.setMarkExam(0);
                }

                ArrayList<MarkSubjectDetail> l = new ArrayList<>();
                l.add(detail);
                childs.add(l);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getFragmentManager().beginTransaction().hide(loadingFragment).commitAllowingStateLoss();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
