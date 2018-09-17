package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.jungeb.seoulapp.Items.ListviewKpopItems;
import com.example.jungeb.seoulapp.Items.ListviewTravelItems;
import com.example.jungeb.seoulapp.R;

import java.util.ArrayList;

public class ListviewTravelAdapter extends BaseAdapter {

    private ArrayList<ListviewTravelItems> TravelItems = new ArrayList<>();

    @Override
    public int getCount() {
        return TravelItems.size();
    }

    @Override
    public ListviewTravelItems getItem(int position) {
        return TravelItems.get(position);
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
            convertView = inflater.inflate(R.layout.listview_travel, parent, false);
        }

        TextView tvTravelDate = (TextView) convertView.findViewById(R.id.tvTravelDate);
        TextView tvTravelTitle = (TextView) convertView.findViewById(R.id.tvTravelTitle);
        TextView tvTravelAddress = (TextView) convertView.findViewById(R.id.tvTravelAddress);
        TextView tvTravelKinds = (TextView) convertView.findViewById(R.id.tvTravelKinds);

        ListviewTravelItems travelItems = getItem(position);

        tvTravelDate.setText(travelItems.getTravelDate());
        tvTravelTitle.setText(travelItems.getTravelTitle());
        tvTravelAddress.setText(travelItems.getTravelAddress());
        tvTravelKinds.setText(travelItems.getTravelTitle());


        return convertView;
    }

    public void addItem(String date, String title, String address, String kinds) {

        ListviewTravelItems customitem = new ListviewTravelItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setTravelDate(date);
        customitem.setTravelTitle(title);
        customitem.setTravelAddress(address);
        customitem.setTravelKind(kinds);

        /* customItems에 customitem을 추가한다. */
        TravelItems.add(customitem);

    }

    public ListviewTravelAdapter() {
        super();
    }
}
