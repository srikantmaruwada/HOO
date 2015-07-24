package com.example.srikantm.myfirstandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    final String API_URL = "https://api.fullcontact.com/v2/person.json?";
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
        webView.loadUrl(Constants.APP_URL);
        //webView.loadUrl("http:///HOO/Home.html");

        int delay = 60000; // delay for 5 sec.

        int period = 60000; // repeat every sec.

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                String response= CallApI(Constants.API_URL);
                String jsonKey = "Default Key";
                String jsonValue = "Default Value";


                           try {
                 JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                  jsonKey = object.getString("one");
                  jsonValue = object.getString("key");
                 //JSONArray photos = object.getJSONArray("photos");
                } catch (JSONException e) {
                          e.printStackTrace();
                }
                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification(R.drawable.logo,"HOO offers",System.currentTimeMillis());
                Intent notificationIntent= new Intent(getApplicationContext(), MainActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);


                notify.setLatestEventInfo(getApplicationContext(), jsonKey, jsonValue, pending);
                notif.notify(0, notify);
            }

        }, delay, period);

    }

    String CallApI(String strURL) {

        // Do some validation here

        try {
            URL url = new URL(strURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
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

class Constants {
    public static final String API_URL = "http://echo.jsontest.com/key/value/one/two" ;
    public static final String APP_URL = "file:///android_asset/Home.html";
}
