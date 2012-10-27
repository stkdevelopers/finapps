package com.stkdevelopers.easytransfer;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/

import com.stkdevelopers.easytransfer.connection.HttpHandler;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecibirCobroActivity extends Activity {

	public static final String USER_PREFERENCES_LOGIN = "user_preferences";
	private SharedPreferences prefs;
	private Editor editor;

	private String idCard="";
	private String clientToken="";
	private String commerceToken="";
	private String accountNumber="";
	private String idAccount="";
	private String value="";
	private String nameprofile="";
	
	//views
	private LinearLayout buttonsLayout;
	private Button acceptTrasnferButton;
	private Button cancelTransferButton;
	private TextView importValueText;
	private TextView status;
	private TextView euro;
	private TextView profileReceiver;
	
	private ImageButton loginButton;
	private LinearLayout progressLayout;
	private boolean inTransfer = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibir_cobro);
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
        	startActivity(new Intent(RecibirCobroActivity.this, LoginActivity.class));
        }
        
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RecibirCobroActivity.this, LoginActivity.class));
			}
		});
        
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
							final boolean resultTransfer = HttpHandler.execPayment(idCard, clientToken, commerceToken, value, idAccount);
							runOnUiThread(new Runnable() {
								public void run() {
									if(resultTransfer){
										Log.i("RecibirCobro","Transferencia creada");
//										Toast.makeText(RecibirTransferActivity.this, "Transferencia realizada con exito",Toast.LENGTH_LONG ).show();
										status.setText("Pago realizado con éxito");
										importValueText.setTextColor(Color.GREEN);
										euro.setTextColor(Color.GREEN);
										buttonsLayout.setVisibility(View.GONE);

									}else{
										Log.i("RecibirCobro","Transferencia fallida");
										Toast.makeText(RecibirCobroActivity.this, "No ha sido posible validar el pago",Toast.LENGTH_LONG ).show();
										importValueText.setTextColor(Color.RED);
										euro.setTextColor(Color.RED);

										status.setText("No ha sido posible validar el pago, intentelo de nuevo.");

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
    	//user token
    	idAccount =  intentBeam.getStringExtra(MainActivity.ID_ACCOUNT);
    	commerceToken = intentBeam.getStringExtra(MainActivity.TOKEN);
    	clientToken = prefs.getString(MainActivity.TOKEN, "");//
    	idCard = prefs.getString(MainActivity.ID_CARD, "");//intentBeam.getStringExtra(MainActivity.ID_CARD);
		nameprofile = intentBeam.getStringExtra(MainActivity.NAME_PROFILE);   	
    	value = intentBeam.getStringExtra(MainActivity.VALUE);   	
		importValueText.setText(value);
		profileReceiver.setText("Cobro solicitado de "+nameprofile);
    	
    }

}
