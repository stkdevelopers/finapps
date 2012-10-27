package com.stkdevelopers.easytransfer.connection;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.stkdevelopers.easytransfer.tools.Account;
import com.stkdevelopers.easytransfer.tools.Operation;
import com.stkdevelopers.easytransfer.tools.Tarjeta;
import com.stkdevelopers.easytransfer.tools.UserProfile;

public class HttpHandler {
	
	
	public static String DOMAIN = "finappsapi.bdigital.org";
	public static String API_KEY ="1b2c1c1393";
	
	public static String ERROR_STATUS = "ERROR";
	public static String OK_STATUS = "OK";

	public static String BASE_URL =DOMAIN+"/api/2012/";
	public static String FINAPPS_URL= "http://"+BASE_URL+API_KEY;
	public static String TAG = "HttpHandler";
	private static String called = "client";
	private static UserProfile user = null;

	
	//returns the token of seession
	public static String login(String username, String password){
		
		String token="ERROR";
		String status="ERROR";
		String URL_LOGIN = FINAPPS_URL+"/access/login";
		
		try {
			
			Log.i(TAG,"intentando hacer login en: "+ URL_LOGIN);
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("stkdevelopers");
			URL urlObj = new URL(URL_LOGIN);
			HttpGet request = new HttpGet(URL_LOGIN);
			HttpHost host = new HttpHost(urlObj.getHost(), urlObj.getPort(),urlObj.getProtocol());
			AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort());
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
			CredentialsProvider cp = new BasicCredentialsProvider();
			cp.setCredentials(scope, creds);
			HttpContext credContext = new BasicHttpContext();
			credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);

			HttpResponse response = httpClient.execute(host, request,credContext);
			 // Obtener respuesta del servidor
	        HttpEntity entity = response.getEntity();

            //Recuperamos el stream de datos del get para transformarlo a StringBuilder
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream);
            instream.close();
