package com.stkdevelopers.easytransfer.adapters;

/**
 * @author Luis Aguilar, Pedro Carrasco, Raul Santos (STK Developers)
 * 
 * 
 * **/
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.stkdevelopers.easytransfer.MainActivity;
import com.stkdevelopers.easytransfer.R;
import com.stkdevelopers.easytransfer.tools.Tarjeta;

public class CardsAdapter extends ArrayAdapter<Tarjeta> {

	private Context context;
	
     public CardsAdapter(Context context, ArrayList<Tarjeta> tarjetas) {
     	super(context,android.R.layout.simple_list_item_1,tarjetas);
     	this.context = context;

   
     }

     public View getView(final int position, View convertView, ViewGroup parent) {
         ImageView  imageView = new ImageView(getContext());
     	final Tarjeta tarjeta = getItem(position);
     	Drawable tarjetaImg = context.getResources().getDrawable(R.drawable.american_express);

     	if (tarjeta.getIssuer().compareTo("VISA_CLASSIC")==0){
         	tarjetaImg = context.getResources().getDrawable(R.drawable.visa_lacaixa);

     	}
     	else if (tarjeta.getIssuer().compareTo("VISA_GOLD")==0){
         	tarjetaImg = context.getResources().getDrawable(R.drawable.visa_gold);

     	}
     	else if (tarjeta.getIssuer().compareTo("AMEX")==0){
         	tarjetaImg = context.getResources().getDrawable(R.drawable.american_express);

     	}

     	
 		imageView.setImageDrawable(tarjetaImg);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(502,320));  
        imageView.setPadding(5, 0, 5, 0);   
        
        return imageView;
     }       
}
