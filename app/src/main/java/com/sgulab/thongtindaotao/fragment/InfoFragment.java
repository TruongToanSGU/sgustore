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
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.models.SvInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.atomic.AtomicInteger;

public class InfoFragment extends SGUFragment {

    private WebView webView;
    private AtomicInteger step = new AtomicInteger();

    private TextView tvInfoMssv;
    private TextView tvInfoTen;
    private TextView tvInfoNs;
    private TextView tvInfoNganh;
    private TextView tvInfoKhoa;
    private TextView tvInfoNK;
    private TextView tvInfoCVHT;
    private TextView tvInfoHDT;
    private TextView tvInfoLop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (WebView) view.findViewById(R.id.webView);

        tvInfoMssv = (TextView) view.findViewById(R.id.tvInfoMssv);
        tvInfoTen = (TextView) view.findViewById(R.id.tvInfoTen);
        tvInfoLop = (TextView) view.findViewById(R.id.tvInfoLop);
        tvInfoKhoa = (TextView) view.findViewById(R.id.tvInfoKhoa);
        tvInfoNganh = (TextView) view.findViewById(R.id.tvInfoNganh);
        tvInfoNK = (TextView) view.findViewById(R.id.tvInfoNK);
        tvInfoNs = (TextView) view.findViewById(R.id.tvInfoNs);
        tvInfoHDT = (TextView) view.findViewById(R.id.tvInfoDt);
        tvInfoCVHT = (TextView) view.findViewById(R.id.tvInfoCVHT);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        step.set(0);
        webView.loadUrl("http://thongtindaotao.sgu.edu.vn/Default.aspx?page=xemdiemthi&id=" + getCurrentMSSV());
    }

    @Override
    public void onSearch(String title) {
        super.onSearch(title);
        step.set(0);
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
            Document document = Jsoup.parse(html);
            final SvInfo svInfo = new SvInfo();
            try {
                svInfo.setId(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblMaSinhVien").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setName(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblTenSinhVien").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setBirthday(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblNgaySinh").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setClassName(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblLop").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setDisciplines(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lbNganh").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setMajors(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoa").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setTrainingSystem(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblHeDaoTao").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setAcademicYear(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblKhoaHoc").html());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                svInfo.setAcademicAdvisors(document.getElementById("ctl00_ContentPlaceHolder1_ctl00_ucThongTinSV_lblCVHT").html());
            } catch (Exception e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSvInfo(svInfo);
                }
            });
        }
    }

    private void showSvInfo(SvInfo svInfo) {
        tvInfoMssv.setText(svInfo.getId());
        tvInfoTen.setText(svInfo.getName());
        tvInfoLop.setText(svInfo.getClassName());
        tvInfoNs.setText(svInfo.getBirthday());
        tvInfoKhoa.setText(svInfo.getDisciplines());
        tvInfoNganh.setText(svInfo.getMajors());
        tvInfoNK.setText(svInfo.getAcademicYear());
        tvInfoHDT.setText(svInfo.getTrainingSystem());
        tvInfoCVHT.setText(svInfo.getAcademicAdvisors());
    }
}
