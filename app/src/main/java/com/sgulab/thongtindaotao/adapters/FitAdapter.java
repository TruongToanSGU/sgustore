package com.sgulab.thongtindaotao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.models.FitFeed;

import java.util.List;

public class FitAdapter extends ArrayAdapter {

    private LayoutInflater mLayoutInflater;

    public FitAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FitFeedHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fit_feed_item, parent, false);
            holder = new FitFeedHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FitFeedHolder) convertView.getTag();
        }
        FitFeed feed = (FitFeed) getItem(position);
        if (feed != null) {
            holder.tvFeedDate.setText(feed.getDate());
            holder.tvFeedTitle.setText(feed.getTitle());
        }
        return convertView;
    }

    private class FitFeedHolder {
        private TextView tvFeedTitle;
        private TextView tvFeedDate;

        public FitFeedHolder(View v) {
            tvFeedDate = (TextView) v.findViewById(R.id.fit_feed_date);
            tvFeedTitle = (TextView) v.findViewById(R.id.fit_feed_title);
        }
    }
}
