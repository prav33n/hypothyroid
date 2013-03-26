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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import ht.v1.R;

public class Display extends Abstract {
	ListView list;
	String query;
	final Activity activity = this;
	String viewtype,foodtitle;
	int foodid;
	SimpleCursorAdapter adapter;
	
	public static final String PREFS_NAME = "display";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        ListView list = (ListView)findViewById(R.id.list);
        db = new Database(this,"ht1.db");
        Bundle msg = getIntent().getExtras();
        viewtype = msg.getString("viewtype");
         if(viewtype.equalsIgnoreCase("food")){
        	foodid = msg.getInt("foodid");
          	query = "select _id,foodproductid,picturefilename,product,descr,materialurl,pictureurl,source from foodproduct where foodid ="+foodid+" "+"ORDER BY _id";
	    	Log.e("foodid","test"+query);
	    	cur = db.query("foodproduct",query);
	    	foodtitle = msg.getString("foodtitle");
	    	activity.setTitle(foodtitle);
      }
        else if(viewtype.equalsIgnoreCase("news")) {
        	query = "select _id,materialid,title,author,materialurl,materialtext,level,picturefilename,source from interesting where type ='article' or type='news' or type='Q&A' ORDER BY _id";
        	cur = db.query("interesting",query);
        	activity.setTitle("Interesting");
  	        
        }
        else {
        	query = "select _id,materialid,title,author,materialurl,materialtext,level,picturefilename,source from interesting where type ='video' ORDER BY _id";
        	cur = db.query("interesting",query);
        	activity.setTitle("Videos");
        }
        // Commit the edits!
        startManagingCursor(cur);
	    cur.moveToFirst();
	    //create adapter for displaying news
    if(viewtype.equalsIgnoreCase("news")) {	       
	    	
	    	adapter = new SimpleCursorAdapter(this, R.layout.display, cur, new String[] {"materialtext","title","picturefilename","source"}, 
	    			new int[] {R.id.title,R.id.materialtext,R.id.picturefilename,R.id.source});
	    	   	adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
	    	    public boolean setViewValue(View view, Cursor cur, int columnIndex) {
	    	    	
	    	    	Log.e("cursorindex",""+cur.getColumnIndex("picturefilename"));
	       	    	if(columnIndex == cur.getColumnIndex("title")) {
	    	    	TextView tv = (TextView)view;
	    	    	tv.setText(cur.getString(cur.getColumnIndex("materialtext")));
	    	    	tv.setTag(cur.getString(cur.getColumnIndex("materialurl")));
	    	    	Log.e("Text tag",tv.getTag().toString());
	    	    	return true;}
	         	    	
	    	    	if(columnIndex == cur.getColumnIndex("source")) {
		    	    	TextView tv = (TextView)view;
		    	    	if(cur.getString(cur.getColumnIndex("source"))!= null)
		    	    	tv.setText("Source:"+cur.getString(cur.getColumnIndex("source")));
		    	    	else
		    	    		tv.setVisibility(View.GONE);
		    	    	return true;}
	    	    	
	    	    	if(columnIndex == cur.getColumnIndex("materialtext")) {
		    	    	TextView tv = (TextView)view;
		    	    	tv.setText(cur.getString(cur.getColumnIndex("title")));
		    	    	tv.setTag(cur.getString(cur.getColumnIndex("materialurl")));
		    	    	Log.e("Text tag",tv.getTag().toString());
		    	       	return true;}
	    	    	
	    	    	if(columnIndex == cur.getColumnIndex("picturefilename")) {
	    	    		 ImageView img = (ImageView) view;
	    	    		 img.setVisibility(View.GONE);
	    	    		return true;
	    	    	}
	    	    	return false;
	    	    }
	    	});
	     	list.setAdapter(adapter);}
    
    //Video Display
    else if(viewtype.equalsIgnoreCase("video")) {	       
    	//findViewById(R.id.materialtext).setVisibility(View.GONE);
    //	(findViewById(R.id.title)).setMinimumWidth(200);  
    	adapter = new SimpleCursorAdapter(this, R.layout.display, cur, new String[] {"title","materialtext","picturefilename","source"}, 
    		new int[] {R.id.title,R.id.materialtext,R.id.picturefilename,R.id.source});
    	  	adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
    	    public boolean setViewValue(View view, Cursor cur, int columnIndex) {
    	    	Log.e("cursorindex",""+cur.getColumnIndex("picturefilename"));
    	      	if(columnIndex == cur.getColumnIndex("title")) {
    	    	TextView tv = (TextView)view;
    	    	tv.setText(cur.getString(cur.getColumnIndex("title")));
    	     	tv.setTag(cur.getString(cur.getColumnIndex("materialurl")));
    	     	Log.e("Text tag",tv.getTag().toString());}
    	    	if(columnIndex == cur.getColumnIndex("source")) {
        	    	TextView tv = (TextView)view;
        	    	if(cur.getString(cur.getColumnIndex("source"))!= null)
		    	    	tv.setText("Source:"+cur.getString(cur.getColumnIndex("source")));
		    	    	else
		    	    		tv.setVisibility(View.GONE);}
    	 
    	    		if(columnIndex == cur.getColumnIndex("picturefilename")) {
    	    		       	                
    	                 try { 
    	                	 ImageView img = (ImageView) view;
      	                	 if(cur.getString(columnIndex)!= null) {
    	               		 img.setImageBitmap(setimage(img,cur.getString(columnIndex)));}
    	                	 else
    	                	img.setVisibility(View.GONE);

    	               } catch (Exception e) {
    	                     e.printStackTrace();
    	               } 
    	                
    	               return true;}
    	    	return false;
    	    }
    	});
    	 list.setAdapter(adapter);
    	 
    	 list.setOnItemClickListener(new OnItemClickListener() 
    	    {
    		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    				long arg3) {
    		   // TODO Auto-generated method stub
    			TextView tv = (TextView)arg1.findViewById(R.id.title);
    			String url =tv.getTag().toString(); 
    			if( url != null) {
    			Log.e("View",url);
    			if (url.contains("youtube")){
    	            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    	            Log.e("youtube","video loaded");             
    	            }
    			else {
    			 Intent i = new Intent(getApplicationContext(), Webview.class);
    		     i.putExtra("url",tv.getTag().toString());
    		     i.putExtra("viewtype", viewtype);
    		     if(viewtype.equalsIgnoreCase("food")) {
    		    	 i.putExtra("foodid", foodid);
    		    	 i.putExtra("foodtitle",foodtitle);}
    		     startActivity(i);}
    			}
    			
    		}
    	  });	 
    
    
    }

    //Food display
  //food display
    if(viewtype.equalsIgnoreCase("food")) {
    	adapter = new SimpleCursorAdapter(this, R.layout.display, cur, new String[] {"product","descr","picturefilename","source"}, 
    			new int[] {R.id.title,R.id.materialtext,R.id.picturefilename,R.id.source});
    	  	adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
    	    public boolean setViewValue(View view, Cursor cur, int columnIndex) {
    	    	Log.e("cursorindex",""+cur.getColumnIndex("picturefilename"));
    	    	if(columnIndex == cur.getColumnIndex("product")) {
	    	    	TextView tv = (TextView)view;
	    	    	tv.setText(cur.getString(cur.getColumnIndex("product")));
	    	    	tv.setTag(cur.getString(cur.getColumnIndex("materialurl")));
	    	    //	Log.e("Text tag",tv.getTag().toString());
	    	    	return true;}
    
	    	  	if(columnIndex == cur.getColumnIndex("source")) {
		    	  	TextView tv = (TextView)view.findViewById(R.id.source);
		    	  	tv.setVisibility(View.GONE);
		    	  /*	if(cur.getString(cur.getColumnIndex("source"))== null)
		    	  		tv.setVisibility(View.GONE);
		    	    	else
		    	    tv.setText("Source:"+cur.getString(cur.getColumnIndex("source")));*/
		    	   	return true;}
    	    	
    	    	if(columnIndex == cur.getColumnIndex("descr")) {
    	    	TextView tv = (TextView)view;
    	    	tv.setVisibility(View.GONE);
    	       	//tv.setText(cur.getString(cur.getColumnIndex("descr")));
    	    	//tv.setTag(cur.getString(cur.getColumnIndex("pictureurl")));
    	        	//Log.e("Text tag",tv.getTag().toString());
    	    	}
    	    	
    	    		if(columnIndex == cur.getColumnIndex("picturefilename")) {
    	    			try {
    	                	 ImageView img = (ImageView) view;
    	                     if(cur.getString(columnIndex)!= null) {
    	               	     img.setImageBitmap(setimage(img,cur.getString(columnIndex)));}
    	                     else
    	                	 img.setVisibility(View.GONE);

    	               } catch (Exception e) {
    	                     e.printStackTrace();
    	               } 
    	                 
    	               return true;}
    	    	return false;
    	    }
    	});
    		    	list.setAdapter(adapter);}

    findViewById(R.id.displayl).setVisibility(View.GONE);
	//findViewById(R.id.title).setVisibility(View.GONE);  //hide the elements in the view
	//findViewById(R.id.materialtext).setVisibility(View.GONE);
	//findViewById(R.id.picturefilename).setVisibility(View.GONE);
	//findViewById(R.id.source).setVisibility(View.GONE);
	
	
	//action listener for list view
/*	list.setOnItemClickListener(new OnItemClickListener() 
    {
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
	   // TODO Auto-generated method stub
		TextView tv = (TextView)arg1.findViewById(R.id.title);
		String url =tv.getTag().toString(); 
		if( url != null) {
		Log.e("View",url);
		if (url.contains("youtube")){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            Log.e("youtube","video loaded");             
            }
		else {
		 Intent i = new Intent(getApplicationContext(), Webview.class);
	     i.putExtra("url",tv.getTag().toString());
	     i.putExtra("viewtype", viewtype);
	     if(viewtype.equalsIgnoreCase("food")) {
	    	 i.putExtra("foodid", foodid);
	    	 i.putExtra("foodtitle",foodtitle);}
	     startActivity(i);}
		}
		
	}
  });*/
	}

	/*	protected void onPause() {
        super.onPause();
        Log.e("on pause","called");
        cur.close();
        db.closedb();
      	}
	
	protected void onResume() {
        super.onResume();
        Log.e("adapter",""+adapter.isEmpty());
        //db.closedb();
        //if(!db.isopen())
        //db = new Database(getApplicationContext(),"ht.db");
    	}*/
	


}
