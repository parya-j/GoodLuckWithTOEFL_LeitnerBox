package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.BoxCard;
import ir.goodluckapps.leitnerbox.BuiltinCard;
import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ReviewActivity extends Activity {
	private boolean _isFront = true;
	private Iterator<BoxCard> _it_todayBoxCards;
	private Iterator<Card> _it_todayCards;
	private Card _currentCard;
	private BoxCard _currentBoxCard;
	private TextView _TV_pronounce;
	private TextView _TV_Content;
	private TextView _TV_part;
	private TextView _TV_Side;
	private TextView _TV_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_review);
		
		List<Card> cards = new ArrayList<Card>();
		List<BoxCard> boxCards = new ArrayList<BoxCard>();
		DatabaseManager.getSingleton(this).getTodayCards(boxCards, cards);
		_it_todayBoxCards = boxCards.iterator();
		_it_todayCards = cards.iterator();
		
		if(!_it_todayBoxCards.hasNext() || !_it_todayCards.hasNext())
		{	
			this.onBackPressed();
            return;
		}
		
		_currentBoxCard = _it_todayBoxCards.next();
		_currentCard = _it_todayCards.next();
		_isFront = true;
		
		setBaseStyle();
		setupGUI();
		updateGUI();
		setClicks();
	}
	
	private void setClicks()
	{
		View button_remember = findViewById(R.id.button_remember);
		View button_forget = findViewById(R.id.button_forget);
		View image_view_go_other_side = findViewById(R.id.image_view_go_other_side);
		
		image_view_go_other_side.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_isFront = !_isFront;
				ReviewActivity.this.updateGUI();
			}
		});
		button_remember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
		    	long nowDate = calendar.getTime().getTime();
		    	long nextDate = nowDate + Helper.calculateDelayOfNextStep(_currentBoxCard.getStep());
				BoxCard boxCard = new BoxCard(_currentBoxCard.getId(), _currentBoxCard.getStep() + 1, _currentCard.isCustomCard(), _currentBoxCard.getCardId(), _currentBoxCard.getFirstEnterDate(), nowDate, nextDate);
				DatabaseManager.getSingleton(ReviewActivity.this).updateCardInBox(boxCard);
				if(_it_todayBoxCards.hasNext() && _it_todayCards.hasNext())
				{
					_currentCard = _it_todayCards.next();
					_currentBoxCard = _it_todayBoxCards.next();
					_isFront = true;
					ReviewActivity.this.updateGUI();	
				}else
				{
					final Dialog dialog = new Dialog(ReviewActivity.this);
		            dialog.setContentView(R.layout.custom_dialog);
		            dialog.setTitle("Note");
		            ((TextView)dialog.findViewById(R.id.textView_term)).setText("There is no more card to review today, please come back tomorrow.");
		            dialog.findViewById(R.id.Yes).setVisibility(View.GONE);
		            Button btn_no = (Button) dialog.findViewById(R.id.No);
		            btn_no.setText("Thanks");
		            
		            btn_no.setOnClickListener(new View.OnClickListener() {
		    	        public void onClick(View arg0) {
		    				dialog.dismiss();
		    				ReviewActivity.this.onBackPressed();
		    	        }
		    	    });
		            dialog.show();					
				}
			}
		});
		
		button_forget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
		    	long nowDate = calendar.getTime().getTime();
		    	long nextDate = nowDate + 86400000;
				BoxCard boxCard = new BoxCard(_currentBoxCard.getId(), 1, _currentCard.isCustomCard(), _currentCard.getId(), nowDate, nowDate, nextDate);
				DatabaseManager.getSingleton(ReviewActivity.this).updateCardInBox(boxCard);
				if(_it_todayBoxCards.hasNext() && _it_todayCards.hasNext())
				{
					_currentCard = _it_todayCards.next();
					_currentBoxCard = _it_todayBoxCards.next();
					_isFront = true;
					ReviewActivity.this.updateGUI();	
				}
				else
				{
					final Dialog dialog = new Dialog(ReviewActivity.this);
		            dialog.setContentView(R.layout.custom_dialog);
		            dialog.setTitle("Note");
		            ((TextView)dialog.findViewById(R.id.textView_term)).setText("There is no more card to review today, please come back tomorrow.");
		            dialog.findViewById(R.id.Yes).setVisibility(View.GONE);
		            Button btn_no = (Button) dialog.findViewById(R.id.No);
		            btn_no.setText("Thanks");
		            
		            btn_no.setOnClickListener(new View.OnClickListener() {
		    	        public void onClick(View arg0) {
		    				dialog.dismiss();
		    				ReviewActivity.this.onBackPressed();
		    	        }
		    	    });
		            dialog.show();
				}
			}
		});
	}
	private void setBaseStyle()
	{
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		TextView TV_Side = (TextView)findViewById(R.id.text_view_side);
		TV_Side.setTypeface(type_andika);
	}

	private void setupGUI()
	{
		_TV_pronounce = (TextView)findViewById(R.id.text_view_content_pronounce);
		_TV_Content = (TextView)findViewById(R.id.text_view_content);
		_TV_Content.setMovementMethod(new ScrollingMovementMethod());
		_TV_part = (TextView)findViewById(R.id.text_view_content_part);
		_TV_Side = (TextView)findViewById(R.id.text_view_side);
		_TV_title = (TextView)findViewById(R.id.title_text);
	}
	private void updateGUI()
	{
		BuiltinCard builtinCard = Helper.convertToBuitinCard(_currentCard);
		int remainedCardsCount = DatabaseManager.getSingleton(this).getTodayCardsCount();
		_TV_title.setText(remainedCardsCount + " card(s) are remained to review.");
		if(_isFront)
		{
    		_TV_pronounce.setText(builtinCard.getFrontPronounce());
    		_TV_Content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    		_TV_Content.setText(builtinCard.getFrontContent());
    		_TV_part.setText(builtinCard.getFrontPart());
    		_TV_Content.scrollTo(0, 0);
			_TV_Side.setText("Front Side");
		}else
		{
    		_TV_pronounce.setText("");
    		String enters = "";
    		if(!builtinCard.getBackFa().trim().equals("") && !builtinCard.getBackEng().trim().equals(""))
    			enters = "\n\n";
    		SpannableString sp = new SpannableString(builtinCard.getBackFa() + enters + builtinCard.getBackEng());
    		sp.setSpan(new RelativeSizeSpan(0.9f), 0, builtinCard.getBackFa().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		_TV_Content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    		_TV_Content.setText(sp);
    		_TV_part.setText("");
			_TV_Side.setText("Back Side");
		}
	}	
}
