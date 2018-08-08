package com.pisystem.raghoji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pisystem.raghoji.R;

import java.util.ArrayList;

public class DashBoardViewAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> images;
    private ArrayList<String> names;
    //Imageloader to load images
    private ImageLoader imageLoader;
    public DashBoardViewAdapter (Context context, ArrayList<String> images, ArrayList<String> names)
    {
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = names;
        }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creating a linear layout
        View grid;
        LayoutInflater lf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        grid=new View(context);
        grid=lf.inflate(R.layout.dash_grid_view, null);

       ImageView networkImageView = (ImageView) grid.findViewById(R.id.imageView2);
        TextView tvregister = (TextView)grid.findViewById(R.id.tv_register);;
        tvregister.setText(names.get(position));
     /*   //Initializing ImageLoader
       // imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(networkImageView, R.drawable.registration,R.drawable.ic_material_estimmator ));
*/      if("registration".equals(images.get(position))) {
            networkImageView.setImageResource(R.drawable.personal_details);
        }else if("messageHR".equals(images.get(position))){
            networkImageView.setImageResource(R.drawable.fee_detail);
        }else if("messageRP".equals(images.get(position))){
            networkImageView.setImageResource(R.drawable.news_events);
        }else if("trsfrfunds".equals(images.get(position))){
            networkImageView.setImageResource(R.drawable.fee_payments);
        }else if("query".equals(images.get(position))){
            networkImageView.setImageResource(R.drawable.query);
        }else if("debt".equals(images.get(position))){
            networkImageView.setImageResource(R.drawable.contact_us);
        }
        return grid;

    }
}
