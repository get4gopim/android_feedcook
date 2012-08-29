package com.example.feedcook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.datasource.TableDataSource;
import com.example.domain.FeedData;
import com.example.utils.FeedArrayAdapter;

public class MainActivity extends Activity {
	
	private static final int REQUEST_CODE = 10;
	private static final int DIALOG_ALERT = 10;
	
	private ListView mainListView;
	private List<FeedData> listData;
	private ArrayAdapter<FeedData> listAdapter;
	private TableDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		datasource = new TableDataSource(this);
		datasource.open();
		
		mainListView = (ListView) findViewById( R.id.mainListView );
	    
	    mainListView.setOnItemClickListener(new OnItemClickListener() {
	    	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	    		FeedData editObj = (FeedData) mainListView.getItemAtPosition(position);
	    		
	    		Intent i = new Intent(MainActivity.this, WebActivity.class);
	    		i.putExtra("editObj", editObj);
	    		startActivity(i);
	    	}
	    });
	    
	    listData = datasource.getAllFeeds(); //new ArrayList<Planet>();
	    
	    if (!(listData != null && listData.size() > 0)) {
	    	Toast.makeText(this, "No item(s) in the list. Add items using the option menu.", Toast.LENGTH_LONG).show();
	    }
	    
	    // Set our custom array adapter as the ListView's adapter.
	    listAdapter = new FeedArrayAdapter(this, listData);
	    mainListView.setAdapter( listAdapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void refreshList() {
    	listData = datasource.getAllFeeds();
	    listAdapter = new FeedArrayAdapter(this, listData);
	    mainListView.setAdapter( listAdapter );
	}
	
	private void refreshList(List<FeedData> planetList) {
	    listAdapter = new FeedArrayAdapter(this, planetList);
	    mainListView.setAdapter( listAdapter );
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuAdd:
				Intent intent = new Intent(this, AddGameActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
				break;
			case R.id.menuDelete:
				List<FeedData> listSelectedGames = getSelectedItems();
				
				if (listSelectedGames.size() == 0) {
					Toast.makeText(this, "Select atleast one item to delete", Toast.LENGTH_SHORT).show();
				} else {
					showDialog(DIALOG_ALERT);
				}
				break;
			case R.id.menuEdit:
				long objId = -1;
				List<FeedData> listSelectedItems = getSelectedItems();
				if (listSelectedItems.size() > 0) {
					FeedData obj = (FeedData) listSelectedItems.get(0);
					objId = obj.getId();
				}
				
				if (objId == -1 || listSelectedItems.size() == 0 || listSelectedItems.size() > 1) {
					Toast.makeText(this, "Select one item to edit", Toast.LENGTH_SHORT).show();
				} else {
					FeedData editObj = datasource.getFeedById( objId );
					Intent editIndent = new Intent (this, AddGameActivity.class);
					editIndent.putExtra("editObj", editObj);
					startActivityForResult(editIndent, REQUEST_CODE);
				}
				break;
				
			case R.id.menuNotification:
				createNotification ();
				break;
			case R.id.menuExit:
				MainActivity.this.finish();
				break;
			default:
				break;
		}

		return true;
	}
	
	public void createNotification() {
	    NotificationManager notificationManager = (NotificationManager) 
	          getSystemService(NOTIFICATION_SERVICE);
	    Notification notification = new Notification(R.drawable.refresh, "A new notification", System.currentTimeMillis());
	    // Hide the notification after its selected
	    notification.flags |= Notification.FLAG_AUTO_CANCEL;

	    Intent intent = new Intent(this, NotificationReceiverActivity.class);
	    PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
	    notification.setLatestEventInfo(this, "This is the title", "This is the text", activity);
	    notification.number += 1;
	    notificationManager.notify(0, notification);

	  }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("returnKey1")) {
				listData = datasource.getAllFeeds();
			    listAdapter = new FeedArrayAdapter(this, listData);
			    mainListView.setAdapter( listAdapter );
		        
				Toast.makeText(this, data.getExtras().getString("returnKey1"), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			// Create out AlterDialog
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure to delete the selected item(s)?");
			builder.setCancelable(true);
			builder.setPositiveButton("Yes", new OkOnClickListener());
			builder.setNegativeButton("No, no", new CancelOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			
		}
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			List<FeedData> listSelectedGames = getSelectedItems();
			
			for (FeedData obj : listSelectedGames) {
				datasource.deleteFeed(obj);
			}
			refreshList();				
			Toast.makeText(MainActivity.this, listSelectedGames.size() +" Game(s) deleted successfully", Toast.LENGTH_SHORT).show();
		}
	}
	
	private List<FeedData> getSelectedItems() {
		List<FeedData> listSelectedGames = new ArrayList<FeedData>();
		for (int i=0;i<listData.size();i++) {
			FeedData obj = (FeedData) mainListView.getAdapter().getItem(i);
			if (obj.isChecked()) {
				listSelectedGames.add(obj);
			}
		}
		return listSelectedGames;
	}
}
