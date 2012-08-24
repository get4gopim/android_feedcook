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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;

import com.example.domain.FeedData;
import com.example.utils.SyndFeedListAdapter;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

public class WebActivity extends Activity {
	
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
                
        /*wv = (WebView) findViewById(R.id.webview);
        
        final String mimeType = "text/html"; 
        final String encoding = "UTF-8"; 
        String html = "<table border='0' cellpadding='2' cellspacing='7' style='vertical-align:top;'><tr><td width='80' align='center' valign='top'><font style='font-size:85%;font-family:arial,sans-serif'><a href='http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGcM8hMahpLOc6BsfzHxJQevosw6g&amp;url=http://indiatoday.intoday.in/story/rajeev-shukla-instructs-rajya-sabha-deputy-chairman-to-adjourn-house/1/214065.html'><img src='//nt2.ggpht.com/news/tbn/FmNz1WoUelp1eM/0.jpg' alt='' border='1' width='80' height='51' /><br /><font size='-2'>India Today</font></a></font></td><td valign='top' class='j'><font style='font-size:85%;font-family:arial,sans-serif'><br /><div style='padding-top:0.8em;'><img alt='' height='1' width='1' /></div><div class='lh'><a href='http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGj-d8Lw_TkmOjdPxB10zUtNVIbmg&amp;url=http://www.ndtv.com/article/india/caught-on-tape-adjourn-the-house-minister-whispered-to-deputy-chairman-257559'><b>Caught on tape: Adjourn the house, minister whispered to Deputy Chairman</b></a><br /><font size='-1'><b><font color='#6f6f6f'>NDTV</font></b></font><br /><font size='-1'>New Delhi: As the BJP shouted its demand for the resignation of the Prime Minister, in the Rajya Sabha, Rajiv Shuka walked up to PJ Kurien, who had just been elected Deputy Chairperson of the Rajya Sabha, and was presiding over the house.</font><br /><font size='-1'><a href='http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNEM3p9v0OQNZ6KX983lEpHE3H2Mdg&amp;url=http://www.dnaindia.com/india/report_coalgate-rajiv-shukla-caught-on-mike-telling-deputy-chairman-to-adjourn-rs_1730960'>Coalgate: Rajiv Shukla caught on mike telling deputy chairman to adjourn RS</a><font size='-1' color='#6f6f6f'><nobr>Daily News & Analysis</nobr></font></font><br /><font size='-1'><a href='http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNECwsKf5YIFtFKNY5-SA63Dtf8YqA&amp;url=http://ibnlive.in.com/news/upa-minister-caught-asking-rs-chair-to-adjourn-house/284247-37-64.html'>UPA Minister Rajiv Shukla caught advising Rajya Sabha Chair to adjourn House</a><font size='-1' color='#6f6f6f'><nobr>IBNLive.com</nobr></font></font><br /><font size='-1'><a href='http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGcM8hMahpLOc6BsfzHxJQevosw6g&amp;url=http://indiatoday.intoday.in/story/rajeev-shukla-instructs-rajya-sabha-deputy-chairman-to-adjourn-house/1/214065.html'>Rajiv Shukla caught on camera &#39;asking&#39; for RS to be adjourned</a><font size='-1' color='#6f6f6f'><nobr>India Today</nobr></font></font><br /><font size='-1' class='p'><a href='http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNEmu2HdDPdeEaBAxcp-2UAxxxjflQ&amp;url=http://www.firstpost.com/politics/cong-leader-kurien-unanimously-elected-rajya-sabha-deputy-chairman-%25E2%2580%258E-424828.html'><nobr>Firstpost</nobr></a></font><br /><font class='p' size='-1'><a class='p' href='http://news.google.com/news/more?ned=in&amp;topic=n&amp;ncl=dr674QhYgd0o2BMfOD2eb3bZH9TvM'><nobr><b>all 53 news articles&nbsp;&raquo;</b></nobr></a></font></div></font></td></tr></table>"; 
 
        wv.loadDataWithBaseURL("", html, mimeType, encoding, "");*/ 

    }
    
    private void refreshRssFeed(SyndFeed feed) {
        this.feed = feed;

        if (feed != null) {
        	setTitle(feed.getTitle());
        	
        	listAdapter = new SyndFeedListAdapter(this, feed);
    	    mainListView.setAdapter( listAdapter );
    	    
    	    
            /*TextView txtView = (TextView) findViewById( R.id.feedTitle );
	        SyndEntry syndEntry = (SyndEntry) feed.getEntries().get(0);
	        txtView.setText(syndEntry.getTitle());
	        final String mimeType = "text/html";
	        final String encoding = "UTF-8"; 
	        wv.loadDataWithBaseURL("", syndEntry.getDescription().getValue(), mimeType, encoding, "");*/
	        
	       /* ArrayAdapter<SyndEntry> adapter = new ArrayAdapter<SyndEntry>(this, android.R.layout.simple_list_item_1, feed.getEntries());
	        setListAdapter(adapter);*/
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
        	Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getLink()));
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
