package com.codepath.instagramclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
public class Photos extends ActionBarActivity {

    public static final String Client_ID="438e5489b66a43d8b871a30da4578a27";
    public static ArrayList<InstagramPhoto> photolist;
    private InstagramPhotosAdapter adpPhoto;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photolist=new ArrayList<>();
        adpPhoto=new InstagramPhotosAdapter(this,photolist);

        ListView lvPhotos=(ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(adpPhoto);
        fetchPopularPhotos();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.scPhotos);
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }

    public void fetchPopularPhotos(){
        /*Popular https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        -Response
                -Type  : {“data”=>[x]=>”type”} (“image” or “video”)
        -URL: {“data=>[x]=>”images”=>”standard resolution”=>”url”}
        -Caption: {“data”=>[x]=>”caption”=>”text”}
        -Author Name: {“data”=>[x]=>”user”=>”username”}*/
        String url="https://api.instagram.com/v1/media/popular?client_id="+Client_ID;
        //Create the network client
        AsyncHttpClient client=new AsyncHttpClient();
        //trigger the GEt request
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess(worked,200)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                // -Type  : {“data”=>[x]=>”type”} (“image” or “video”)
                //-URL: {“data=>[x]=>”images”=>”standard resolution”=>”url”}
                //-Caption: {“data”=>[x]=>”caption”=>”text”}
                //-Author Name: {“data”=>[x]=>”user”=>”username”}*/
                // Log.i("Debug",response.toString());
                //Iterate each of the photo items and decode them into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    //iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photojson = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photojson.getJSONObject("user").getString("username");
                        photo.caption = photojson.getJSONObject("caption").getString("text");
                        photo.imageUrl = photojson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photojson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photojson.getJSONObject("likes").getInt("count");
                        photo.proPic = photojson.getJSONObject("user").getString("profile_picture");
                        photo.commentsCount = photojson.getJSONObject("comments").getInt("count");
                        photo.comments = new ArrayList<comments>();
                        photo.createdTime = photojson.getLong("created_time") * 1000;

                        JSONArray photoCommentsJSON = photojson.getJSONObject("comments").getJSONArray("data");
                        for (int j = 0; j < photoCommentsJSON.length(); j++) {
                            JSONObject commentJSON = photoCommentsJSON.getJSONObject(j);
                            comments comment = new comments();
                            comment.text = commentJSON.getString("text");
                            comment.username = commentJSON.getJSONObject("from").getString("username");
                            comment.profilePictureURL = commentJSON.getJSONObject("from").getString("profile_picture");
                            comment.commentTime = commentJSON.getLong("created_time") * 1000;
                            photo.comments.add(comment);
                        }

                        photolist.add(photo);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adpPhoto.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            //onFailure(failed)

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }
    // Show the comments for a photo
    public void showComments(View view) {
       TextView viewAllComments = (TextView)view;
        //TextView viewAllComments = (TextView)findViewById(R.id.tvViewAllComments);
        int photoPosition = (int)viewAllComments.getTag();

        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("photo_position", photoPosition);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action b
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
