package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.adapters.FitAdapter;
import com.sgulab.thongtindaotao.models.FitFeed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FitFragment extends SGUFragment {

    private final String URL = "http://fit.sgu.edu.vn/drupal/tb_sv";
    private WebView webView;
    private ListView listInfo;

    private FitAdapter fitAdapter;

    private List<FitFeed> feeds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webView);
        listInfo = (ListView) view.findViewById(R.id.info_feeds);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        feeds = new ArrayList<>();
        fitAdapter = new FitAdapter(getContext(), R.layout.fit_feed_item, feeds);
        listInfo.setAdapter(fitAdapter);

        showLoading();
        webView.loadUrl(URL);
    }

    @Override
    public void onSearch(String title) {
        if (!sharedPreferences.getSharedPrefLoginUsingAccount()) {
            super.onSearch(title);
        }
        showLoading();
        webView.loadUrl(URL);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);");
        }
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            Document document = Jsoup.parse(html);

            Element table = document.getElementsByClass("views-table").first();
            Elements rows = table.child(1).children();

            feeds.clear();

            for (int i = 0 ; i < rows.size(); i++) {
                FitFeed feed = new FitFeed();
                Element row = rows.get(i);
                feed.setTitle(row.child(0).child(0).html());
                feed.setDate(row.child(1).html().trim());
                feed.setSummary(row.child(2).html());
                feeds.add(feed);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    fitAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
