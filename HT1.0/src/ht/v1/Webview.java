package ht.v1;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import ht.v1.R;

public class Webview extends Abstract {
	WebView webview;
	final Activity activity = this;
	String viewtype, foodtitle;
	int foodid;
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	     setContentView(R.layout.webview);
		 webview = (WebView)findViewById(R.id.webview);
		 webview.getSettings().setJavaScriptEnabled(true);
		 webview.getSettings().setPluginsEnabled(true);
		 Bundle bundle = getIntent().getExtras() ;
		 String url = bundle.getString("url");
		 viewtype = bundle.getString("viewtype");
		 if(viewtype.equalsIgnoreCase("food")){
	        	foodid = bundle.getInt("foodid");
	          	foodtitle = bundle.getString("foodtitle");
		    	}
		
		 		 
		/*  webview.setWebChromeClient(new WebChromeClient() {
	            public void onProgressChanged(WebView view, int progress)
	            {
	                activity.setTitle("Loading...");
	                activity.setProgress(progress * 100);
	                if(progress == 100)
	                    activity.setTitle("Content Loaded");
	            }
	        });*/
		 if(viewtype.equals("local"))
			 webview.loadUrl("file:///android_asset/termsconditions.html"); 
		 else {
			 Log.e("received url", url);
			webview.loadUrl(url);}
		/* webview.setWebViewClient(new WebViewClient() {
			 @Override
			    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			    	//view.loadUrl(url);
			    	//return true;
			    	
			    	if (url.contains("youtube")){
	                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	                    Log.e("youtube","video loaded");
	                    return true;
	                    }
	                    else
	                    {
	                   	 view.loadUrl(url); 
	                    return true;
	                  } 
			      
			    }
		 });*/
		 
	    }
	 
	 /* protected void onPause() {
	        super.onPause();
	        Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype",viewtype);
		     if(viewtype.equalsIgnoreCase("food")){
		    	 i.putExtra("foodtitle",foodtitle );
			     i.putExtra("foodid",foodid);
		     }
		     startActivity(i);
	         
	      	}
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	 Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype",viewtype);
		     if(viewtype.equalsIgnoreCase("food")){
		    	 i.putExtra("foodtitle",foodtitle );
			     i.putExtra("foodid",foodid);
		     }
		     startActivity(i);
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }*/

	}
