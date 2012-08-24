package com.example.utils;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.domain.FeedData;
import com.example.feedcook.R;

public class FeedArrayAdapter extends ArrayAdapter<FeedData> {

	private LayoutInflater inflater;

	public FeedArrayAdapter(Context context, List<FeedData> planetList) {
		super(context, R.layout.simplerow, R.id.rowGameName, planetList);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Planet to display
		FeedData obj = (FeedData) this.getItem(position);

		// The child views in each row.
		TextView textTitle;
		TextView textFeedSource;
		CheckBox checkBox;

		// Create a new row view
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.simplerow, null);

			// Find the child views.
			textTitle = (TextView) convertView.findViewById(R.id.rowGameName);
			textFeedSource = (TextView) convertView.findViewById(R.id.rowGenere);
			checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
			
			convertView.setTag(new ViewHolder(textTitle, textFeedSource, checkBox));
			
			checkBox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					FeedData obj = (FeedData) cb.getTag();
					obj.setChecked(cb.isChecked());
				}
			});
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			textTitle = viewHolder.getFeedName();
			textFeedSource = viewHolder.getFeedSource();
			checkBox = viewHolder.getCheckBox();
		}

		checkBox.setTag(obj);
		textTitle.setText(obj.getFeedName());
		textFeedSource.setText(obj.getFeedSource());
		checkBox.setChecked(obj.isChecked());

		return convertView;
	}
	
	
	public class ViewHolder {

	    private TextView feedName ;
	    private TextView feedSource ;
	    private CheckBox checkBox ;
	    private long id ;
	    
	    public ViewHolder() {}

		public TextView getFeedName() {
			return feedName;
		}

		public void setFeedName(TextView feedName) {
			this.feedName = feedName;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public TextView getFeedSource() {
			return feedSource;
		}

		public void setFeedSource(TextView feedSource) {
			this.feedSource = feedSource;
		}

		public ViewHolder(TextView feedName, TextView feedSource,
				CheckBox checkBox) {
			super();
			this.feedName = feedName;
			this.feedSource = feedSource;
			this.checkBox = checkBox;
		}

		

	    
	}
	
}
