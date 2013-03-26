package ht.v1;

import ht.v1.R;
import java.sql.Time;
import java.util.ArrayList;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

public class Settings extends Abstract{
	
	Spinner cfooddelay,cfoodalarmtoneid,calarmtype,csnoozeduration;
	TimePicker cpilltime;
	private ArrayList<Integer> soundid = new ArrayList<Integer>();
	private ArrayList<Integer> snoozedurationv = new ArrayList<Integer>() ;
	private ArrayList<Integer>  fooddelayv = new ArrayList<Integer>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database(getApplicationContext(),"ht1.db");
        setContentView(R.layout.settings);
        snoozedurationv.add(5);
        snoozedurationv.add(10);
        snoozedurationv.add(15);
        fooddelayv.add(30);
        fooddelayv.add(45);
        fooddelayv.add(60);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       cur= db.query("settings","select * from alarm where recordtype = 'current'");
       startManagingCursor(cur);
       cur.moveToFirst();
       //assign elemtent of settings page
       	cpilltime = (TimePicker)findViewById(R.id.pilltime);
       	cpilltime.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        cpilltime.setIs24HourView(false); //set 24 hour format for the time picker
       	cfooddelay = (Spinner)findViewById(R.id.fooddelay);
        cfoodalarmtoneid = (Spinner)findViewById(R.id.foodalarmtoneid);  // get alarm tone from DB
        calarmtype = (Spinner)findViewById(R.id.alarmtype);
        csnoozeduration = (Spinner)findViewById(R.id.snoozeduration);
        String dtStart = cur.getString(cur.getColumnIndex("pilltime"));
        Log.e("food delay",""+cur.getString(cur.getColumnIndex("pilltime")));
        Time t = Time.valueOf(dtStart);
        Log.e("pill time",""+t.getHours()+""+t.getMinutes()) ;
        cpilltime.setCurrentHour(t.getHours());
        cpilltime.setCurrentMinute(t.getMinutes());
        Log.e("snooze duration",""+snoozedurationv.indexOf(cur.getInt(cur.getColumnIndex("snoozeduration"))));
        csnoozeduration.setSelection(snoozedurationv.indexOf(cur.getInt(cur.getColumnIndex("snoozeduration"))));
        Log.e("food delay",""+fooddelayv.indexOf(cur.getInt(cur.getColumnIndex("fooddelay"))));
        cfooddelay.setSelection(fooddelayv.indexOf(cur.getInt(cur.getColumnIndex("fooddelay"))));
        Log.e("alarm type",""+cur.getString(cur.getColumnIndex("alarmtype")));
        if(cur.getString(cur.getColumnIndex("alarmtype")).equalsIgnoreCase("Loud")) {calarmtype.setSelection(0);}
        else if(cur.getString(cur.getColumnIndex("alarmtype")).equalsIgnoreCase("Silent")) {calarmtype.setSelection(2);}
        else if (cur.getString(cur.getColumnIndex("alarmtype")).equalsIgnoreCase("Vibrate")){calarmtype.setSelection(1);}
        else{calarmtype.setSelection(3);}
        
        //csnoozeduration.setSelection(snoozedurationv.indexOf(cur.getInt(cur.getColumnIndex("snoozeduration"))));
        //cpilltime = (TimePicker)findViewById(R.id.pilltime);
        
        Cursor cur1= db.queryall("sound");
        startManagingCursor(cur1);
        cur1.moveToFirst();
        Log.e("cursor Count",""+cur1.getColumnCount()); //load sound filenames to the spinner
        ArrayList<String> options=new ArrayList<String>();
        while (!cur1.isAfterLast()) {
			options.add(cur1.getString(cur1.getColumnIndex("soundname")));
			soundid.add(cur1.getInt(cur1.getColumnIndex("soundid")));
			cur1.moveToNext();
			}
		cur1.close();
		//db.checkDB.close();
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
		 cfoodalarmtoneid.setAdapter(adapter);
		  	final int iCurrentSelection = soundid.indexOf(cur.getInt(cur.getColumnIndex("foodalarmtoneid"))); //action listener for playing sound
	    	cfoodalarmtoneid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
			    	Log.e("selected item",""+iCurrentSelection+"//"+i);
			    
			    	if (iCurrentSelection != i){
			             		playsound(soundid.get(cfoodalarmtoneid.getSelectedItemPosition()),1);
			             		try {
			        				Thread.sleep(3000);
			        			} catch (InterruptedException e) {
			        				// TODO Auto-generated catch block
			        				e.printStackTrace();
			        			}
			                  	   mediaPlayer.stop();
			                  	   
			    }     
			    }

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				} 	    	});

	    /*	//action listener for alarm type
	    	calarmtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
			    	if ((i == 1)||(i==2)){
			    		cfoodalarmtoneid.setEnabled(false);		    }
			    	else if ((i == 0))
			    		cfoodalarmtoneid.setEnabled(true);
			    }

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				} 	    	});*/

	    	
	    	Log.e("food tone",""+soundid.indexOf(cur.getInt(cur.getColumnIndex("foodalarmtoneid"))));
	    	cfoodalarmtoneid.setSelection(soundid.indexOf(cur.getInt(cur.getColumnIndex("foodalarmtoneid"))));
            cur.close();
            db.closedb();
               
    }
	  public void onClickSave(View v) {
		  db = new Database(getApplicationContext(),"ht1.db");
		  cpilltime = (TimePicker)findViewById(R.id.pilltime);
		  Time pilltime = new Time(cpilltime.getCurrentHour(),cpilltime.getCurrentMinute(),0);
	      cfooddelay = (Spinner)findViewById(R.id.fooddelay);	
	      int fooddelay = fooddelayv.get(cfooddelay.getSelectedItemPosition());
	      cfoodalarmtoneid = (Spinner)findViewById(R.id.foodalarmtoneid);  // get alarm tone from DB
		  int foodalarmtoneid = soundid.get(cfoodalarmtoneid.getSelectedItemPosition());
		  calarmtype = (Spinner)findViewById(R.id.alarmtype);
		  String alarmtype = calarmtype.getSelectedItem().toString();
		  csnoozeduration = (Spinner)findViewById(R.id.snoozeduration);
		  int snoozeduration = snoozedurationv.get(csnoozeduration.getSelectedItemPosition());
		  Log.e("Database values update",""+pilltime+"//"+fooddelay+"//"+foodalarmtoneid+"//"+alarmtype+"//"+snoozeduration);
		  db.checkDB.execSQL("update alarm set "+
					"pilltime = '"+pilltime+"',foodalarmtoneid="+foodalarmtoneid+",alarmtype = '"+alarmtype+"',pillalarmtoneid="+foodalarmtoneid+",snoozeduration ="+snoozeduration+",fooddelay ="+fooddelay+
					" where recordtype ='current'");
		  db.closedb();
		  	displaytoast("Settings Saved");
		  	removealarm("pill alarm",0);
		  	removealarm("pill alarm",1);
		  	removealarm("food alarm",2);
		  	if(!alarmtype.equalsIgnoreCase("Off"))
			setalarm(pilltime,"pill alarm",0);
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			startActivity (new Intent(getApplicationContext(), home.class));
		  
	       }
	  
	/*  
	 protected void onPause() {
	        super.onPause();
	        Log.e("on pause settings ","called");
	        db.closedb();
         	} 
	  
	  
		
		protected void onResume() {
	        super.onResume();
	        Log.e("on Resume settings","called");
	        db = new Database(getApplicationContext(),"ht.db");}*/
}
