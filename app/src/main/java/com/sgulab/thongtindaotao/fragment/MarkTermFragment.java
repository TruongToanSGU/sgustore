package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.adapters.MarkAdapter;
import com.sgulab.thongtindaotao.adapters.ScreenSlidePagerAdapter;
import com.sgulab.thongtindaotao.objects.MarkSubject;
import com.sgulab.thongtindaotao.objects.MarkSubjectDetail;
import com.sgulab.thongtindaotao.objects.MarkTerm;
import com.sgulab.thongtindaotao.utils.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MarkTermFragment extends SGUFragment {

    private WebView webView;
    private AtomicInteger step = new AtomicInteger();

    private Fragment loadingFragment;

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private List<MarkTerm> terms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_term_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webView);
        mPager = (ViewPager) view.findViewById(R.id.pager);

        terms = new ArrayList<>();
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), terms);
        mPager.setAdapter(mPagerAdapter);

        loadingFragment = getFragmentManager().findFragmentByTag("SimpleLoading");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
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
            terms.clear();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPagerAdapter.notifyDataSetChanged();
                    mPager.setAdapter(mPagerAdapter);
                }
            });

            final Document document = Jsoup.parse(html);
            Elements diems = document.getElementsByClass("title-hk-diem");

            for (Element diem : diems) {
                Element e = diem.nextElementSibling();
                List<MarkSubjectDetail> details = new ArrayList<>();
                while (e != null && e.className().equals("row-diem")) {
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
                    //groups.add(s);

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

                    details.add(detail);
                    e = e.nextElementSibling();
                }

                MarkTerm term = new MarkTerm();
                term.setMarks(details);
                term.setTermFullName(diem.child(0).child(0).html());
                Element d = diem.nextElementSibling();
                int idx = 0;
                while (d != null && d.className().equals("row-diem")) {
                    d = d.nextElementSibling();
                }
                while (d != null && d.className().equals("row-diemTK")) {
                    switch (idx) {
                        case 0:
                            try {
                                term.setTermAvg10(Float.parseFloat(d.child(0).child(1).html().trim()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 1:
                            try {
                                term.setTermAvg4(Float.parseFloat(d.child(0).child(1).html().trim()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 2:
                            try {
                                term.setAllTermAvg10(Float.parseFloat(d.child(0).child(1).html().trim()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 3:
                            try {
                                term.setAllTermAvg4(Float.parseFloat(d.child(0).child(1).html().trim()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 4:
                            try {
                                term.setPassedTc(Integer.parseInt(d.child(0).child(1).html().trim()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 5:
                            try {
                                term.setAllPassedTc(Integer.parseInt(d.child(0).child(1).html().trim()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 6:
                            try {
                                term.setAvgConduct(Integer.parseInt(d.child(0).child(1).html()));
                            } catch (Exception ex) {
                            }
                            break;
                        case 7:
                            try {
                                term.setConductType(d.child(0).child(1).html().trim());
                            } catch (Exception ex) {
                            }
                            break;
                    }
                    d = d.nextElementSibling();
                    idx++;
                }
                terms.add(term);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getFragmentManager().beginTransaction().hide(loadingFragment).commitAllowingStateLoss();
                    mPagerAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