//			Log.i(TAG,"result: " +result);

	        //Objeto  Json
			JSONObject j_object = new JSONObject(result);
	        token = j_object.getString("token");
	        status = j_object.getString("status");
	  

			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			
			
		}
		
		return token;
		
	}
	
	//listado de tarjetas objeto en una sesión
		public static UserProfile getUserProfile(String token){
		    try {
		    	String call = "client";
			    HttpClient httpclient = new DefaultHttpClient();
			    //Preparar objeto get
			    HttpGet httpget = new HttpGet(FINAPPS_URL+"/"+token+"/operations/"+call+"/profile/@me"); 
				Log.i(TAG,"intentando hacer login en: "+ FINAPPS_URL+"/"+token+"/operations/"+called+"/profile/@me");	

			    HttpResponse response;
		        response = httpclient.execute(httpget);
		        // Examine the response status
		        Log.i(TAG,"tarjetas: " + response.getStatusLine().toString());        
		        
		        // Obtener respuesta del servidor
		        HttpEntity entity = response.getEntity();
	            // Recuperamos el stream de datos del get para transformarlo a StringBuilder
	            InputStream instream = entity.getContent();
	            String result= convertStreamToString(instream);
	            instream.close();
	            
//	            Log.i(TAG, "Result:::> " + result);
	            
		        //Objeto  Json
				JSONObject j_object = new JSONObject(result);
		        String status = j_object.getString("status");
		        String msg = j_object.getString("msg");
		        String data = j_object.getString("data");
   	        	
		        JSONObject json_data = new JSONObject(data);	
		        String id = json_data.getString("id");		        	
	        	String holder = json_data.getString("holder");
	    
//		        Log.i(TAG,"Status: " + status + " message:"+msg + " id: " + id + " holder: " + holder);
	        	JSONObject json_data_holder = new JSONObject(holder);
	        	String username = json_data_holder.getString("username");
	        	String firstName = json_data_holder.getString("firstName");
	        	String lastName = json_data_holder.getString("lastName");
	        	String password = json_data_holder.getString("password");	        	


	        	//filtramos si es comercio/cliente comprobando su publicProfile (cliente no tiene)
	        	int typeProfile = UserProfile.CLIENT;
	        	String publicProfile = "";
	        	try{
	        		typeProfile = UserProfile.COMMERCE;
	        		publicProfile = json_data.getString("publicProfile");	
	        	}catch(Exception e){
	        		typeProfile = UserProfile.CLIENT;
	        		e.printStackTrace();
	        	}
	        	
	        	ArrayList<Tarjeta> tarjetas =new ArrayList<Tarjeta>();
	        	if(typeProfile == UserProfile.CLIENT ){
		        	String cardsid = json_data.getString("cards");	
		        	//recuperamos sus tarjetas de credito
		        	ArrayList<String> cardsarray = new ArrayList<String>();
		        	cardsid = cardsid.replace("[", "").replace("]", "").replace("\"","");
			        String[] cardsString = cardsid.split(",");
			        Log.i(TAG,"sizecards: " + cardsString.length);
			        for(int i = 0; i<cardsString.length;i++){
			        	cardsarray.add(cardsString[i].trim());
			        	Log.i(TAG,"tarjeta: " +cardsString[i].trim() );
			        }
		        	tarjetas = getTarjetas(cardsarray, token);

	        	}
	        	String accountsid = json_data.getString("accounts");
		        //recuperamos sus cuetnas bancarias
		        ArrayList<String> accountsarray = new ArrayList<String>();
		        accountsid= accountsid.replace("[", "").replace("]", "").replace("\"","");
		        String[] accountsString = accountsid.split(",");
		        Log.i(TAG,"sizeaccounts: " + accountsString.length);
		        for(int i = 0; i<accountsString.length;i++){
		        	accountsarray.add(accountsString[i].trim());
		        	Log.i(TAG,"account: " +accountsString[i].trim() );
		        }
		        
		        //recogemos toda la informacion de las cuentas y las tajretas
	        	ArrayList<Account> accounts = getAccounts(accountsarray, token);
	        	
	        	user = new UserProfile(id,typeProfile, username, password, firstName, lastName, "", accounts, tarjetas,publicProfile);

	
		    } catch (Exception e) {
		    	//probara si es comercio
		    	Log.e(TAG, e.toString());
		    	try{
		    		String call = "commerce";
		    	 	HttpClient httpclient = new DefaultHttpClient();
				    //Preparar objeto get
				    HttpGet httpget = new HttpGet(FINAPPS_URL+"/"+token+"/operations/"+call+"/profile/@me"); 
					Log.i(TAG,"intentando hacer login en: "+ FINAPPS_URL+"/"+token+"/operations/"+called+"/profile/@me");	

				    HttpResponse response;
			        response = httpclient.execute(httpget);
			        // Examine the response status
			        Log.i(TAG,"tarjetas: " + response.getStatusLine().toString());        
			        
			        // Obtener respuesta del servidor
			        HttpEntity entity = response.getEntity();
		            // Recuperamos el stream de datos del get para transformarlo a StringBuilder
		            InputStream instream = entity.getContent();
		            String result= convertStreamToString(instream);
		            instream.close();
		            
//		            Log.i(TAG, "Result:::> " + result);
		            
			        //Objeto  Json
					JSONObject j_object = new JSONObject(result);
			        String status = j_object.getString("status");
			        String msg = j_object.getString("msg");
			        String data = j_object.getString("data");
	   	        	
			        JSONObject json_data = new JSONObject(data);	
			        String id = json_data.getString("id");		        	
		        	String holder = json_data.getString("holder");
		    
//			        Log.i(TAG,"Status: " + status + " message:"+msg + " id: " + id + " holder: " + holder);
		        	JSONObject json_data_holder = new JSONObject(holder);
		        	String username = json_data_holder.getString("username");
		        	String firstName = json_data_holder.getString("firstName");
		        	String lastName = json_data_holder.getString("lastName");
		        	String password = json_data_holder.getString("password");	        	


		        	//filtramos si es comercio/cliente comprobando su publicProfile (cliente no tiene)
		        	int typeProfile = UserProfile.CLIENT;
		        	String publicProfile = "";
		        	try{
		        		typeProfile = UserProfile.COMMERCE;
		        		publicProfile = json_data.getString("publicProfile");	
		        	}catch(Exception ex){
		        		typeProfile = UserProfile.CLIENT;
		        		ex.printStackTrace();
		        	}
		        	
		        	ArrayList<Tarjeta> tarjetas =new ArrayList<Tarjeta>();
		        	if(typeProfile == UserProfile.CLIENT ){
			        	String cardsid = json_data.getString("cards");	
			        	//recuperamos sus tarjetas de credito
			        	ArrayList<String> cardsarray = new ArrayList<String>();
			        	cardsid = cardsid.replace("[", "").replace("]", "").replace("\"","");
				        String[] cardsString = cardsid.split(",");
				        Log.i(TAG,"sizecards: " + cardsString.length);
				        for(int i = 0; i<cardsString.length;i++){
				        	cardsarray.add(cardsString[i].trim());
				        	Log.i(TAG,"tarjeta: " +cardsString[i].trim() );
				        }
			        	tarjetas = getTarjetas(cardsarray, token);

		        	}
		        	String accountsid = json_data.getString("accounts");
			        //recuperamos sus cuetnas bancarias
			        ArrayList<String> accountsarray = new ArrayList<String>();
			        accountsid= accountsid.replace("[", "").replace("]", "").replace("\"","");
			        String[] accountsString = accountsid.split(",");
			        Log.i(TAG,"sizeaccounts: " + accountsString.length);
			        for(int i = 0; i<accountsString.length;i++){
			        	accountsarray.add(accountsString[i].trim());
			        	Log.i(TAG,"account: " +accountsString[i].trim() );
			        }
			        
			        //recogemos toda la informacion de las cuentas y las tajretas
		        	ArrayList<Account> accounts = getAccounts(accountsarray, token);
		        	
		        	user = new UserProfile(id,typeProfile, username, password, firstName, lastName, "", accounts, tarjetas,publicProfile);
		    	}catch(Exception exe){
		    		exe.printStackTrace();
		    	}
		    }
			
			return user;
		}

	
	
	//listado de tarjetas objeto en una sesión
	public static ArrayList<Tarjeta> getTarjetas(ArrayList<String> idTarjetasClient, String token){
	 
		ArrayList<Tarjeta> tarjetas = new ArrayList<Tarjeta>();
	  //Recogemos las tarjetas
		for (String idTarjeta :idTarjetasClient){
			
			Log.i(TAG,"getTarjetas: "+FINAPPS_URL+"/"+token+"/card/"+idTarjeta+"/status");
		    try {
			    HttpClient httpclient = new DefaultHttpClient();
			    //Preparar objeto get
			    HttpGet httpget = new HttpGet(FINAPPS_URL+"/"+token+"/operations/card/"+idTarjeta+"/status"); 
			    HttpResponse response;
		        response = httpclient.execute(httpget);
		        // Examine the response status
		        Log.i(TAG,"getComercio: " + response.getStatusLine().toString());
	
		        // Obtener respuesta del servidor
		        HttpEntity entity = response.getEntity();
	            // Recuperamos el stream de datos del get para transformarlo a StringBuilder
	            InputStream instream = entity.getContent();
	            String result= convertStreamToString(instream);
	            instream.close();
		        //Objeto  Json
				JSONObject j_object = new JSONObject(result);
		        String status = j_object.getString("status");
		        String msg = j_object.getString("msg");
		        String cards = j_object.getString("data");
		        Log.i(TAG,"Status: " + status + " message:"+msg);
				       
	        	JSONObject json_data = new JSONObject(cards);
	        	String id = json_data.getString("id");
	        	String number = json_data.getString("number");
	        	String holder = json_data.getString("holder");
	        	String linkAccount = json_data.getString("linkAccount");
//		        	String deprecateDate = json_data.getString("deprecateDate");
	        	String securityCode= "";
//	        	securityCode = json_data.getString("securityCode");
	       
	        	String mode = json_data.getString("mode");
	        	String issuer = json_data.getString("issuer");
	        	
	        	double creditLimit = 0;
	        	double totalDebt = 0;
	        	double creditAvailable =0;
	        	double interestRate = 0;
	        	

//	        	String creditOptions = j_object.getString("creditOptions");
//	        	JSONObject json_data_opt = new JSONObject(creditOptions);
//	        	double creditLimit = json_data_opt.getDouble("creditLimit");
//	        	double totalDebt = json_data_opt.getDouble("creditLimit");
//	        	double creditAvailable = json_data_opt.getDouble("creditAvailable");
//	        	double interestRate = json_data_opt.getDouble("interestRate");

	        	
	        	
	        	Date date =new Date();
	        	Date datedue = new Date();
	        	tarjetas.add(new Tarjeta(id, number, holder, linkAccount, date, securityCode, mode, issuer, creditLimit, datedue, totalDebt, creditAvailable, interestRate));
	
		    } catch (Exception e) {
		    	Log.e(TAG, e.toString());
		    }
		}
		    	
		return tarjetas;
	}

	
	//listado de tarjetas objeto en una sesión
		public static ArrayList<Account> getAccounts(ArrayList<String> idAccountsArray, String token){
		 
			ArrayList<Account> accounts = new ArrayList<Account>();
		  //Recogemos las tarjetas
			for (String idAccount :idAccountsArray){
				
				Log.i(TAG,"llamo a: "+FINAPPS_URL+"/"+token+"/operations/account/"+idAccount);
			    try {
				    HttpClient httpclient = new DefaultHttpClient();
				    //Preparar objeto get
				    HttpGet httpget = new HttpGet(FINAPPS_URL+"/"+token+"/operations/account/"+idAccount); 
				    HttpResponse response;
			        response = httpclient.execute(httpget);
			        // Examine the response status
			        Log.i(TAG,"getComercio: " + response.getStatusLine().toString());
		
			        // Obtener respuesta del servidor
			        HttpEntity entity = response.getEntity();
		            // Recuperamos el stream de datos del get para transformarlo a StringBuilder
		            InputStream instream = entity.getContent();
		            String result= convertStreamToString(instream);
		            instream.close();
			        //Objeto  Json
					JSONObject j_object = new JSONObject(result);
			        String status = j_object.getString("status");
			        String msg = j_object.getString("msg");
			        String cards = j_object.getString("data");
					 
		        	JSONObject json_data = new JSONObject(cards);
		        	String id = json_data.getString("id");
//		        	String holders = json_data.getString("holders");
//		        	String officce = json_data.getString("officce");
		        	String accountNumber = json_data.getString("accountNumber");
		        	
		        	String IBAN =json_data.getString("iban");

//		        	String currency = json_data.getString("currency");
		        	double availableBalance = json_data.getDouble("availableBalance");
		        	double retainedBalance = json_data.getDouble("retainedBalance");
		        	double actualBalance = json_data.getDouble("actualBalance");
		        	
			        Log.i(TAG,"Status: " + status + " message:"+msg + " accountNumber: " + accountNumber);

		        	accounts.add(new Account(id, accountNumber, IBAN, 0, availableBalance, retainedBalance, actualBalance));
			    } catch (Exception e) {
			    	Log.e(TAG, e.toString());
			    }
			}
			
	    	
			return accounts;
		}

	
	//listado de tarjetas en una sesion
	public static ArrayList<Integer> getTarjetasId(String token){
		ArrayList<Integer> ids_tarjetas = new ArrayList<Integer>();

		//Recogemos Ids tarjetas asociadas
	    try {
		    HttpClient httpclient = new DefaultHttpClient();
		    //Preparar objeto get
		    HttpGet httpget = new HttpGet(FINAPPS_URL+"/"+token+"/card/list"); 
		    HttpResponse response;
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i(TAG,"getComercio: " + response.getStatusLine().toString());

	        // Obtener respuesta del servidor
	        HttpEntity entity = response.getEntity();
            // Recuperamos el stream de datos del get para transformarlo a StringBuilder
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream);
            instream.close();
	        //Objeto  Json
			JSONObject j_object = new JSONObject(result);
	        String status = j_object.getString("status");
	        String msg = j_object.getString("msg");
	        String cards = j_object.getString("cards");
	        Log.i(TAG,"Status: " + status + " message:"+msg);
			
            JSONArray jArray = new JSONArray(cards);
	        for (int i = 0; i < jArray.length(); i++) {
	        	JSONObject json_data = jArray.getJSONObject(i);
	        	ids_tarjetas.add(json_data.getInt("id_card"));
			}
	        

	    } catch (Exception e) {
	    	Log.e(TAG, e.toString());
	    }
	    return ids_tarjetas;
	    
	}
	
	
	public static String getPayment(String id_card, String token, String value){
		/**
		 * Request api/2012/<API_Key>/<TOKEN>/operations/ by client
		GET ../payment/{id_card}/code?<PARAMETERS>
		Parameters
		value=<DOUBLE>  (value to be payed to commerce)
		Response {
				status : <OK,ERROR>,
				msg : <STRING>,
				data : {
					code: STRING,
					idClient: STRING,
					idCard: STRING,
					value: DOUBLE,
					status: enum [OPEN | EXECUTED | CLOSED],
					timestamp: DATE,
					deprecateDate: DATE
				}
			
			}
		 * */
		String ret = "ERROR";
		String ERROR = "ERROR";
		String URL_PAYMENTCODE = FINAPPS_URL + "/" + token + "/operations/payment/"+id_card+"/code?value="+value;
		
		Log.i(TAG, "URL PAYMENTCODE::::::: " + URL_PAYMENTCODE);
		
		try{
			// montamos el json que se tiene que enviar
			 HttpClient httpclient = new DefaultHttpClient();
		    //Preparar objeto get
		    HttpGet httpget = new HttpGet(URL_PAYMENTCODE); 
		    HttpResponse response;
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i(TAG,"getComercio: " + response.getStatusLine().toString());

	        // Obtener respuesta del servidor
	        HttpEntity entity = response.getEntity();
            // Recuperamos el stream de datos del get para transformarlo a StringBuilder
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream);
            instream.close();
	            
            //Objeto  Json
			JSONObject j_object = new JSONObject(result);
	        String status = j_object.getString("status");
	        String msg = j_object.getString("msg");
	        String data = j_object.getString("data");
	        
	        JSONObject j_objectdata = new JSONObject(data);
	        String j_od_code = j_objectdata.getString("code");
	        String j_od_idclient = j_objectdata.getString("idClient");
	        String j_od_idcard = j_objectdata.getString("idCard");
	        String j_od_value = j_objectdata.getString("value");
	        String j_od_status = j_objectdata.getString("status");
	        String j_od_timestamp = j_objectdata.getString("timestamp");
	        String j_od_deprecateDate = j_objectdata.getString("deprecateDate");
	        
	        Log.i(TAG,"getPayment status:::: " + status + " message:"+msg);
			

	        if (status.compareTo("OK")==0){
	        	ret = j_od_code;
	        }
	        
		}catch (Exception e){
			Log.e(TAG, "Error al hacer la transferencia");
			e.printStackTrace();
		}
		
		return ret;
		
	}
	//id_card Client // id_account comercio
	public static boolean execPayment(String id_card, String clienttoken, String commercetoken, String value, String id_account){
		/**
		 * Request:	http://finappsapi.bdigital.org/api/2012/1b2c1c1393/<TOKEN>/operations/
		 * POST DATA: ../payment/{code}/exec
		 * Payload: { idAcount: id_accont }
		 * 
		 * Response:
		 * {
			status : <OK,ERROR>,
			msg : <STRING>,
			data : {
					code: STRING,
					idCommerce: STRING,
					idAccount: STRING,
					value: DOUBLE,
					status: enum [PEN | EXECUTED | CLOSED],
					timestamp: DATE,
					deprecateDate: DATE
				}
			}
		 * */
		
		boolean ret = false;
		
		String code = getPayment(id_card, clienttoken, value);
		if (code.compareTo("ERROR")==0){
			Log.e(TAG, "Error al obtener el code");
			return ret;
		}
		
		String ERROR = "ERROR";
		String URL_BILLING = FINAPPS_URL + "/" + commercetoken + "/operations/payment/"+code+"/exec";

		Log.i(TAG, "URL BILLING::::::: " + URL_BILLING);
		
		try{
			// montamos el json que se tiene que enviar
			String jsonString = "{" + "\"idAccount\": \""+id_account+"\" }";
			Log.i(TAG, "Voy a enviar esto como POST: " + jsonString);
			
			HttpResponse response;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL_BILLING);
			StringEntity se = new StringEntity(jsonString);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(se);
			response = httpclient.execute(httppost);

			if (response != null) {
				InputStream in = response.getEntity().getContent(); 

				ret = true;
				String result = convertStreamToString(in);

				Log.i("execPayment","Result: "+result);
				JSONObject j_object = new JSONObject(result);
				String statusResponse = j_object.getString("status");
				if(statusResponse.compareTo(ERROR)!=0){
					ret = true;
				}else{
					ret = false;
				}

			}

		}catch (Exception e){
			Log.e(TAG, "Error al hacer la el execPayment");
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	
	public static String getPaymentStatus(String token, String code ){
		
		/*****
		 *  Request api/2012/<API_Key>/<TOKEN>/operations/ by client
			GET ../payment/{code}/status
			Response {applicatio/json}
			{
				status : <OK,ERROR>,  
				msg : <STRING>,
				data : {
					code: STRING,
					idClient: STRING,
					idCard: STRING,
					value: DOUBLE,
					status: enum [PEN | EXECUTED | CLOSED],
					timestamp: DATE,
					deprecateDate: DATE
				}
			}

		 */
		String ret = "ERROR";
		String URL_STATUS = FINAPPS_URL + "/" + token + "/operations/payment/"+code+"/status";
		
		Log.i(TAG, "URL URL_STATUS::::::: " + URL_STATUS);
		
		try{

			// montamos el json que se tiene que enviar
			 HttpClient httpclient = new DefaultHttpClient();
		    //Preparar objeto get
		    HttpGet httpget = new HttpGet(URL_STATUS); 
		    HttpResponse response;
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i(TAG,"getPaymentStatus: " + response.getStatusLine().toString());

	        // Obtener respuesta del servidor
	        HttpEntity entity = response.getEntity();
           // Recuperamos el stream de datos del get para transformarlo a StringBuilder
           InputStream instream = entity.getContent();
           String result= convertStreamToString(instream);
           instream.close();
	            
            //Objeto  Json
			JSONObject j_object = new JSONObject(result);
	        String status = j_object.getString("status");
	        String msg = j_object.getString("msg");
	        String data = j_object.getString("data");
	        
	        JSONObject j_objectdata = new JSONObject(data);
	        String j_od_code = j_objectdata.getString("code");
	        String j_od_idclient = j_objectdata.getString("idClient");
	        String j_od_idcard = j_objectdata.getString("idCard");
	        String j_od_value = j_objectdata.getString("value");
	        String j_od_status = j_objectdata.getString("status");
	        String j_od_timestamp = j_objectdata.getString("timestamp");
	        String j_od_deprecateDate = j_objectdata.getString("deprecateDate");
	        
	        Log.i(TAG,"getPayment status:::: " + status + " message:"+msg);
			

	        if (status.compareTo("OK")==0){
	        	ret = j_od_status;
	        }

		}catch (Exception e){
			Log.e(TAG, "Error al hacer la el execPayment");
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static Operation getOperations(String token, String id_account){
		return getOperations(token, id_account, "TRANSFER", "DESC", "100", "1");
	}
	
	public static Operation getOperations(String token, String id_account, String filter, String order, String limit, String pag){
		
		if ( filter==null && filter.compareTo("")==0 )		filter = "TRANSFER";
		if ( order==null && order.compareTo("")==0 )		order = "DESC";
		if ( limit==null && limit.compareTo("")==0 )		limit = "100";
		if ( pag==null && pag.compareTo("")==0 )			pag = "1";
		/**
		 * 
		 * Request api/2012/<API_Key>/<TOKEN>/operations/

			GET ../account/{id_account}/operations?<PARAMETERS>
			Parameters
				filter=<STRING[“DEPOSIT”|”CHARGE”|”PAYROLL”|”TRANSFER”]>
				order= <STRING[“ASC”|”DES”]>
				limit<INT> (results per page)
				pag=<INT> (page number)
			Response {applicatio/json}
			{
				status : <OK,ERROR>,
				msg : <STRING>,
				data : {
					[{id : STRING,
					account: id_account,
					date: DATE,
					type: enum[DEPOSIT|CHARGE|PAYROLL|TRANSFER],
					concept: STRING,
					description: STRING,
					value: DOUBLE,
					actualBalance: DOUBLE,
					options: STRING},
					...]}
			}
		 * */

		String ret = "ERROR";
		String URL_OPERATIONS = FINAPPS_URL + "/" + token + "/operations/account/" + id_account + "/operations?filter="+filter+"&order="+order+"&limit="+limit+"&pag="+pag;
		
		Log.i(TAG, "URL OPERATIONS::::::: " + URL_OPERATIONS);
		
		Operation operation = null;
		try{

			// montamos el json que se tiene que enviar
			 HttpClient httpclient = new DefaultHttpClient();
		    //Preparar objeto get
		    HttpGet httpget = new HttpGet(URL_OPERATIONS); 
		    HttpResponse response;
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i(TAG,"getOperations: " + response.getStatusLine().toString());

	        // Obtener respuesta del servidor
	        HttpEntity entity = response.getEntity();
           // Recuperamos el stream de datos del get para transformarlo a StringBuilder
           InputStream instream = entity.getContent();
           String result= convertStreamToString(instream);
           instream.close();
	             
            //Objeto  Json
			JSONObject j_object = new JSONObject(result);
	        String id = j_object.getString("id");
	        String account = j_object.getString("account");
	        String date = j_object.getString("date");
	        String type = j_object.getString("type");
	        String concept = j_object.getString("concept");
	        String description = j_object.getString("description");
	        String value = j_object.getString("value");
	        String actualBalance = j_object.getString("actualBalance");
	        String options = j_object.getString("options");
	        
	        Log.i(TAG,"getOperations id:::: " + id + " value:"+value);
	        
	        operation = new Operation();
	        operation.setId(id);
	        operation.setAccount(account);
	        operation.setDate(date);
	        operation.setType(type);
	        operation.setConcept(concept);
	        operation.setDescription(description);
	        operation.setValue(value);
	        operation.setActualBalance(actualBalance);
	        operation.setOptions(options);

		}catch (Exception e){
			Log.e(TAG, "Error al hacer la el getOperations");
			e.printStackTrace();
		}
		
		return operation;
	}
	
	/////////////////////REALIZADO DE TRANSFERENCIAS
	
	//TRANSFERENCIA ENTRE  USUARIOs
	public static boolean transferMoney(String originAccount,String destinationAccount, String token, String value) {
		/**
		 * { "originAccount": "2100 8888 8 0000000002",
		 * "destinationAccount":"2100 8888 12 0000000003", "value":2300,
		 * "additionalData": {"concept":"Transferencia test", "payee":"menix"}
		 * }' http://<Base_URL>/<API_Key>/<Token>/operations/account/transfer
		 * 
		 * http://finappsapi.bdigital.org/api/2012/1b2c1c1393/f13-a1da-67
		 * bf61ee2095/operations/account/transfer
		 * 
		 * */
		boolean ret = false;
//		token = "f13-a1da-67bf61ee2095";
//		originAccount = "2100 1111 60 0000000078";
//		destinationAccount = "2100 1111 56 0000000035";
//		value = "12";
	
		String ERROR = "ERROR";
		String URL_TRANSFER = FINAPPS_URL + "/" + token
				+ "/operations/account/transfer";
	
		Log.i(TAG, "URL TRANSFER::::::: " + URL_TRANSFER);
	
		String concept = "easytransfer";
		String payee = "menix";
		try {
			// montamos el json que se tiene que enviar
			String jsonString = "{" + "\"originAccount\": \"" + originAccount
					+ "\"," + "\"destinationAccount\": \"" + destinationAccount
					+ "\"," + "\"value\": " + value + ","
					+ "\"additionalData\": {" + "\"concept\": \"" + concept
					+ "\"," + "\"payee\": \"" + payee + "\" " + "}" + "}";
	
			Log.i(TAG, "Voy a enviar esto como POST: " + jsonString);
			HttpResponse response;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL_TRANSFER);
			StringEntity se = new StringEntity(jsonString);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppost.setEntity(se);
			response = httpclient.execute(httppost);
	
			if (response != null) {
				InputStream in = response.getEntity().getContent(); 
				
			ret = true;
			String result = convertStreamToString(in);
			
			Log.i("Trasnfer","Result: "+result);
			JSONObject j_object = new JSONObject(result);
			String statusResponse = j_object.getString("status");
	        if(statusResponse.compareTo(ERROR)!=0){
	        	ret = true;
	        }else{
	        	ret = false;
	        }
			
			}
		} catch (Exception e) {
			Log.e(TAG, "Error al hacer la transferencia");
		}
	
		return ret;
	}

	//////////////////////////
	//PAGO A COMERCIO/AUTONOMO
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// HTTP UTILS

	
	//Metodo conversor de imputstream en String
	private static String convertStreamToString(InputStream is) {
	
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	
	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	//Nuevo metodo de comprobacion de network available
	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
}
