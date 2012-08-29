package com.example.feedcook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NotificationReceiverActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_notification_receiver, menu);
        return true;
    }
}
