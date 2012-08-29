package com.example.feedcook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.feed.SyndFeedHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;

import com.example.domain.FeedData;
import com.example.utils.SyndFeedListAdapter;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

public class WebActivity extends Activity {
	
	protected static final String TAG = WebActivity.class.getSimpleName();
	
	private ListView mainListView;
	private SyndFeed feed;
	private WebView wv;
	
	private FeedData editObj;

	private SyndFeedListAdapter listAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mainListView = (ListView) findViewById( R.id.mainListView );
        
        editObj = (FeedData) getIntent().getSerializableExtra("editObj");
        
        new DownloadRssFeedTask().execute();
        
        setTitle(editObj.getFeedSource() + " - " + editObj.getFeedName());
                
    }
    
    private void refreshRssFeed(SyndFeed feed) {
        this.feed = feed;

        if (feed != null) {
        	setTitle(feed.getTitle());
        	
        	listAdapter = new SyndFeedListAdapter(this, feed);
    	    mainListView.setAdapter( listAdapter );
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuRefresh:
				new DownloadRssFeedTask().execute();
				break;
			case R.id.menuBack:
				super.finish();
				break;
			default:
				break;
		}

		return true;
	}
    
    public void urlLinkHandler(View v) {
    	if (feed.getLink() != null) {
    		Log.i(TAG, feed.getLink());
    		Intent browser = new Intent (this, BrowserActivity.class);
    		browser.putExtra("title", feed.getTitle());
    		browser.putExtra("url", feed.getLink());
        	startActivity(browser);
    	}
    }
    
    private class DownloadRssFeedTask extends AsyncTask<Void, Void, SyndFeed> {
    	
    	private ProgressDialog progressDialog;
    	
        @Override
        protected void onPreExecute() {
            // before the network request begins, show a progress indicator
            showLoadingProgressDialog();
        }

        @Override
        protected SyndFeed doInBackground(Void... params) {
            try {
                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Configure the SyndFeed message converter.
                List<MediaType> mediaTypes = new ArrayList<MediaType>();
                mediaTypes.add(MediaType.TEXT_XML);
                mediaTypes.add(MediaType.TEXT_HTML);
                mediaTypes.add(MediaType.TEXT_PLAIN);
                mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
                mediaTypes.add(MediaType.APPLICATION_JSON);
                mediaTypes.add(MediaType.APPLICATION_RSS_XML);
                mediaTypes.add(MediaType.APPLICATION_XML);
                mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
                
                SyndFeedHttpMessageConverter syndFeedConverter = new SyndFeedHttpMessageConverter();
                syndFeedConverter.setSupportedMediaTypes(mediaTypes);
                
                // Add the SyndFeed message converter to the RestTemplate instance
                restTemplate.getMessageConverters().add(syndFeedConverter);

                // The URL for making the request
                String url = editObj.getFeedUrl(); //"http://timesofindia.feedsportal.com/c/33039/f/533916/index.rss";

                // Initiate the request and return the results
                return restTemplate.getForObject(url, SyndFeed.class);
            } catch (Exception e) {
                //Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }
        
        @Override
        protected void onPostExecute(SyndFeed feed) {
            // hide the progress indicator when the network request is complete
            dismissProgressDialog();

            // return the list of states
            refreshRssFeed(feed);
        }
        
        public void showLoadingProgressDialog() {
            this.showProgressDialog("Loading. Please wait...");
        }

        public void showProgressDialog(CharSequence message) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(WebActivity.this);
                progressDialog.setTitle("Loading Feeds...");
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
            }

            progressDialog.setMessage(message);
            progressDialog.show();
        }

        public void dismissProgressDialog() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

    }
}
