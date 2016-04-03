package com.sgulab.thongtindaotao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.sgulab.thongtindaotao.R;
import com.sgulab.thongtindaotao.adapters.MarkAdapter;
import com.sgulab.thongtindaotao.models.MarkSubject;
import com.sgulab.thongtindaotao.models.MarkSubjectDetail;
import com.sgulab.thongtindaotao.models.MarkTerm;

import java.util.ArrayList;
import java.util.List;

public class WeekSchedulePageFragment extends BaseFragment {

    private ExpandableListView listMark;
    private MarkAdapter adapter;
    private ArrayList<MarkSubject> groups;
    private ArrayList<Object> childs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_schedule_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        listMark = (ExpandableListView) view.findViewById(R.id.list_diem);
//        groups = new ArrayList<>();
//        childs = new ArrayList<>();
//        MarkTerm data = (MarkTerm) getArguments().getSerializable(TERM);
//        List<MarkSubjectDetail> marks = data.getMarks();
//        for (MarkSubjectDetail detail : marks) {
//            groups.add(detail);
//            ArrayList<MarkSubjectDetail> l = new ArrayList<>();
//            l.add(detail);
//            childs.add(l);
//        }
//        if (data.getTermAvg10() != 0) {
//            ((TextView) view.findViewById(R.id.tvAvg10)).setText(data.getTermAvg10() + "");
//            ((TextView) view.findViewById(R.id.tvAvg4)).setText(data.getTermAvg4() + "");
//            ((TextView) view.findViewById(R.id.tvAllAvg10)).setText(data.getAllTermAvg10() + "");
//            ((TextView) view.findViewById(R.id.tvAllAvg4)).setText(data.getAllTermAvg4() + "");
//            ((TextView) view.findViewById(R.id.tvPassedTc)).setText(data.getPassedTc() + "");
//            ((TextView) view.findViewById(R.id.tvAllPassedTc)).setText(data.getAllPassedTc() + "");
//            ((TextView) view.findViewById(R.id.tvAvgConduct)).setText(data.getAvgConduct() + "");
//            ((TextView) view.findViewById(R.id.tvConductType)).setText(data.getConductType() + "");
//        }
//        adapter = new MarkAdapter(getActivity(), groups, childs);
//        listMark.setAdapter(adapter);
    }

    public void update() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public static final String TERM = "TERM";
    public static Fragment newInstance(MarkTerm term) {
        WeekSchedulePageFragment f = new WeekSchedulePageFragment();
        Bundle args = new Bundle();
        args.putSerializable(TERM, term);
        f.setArguments(args);
        return f;
    }
}
