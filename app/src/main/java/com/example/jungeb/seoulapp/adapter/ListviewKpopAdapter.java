package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jungeb.seoulapp.Items.ListviewCultureItems;
import com.example.jungeb.seoulapp.Items.ListviewKpopItems;
import com.example.jungeb.seoulapp.R;

import java.util.ArrayList;

public class ListviewKpopAdapter extends BaseAdapter {

    private ArrayList<ListviewKpopItems> KpopItems = new ArrayList<>();

    @Override
    public int getCount() {
        return KpopItems.size();
    }

    @Override
    public ListviewKpopItems getItem(int position) {
        return KpopItems.get(position);
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
            convertView = inflater.inflate(R.layout.listview_kpop, parent, false);
        }

        TextView tvKpopTitle = (TextView) convertView.findViewById(R.id.tvKpopTitle) ;
        TextView tvKpopAddress = (TextView) convertView.findViewById(R.id.tvKpopAddress) ;
        TextView tvKpopKinds = (TextView) convertView.findViewById(R.id.tvKpopKinds) ;

        ListviewKpopItems kpopItems = getItem(position);

        tvKpopTitle.setText(kpopItems.getKpopTitle());
        tvKpopAddress.setText(kpopItems.getKpopAddress());
        tvKpopKinds.setText(kpopItems.getKpopKinds());


        return convertView;
    }

    public void addItem(String title, String address, String kinds) {

        ListviewKpopItems customitem = new ListviewKpopItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setKpopTitle(title);
        customitem.setKpopAddress(address);
        customitem.setKpopKinds(kinds);

        /* customItems에 customitem을 추가한다. */
        KpopItems.add(customitem);

    }

    public ListviewKpopAdapter() {
        super();
    }
}
