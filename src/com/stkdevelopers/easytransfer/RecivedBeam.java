package com.stkdevelopers.easytransfer;

/**
 * Clase receptora de android Beam para unión de dispositivos, 
 * filtra que tipo de transferencia se va a dar
 * 
 * **/

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class RecivedBeam extends Activity {
	public static String TRANSFER_PATH = "transfer";
	public static String CORBRO_PATH = "cobro";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Uri uri = intent.getData();
		if (uri != null && uri.getPath() != null) {
			Log.i("Recibir", uri.toString());
			 List<String> paths = uri.getPathSegments();
			 if(paths.get(0).compareTo(TRANSFER_PATH)==0){

				 Log.i("ReceiverdBeam","transfer - " + paths.size());
				 Intent transferIntent = new Intent(RecivedBeam.this,RecibirTransferActivity.class);
				 transferIntent.putExtra(MainActivity.TOKEN, paths.get(1));	
				 transferIntent.putExtra(MainActivity.ID_ACCOUNT, paths.get(2));
				 transferIntent.putExtra(MainActivity.VALUE,paths.get(3));
				 transferIntent.putExtra(MainActivity.NUMBER_ACCOUNT,paths.get(4));
				 transferIntent.putExtra(MainActivity.NAME_PROFILE,paths.get(5));

				 startActivity(transferIntent);
				 
			 }else{
				 
				 Log.i("ReceiverdBeam","cobro");
				 Intent cobroIntent = new Intent(RecivedBeam.this,RecibirCobroActivity.class);
				 cobroIntent.putExtra(MainActivity.TOKEN, paths.get(1));	
				 cobroIntent.putExtra(MainActivity.ID_ACCOUNT, paths.get(2));
				 cobroIntent.putExtra(MainActivity.VALUE,paths.get(3));
				 cobroIntent.putExtra(MainActivity.NAME_PROFILE,paths.get(4));

				 startActivity(cobroIntent);
				 
			 }
			 finish();
		} else {
			 onBackPressed();
		}
		
	}
	


}
