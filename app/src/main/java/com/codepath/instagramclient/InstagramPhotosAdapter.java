package com.codepath.instagramclient;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.format.DateUtils;


import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private final static String INSTAGRAM_BLUE = "#315E82";

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    private static class ViewHolder {
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvLikeCount;
        ImageView ivProPic;
        TextView tvUserName;
        TextView tvTimeStamp;
        TextView tvViewAllComments;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder;
        //get data item for this position
        InstagramPhoto photo=getItem(position);
        //check if we are using a recycled view,if not we need to inflate
        if (convertView==null) {
            //create new view from the template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvLikeCount = (TextView)convertView.findViewById(R.id.tvLikes);
            viewHolder.ivProPic = (ImageView)convertView.findViewById(R.id.ivProPic);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvTimeStamp=(TextView)convertView.findViewById(R.id.tvCommentTime);
           // viewHolder.tvTimeStamp = (TextView)convertView.findViewById(R.id.tvTimeStamp);
            viewHolder.tvViewAllComments = (TextView)convertView.findViewById(R.id.tvViewAllComments);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //look up the views for populating the data
           // TextView tvCaption=(TextView) convertView.findViewById(R.id.tvCaption);
           // ImageView ivPhoto=(ImageView) convertView.findViewById(R.id.ivPhoto);
       // ImageView ivProPic=(ImageView) convertView.findViewById(R.id.ivProPic);
            //TextView tvUserName=(TextView) convertView.findViewById(R.id.tvUserName);
           // TextView tvLikesCount=(TextView) convertView.findViewById(R.id.tvLikesCount);


        //Insert model data into each of the view items
        viewHolder.tvCaption.setText(photo.caption);
        viewHolder.tvUserName.setText(photo.username);
        // like count
        viewHolder.tvLikeCount.setText(photo.likesCount + " likes");
        // set the relative timestamp
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                photo.createdTime,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString();
        viewHolder.tvTimeStamp.setText(relativeTime);

        // change the color of the heart icon
        //viewHolder.tvLikeCount.setCom
        Drawable heartIcon = viewHolder.tvLikeCount.getCompoundDrawables()[0];
        ColorFilter colorFilter = new LightingColorFilter(Color.parseColor(INSTAGRAM_BLUE), Color.parseColor(INSTAGRAM_BLUE));
        heartIcon.setColorFilter(colorFilter);
       // tvLikesCount.setText(String.valueOf(photo.likesCount));
       // Picasso.with(getContext()).load(photo.proPic).into(ivProPic);
        Picasso.with(getContext()).load(photo.proPic).transform(new CircleTransform()).into(viewHolder.ivProPic);
        //clear out the image view
        viewHolder.ivPhoto.setImageResource(0);
            //insert image view using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.ivPhoto);
        viewHolder.tvViewAllComments.setText("View all " + photo.commentsCount + " comments");
        viewHolder.tvViewAllComments.setTag(position);
        //return created item as view
        return convertView;

    }
}
