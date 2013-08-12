package com.photo.fuguu;

/**
 * Created by JEETU on 8/12/13.
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
public class ListViewAdapter  extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<PictureList> picturearraylist = null;
    private ArrayList<PictureList> arraylist;

    public ListViewAdapter(Context context,
                           List<PictureList> picturearraylist) {
        this.context = context;
        this.picturearraylist = picturearraylist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<PictureList>();
        this.arraylist.addAll(picturearraylist);
        imageLoader = new ImageLoader(context);
    }

    public class ViewHolder {
        ImageView phone;
    }

    @Override
    public int getCount() {
        return picturearraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return picturearraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the ImageView in listview_item.xml
            holder.phone = (ImageView) view.findViewById(R.id.phone);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into ImageView
        imageLoader.DisplayImage(picturearraylist.get(position).getPhone(),
                holder.phone);
        // Log.i("Khua bhai",phonearraylist.get(position).getPhone());

        // Listen for GridView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data phone
                intent.putExtra("phone",
                        (picturearraylist.get(position).getPhone()));
                // Start SingleItemView Class
                context.startActivity(intent);
            }
        });
        return view;
    }
}