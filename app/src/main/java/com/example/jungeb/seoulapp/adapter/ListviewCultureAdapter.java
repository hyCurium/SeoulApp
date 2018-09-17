package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jungeb.seoulapp.Items.ListviewCultureItems;
import com.example.jungeb.seoulapp.R;

import java.util.ArrayList;

public class ListviewCultureAdapter extends BaseAdapter{

    private ArrayList<ListviewCultureItems> cultureItems = new ArrayList<>();

    @Override
    public int getCount() {
        return cultureItems.size();
    }

    @Override
    public ListviewCultureItems getItem(int position) {
        return cultureItems.get(position);
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
            convertView = inflater.inflate(R.layout.listview_culture, parent, false);
        }

        TextView tvCultureTitle = (TextView) convertView.findViewById(R.id.tvCultureTitle) ;
        TextView tvCultureAddress = (TextView) convertView.findViewById(R.id.tvCultureAddress) ;
        TextView tvCultureKinds = (TextView) convertView.findViewById(R.id.tvCultureKinds) ;

        ListviewCultureItems cultureItems = getItem(position);

        tvCultureTitle.setText(cultureItems.getCultureTitle());
        tvCultureAddress.setText(cultureItems.getCultureAddress());
        tvCultureKinds.setText(cultureItems.getCultureKinds());


        return convertView;
    }

    public void addItem(String title, String address, String kinds) {

        ListviewCultureItems customitem = new ListviewCultureItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setCultureTitle(title);
        customitem.setCultureAddress(address);
        customitem.setCultureKinds(kinds);

        /* customItems에 customitem을 추가한다. */
        cultureItems.add(customitem);

    }

    public ListviewCultureAdapter() {
        super();
    }
}
