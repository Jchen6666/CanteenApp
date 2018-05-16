package com.example.jerrychen.canteenapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jerrychen on 3/31/17.
 */

public class DishListItemAdapter extends ArrayAdapter<Dish>{
    private int resource;

    public DishListItemAdapter(Context context,int resource,List<Dish>objects){
        super(context,resource,objects);
        this.resource=resource;
    }
    @Override
    public View getView(int position , View convertView, ViewGroup parent){
        Dish dish=getItem(position);
        String title=dish.getTitle();
        String description=dish.getDescription();
        String pictureurl=dish.getPictureurl();
        double price =dish.getPrice();
        LinearLayout dishView;
        if (convertView==null){
            dishView =new LinearLayout(getContext());
            String inflater=Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li=(LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource,dishView,true);

        }else {
            dishView=(LinearLayout)convertView;
        }
        TextView titleview=(TextView)dishView.findViewById(R.id.dishlist_item_title);
        TextView descriptionview=(TextView)dishView.findViewById(R.id.dishlist_item_description);
        TextView priceview=(TextView)dishView.findViewById(R.id.dishlist_item_price);
        ImageView imageView=(ImageView)dishView.findViewById(R.id.dishlist_item_photo);
        Picasso.with(getContext()).load(pictureurl).into(imageView);

        titleview.setText(title);
        descriptionview.setText(description);
        priceview.setText(" DKK"+price);

        return dishView ;
    }
}
