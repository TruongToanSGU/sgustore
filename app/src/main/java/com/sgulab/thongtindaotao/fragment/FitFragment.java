package com.sgulab.thongtindaotao.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

    private final String BASE_URL = "http://fit.sgu.edu.vn";
    private final String URL = "http://fit.sgu.edu.vn/drupal/tb_sv";
    private WebView webView;
    private ListView listInfo;

    private FitAdapter fitAdapter;

    private List<FitFeed> feeds;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private TextView tvSummary;
    private View bottomSheet;
    private ImageButton btnWeb;

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
        tvSummary = (TextView) view.findViewById(R.id.feed_summary);

        bottomSheet = view.findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        btnWeb = (ImageButton) view.findViewById(R.id.button_action_web);


        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        feeds = new ArrayList<>();
        fitAdapter = new FitAdapter(getContext(), R.layout.fit_feed_item, feeds);
        listInfo.setAdapter(fitAdapter);

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(btnWeb.getTag().toString())));
            }
        });

        listInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showFeedSummary(position);
            }
        });

        showLoading();
        webView.loadUrl(URL);
    }

    private void showFeedSummary(int position) {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            tvSummary.setText(Html.fromHtml(feeds.get(position).getSummary()));
            btnWeb.setTag(feeds.get(position).getUrl());
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
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
                feed.setUrl(BASE_URL + row.child(0).child(0).attr("href"));
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
