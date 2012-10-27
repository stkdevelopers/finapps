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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//CONSTANTS
	public static final String USER_PREFERENCES_LOGIN = "user_preferences";
	public static final String USER_TIPE = "typeUser";
	public static final String ID_CARD = "idCard";
	public static final String ID_ACCOUNT = "idAccount";
	public static final String VALUE = "values";
	public static final String NAME_PROFILE = "nameProfile";
	public static final String TOKEN = "token";
	public static final String NUMBER_ACCOUNT = "account_number";

	public static int TRANSFER_ACTIVITY = 0;
	public static int COBRAR_ACTIVITY = 1;

	private SharedPreferences prefs;
	private Editor editor;
	private EditText value;
	private ImageButton loginButton;
	private Button transferButton;
	private TextView titleText;
	private TextView tpvText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	prefs = getSharedPreferences("preferences",Context.MODE_PRIVATE);
		editor = prefs.edit();

		value = (EditText)findViewById(R.id.valueText);
		titleText = (TextView) findViewById(R.id.titleText);
		transferButton = (Button) findViewById(R.id.compraButton);
		transferButton.setVisibility(View.GONE);
		tpvText = (TextView) findViewById(R.id.tpvtext);
		tpvText.setVisibility(View.GONE);
        if (prefs.getString(USER_PREFERENCES_LOGIN, "").compareTo("")==0){
        	startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(MainActivity.this, LoginActivity.class));

			}
		});
        
        transferButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(value.getText().toString().compareTo("")!=0){
					//Cliente particular
					editor.putString(VALUE,value.getText().toString());
					editor.commit();
					if(prefs.getString(USER_TIPE, "client").compareTo("client")==0){
						Intent intentTransfer =new Intent(MainActivity.this,EnviarTransferActivity.class);
						startActivityForResult(intentTransfer, TRANSFER_ACTIVITY);
					}else{//perfil profesional
						Intent intentTransfer =new Intent(MainActivity.this,EnviarCobroActivity.class);
						startActivityForResult(intentTransfer, COBRAR_ACTIVITY);
					}
				}else{//sin valor en el capo de dinero
					Toast.makeText(MainActivity.this, "Introduzca un valor", Toast.LENGTH_LONG).show();
				}

			}
		});
    }
    
    @Override
    protected void onResume() {
		titleText.setText("Bienvenido " + prefs.getString(NAME_PROFILE, ""));
		value.setText("");
    	//si esta logeado aun no muestra el botón
		if (prefs.getString(USER_PREFERENCES_LOGIN, "").compareTo("")!=0){
			//es cliente
			if(prefs.getString(USER_TIPE, "client").compareTo("client")==0){
				transferButton.setText("Realizar transferencia");
				tpvText.setVisibility(View.GONE);

			}else{
				transferButton.setText("Realizar Cobro");
				tpvText.setVisibility(View.VISIBLE);

			}
			transferButton.setVisibility(View.VISIBLE);
		}
    	super.onResume();
    }

}
