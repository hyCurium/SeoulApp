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


public class CultureTourFragment extends Fragment {

    ListView lvCulture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_culture_tour, container, false);

        //커스텀 리스트뷰 확인용
        lvCulture = (ListView)view.findViewById(R.id.lvCulture);

        ListviewCultureAdapter listviewCultureAdapter = new ListviewCultureAdapter();
        lvCulture.setAdapter(listviewCultureAdapter);

        for (int i=1; i<11; i++) {
            listviewCultureAdapter.addItem("숭례문" + i, "서울특별시 중구 세종대로 40", "문화재");
        }

        return view;
    }
}
