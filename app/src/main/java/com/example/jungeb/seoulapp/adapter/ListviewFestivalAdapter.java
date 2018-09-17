package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jungeb.seoulapp.Items.ListviewFestivalItems;
import com.example.jungeb.seoulapp.R;

import java.util.ArrayList;

public class ListviewFestivalAdapter extends BaseAdapter{

    private ArrayList<ListviewFestivalItems> festivalItems = new ArrayList<>();

    @Override
    public int getCount() {
        return festivalItems.size();
    }

    @Override
    public ListviewFestivalItems getItem(int position) {
        return festivalItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_festival, parent, false);
        }

        TextView tvFestivalTitle = (TextView) convertView.findViewById(R.id.tvFestivalTitle) ;
        TextView tvFestivalAddress = (TextView) convertView.findViewById(R.id.tvFestivalAddress) ;
        TextView tvFestivalKinds = (TextView) convertView.findViewById(R.id.tvFestivalKinds) ;

        ListviewFestivalItems festivalItems = getItem(position);

        tvFestivalTitle.setText(festivalItems.getFestivalTitle());
        tvFestivalAddress.setText(festivalItems.getFestivalAddress());
        tvFestivalKinds.setText(festivalItems.getFestivalKinds());


        return convertView;
    }

    public void addItem(String title, String address, String kinds) {

        ListviewFestivalItems customitem = new ListviewFestivalItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setFestivalTitle(title);
        customitem.setFestivalAddress(address);
        customitem.setFestivalKinds(kinds);

        /* customItems에 customitem을 추가한다. */
        festivalItems.add(customitem);

    }

    public ListviewFestivalAdapter() {
        super();
    }

}
