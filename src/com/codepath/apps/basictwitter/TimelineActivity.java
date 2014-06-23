package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvtweets;
	private final int REQUEST_CODE = 20;
	private Tweet editTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		populateTimeline();
		lvtweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		// aTweets = new ArrayAdapter<Tweet>(this,
		// android.R.layout.simple_list_item_1, tweets);
		aTweets = new TweetArrayAdapter(this, tweets);
		lvtweets.setAdapter(aTweets);

		/*
		 * lvtweets.setOnScrollListener(new EndlessScrollListener() {
		 * 
		 * @Override public void onLoadMore(int page, int totalItemsCount) { //
		 * Triggered only when new data needs to be appended to the list // Add
		 * whatever code is needed to append new items to your // AdapterView //
		 * customLoadMoreDataFromApi(page);
		 * customLoadMoreDataFromApi(totalItemsCount); } });
		 */
	}

	public void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJSONArray(json));
			}

			@Override
			public void onFailure(Throwable e, String s) {
				// TODO Auto-generated method stub
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}

	public void onComposeAction(MenuItem mi) {
		Intent i = new Intent(this, ComposeActivity.class);
		i.putExtra("tweet", tweets);
		startActivityForResult(i, REQUEST_CODE);

		Toast.makeText(this, "ready to compose", Toast.LENGTH_LONG).show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 20) {
				editTweet = (Tweet) data.getSerializableExtra("bodyforTweet");
				aTweets.insert(editTweet, 0);
				aTweets.notifyDataSetChanged();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

}