package ir.goodluckapps.leitnerbox.widgets;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import ir.goodluckapps.leitnerbox.BoxCard;
import ir.goodluckapps.leitnerbox.BuiltinCard;
import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.RemoteViews;

public class ReviewWidgetProvider extends AppWidgetProvider {
	// ----------------------------------------------------------------
	private static final String ACTION_WIDGET_CONTROL = "ir.goodluckapps.leitnerbox.WIDGET_CONTROL";
	public static final String URI_SCHEME = "review_widget";
	private static final String PREF = "ir.goodluckapps.leitnerbox.review.widget";
	private static final String PREF_BOX_CARD_ID = "BOX_CARD_ID";
	private static final String PREF_IS_FRONT = "IS_FRONT";
	// ----------------------------------------------------------------
	private class State
	{
		private Card card = new Card();
		private BoxCard boxCard = new BoxCard();
		private boolean isFront = true;
		private int remainedCardsCount = 0;
	}

	// ----------------------------------------------------------------
	private void setState(Context context, State state)
	{
		SharedPreferences config = context.getSharedPreferences(PREF, 0);
		SharedPreferences.Editor editor = config.edit();
		editor.putInt(PREF_BOX_CARD_ID, state.boxCard.getId());
		editor.putBoolean(PREF_IS_FRONT, state.isFront);
		editor.commit();
	}
	// ----------------------------------------------------------------	
	private State getState(Context context)
	{
        State state = new State();
        SharedPreferences config = context.getSharedPreferences(PREF, 0);
	    state.isFront = config.getBoolean(PREF_IS_FRONT, true);
	    DatabaseManager.getSingleton(context).getSingleTodayCard(state.boxCard, state.card);
	    state.remainedCardsCount = DatabaseManager.getSingleton(context).getTodayCardsCount();
	    return state;
	}
	// ----------------------------------------------------------------
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) 
		{
			int appWidgetId = appWidgetIds[i];			
			updateRemoteViews(context, appWidgetId);
		}
	}
	// ----------------------------------------------------------------
	private void updateRemoteViews(Context context, int appWidgetId)
	{
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_review);
		updateGUI(context, remoteViews);
		setClicks(context, remoteViews, appWidgetId);
		AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
	}
	// ----------------------------------------------------------------
	private void updateGUI(Context context, RemoteViews remoteViews)
	{
		State state = getState(context);
		if(state.boxCard.getId() < 1)
		{
			remoteViews.setTextViewText(R.id.text_view_content_pronounce, "");
			remoteViews.setFloat(R.id.text_view_content, "setTextSize", 22);
			remoteViews.setTextViewText(R.id.text_view_content, "There is no card to review.");
			remoteViews.setTextViewText(R.id.text_view_content_part, "");
			remoteViews.setViewVisibility(R.id.image_view_go_other_side, View.GONE);
			remoteViews.setViewVisibility(R.id.button_remember, View.GONE);
			remoteViews.setViewVisibility(R.id.button_forget, View.GONE);
			remoteViews.setViewVisibility(R.id.text_view_remained_cards, View.GONE);
		}else
		{
			remoteViews.setViewVisibility(R.id.image_view_go_other_side, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.button_remember, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.button_forget, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.text_view_remained_cards, View.VISIBLE);
			remoteViews.setTextViewText(R.id.text_view_remained_cards, state.remainedCardsCount + " card(s) are remained to review.");
			BuiltinCard builtinCard = Helper.convertToBuitinCard(state.card);
			if(state.isFront)
			{
				remoteViews.setTextViewText(R.id.text_view_content_pronounce, builtinCard.getFrontPronounce());
				remoteViews.setFloat(R.id.text_view_content, "setTextSize", 25);
				remoteViews.setTextViewText(R.id.text_view_content, builtinCard.getFrontContent());
				remoteViews.setTextViewText(R.id.text_view_content_part, builtinCard.getFrontPart());
			}else
			{
				remoteViews.setTextViewText(R.id.text_view_content_pronounce, "");
	    		String enters = "";
	    		if(!builtinCard.getBackFa().trim().equals("") && !builtinCard.getBackEng().trim().equals(""))
	    			enters = "\n\n";
				SpannableString sp = new SpannableString(builtinCard.getBackFa() + enters + builtinCard.getBackEng());
	    		sp.setSpan(new RelativeSizeSpan(0.9f), 0, builtinCard.getBackFa().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    		remoteViews.setFloat(R.id.text_view_content, "setTextSize", 18);
	    		remoteViews.setTextViewText(R.id.text_view_content, sp);
				remoteViews.setTextViewText(R.id.text_view_content_part, "");
			}
		}
	}
	// ----------------------------------------------------------------
	private void setClicks(Context context, RemoteViews remoteViews, int appWidgetId)
	{
		State state = getState(context);
		if(state.boxCard.getId() > 0)
		{
			PendingIntent pendingIntentGoOtherSide = makeControlPendingIntent(context, "goto_other_side", appWidgetId);
			remoteViews.setOnClickPendingIntent(R.id.image_view_go_other_side, pendingIntentGoOtherSide);	
			
			PendingIntent pendingIntentRemember = makeControlPendingIntent(context, "remember", appWidgetId);
			remoteViews.setOnClickPendingIntent(R.id.button_remember, pendingIntentRemember);
			
			PendingIntent pendingIntentForget = makeControlPendingIntent(context, "forget", appWidgetId);
			remoteViews.setOnClickPendingIntent(R.id.button_forget, pendingIntentForget);
		}
	}
	// ----------------------------------------------------------------
	private PendingIntent makeControlPendingIntent (Context context, String command, int appWidgetId) {
	    Intent active = new Intent();
	    active.setAction(ACTION_WIDGET_CONTROL);
	    active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	    Uri data = Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/#" + command),
	       String.valueOf(appWidgetId));
	    active.setData(data);
	    return(PendingIntent.getBroadcast(context,
	       0, active, PendingIntent.FLAG_UPDATE_CURRENT));
	}
	// ----------------------------------------------------------------
	@Override
	public void onReceive(Context context, Intent intent)
	{
		final String action = intent.getAction();
		if(AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action))
		{
            final int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                this.onDeleted(context, new int[] { appWidgetId });
            }
		}
		else if(ACTION_WIDGET_CONTROL.equals(action))
		{
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                this.onHandleAction(context, appWidgetId, intent.getData());
            }
		} else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
			if (!URI_SCHEME.equals(intent.getScheme())) {
                final int[] appWidgetIds = intent.getExtras().getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                for (int appWidgetId : appWidgetIds)
                    setAlarm(context, appWidgetId, 5);
			}
			super.onReceive(context, intent);
		} else
		{
			super.onReceive(context, intent);
		}
	}
	// ----------------------------------------------------------------
	public void onHandleAction(Context context, int appWidgetId, Uri data) {
		String controlType = data.getFragment();
		if(controlType.toLowerCase(Locale.getDefault()).equals("goto_other_side"))
		{
			State state = getState(context);
			state.isFront = !state.isFront;
			setState(context, state);
		} else if(controlType.toLowerCase(Locale.getDefault()).equals("remember"))
		{
			State state = getState(context);
	    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
	    	long nowDate = calendar.getTime().getTime();
	    	long nextDate = nowDate + Helper.calculateDelayOfNextStep(state.boxCard.getStep());
			BoxCard boxCard = new BoxCard(state.boxCard.getId(), state.boxCard.getStep() + 1, state.card.isCustomCard(), state.boxCard.getCardId(), state.boxCard.getFirstEnterDate(), nowDate, nextDate);
			DatabaseManager.getSingleton(context).updateCardInBox(boxCard);
			state.isFront = true;
			setState(context, state);
		} else if(controlType.toLowerCase(Locale.getDefault()).equals("forget"))
		{
			State state = getState(context);
	    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
	    	long nowDate = calendar.getTime().getTime();
	    	long nextDate = nowDate + 86400000;
			BoxCard boxCard = new BoxCard(state.boxCard.getId(), 1, state.card.isCustomCard(), state.card.getId(), nowDate, nowDate, nextDate);
			DatabaseManager.getSingleton(context).updateCardInBox(boxCard);
			state.isFront = true;
			setState(context, state);
		}
		updateRemoteViews(context, appWidgetId);
	}
	// ----------------------------------------------------------------
	@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds)
            setAlarm(context, appWidgetId, -1);
        
        super.onDeleted(context, appWidgetIds);
    }
	// ----------------------------------------------------------------	
    private void setAlarm(Context context , int appWidgetId, int updateRateSeconds) {
    	
        Intent widgetUpdate = new Intent();
        widgetUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId });
        widgetUpdate.setData(Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/"), String.valueOf(appWidgetId)));
        PendingIntent newPending = PendingIntent.getBroadcast(context, 0, widgetUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        // schedule the updating
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (updateRateSeconds >= 0) {
            alarms.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), updateRateSeconds * 1000, newPending);
        } else {
            alarms.cancel(newPending);
        }
    }
}
