package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.models.ScheduleInfo;

import java.util.ArrayList;
import java.util.List;

public class WeekSchedulePageFragment extends BaseFragment {

    List<ScheduleInfo> infoList;

    private final int MIN_SESSION = 1;
    private final int MAX_SESSION = 13;
    private int count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_schedule_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        count = args.getInt(INFO_COUNT, 0);
        if (infoList == null) {
            infoList = new ArrayList<>();
        } else {
            infoList.clear();
        }

        for (int i = 0; i < count; i++) {
            ScheduleInfo info = (ScheduleInfo) getArguments().getSerializable(INFO + i);
            infoList.add(info);
        }

        if (infoList.size() == 0) return;
        LinearLayout root = (LinearLayout) view.findViewById(R.id.root);
        int idx = MIN_SESSION;
        for (final ScheduleInfo info : infoList) {
            View childBefore = new View(getActivity());
            childBefore.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, info.getSessionBegin() - idx));
            root.addView(childBefore);
            idx += (info.getSessionBegin() - idx + 1);
            View infoChild = getActivity().getLayoutInflater().inflate(R.layout.schedule_item, root, false);
            infoChild.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, info.getSessionDuration()));
            infoChild.setClickable(true);
            infoChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TBottomDialog(getContext(), info).show();
                }
            });
            ScheduleHolder holder = new ScheduleHolder(infoChild);
            holder.tvSubject.setText(info.getName());
            holder.tvRoom.setText(info.getRoom());
            root.addView(infoChild);
            idx += info.getSessionDuration() - 1;
        }
        View childAfter = new View(getActivity());
        childAfter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,
                MAX_SESSION - (infoList.get(infoList.size() - 1).getSessionBegin() + infoList.get(infoList.size() - 1).getSessionDuration() - 1)));
        root.addView(childAfter);
        root.invalidate();
    }

    public static final String INFO = "INFO";
    public static final String INFO_COUNT = "INFO_COUNT";
    public static Fragment newInstance(List<ScheduleInfo> infos) {
        WeekSchedulePageFragment f = new WeekSchedulePageFragment();
        Bundle args = new Bundle();
        if (infos != null) {
            Log.e("zzz", "New instance..." + infos.size());
            for (int i = 0; i < infos.size(); i++) {
                args.putSerializable(INFO + i, infos.get(i));
            }

            args.putInt(INFO_COUNT, infos.size());
        }

        f.setArguments(args);
        return f;
    }

    private class ScheduleHolder {
        private TextView tvSubject;
        private TextView tvRoom;

        public ScheduleHolder(View v) {
            tvSubject = (TextView) v.findViewById(R.id.tv_schedule_subject);
            tvRoom = (TextView) v.findViewById(R.id.tv_schedule_room);
        }
    }
}
