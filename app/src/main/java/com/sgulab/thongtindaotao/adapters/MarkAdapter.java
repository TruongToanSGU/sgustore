package com.sgulab.thongtindaotao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.objects.MarkSubject;
import com.sgulab.thongtindaotao.objects.MarkSubjectDetail;

import java.util.ArrayList;

public class MarkAdapter extends BaseExpandableListAdapter {

    private ArrayList<MarkSubject> groups;
    private ArrayList<Object> childs;
    private LayoutInflater mInflater;

    public MarkAdapter(Context context, ArrayList<MarkSubject> groups, ArrayList<Object> childs) {
        this.groups = groups;
        this.childs = childs;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<MarkSubjectDetail>)childs.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((ArrayList<MarkSubjectDetail>)childs.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mark_group, parent, false);
            holder = new GroupHolder();
            holder.tvSName = (TextView) convertView.findViewById(R.id.tvSName);
            holder.tvClass = (TextView) convertView.findViewById(R.id.tvClass);
            holder.tvFinalMark = (TextView) convertView.findViewById(R.id.tvFinalMark);
            convertView.setTag(holder);
        }
        holder = (GroupHolder) convertView.getTag();
        MarkSubject subject = (MarkSubject) getGroup(groupPosition);
        holder.tvSName.setText(subject.getName());
        holder.tvFinalMark.setText(subject.getFinalMark() + "");
        holder.tvClass.setText(subject.getClassification());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mark_detail, parent, false);
            holder = new ChildHolder();
            holder.tvSId = (TextView) convertView.findViewById(R.id.tvSId);
            holder.tvTC = (TextView) convertView.findViewById(R.id.tvTC);
            holder.tvPerProcess = (TextView) convertView.findViewById(R.id.tvPerProcess);
            holder.tvPerExam = (TextView) convertView.findViewById(R.id.tvPerExam);
            holder.tvMarkProcess = (TextView) convertView.findViewById(R.id.tvMarkProcess);
            holder.tvMarkExam = (TextView) convertView.findViewById(R.id.tvMarkExam);
            convertView.setTag(holder);
        }
        holder = (ChildHolder) convertView.getTag();
        MarkSubjectDetail subject = (MarkSubjectDetail) getChild(groupPosition, childPosition);
        holder.tvSId.setText(subject.getId());
        holder.tvTC.setText(subject.getTc() + "");
        holder.tvPerProcess.setText(subject.getPercentProcess() + "");
        holder.tvPerExam.setText(subject.getPercentExam() + "");
        holder.tvMarkProcess.setText(subject.getMarkProcess() + "");
        holder.tvMarkExam.setText(subject.getMarkExam() + "");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    private class GroupHolder {
        TextView tvSName;
        TextView tvFinalMark;
        TextView tvClass;
    }

    private class ChildHolder {
        TextView tvSId;
        TextView tvTC;
        TextView tvPerProcess;
        TextView tvPerExam;
        TextView tvMarkProcess;
        TextView tvMarkExam;
    }
}
