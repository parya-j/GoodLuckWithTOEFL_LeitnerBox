package ir.goodluckapps.leitnerbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import ir.goodluckapps.leitnerbox.activities.BoxActivity;

public class AlarmReciever extends BroadcastReceiver
{
	private Context _context;
	// ----------------------------------------------------------------
//	private static final String PREF = "ir.goodluckapps.leitnerbox.packages";
	private int _appVersion = 0;
//	private int _packagesVersion = 0;
    @Override
       public void onReceive(Context context, Intent intent)
       {
    		_context = context;
    		int todayCardsCount = DatabaseManager.getSingleton(context).getTodayCardsCount();
    		if(todayCardsCount > 0)
    		{
        		NotificationCompat.Builder mBuilder =
        			    new NotificationCompat.Builder(context)
        			    .setSmallIcon(R.drawable.ic_launcher)
        			    .setContentTitle("Good Luck with TOEFL - LeitnerBox")
        			    .setContentText(todayCardsCount + " card(s) are remained to review. Click to review.")
        			    .setAutoCancel(true);
        		Intent resultIntent = new Intent(context, BoxActivity.class);
        		resultIntent.putExtra("notification_id", Integer.toString(2015));
        		PendingIntent resultPendingIntent =
            		    PendingIntent.getActivity(
            		    context,
            		    2015,
            		    resultIntent,
            		    PendingIntent.FLAG_UPDATE_CURRENT
            		);
        		mBuilder.setContentIntent(resultPendingIntent);
        		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        		mNotifyMgr.notify(2015, mBuilder.build());
    		}
    		updateVersionController();
//    		SharedPreferences config = _context.getSharedPreferences(PREF, 0);
    		if(getVersion() < _appVersion)
    		{
        		NotificationCompat.Builder mBuilder =
        			    new NotificationCompat.Builder(context)
        			    .setSmallIcon(R.drawable.ic_launcher)
        			    .setContentTitle("Good Luck with LeitnerBox")
        			    .setContentText("A new version with more packages is already available.")
        			    .setAutoCancel(true);
        		
        		Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir/app/ir.goodluckapps.leitnerbox"));
        		resultIntent.putExtra("notification_id", Integer.toString(2016));
        		PendingIntent resultPendingIntent =
            		    PendingIntent.getActivity(
            		    context,
            		    2016,
            		    resultIntent,
            		    PendingIntent.FLAG_UPDATE_CURRENT
            		);
        		mBuilder.setContentIntent(resultPendingIntent);
        		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        		mNotifyMgr.notify(2016, mBuilder.build());    			
    		}
//    		int lastPackageVersion = config.getInt("packageVersion", 0);
//    		if(lastPackageVersion < _packagesVersion)
//    		{
//        		NotificationCompat.Builder mBuilder =
//        			    new NotificationCompat.Builder(context)
//        			    .setSmallIcon(R.drawable.ic_launcher)
//        			    .setContentTitle("Good Luck with TOEFL - LeitnerBox")
//        			    .setContentText("New package(s) are already available. Click to download.")
//        			    .setAutoCancel(true);
//        		Intent resultIntent = new Intent(context, PremiumActivity.class);
//        		resultIntent.putExtra("notification_id", Integer.toString(2017));
//        		PendingIntent resultPendingIntent =
//            		    PendingIntent.getActivity(
//            		    context,
//            		    2017,
//            		    resultIntent,
//            		    PendingIntent.FLAG_UPDATE_CURRENT
//            		);
//        		mBuilder.setContentIntent(resultPendingIntent);
//        		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        		mNotifyMgr.notify(2017, mBuilder.build());    			
//    		}
    		Helper.setAlarm(context);
       }
	// ----------------------------------------------------------------
    public int getVersion()
	{
		try
		{
			return _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0).versionCode;
		}catch(NameNotFoundException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	// ---------------------------------------------------------------- 
	private String convertStreamToString(InputStream is) throws UnsupportedEncodingException 
	{ 
	      BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        StringBuilder sb = new StringBuilder();
	         String line = null;
	         try {
	                while ((line = reader.readLine()) != null) {
	                        sb.append(line + "\n");
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
	// ----------------------------------------------------------------
	public boolean isConnected() 
	{
		ConnectivityManager manager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	} 
	// ----------------------------------------------------------------    
	public String getContents(String url) 
	{
		String contents ="";	 
		try {
			if(isConnected())
			{
				URLConnection conn = new URL(url).openConnection();
				conn.setConnectTimeout(5000);
				conn.connect();
	
		        InputStream in = conn.getInputStream();
		        contents = convertStreamToString(in);
			}
	   } catch (MalformedURLException e) {
	        e.printStackTrace();
	   } catch (IOException e) {
	        e.printStackTrace();
	   }
	   return contents;
	}
	// ---------------------------------------------------------------- 
    private void updateVersionController()
	{
		try
		{
			String url = "http://www.goodluckapps.ir/apps/leitnerbox/ir.goodluckapps.leitnerbox.xml";
			String content = getContents(url).replace('\n', ' ').trim();
			
			XmlPullParserFactory xmlPullFactory = XmlPullParserFactory.newInstance();
			xmlPullFactory.setNamespaceAware(true);
			XmlPullParser xmlPullParser = xmlPullFactory.newPullParser();
			xmlPullParser.setInput(new StringReader(content));
			xmlPullParser.next();
			int eventType = xmlPullParser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				String tagName = xmlPullParser.getName();
				switch(eventType)
				{
				case XmlPullParser.START_TAG:
					if(tagName.equals("application"))
					{						
						_appVersion = Integer.parseInt(xmlPullParser.getAttributeValue(null, "version"));
//						_packagesVersion = Integer.parseInt(xmlPullParser.getAttributeValue(null, "package_version"));
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				xmlPullParser.next();
				eventType = xmlPullParser.getEventType();
			}
		}
		catch(XmlPullParserException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// ----------------------------------------------------------------
}