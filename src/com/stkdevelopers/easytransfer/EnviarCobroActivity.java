package com.stkdevelopers.easytransfer;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
@TargetApi(16)
public class EnviarCobroActivity extends Activity {
	private SharedPreferences prefs;
	private Editor editor;
	private NfcAdapter mAdapter;
    private NdefMessage mMessage;
    private double value=0;
    private TextView cobroActual;
   
	@Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.envio_cobro);
        prefs = getSharedPreferences("preferences",Context.MODE_PRIVATE);
		editor = prefs.edit();
        String value = prefs.getString(MainActivity.VALUE, "");
        String token = prefs.getString(MainActivity.TOKEN, "");
        String idAccount = prefs.getString(MainActivity.ID_ACCOUNT, "");

		cobroActual = (TextView) findViewById(R.id.cobroActual);
		cobroActual.setText("Realizando cobro de "+value+ " €");
        String nameProfile = prefs.getString(MainActivity.NAME_PROFILE, "usuario");

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        // Create an NDEF message a URL        
        mMessage = new NdefMessage(NdefRecord.createUri("stk://easytransfer/cobro/"+token+"/"+idAccount+"/"+value+"/"+nameProfile));

 
        
        if (mAdapter != null) {
            mAdapter.setNdefPushMessage(mMessage, this);
        }else {
//        	pedira activar nfc en caso que este desactivado
	        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
        
        mAdapter.setOnNdefPushCompleteCallback(new OnNdefPushCompleteCallback() {
			
			@Override
			public void onNdefPushComplete(NfcEvent event) {
				Log.i("Callback","NdefOK");	
				finish();

			}
		}, this, this);
         
       
    }
	

}
