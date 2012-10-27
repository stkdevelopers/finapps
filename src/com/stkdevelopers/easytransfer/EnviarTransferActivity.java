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
import android.widget.Toast;
@TargetApi(16)
public class EnviarTransferActivity extends Activity {
	private SharedPreferences prefs;
	private Editor editor;
	
	private NfcAdapter mAdapter;
    private NdefMessage mMessage;
    private double value=0;
    
    private TextView atualImport;
   
	@Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.envio_transfer);

        prefs = getSharedPreferences("preferences",Context.MODE_PRIVATE);
		editor = prefs.edit();

        String idacard = prefs.getString(MainActivity.ID_CARD, "");
        String idaccountMe = prefs.getString(MainActivity.ID_ACCOUNT, "");
        String value = prefs.getString(MainActivity.VALUE, "0");
        String token = prefs.getString(MainActivity.TOKEN, "");
        String accountNumber = prefs.getString(MainActivity.NUMBER_ACCOUNT, "");
        mAdapter = NfcAdapter.getDefaultAdapter(this);
       
        atualImport = (TextView) findViewById(R.id.importActual);
        atualImport.setText("Realizando transferencia de : "+value+ " €");
        
        String nameProfile = prefs.getString(MainActivity.NAME_PROFILE, "usuario");
        Log.i("idcard","idcard "+idacard);
        // Create an NDEF message a URL        
        mMessage = new NdefMessage(NdefRecord.createUri("stk://easytransfer/transfer/"+token+"/"+idaccountMe+"/"+value+"/"+accountNumber+"/"+nameProfile));
        Log.i("BEAM","stk://easytransfer/transfer/"+token+"/"+idaccountMe+"/"+value+"/"+accountNumber);
        if (mAdapter != null) {
            mAdapter.setNdefPushMessage(mMessage, this);
            
        } else {
	        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
        
        mAdapter.setOnNdefPushCompleteCallback(new OnNdefPushCompleteCallback() {
			
			@Override
			public void onNdefPushComplete(NfcEvent event) {
				Log.i("Callback","NdefOK");	
				finish();
				Toast.makeText(EnviarTransferActivity.this, "Transferencia solicitada.", Toast.LENGTH_LONG).show();
			}
		}, this, this);
    }
}
