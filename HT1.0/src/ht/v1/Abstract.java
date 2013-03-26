package ht.v1;


import ht.v1.R;
import java.io.InputStream;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public abstract class Abstract extends Activity{ // contains common implemtation of the functuions.
	Cursor cur;
	Database db;
	MediaPlayer mediaPlayer;
	public static final String PREF_NAME= "settings";
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        //setContentView(R.layout.main);
	    }
	 
		public void playsound(int soundid, int status) { //function to play a sound file from raw folder
			Database dbs = new Database(getApplicationContext(),"ht1.db");
			Cursor cur = dbs.query("sound", "select soundfilename from sound where soundid="+soundid);
			startManagingCursor(cur);
            cur.moveToFirst();
            int resID = getResources().getIdentifier(cur.getString(0),"raw", getPackageName());
            cur.close();
            cur = dbs.query("alarm", "select alarmtype from alarm where recordtype = 'current'");
			startManagingCursor(cur);
            cur.moveToFirst();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),resID);
        	AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        	int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        	mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
            if(status!=1 && resID !=0) {
            if(cur.getString(0).equalsIgnoreCase("Vibrate")) {// Get instance of Vibrator from current Context
            	getApplicationContext();
            	Log.e("vibrator activated","vibrate");
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
              	// Vibrate for 300 milliseconds
            	v.vibrate(3000); }
            else if(cur.getString(0).equalsIgnoreCase("Loud")) {
               
          	  	mediaPlayer.start(); 
          	  	 	  	}}
            else if(resID !=0){
            	 mediaPlayer.start(); 
            }
            cur.close();
        	dbs.closedb();
     		}
		
		public boolean displaytoast(String message) { //function to display toast message
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(getApplicationContext(),message, duration);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			return true;
			
		}
		
		
		
		public Boolean setalarm(Time t,String message,int intentcode) { // function to set alarm through broadcast receiver
			 // get a Calendar object with current time
			 Calendar cal = Calendar.getInstance();
	    	 Date alarmTime = new Date(System.currentTimeMillis());
			 Date nextday = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
			 Log.e("date",""+alarmTime.toString()+nextday.toString());
			 alarmTime.setHours(t.getHours());
		     alarmTime.setMinutes(t.getMinutes());
		     cal.setTimeInMillis(alarmTime.getTime());
		     try {
		     Intent intent = new Intent(getApplicationContext(), alarmreceiver.class); 
			 intent.putExtra("alarmmessage",message);
			 // use a static variable for the request code the same code should be used for canceling the broadcast. 
			 PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),intentcode, intent,PendingIntent.FLAG_UPDATE_CURRENT);
			 // Get the AlarmManager service
			 AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			// Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
			 if(intentcode==0) 
				 am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),(1000 * 60 * 60 * 24),sender);
			 else
			 am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
			 return true;
			 }
			 
			 catch(Exception e) {Log.e("error",e.getMessage()); return false;}
		}
		
		
		public Boolean removealarm(String message,int intentcode) { // function to remove alarm through broadcast receiver
			   try {
		     Intent intent = new Intent(getApplicationContext(), alarmreceiver.class); 
			 intent.putExtra("alarmmessage",message);
			 // use a static variable for the request code the same code should be used for canceling the broadcast. 
			 PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),intentcode, intent,PendingIntent.FLAG_UPDATE_CURRENT);
	    	 // Get the AlarmManager service
			 AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			 am.cancel(sender);
			 return true;
			 }
			 
			 catch(Exception e) {Log.e("error",e.getMessage()); return false;}
		}
	/*	public boolean alarmprogress(long time, String str) {
			
			final RelativeLayout progress = (RelativeLayout)findViewById(R.id.progress);
			final String msg = str;
			progress.setVisibility(View.VISIBLE);
			final ProgressBar alarmprogress = (ProgressBar) findViewById(R.id.alarmprogress);
			alarmprogress.setProgress(0);
		    final TextView tv = (TextView)findViewById(R.id.progressmessage);
		    final long totalMsecs = time*60* 1000; // 10 seconds in milli seconds
			int callInterval = 1000;
              
			*//** CountDownTimer *//*
			new CountDownTimer(totalMsecs, callInterval) {
			public void onTick(long millisUntilFinished) {
			Time t = new Time(millisUntilFinished);
			t.setHours(0);
            tv.setText(msg + t.toString() );
			float fraction = millisUntilFinished / (float) totalMsecs;
			// progress bar is based on scale of 1 to 100; 
		    alarmprogress.setProgress ( (int) ((100-fraction * 100)) );
		     }
			public void onFinish() {
		    progress.setVisibility(View.INVISIBLE);
			Log.d("TAG", ">>>>>>>>> countdown timer on finish");
			}
			}.start();
	return true;
	}*/
		
		public Time addtime(Time t,int m) {  //function to add minutes to the time
			 if((t.getMinutes() + m) > 60)
				    t = new Time((t.getHours()+1),(t.getMinutes()+ m - 60),0);
				   else
					   t = new Time((t.getHours()),(t.getMinutes()+ m),0);
			return t;	   
					
		}
		
		public Bitmap setimage(ImageView img, String filename) {
	        try {
	        	 AssetManager mngr = getAssets();
	        	InputStream in = mngr.open(filename);
	        	//decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(in,null,o);
	            //Find the correct scale value. It should be the power of 2.
	            final int REQUIRED_SIZE=300;
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	            //decode with inSampleSize
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize=scale;
	            return BitmapFactory.decodeStream(in, null, o2);
	            } catch (Exception e) { return null;}
		}
		
		@Override
		protected void onStop() {
		    super.onStop();
		    // deallocate all memory
		    if (mediaPlayer != null) {
		        if (mediaPlayer.isPlaying()) {
		        	mediaPlayer.stop();
		        }
		        mediaPlayer.release();
		        mediaPlayer = null;
		    }
		}
		
		public void onClickInfo(View v) {
			Intent i = new Intent(getApplicationContext(), Webview.class);
		    i.putExtra("viewtype","local");
	        startActivity(i);  
					}
}
