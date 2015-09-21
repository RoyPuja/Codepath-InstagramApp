package com.codepath.instagramclient;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.List;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.text.format.DateUtils;
import org.w3c.dom.Text;
import android.text.Html;

/**
 * Created by pnroy on 9/20/15.
 */
public class CommentAdapter extends ArrayAdapter<comments> {
    private final static String INSTAGRAM_BLUE = "#315E82";
    public CommentAdapter(Context context,  List<comments> objects) {
        super(context,0, objects);
    }
    public static class ViewHolder{
        ImageView ivUserProPic;
        TextView tvText;
        TextView tvCommentDate;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder;
        comments comment=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivUserProPic = (ImageView)convertView.findViewById(R.id.ivCommentUserProfile);
            viewHolder.tvText = (TextView)convertView.findViewById(R.id.tvCommentText);
            viewHolder.tvCommentDate = (TextView)convertView.findViewById(R.id.tvCommentTimeStamp);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // set image using Picasso
        Picasso.with(getContext())
                .load(comment.profilePictureURL)
                .placeholder(R.drawable.placeholder_image)
                .into(viewHolder.ivUserProPic);

        String commentHtml = "<font color='" + INSTAGRAM_BLUE + "'>" + comment.username + "</font> " + comment.text;
        viewHolder.tvText.setText(Html.fromHtml(commentHtml));

        // set the relative timestamp
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                comment.commentTime,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString();
        viewHolder.tvCommentDate.setText(relativeTime);

        return convertView;

    }
}
