package com.example.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.feedcook.R;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

/**
 * @author 
 * @author 
 */
public class SyndFeedListAdapter extends BaseAdapter {

    private SyndFeed syndFeed;
    private final LayoutInflater layoutInflater;

    public SyndFeedListAdapter(Context context, SyndFeed feed) {
        this.syndFeed = feed;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return syndFeed != null ? syndFeed.getEntries().size() : 0;
    }

    public SyndEntry getItem(int position) {
        return (SyndEntry) syndFeed.getEntries().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SyndEntry syndEntry = getItem(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_web, parent, false);
        }

        if (syndEntry != null) {
            TextView t = (TextView) convertView.findViewById(R.id.feedTitle);
            t.setText(syndEntry.getTitle());
           
            if (syndEntry.getPublishedDate() != null) {
            	t = (TextView) convertView.findViewById(R.id.feedDate);
            	t.setText(syndEntry.getPublishedDate().toString());
            }

            SyndContent description = syndEntry.getDescription();
            if (description != null) {
            	String content = description.getValue();
            	if (content.indexOf("<") > 0) {
            		content = content.substring(0, content.indexOf("<"));
            	}
            	t = (TextView) convertView.findViewById(R.id.feedDesc);
            	t.setText (content);
            	
            	WebView wv = (WebView) convertView.findViewById(R.id.webview);
    	        wv.loadDataWithBaseURL("", description.getValue(), "text/html", "UTF-8", "");
            }
        }

        return convertView;
    }

}
