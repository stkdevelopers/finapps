package com.stkdevelopers.easytransfer;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stkdevelopers.easytransfer.connection.HttpHandler;

public class RecibirTransferActivity extends Activity {



	public static final String USER_PREFERENCES_LOGIN = "user_preferences";
	private SharedPreferences prefs;
	private Editor editor;
	
	//profile values
	private String idAccount="";
	private String token="";
	private String value="";
	private String numberAccount="";
	private String nameprofile= "";

	//views
	private LinearLayout buttonsLayout;
	private Button acceptTrasnferButton;
	private Button cancelTransferButton;
	private TextView importValueText;
	private TextView status;
	private TextView euro;
	private TextView profileReceiver;

	
	private LinearLayout progressLayout;
	private boolean inTransfer = false;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibir_transferencia);       
    	prefs = getSharedPreferences("preferences",Context.MODE_PRIVATE);
		editor = prefs.edit();
		
		buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
		acceptTrasnferButton = (Button) findViewById(R.id.acceptButton);
		cancelTransferButton = (Button) findViewById(R.id.cancelButton);
		importValueText = (TextView) findViewById(R.id.importValue);
		euro = (TextView) findViewById(R.id.euro);
		profileReceiver = (TextView) findViewById(R.id.profilename);
		
		progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
		progressLayout.setVisibility(View.GONE);
		status= (TextView) findViewById(R.id.status);
		importValueText.setText("");
		
		//setea la informacion del intent NFCBeam
		parseIntent();
		
        if (prefs.getString(USER_PREFERENCES_LOGIN, "").compareTo("")==0){
        	Intent intentLogin = new Intent(RecibirTransferActivity.this, LoginActivity.class);
        	startActivity(intentLogin);
        }
               
        acceptTrasnferButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	//si esta logeado aun no muestra el botón
				if (prefs.getString(USER_PREFERENCES_LOGIN, "").compareTo("")!=0){
					//realizamos transferencia
					final String numberAcount = prefs.getString(MainActivity.NUMBER_ACCOUNT,"");
					progressLayout.setVisibility(View.VISIBLE);
					new Thread(){
						public void run(){
							inTransfer = true;
							final boolean resultTransfer = HttpHandler.transferMoney(numberAccount,numberAcount, token, value);
							runOnUiThread(new Runnable() {
								public void run() {
									if(resultTransfer){
										Log.i("RecibirTransfer","Transferencia creada");
//										Toast.makeText(RecibirTransferActivity.this, "Transferencia realizada con exito",Toast.LENGTH_LONG ).show();
										status.setText("Transferencia realizada con éxito");
										importValueText.setTextColor(Color.GREEN);
										euro.setTextColor(Color.GREEN);

										buttonsLayout.setVisibility(View.GONE);
									}else{
										Log.i("RecibirTransfer","Transferencia fallida");
										Toast.makeText(RecibirTransferActivity.this, "No ha sido posible validar la transferencia",Toast.LENGTH_LONG ).show();
										importValueText.setTextColor(Color.RED);
										euro.setTextColor(Color.RED);

										status.setText("No ha sido posible validar la transferencia, intentelo de nuevo.");
									}	
									progressLayout.setVisibility(View.GONE);
									inTransfer = false;

								}
							});
						}
					}.start();
				}
			}
		});
        
        cancelTransferButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
    }
   
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!inTransfer)
			super.onBackPressed();
	}
    
    private void parseIntent(){
    	Intent intentBeam = getIntent();
    	token = intentBeam.getStringExtra(MainActivity.TOKEN);
    	idAccount = intentBeam.getStringExtra(MainActivity.ID_ACCOUNT);
    	value = intentBeam.getStringExtra(MainActivity.VALUE);
    	numberAccount = intentBeam.getStringExtra(MainActivity.NUMBER_ACCOUNT);
		nameprofile = intentBeam.getStringExtra(MainActivity.NAME_PROFILE);   	
		profileReceiver.setText("Transferencia solicitada de " + nameprofile);
    	importValueText.setText(value);

    	
    }
    
    

}
