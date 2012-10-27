package com.stkdevelopers.easytransfer;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.stkdevelopers.easytransfer.adapters.CardsAdapter;
import com.stkdevelopers.easytransfer.connection.HttpHandler;
import com.stkdevelopers.easytransfer.tools.Account;
import com.stkdevelopers.easytransfer.tools.Tarjeta;
import com.stkdevelopers.easytransfer.tools.UserProfile;

public class LoginActivity extends Activity{

	private static final String TAG = LoginActivity.class.getSimpleName();
	private SharedPreferences prefs;
	private Editor editor;
	
	//views
	private EditText userText;
	private EditText passText;

	private Button acceptButton;
	private Button cancelButton;
	private Button selectCard;

	private UserProfile user;
	
	//layotus tarjeta /login
	
	private LinearLayout loginLayout;
	private LinearLayout cardsLayout;
	private Gallery galleryTarjetas;
	private Spinner accountsSpinner;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//create objects config
    	prefs = getSharedPreferences("preferences",Context.MODE_PRIVATE);
		editor = prefs.edit();

		//create views
		galleryTarjetas = (Gallery) findViewById(R.id.gallerycards);
		userText = (EditText) findViewById(R.id.userText);	
		passText = (EditText) findViewById(R.id.passText);
		cancelButton = (Button) findViewById(R.id.cancelPinButton);
		acceptButton = (Button) findViewById(R.id.acceptPinButton);
		cardsLayout = (LinearLayout) findViewById(R.id.cardLayout);
		loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
		selectCard = (Button) findViewById(R.id.selectCardButton);
		accountsSpinner = (Spinner) findViewById(R.id.accountsSpinner);
		cardsLayout.setVisibility(View.GONE);
		loginLayout.setVisibility(View.VISIBLE);
		
		
		userText.setText(prefs.getString(MainActivity.USER_PREFERENCES_LOGIN, ""));		
		acceptButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String username = userText.getText().toString();
				final String password = passText.getText().toString();
			
				
				final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.setTitle("Comprobación de usuario");
				dialog.setMessage("Comprobando usuario, espere ...");
				dialog.show();
				
				new Thread(){
					public void run(){
						
						final String token = HttpHandler.login(username, password);
						Log.i(TAG,"::::::::::::::: token: " + token);					
						//identificación valida
						if(token!=HttpHandler.ERROR_STATUS){

							Log.i(TAG,"logged from: "+ username + " -  pass: " + password );
							//actualizar ui
							runOnUiThread(new Runnable() {
								public void run() {
									editor.clear();
									editor.putString(MainActivity.USER_PREFERENCES_LOGIN, username);
									editor.putString(MainActivity.TOKEN, token);
									editor.commit();
									dialog.setMessage("Recogiendo datos de cliente...");
								}
							});
							//procedemos a recoger los datos del perfil del cliente, con sus tarjetas y cuentas
							user  = HttpHandler.getUserProfile(token);	
							if(user!=null){//user ok procedemos a mostrar las tarjetas que tiene					
								//ES CLIENTE TRANSFERENCIAS PASGS
								if(user.getProfileRole() == UserProfile.CLIENT){
									editor.putString(MainActivity.USER_TIPE, "client");
									editor.putString(MainActivity.NAME_PROFILE, user.getFirstname() +" " +user.getLastname());
									editor.commit();
									//si tiene tarjetas las mostramos
									if(!user.getTarjetas().isEmpty()){
										editor.putString(MainActivity.ID_CARD, user.getTarjetas().get(0).getId());
										editor.putString(MainActivity.ID_ACCOUNT, user.getTarjetas().get(0).getLinkAccount());
										editor.commit();
										runOnUiThread(new Runnable() {
											public void run() {
												accountsSpinner.setVisibility(View.GONE);
												cardsLayout.setVisibility(View.VISIBLE);
												loginLayout.setVisibility(View.GONE);
												galleryTarjetas.setVisibility(View.VISIBLE);
												galleryTarjetas.setAdapter(new CardsAdapter(LoginActivity.this, user.getTarjetas()));
												
												//esconderkeyboard
												InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
												imm.hideSoftInputFromWindow(userText.getWindowToken(), 0);
												dialog.dismiss();
												
												//se encarga listener de tarjetas de acabar la tarea de la recogida de la tarjeta por defecto
	
											}
										});
									}else{
										runOnUiThread(new Runnable() {
											public void run() {
												dialog.dismiss();
												Toast.makeText(LoginActivity.this, "No tiene tarjetas de credito vinculadas", Toast.LENGTH_LONG).show();
											}
										});
									}
								//ES COMERCIO COBROS
								}else{
									editor.putString(MainActivity.USER_TIPE, "commerce");
									editor.putString(MainActivity.NAME_PROFILE, user.getFirstname() +" " +user.getLastname());
									editor.commit();
									if(!user.getAccounts().isEmpty()){
										runOnUiThread(new Runnable() {
											public void run() {
												accountsSpinner.setVisibility(View.VISIBLE);
												cardsLayout.setVisibility(View.VISIBLE);
												loginLayout.setVisibility(View.GONE);
												galleryTarjetas.setVisibility(View.GONE);
												
												ArrayList<String> accountNumberArrary = new ArrayList<String>();
												for (Account acc : user.getAccounts()){
													accountNumberArrary.add(acc.getAccountNumber());
												}
												accountsSpinner.setAdapter(new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item,accountNumberArrary ));
												InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
												imm.hideSoftInputFromWindow(userText.getWindowToken(), 0);
												dialog.dismiss();
												
												//se encarga listener de tarjetas de acabar la tarea de la recogida de la tarjeta por defecto
	
											}
										});
									}else{
										runOnUiThread(new Runnable() {
											public void run() {
												dialog.dismiss();
												Toast.makeText(LoginActivity.this, "No tiene cuentas vinculadas", Toast.LENGTH_LONG).show();
											}
										});
									}
								}
								
							}else{//client fail
								runOnUiThread(new Runnable() {
									public void run() {
										dialog.dismiss();
										Toast.makeText(LoginActivity.this, "Problema con el login como particular", Toast.LENGTH_LONG).show();			

									}
								});
							}
						//identificación no valida
						}else{
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(LoginActivity.this, "Login incorrecto", Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}
							});
						}
					}
				}.start();

			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		galleryTarjetas.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				// TODO Auto-generated method stub
				Tarjeta card = (Tarjeta)arg0.getItemAtPosition(arg2);
				editor.putString(MainActivity.ID_CARD, card.getId());
				editor.putString(MainActivity.ID_ACCOUNT, card.getLinkAccount());
				editor.commit();
				Log.i(TAG,card.getNumber());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});	
		accountsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				// TODO Auto-generated method stub
				String accoutnNumber = (String)arg0.getItemAtPosition(arg2);
				editor.putString(MainActivity.NUMBER_ACCOUNT, accoutnNumber);
				for(Account account : user.getAccounts()){
					if(account.getAccountNumber().compareTo(accoutnNumber)==0){
						editor.putString(MainActivity.ID_ACCOUNT, account.getId());
						editor.commit();
					}
				}
				editor.commit();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});	
		//seleccion de tarjeta finalmente guardamos el numero de account de esta tarjeta en prefs
		selectCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				for(Account account : user.getAccounts()){
					if(account.getId().compareTo(prefs.getString(MainActivity.ID_ACCOUNT, ""))==0){
						editor.putString(MainActivity.NUMBER_ACCOUNT, account.getAccountNumber());
						editor.commit();
						Log.i("LoginActivity","Account Associet");
					}
				}
				editor.commit();
				finish();
			}
		});
		

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
	}
	
}
