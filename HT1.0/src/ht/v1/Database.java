package ht.v1;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.database.sqlite.SQLiteOpenHelper;
import ht.v1.R;

public class Database extends SQLiteOpenHelper{

String DB_PATH,DB_NAME;
Context con;
Cursor Cur;
View V;
AssetManager assets;
SQLiteDatabase checkDB;
public Database(Context con, String DB_NAME) {
	super(con,DB_NAME,null,2);
	try {
	this.con = con;
	checkDB = SQLiteDatabase.openDatabase(con.getDatabasePath(DB_NAME).toString(), null,SQLiteDatabase.OPEN_READWRITE);
	Log.e("database constructor","Open //"+checkDB.getVersion());
    }
	catch(Exception e) {Log.e("constructor",""+e.getMessage());}
}

public boolean isopen() {
	return checkDB.isOpen();
	
}

public boolean closedb() {
	boolean test = checkDB.isOpen();
	if(test)
	checkDB.close();
	Log.e("Databse closed",""+ test);
	return test;
}

public long insert(String DB_TABLE, ContentValues values) {
   	ContentValues initialValues = new ContentValues();
	checkDB.beginTransaction();
	long test = checkDB.insertOrThrow(DB_TABLE, null, initialValues);
	checkDB.setTransactionSuccessful();
	checkDB.endTransaction();
	Log.e("insert", ""+test);
	//checkDB.close();
	return test;
 }


public Cursor queryall(String DB_TABLE) {
	Cur = checkDB.rawQuery("Select * from "+DB_TABLE, null);
	return Cur;
	}

public Cursor query(String DB_TABLE,String query) {
	try {
	Cur = checkDB.rawQuery(query, null);
	Log.e("sql query",""+Cur.getColumnCount());
	return Cur;}
	catch(Exception e) {Log.e("sql query",e.getMessage()); return null;}
}

@Override
public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub
	
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub
	
}


}

