package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jungeb.seoulapp.Items.ListviewBookmarkItems;
import com.example.jungeb.seoulapp.R;

import java.util.ArrayList;

public class ListviewBookmarkAdapter extends BaseAdapter{

    private ArrayList<ListviewBookmarkItems> bookmarkItems = new ArrayList<>();

    @Override
    public int getCount() {
        return bookmarkItems.size();
    }

    @Override
    public ListviewBookmarkItems getItem(int position) {
        return bookmarkItems.get(position);
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
            convertView = inflater.inflate(R.layout.listview_bookmark, parent, false);
        }

        TextView tvBookmarkTitle = (TextView) convertView.findViewById(R.id.tvBookmarkTitle) ;
        TextView tvBookmarkAddress = (TextView) convertView.findViewById(R.id.tvBookmarkAddress) ;
        TextView tvBookmarkKinds = (TextView) convertView.findViewById(R.id.tvBookmarkKinds) ;

        ListviewBookmarkItems bookmarkItems = getItem(position);

        tvBookmarkTitle.setText(bookmarkItems.getBookmarkTitle());
        tvBookmarkAddress.setText(bookmarkItems.getBookmarkAddress());
        tvBookmarkKinds.setText(bookmarkItems.getBookmarkKinds());


        return convertView;
    }

    public void addItem(String title, String address, String kinds) {

        ListviewBookmarkItems customitem = new ListviewBookmarkItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setBookmarkTitle(title);
        customitem.setBookmarkAddress(address);
        customitem.setBookmarkKinds(kinds);

        /* customItems에 customitem을 추가한다. */
        bookmarkItems.add(customitem);

    }

    public ListviewBookmarkAdapter() {
        super();
    }

}
