package com.example.domain;

import java.io.Serializable;

public class FeedData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String feedName = "" ;
	private String feedSource = "" ;
	private String feedUrl = "" ;
	private String fav = "" ;
	private boolean checked = false ;
    
    public FeedData() {}

	public FeedData( String feedName ) {
      this.feedName = feedName ;
    }
    
    private long id;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

	public String getFav() {
		return fav;
	}

	public void setFav(String fav) {
		this.fav = fav;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getFeedSource() {
		return feedSource;
	}

	public void setFeedSource(String feedSource) {
		this.feedSource = feedSource;
	}

	@Override
	public String toString() {
		return "FeedData [feedName=" + feedName + ", feedSource=" + feedSource
				+ ", feedUrl=" + feedUrl + ", fav=" + fav + ", id=" + id + "]";
	}

    
	
    
}
