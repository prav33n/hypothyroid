package ht.v1;

import ht.v1.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.util.Log;

public class Copyfiles{
	
String DB_NAME;

Context con;
AssetManager assetManager;
public Copyfiles(String DB_NAME, Context con){
	final String DB_PATH = "/data/data/"+con.getApplicationContext().getPackageName()+"/databases/";
	String update = null;
	try {
	InputStream myinput = con.getAssets().open(DB_NAME);
	Log.e("Database directory",DB_PATH+DB_NAME);
	File f = new File(DB_PATH+DB_NAME); 
	File fo = new File(DB_PATH+"ht.db");
	if(fo.exists()){
		
		Database db = new Database(con.getApplicationContext(),"ht.db");
		Cursor cur= db.query("settings","select * from alarm where recordtype = 'current'");
	    cur.moveToFirst();
	    update = "update alarm set "+
		"pilltime = '"+cur.getString(cur.getColumnIndex("pilltime"))+"',foodalarmtoneid="+cur.getInt(cur.getColumnIndex("foodalarmtoneid"))+",alarmtype = '"+cur.getString(cur.getColumnIndex("alarmtype"))+"',pillalarmtoneid="+cur.getInt(cur.getColumnIndex("pillalarmtoneid"))+",snoozeduration ="+cur.getInt(cur.getColumnIndex("snoozeduration"))+",fooddelay ="+cur.getString(cur.getColumnIndex("fooddelay"))+
		" where recordtype ='current'";
	    Log.e("DB old exists","close"+update);
	    cur.close();
	    con.deleteDatabase("ht.db");
	}
	if(f.exists()) {Log.e("DB exists",""+con.getDatabasePath(DB_NAME));}
	else {
	File dir  = new File(DB_PATH);
	dir.mkdirs();
	OutputStream myoutput =  new FileOutputStream(DB_PATH+DB_NAME);
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myinput.read(buffer)) > 0) {
	myoutput.write(buffer, 0, length);
	}
	// Close the streams
	myoutput.flush();
	myoutput.close();
	myinput.close();
	Database db = new Database(con.getApplicationContext(),"ht1.db");
	db.checkDB.execSQL(update);
	}
	Log.e("DBcopy","completed");}
	catch(IOException e) {Log.e("DBcopy","failed");}
}
}

