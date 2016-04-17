package com.sgulab.thongtindaotao.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.models.ScheduleInfo;

public class TBottomDialog extends Dialog {

    public TBottomDialog(Context context, ScheduleInfo info) {
        super(context);

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);

        if (info != null) {
            ScheduleInfoHolder holder = new ScheduleInfoHolder(contentView);
            holder.tvScheduleInfoDetailId.setText(info.getId());
            holder.tvScheduleInfoDetailName.setText(info.getName());
            holder.tvScheduleInfoDetailRoom.setText(info.getRoom());
            holder.tvScheduleInfoDetailGroup.setText(info.getGroup() + "");
            holder.tvScheduleInfoDetailDay.setText(info.getDayOfWeek() + "");
            holder.tvScheduleInfoDetailStart.setText(info.getSessionBegin() + "");
            holder.tvScheduleInfoDetailDur.setText(info.getSessionDuration() + "");
            holder.tvScheduleInfoDetailTeacher.setText(info.getTeacher());
            holder.tvScheduleInfoDetailClass.setText(info.getClassName());
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(contentView);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

//        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
    }

    private class ScheduleInfoHolder {
        private TextView tvScheduleInfoDetailId;
        private TextView tvScheduleInfoDetailName;
        private TextView tvScheduleInfoDetailRoom;
        private TextView tvScheduleInfoDetailGroup;
        private TextView tvScheduleInfoDetailDay;
        private TextView tvScheduleInfoDetailStart;
        private TextView tvScheduleInfoDetailDur;
        private TextView tvScheduleInfoDetailTeacher;
        private TextView tvScheduleInfoDetailClass;

        public ScheduleInfoHolder(View v) {
            tvScheduleInfoDetailId = (TextView) v.findViewById(R.id.tv_schedule_detail_id);
            tvScheduleInfoDetailName = (TextView) v.findViewById(R.id.tv_schedule_detail_name);
            tvScheduleInfoDetailRoom = (TextView) v.findViewById(R.id.tv_schedule_detail_room);
            tvScheduleInfoDetailGroup = (TextView) v.findViewById(R.id.tv_schedule_detail_group);
            tvScheduleInfoDetailDay = (TextView) v.findViewById(R.id.tv_schedule_detail_day);
            tvScheduleInfoDetailStart = (TextView) v.findViewById(R.id.tv_schedule_detail_start);
            tvScheduleInfoDetailDur = (TextView) v.findViewById(R.id.tv_schedule_detail_dur);
            tvScheduleInfoDetailTeacher = (TextView) v.findViewById(R.id.tv_schedule_detail_teacher);
            tvScheduleInfoDetailClass = (TextView) v.findViewById(R.id.tv_schedule_detail_class);
        }
    }
}
