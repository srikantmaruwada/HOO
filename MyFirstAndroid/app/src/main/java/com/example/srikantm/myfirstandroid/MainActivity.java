package com.example.srikantm.myfirstandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView)
                findViewById(R.id.webview);
        //webView.loadUrl("http://www.google.com");
        webView.setWebViewClient(new MyWebViewClient());
        String url = "http://www.google.com";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);//Maybe you don't need this rule
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.loadUrl("file:///android_asset/Home.html");

        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification(R.drawable.logo,"HOO offers",System.currentTimeMillis());
        Intent notificationIntent= new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);


        notify.setLatestEventInfo(getApplicationContext(), "Thunderwave Water Sports", "Banana Ride offers from 1st Aug to 10th Aug", pending);
        notif.notify(0, notify);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.indexOf("tel:") > -1) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                return true;
            } else {
                return true;
            }
        }


    }
}
