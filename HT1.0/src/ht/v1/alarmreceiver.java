package ht.v1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import ht.v1.R;

public class alarmreceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	
		try {
		
		     Bundle bundle = intent.getExtras();
		     Log.e("alarm tone id",bundle.getString("alarmmessage"));
		     Intent i = new Intent(context, home.class);
		     i.putExtra("alarmmessage", bundle.getString("alarmmessage"));
	         i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     context.startActivity(i);  
		    } catch (Exception e) {
		     Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
		     e.printStackTrace();
		 
		    }
	}

}