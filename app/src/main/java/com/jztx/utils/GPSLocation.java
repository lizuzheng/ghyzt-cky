package com.jztx.utils;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;

/**
 * 
 * @author BeiKe
 * 
 *
 */
public class GPSLocation {



    public static final boolean isOPen(final Context context) { 
        LocationManager locationManager  
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
       
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
       
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
        if (gps ||network) { 
            return true; 
        } 
   
        return false; 
    }
    /**
     * 
     * @param context
     */ 
    public static final boolean  openGPS(Context context) { 
        Intent GPSIntent = new Intent(); 
        GPSIntent.setClassName("com.android.settings", 
                "com.android.settings.widget.SettingsAppWidgetProvider"); 
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE"); 
        GPSIntent.setData(Uri.parse("custom:3")); 
        try { 
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
            return true;
        } catch (CanceledException e) { 
            e.printStackTrace(); 
            return false;
        } 
    }
    
    /**
     *
     * @param Intent
     */
    public static final Intent openGPSStting()
    {
    	Intent intent =new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);			               	
        return intent;
    }
}
