package ht.v1;
/*application coded by Praveen Jelish */

import ht.v1.R;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class home extends Abstract{
    /** Called when the activitiy is first created. */
	Context con;
	int notification = 0;	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	con = getApplicationContext();
    	new Copyfiles("ht1.db",con);
       	//requestWindowFeature(Window.FEATURE_LEFT_ICON);
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        //getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.backgorund);
        setContentView(R.layout.main);
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.titlebar);
        }
       
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        
        Bundle extras = getIntent().getExtras(); //Handle notification
        if (extras != null) {
        	
        	String AlarmMessage = extras.getString("alarmmessage");
           // final Database db = new Database(this,"ht.db");
            Log.e("tone",AlarmMessage);
            
            if (AlarmMessage.equalsIgnoreCase("pill alarm")) {
            	db = new Database(con,"ht1.db");
              	Cursor cur = db.query("sound", "select foodalarmtoneid from alarm where recordtype ='current'");
            	startManagingCursor(cur);
            	cur.moveToFirst();
                playsound(cur.getInt(0),0);
                cur.close();
                alertbox.setTitle("Time for a Pill");
                alertbox.setIcon(R.drawable.pills);
               // alertbox.setMessage("Time for taking Pills!");
                alertbox.setPositiveButton("Take a Pill", new DialogInterface.OnClickListener() {
                	// do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                 	   Cursor cur = db.query("alarm", "select fooddelay from alarm where recordtype ='current'");
                 	   startManagingCursor(cur);
                        cur.moveToFirst();
                 	   Time t = new Time(java.lang.System.currentTimeMillis());
                 	   alarmprogress(cur.getInt(0),"OK to Eat in ");
                 	   notification = 2;
                 	   t = addtime(t,cur.getInt(0));
                 	   Log.e("food delay",""+cur.getColumnCount()+cur.getInt(0)+setalarm(t,"food alarm",2));
                	   cur.close();
                	   Log.e("food delay closed",""+db.closedb());
                  	   if(mediaPlayer.isPlaying())
                	   mediaPlayer.stop();
                  	     }
                });
                // set a negative/no button and create a listener
                alertbox.setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                	//do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                 	   Cursor cur = db.query("alarm", "select snoozeduration from alarm where recordtype ='current'");
                 	   startManagingCursor(cur);
                 	  Log.e("snooze duration",""+cur.getColumnCount());
                       cur.moveToFirst();
                 	   Time t = new Time(java.lang.System.currentTimeMillis());
                 	   alarmprogress(cur.getInt(0),"Snooze ");
                 	   t = addtime(t,cur.getInt(0));
                 	   Log.e("snooze duration",""+cur.getColumnCount()+cur.getInt(cur.getColumnIndex("snoozeduration"))+setalarm(t,"pill alarm",1));
                 	   cur.close();
                 	   Log.e("food delay closed",""+db.closedb());
                 	   if(mediaPlayer.isPlaying())
                    	   mediaPlayer.stop();
                      }

               
                });
                
               alertbox.setNeutralButton("Skip", new DialogInterface.OnClickListener() {
            	     	// do something when the button is clicked
                   public void onClick(DialogInterface arg0, int arg1) {
                   	mediaPlayer.stop();
                   } 
                   
              }); 
           	// Do something with the data
               alertbox.setCancelable(false);
               final AlertDialog dlg = alertbox.create();
               final Timer t = new Timer();
                  t.schedule(new TimerTask() {                
                                    public void run() {                
                                        dlg.dismiss(); // when the task active then close the dialog
                                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                                  //      findViewById(R.id.progress).setVisibility(View.GONE);
                                           }                
                                }, 1800000); // after 30 minutes (or 2000 miliseconds), the task will be active.
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                android.view.Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
                int width = display.getWidth(); 
                int height = display.getHeight();
                Log.e("Width , Height",""+width+"//"+height);
                lp.copyFrom(dlg.getWindow().getAttributes());
                lp.width = 	width;
                lp.height = height - 200;
                lp.x=-170;
                lp.y=100;
                dlg.getWindow().setLayout(width, height-200);
                dlg.show();
                } 	
            
            else if (AlarmMessage.equalsIgnoreCase("food alarm")) {
            	db = new Database(con,"ht1.db");
         	    Cursor cur = db.query("sound", "select foodalarmtoneid from alarm where recordtype ='current'");
                startManagingCursor(cur);
                cur.moveToFirst();
                playsound(cur.getInt(0),0);
                cur.close();
                db.closedb();
                Log.e("food alar closed",""+db.closedb());
               // alertbox.setTitle("Ready to Eat");
                alertbox.setIcon(R.drawable.breakfastbutton);
                alertbox.setTitle("Time to Eat!");
            	alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     // do something when the button is clicked
                     public void onClick(DialogInterface arg0, int arg1) {
                    	  	 findViewById(R.id.progress).setVisibility(View.GONE);
                  	   if(mediaPlayer.isPlaying())
                    	   mediaPlayer.stop();
         	  
                     }
                 });
                alertbox.setCancelable(true);
                final AlertDialog dlg = alertbox.create();
                
                final Timer t = new Timer();
                  t.schedule(new TimerTask() {                
                                    public void run() {                
                                        dlg.dismiss(); // when the task active then close the dialog
                                        findViewById(R.id.progress).setVisibility(View.GONE);
                                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                                        
                                           }                
                                }, 1800000); // after 30 minutes (or 2000 miliseconds), the task will be active.
                              
                  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                  lp.copyFrom(dlg.getWindow().getAttributes());
                  android.view.Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
                  int width = display.getWidth(); 
                  int height = display.getHeight();
                  Log.e("Width , Height",""+width+"//"+height);
                  lp.copyFrom(dlg.getWindow().getAttributes());
                  lp.width = width;
                  lp.height = height - 200;
                  lp.x=-170;
                  lp.y=100;
                  //dlg.getWindow().setAttributes(lp);
                  dlg.getWindow().setLayout(width, height-200);
                  dlg.show();
                  
}
       }
     
    }
    public void onClickFood(View v) {
      	startActivity (new Intent(con, Food.class));
       }
    public void onClickNews(View v) {
    	//startActivity (new Intent(con, Display.class));
    	Intent i = new Intent(con, Interesting.class);
	    i.putExtra("viewtype","news");
        startActivity(i);  
      	}
    public void onClickSettings(View v) {
    	startActivity (new Intent(con, Settings.class));
       }
    public void onClickVideo(View v) {
    	Intent i = new Intent(con, Display.class);
	    i.putExtra("viewtype","video");
        startActivity(i);  
       }
    public void shareapp(View v){
    	Intent shareIntent = 
	    	 new Intent(android.content.Intent.ACTION_SEND);
	    	//set the type
	    	shareIntent.setType("text/plain");
        	//add a subject
	    	String shareMessage = "Check out Hypothyroid App: http://bit.ly/hypothyroid";
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
    
    public boolean alarmprogress(long time, String str) {
		findViewById(R.id.hshare).setVisibility(View.GONE);
		final RelativeLayout progress = (RelativeLayout)findViewById(R.id.progress);
		final String msg = str;
		progress.setVisibility(View.VISIBLE);
		final ProgressBar alarmprogress = (ProgressBar) findViewById(R.id.alarmprogress);
		alarmprogress.setProgress(0);
	    final TextView tv = (TextView)findViewById(R.id.progressmessage);
	    final long totalMsecs = time*60* 1000; // 10 seconds in milli seconds
		int callInterval = 1000;
          
		/** CountDownTimer */
		new CountDownTimer(totalMsecs, callInterval) {
		public void onTick(long millisUntilFinished) {
		Time t = new Time(millisUntilFinished);
		t.setHours(0);
		tv.setText(msg + t.toString() );
		float fraction = millisUntilFinished / (float) totalMsecs;
		// progress bar is based on scale of 1 to 100;
		//Log.d("TAG", "countdown timer on finish" + fraction + msg );
	    alarmprogress.setProgress ( (int) ((100-fraction * 100)) );
	    progress.setVisibility(View.VISIBLE);
	     }
		public void onFinish() {
	    progress.setVisibility(View.GONE);
	    findViewById(R.id.hshare).setVisibility(View.VISIBLE);
		Log.d("TAG", ">>>>>>>>> countdown timer on finish");
		}
		}.start();
return true;
}
    
     /*protected void onPause() {
        super.onPause();
        Log.e("on pause home","called");
        db.closedb();
     	
        }
	
	protected void onResume() {
        super.onResume();
        Log.e("on resume home","called");
        db = new Database(con,"ht.db");
                   }*/
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	       return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	  
	}