package com.sgulab.thongtindaotao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.models.ExamSchedule;

import java.util.List;

public class ExamAdapter extends BaseAdapter {

  private List<ExamSchedule> examSchedules;
  private LayoutInflater inflater;

  public ExamAdapter(Context context, List<ExamSchedule> examSchedules) {
    this.examSchedules = examSchedules;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return examSchedules.size();
  }

  @Override
  public Object getItem(int position) {
    return examSchedules.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ExamHolder holder;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.fragment_exam_item, parent, false);
      holder = new ExamHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ExamHolder) convertView.getTag();
    }

    ExamSchedule examSchedule = (ExamSchedule) getItem(position);
    holder.tvExamDate.setText(examSchedule.getDate());
    holder.tvExamName.setText(examSchedule.getSubjectName());
    holder.tvExamRoom.setText(examSchedule.getRoom());

    return convertView;
  }

  private class ExamHolder {
    private TextView tvExamDate;
    private TextView tvExamName;
    private TextView tvExamRoom;

    public ExamHolder(View view) {
      tvExamDate = (TextView) view.findViewById(R.id.tv_exam_date);
      tvExamName = (TextView) view.findViewById(R.id.tv_exam_name);
      tvExamRoom = (TextView) view.findViewById(R.id.tv_exam_room);
    }
  }
}
