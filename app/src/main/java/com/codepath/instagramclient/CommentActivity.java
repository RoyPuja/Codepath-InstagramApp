package com.codepath.instagramclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.List;

/**
 * Created by pnroy on 9/20/15.
 */
public class CommentActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // grab the comments to display
        int photoPosition = getIntent().getExtras().getInt("photo_position");
        InstagramPhoto photo = Photos.photolist.get(photoPosition);

        CommentAdapter adapter = new CommentAdapter(this, photo.comments);
        ListView lvComments = (ListView)findViewById(R.id.lvComments);
        lvComments.setAdapter(adapter);
    }
}
