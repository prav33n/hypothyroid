package ht.v1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ht.v1.R;

public class Interesting extends Abstract{
	ListView list;
	String query;
	final Activity activity = this;
	String viewtype,foodtitle;
	int foodid;
	SimpleCursorAdapter adapter;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.interesting);
	        ListView list = (ListView)findViewById(R.id.ilist);
	        db = new Database(this,"ht1.db");
	        Bundle msg = getIntent().getExtras();
	        viewtype = msg.getString("viewtype");
	        query = "select _id,materialid,title,author,materialurl,materialtext,level,picturefilename,source from interesting where type ='article' or type='news' or type='Q&A' ORDER BY _id";
        	cur = db.query("interesting",query);
        	activity.setTitle("Interesting");
        	
        	adapter = new SimpleCursorAdapter(this, R.layout.interesting, cur, new String[] {"materialtext","title","source","materialurl","materialurl"}, 
	    			new int[] {R.id.ititle,R.id.imaterialtext,R.id.isource,R.id.ishare,R.id.iview});
	    	   	adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
	    	    public boolean setViewValue(View view, Cursor cur, int columnIndex) {
	    	    	
	       	    	if(columnIndex == cur.getColumnIndex("title")) {
	    	    	TextView tv = (TextView)view;
	    	    	tv.setText(cur.getString(cur.getColumnIndex("materialtext")));
	    	    	tv.setTag(cur.getString(cur.getColumnIndex("materialurl")));
	    	    	Log.e("Text tag",tv.getTag().toString());
	    	    	return true;}
	       	    	else if(columnIndex == cur.getColumnIndex("materialurl")) {
		    	    	Button button = (Button)view;
		    	     	button.setTag(cur.getString(columnIndex));
		    	     	return true;}
	    	    	
	       	    	else if(columnIndex == cur.getColumnIndex("source")) {
		    	    	TextView tv = (TextView)view;
		    	    	if(cur.getString(cur.getColumnIndex("source"))!= null)
		    	    	tv.setText("Source:"+cur.getString(cur.getColumnIndex("source")));
		    	    	else
		    	    		tv.setVisibility(View.GONE);
		    	    	return true;}
	    	    	
	       	    	else if(columnIndex == cur.getColumnIndex("materialtext")) {
		    	    	TextView tv = (TextView)view;
		    	    	tv.setText(cur.getString(cur.getColumnIndex("title")));
		    	    	tv.setTag(cur.getString(cur.getColumnIndex("materialurl")));
		    	    	Log.e("Text tag",tv.getTag().toString());
		    	       	return true;}
	       	    return true;
	    	    }
	    	});
	     	list.setAdapter(adapter);
	     	list.setFocusable(false);
	     	findViewById(R.id.idisplay).setVisibility(View.GONE);    	
	     	
	     	list.setOnItemClickListener(new OnItemClickListener() 
	        {
	    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	    			long arg3) {
	    	   // TODO Auto-generated method stub
	    		TextView tv = (TextView)arg1.findViewById(R.id.ititle);
	    		String url =tv.getTag().toString(); 
	    		if( url != null) {
	    		Log.e("View",url);
	    	 	 Intent i = new Intent(getApplicationContext(), Webview.class);
	    	     i.putExtra("url",tv.getTag().toString());
	    	     i.putExtra("viewtype", viewtype);
	    	     startActivity(i);	    		}
	  		
	    	}
	      });
	 }
	 
	 
		public void sharelink(View v){
		  	Button button = (Button)v.findViewById(R.id.ishare);
	    	String url = button.getTag().toString();	
			Intent shareIntent = 
		    	 new Intent(android.content.Intent.ACTION_SEND);
		    	//set the type
		    	shareIntent.setType("text/plain");
	       	//add a subject
		    	String shareMessage = "Check out "+url+" on Hypothyroidism. Got it from http://bit.ly/hypothyroid";
		      	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
		    	//build the body of the message to be shared
		       	//add the message
		    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
		    	 shareMessage);
		    	Log.e("button selected",shareMessage);
		    	//start the chooser for sharing
		    	startActivity(Intent.createChooser(shareIntent, 
		    	 "Quotes Share"));
		}
		
		
		public void viewarticle(View v){
		  	Button button = (Button)v.findViewById(R.id.iview);
	    	String url = button.getTag().toString();	
	   		if( url != null) {
    		Log.e("View",url);
    	 	 Intent i = new Intent(getApplicationContext(), Webview.class);
    	     i.putExtra("url",url);
    	     i.putExtra("viewtype", viewtype);
    	     startActivity(i);	  
		}
	 
      }
		
}

