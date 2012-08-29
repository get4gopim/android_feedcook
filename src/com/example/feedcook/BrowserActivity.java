package com.example.feedcook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class BrowserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        
        setTitle(title);
        WebView wv = (WebView) findViewById(R.id.webview);
        wv.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browser, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuRefresh:
				break;
			case R.id.menuBack:
				super.finish();
				break;
			default:
				break;
		}

		return true;
	}
}
