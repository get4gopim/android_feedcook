package com.example.feedcook;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.datasource.TableDataSource;
import com.example.domain.FeedData;

public class AddGameActivity extends Activity {

	private TableDataSource datasource;

	private static String SEPERATOR = ", ";
	private Resources res;
	private FeedData editObj;
	
	private EditText txtFeedName;
	private EditText txtFeedUrl;
	private EditText txtFeedSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_game);
		
		txtFeedName = (EditText) findViewById(R.id.txtTitle);
		txtFeedUrl = (EditText) findViewById(R.id.txtUrl);
		txtFeedSource = (EditText) findViewById(R.id.txtFeedSource);

		res = getResources();

		editObj = (FeedData) getIntent().getSerializableExtra("editObj");
		setView();
	}

	private void setView() {
		if (editObj != null) {
			// Toast.makeText(this, editGame.getGenere(),
			// Toast.LENGTH_LONG).show();
			setTitle(editObj.getFeedName());

			txtFeedName.setText(editObj.getFeedName());
			txtFeedUrl.setText(editObj.getFeedUrl());
			txtFeedSource.setText(editObj.getFeedSource());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_game, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuSave:
				btnSaveHandler (null);
				break;
			case R.id.menuCancel:
				btnCancelHandler (null);
				break;
			default:
				break;
		}

		return true;
	}

	public void btnSaveHandler(View view) {
		datasource = new TableDataSource(this);
		datasource.open();

		FeedData obj = new FeedData();
		obj.setFeedName(txtFeedName.getText().toString());
		obj.setFeedUrl(txtFeedUrl.getText().toString());
		obj.setFeedSource(txtFeedSource.getText().toString());

		// Toast.makeText(this, planet.toString(), Toast.LENGTH_LONG).show();

		if (editObj != null) {
			datasource.saveFeedById(obj, editObj.getId());
		} else {
			datasource.addFeed(obj);
		}

		datasource.close();
		returnResult();
	}

	private void returnResult() {
		Intent data = new Intent();
		data.putExtra("returnKey1", "Feed Saved Successfully");
		setResult(RESULT_OK, data);
		super.finish();
	}

	public void btnCancelHandler(View view) {
		super.finish();
	}
	
}
