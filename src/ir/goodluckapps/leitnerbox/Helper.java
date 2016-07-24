package ir.goodluckapps.leitnerbox;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Helper {
	
	public static void setAlarm(Context context)
	{
	    //----------------------------------------------------------------
    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
    	Date Time_next;
    	if(calendar.get(Calendar.HOUR_OF_DAY) >= 8 && calendar.get(Calendar.HOUR_OF_DAY) < 16)
    	{
    		Time_next = calendar.getTime();
    		Time_next.setMinutes(0);
    		Time_next.setHours(16);
    	}else if(calendar.get(Calendar.HOUR_OF_DAY) >= 16 && calendar.get(Calendar.HOUR_OF_DAY) < 20)
    	{
    		Time_next = calendar.getTime();
    		Time_next.setMinutes(0);
    		Time_next.setHours(20);        		
    	}else if(calendar.get(Calendar.HOUR_OF_DAY) >= 20)
    	{
    		Time_next = calendar.getTime();
    		Time_next.setMinutes(0);
    		Time_next.setHours(8);
    		Time_next.setTime(Time_next.getTime() + 24 * 60 * 60 * 1000);            		
    	}else
    	{
    		Time_next = calendar.getTime();
    		Time_next.setMinutes(0);
    		Time_next.setHours(8);
    	}
		
		Intent intentAlarm = new Intent(context, AlarmReciever.class);
		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(context, 0, intentAlarm, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, Time_next.getTime(), pendingIntentAlarm);
	}
	
	public static long calculateDelayOfNextStep(int currentStep)
	{
		long singleDay = 86400000;
		switch(currentStep)
		{
			case 1:
			{
				// next step is 2, next Date = 2 * singleDate 
				return 2 * singleDay;
			}
			case 2:
			{
				// next step is 3, next Date = 2 * singleDate
				return 5 * singleDay;				
			}
			case 3:
			{
				// next step is 4, next Date = 8 * singleDate 
				return 8 * singleDay;
			}
			case 4:
			{
				// next step is 5, next Date = 14 * singleDate 
				return 14 * singleDay;				
			}
		}
    	return 0;
	}
	
	public static void recalculateBox(Context context)
	{
		for(int room = 30; room > 0; room--)
		{
			if(room == 30 || room == 16 || room == 8 || room == 3 || room == 1)
			{
				continue;
			}
			DatabaseManager.getSingleton(context).getTodayCardsCount();
		}
	}
	
	public static LocalDate getLocalDate(long miliDate)
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		calendar.setTimeInMillis(miliDate);
		LocalDate localDate = new LocalDate();
		localDate = localDate.withYear(calendar.get(Calendar.YEAR));
		localDate = localDate.withDayOfYear(calendar.get(Calendar.DAY_OF_YEAR));
		return localDate;
	}

	public static BuiltinCard convertToBuitinCard(Card card)
	{
		BuiltinCard builtinCard = new BuiltinCard();
		builtinCard.setId(card.getId());
		builtinCard.setPackageId(card.getPackageId());
		
		if(card.isCustomCard())
		{
			builtinCard.setFrontPronounce("");
			builtinCard.setFrontPart("");
			builtinCard.setFrontContent(card.getFront());
			builtinCard.setBackFa(card.getBack());
			builtinCard.setBackEng("");
			return builtinCard;
		}
		
		Pattern pattern = Pattern.compile("/(.*?)/");
		Matcher matcher = pattern.matcher(card.getFront());
		if (matcher.find())
			if(!matcher.group(1).equals(""))
				builtinCard.setFrontPronounce("/" + matcher.group(1) + "/");
		
		pattern = Pattern.compile("<(.*?)>");
		matcher = pattern.matcher(card.getFront());
		if (matcher.find())
			builtinCard.setFrontContent(matcher.group(1));

		pattern = Pattern.compile(":(.*?):");
		matcher = pattern.matcher(card.getFront());
		if (matcher.find())
			if(!matcher.group(1).equals(""))
				builtinCard.setFrontPart("(" + matcher.group(1) + ")");

		pattern = Pattern.compile("<(.*?)>");
		matcher = pattern.matcher(card.getBack());
		if (matcher.find())
			builtinCard.setBackEng(matcher.group(1));
		
		pattern = Pattern.compile("/(.*?)/");
		matcher = pattern.matcher(card.getBack());
		if (matcher.find())
			builtinCard.setBackFa(matcher.group(1));
		
		return builtinCard;
	}
	
	public static void upgradeTo(Context context, String packageName, View packageView, int recordNumber)
	{
		if(packageName.equals("504"))
		{
			AsyncHTTPConnection.singleExecute(context, 2, packageName, "504 Words", packageView, recordNumber, 500);
		}else if(packageName.equals("GoodLuck_Words_Level_1"))
		{
			AsyncHTTPConnection.singleExecute(context, 3, packageName, "Good Luck Words Level 1", packageView, recordNumber, 500);
		}else if(packageName.equals("GoodLuck_Words_Level_2"))
		{
			AsyncHTTPConnection.singleExecute(context, 4, packageName, "Good Luck Words Level 2", packageView, recordNumber, 500);
		}else if(packageName.equals("GoodLuck_Words_Level_3"))
		{
			AsyncHTTPConnection.singleExecute(context, 5, packageName, "Good Luck Words Level 3", packageView, recordNumber, 500);
		}
	}	
}
