package com.example.jungeb.seoulapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.adapter.ListviewCultureAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewFestivalAdapter;


public class FestivalTourFragment extends Fragment {

    ListView lvFestival;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_tour, container, false);

        //커스텀 리스트뷰 확인용
        lvFestival = (ListView)view.findViewById(R.id.lvFestival);

        ListviewFestivalAdapter listviewFestivalAdapter = new ListviewFestivalAdapter();
        lvFestival.setAdapter(listviewFestivalAdapter);

        for (int i=1; i<11; i++) {
            listviewFestivalAdapter.addItem("여의도 밤도깨비 야시장" + i, "서울특별시 영등포구 여의동로 330", "축제");
        }

        return view;
    }
}
