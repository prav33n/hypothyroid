package ht.v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ht.v1.R;

public class Food extends Abstract{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);
    }
	
	public void onWalnutsclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","Walnuts" );
		     i.putExtra("foodid", 2);
	         startActivity(i); 
		   	   }
	  public void onSoyflourclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","Soy Flour" );
		     i.putExtra("foodid", 1);
	         startActivity(i); 
	   }
	  public void onCottenseedclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","Cotton Seed" );
		     i.putExtra("foodid", 3);
	         startActivity(i); 
	   }
	  public void onCalciumsupplementsclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","Cacium Supplements" );
		     i.putExtra("foodid", 7);
	         startActivity(i); 
	   }
	  public void onIronsupplementsclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","Iron Supplements" );
		     i.putExtra("foodid", 6);
	         startActivity(i); 
	   }
	  public void onHighfiberlclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","High Fiber Foods" );
		     i.putExtra("foodid", 5);
	         startActivity(i); 
	   }
	  public void onAntacidsclick(View v) {
		  Intent i = new Intent(getApplicationContext(), Display.class);
		     i.putExtra("viewtype","food" );
		     i.putExtra("foodtitle","Antacids" );
		     i.putExtra("foodid", 4);
	         startActivity(i); 
	   }


}
