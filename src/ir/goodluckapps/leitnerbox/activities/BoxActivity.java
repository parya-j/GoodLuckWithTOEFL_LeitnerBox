package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.BoxCard;
import ir.goodluckapps.leitnerbox.CardPackage;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.widgets.BoxCardPackageArrayAdapter;

import java.util.Iterator;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BoxActivity extends Activity {

	private MediaPlayer _mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_box);
		setClicks();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		updateGUI();
	}
	
	private void setClicks()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
        
		Button buttonAddCards = (Button)findViewById(R.id.button_add_card);
		buttonAddCards.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mediaPlayer.start();
				Dialog selectPackageDialog = new Dialog(BoxActivity.this);
				selectPackageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				selectPackageDialog.setContentView(R.layout.dialog_packages);
				ListView listViewPackages = (ListView) selectPackageDialog.findViewById(R.id.List_view_packages);
				List<CardPackage> cardPacakgesList = DatabaseManager.getSingleton(BoxActivity.this).getAllAvailablePackages();
				CardPackage[] cardPackages = new CardPackage[cardPacakgesList.size()];
				cardPackages = cardPacakgesList.toArray(cardPackages);
				BoxCardPackageArrayAdapter cardPackageAdapter = new BoxCardPackageArrayAdapter(BoxActivity.this, cardPackages, selectPackageDialog);
				listViewPackages.setAdapter(cardPackageAdapter);
				selectPackageDialog.show();				
			}
		});
		
		Button review = (Button)findViewById(R.id.button_review);
		review.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mediaPlayer.start();
	    		if(DatabaseManager.getSingleton(BoxActivity.this).getTodayCardsCount() < 1)
	    		{
					final Dialog dialog = new Dialog(BoxActivity.this);
		            dialog.setContentView(R.layout.custom_dialog);
		            dialog.setTitle("Note");
		            ((TextView)dialog.findViewById(R.id.textView_term)).setText("There is no card to review today, please come back tomorrow.");
		            dialog.findViewById(R.id.Yes).setVisibility(View.GONE);
		            Button btn_no = (Button) dialog.findViewById(R.id.No);
		            btn_no.setText("Thanks");
		            
		            btn_no.setOnClickListener(new View.OnClickListener() {
		    	        public void onClick(View arg0) {
		    				dialog.dismiss();
		    	        }
		    	    });
		            dialog.show();
	    		}
	    		else
	    		{
					Intent reviewIntent = new Intent(BoxActivity.this, ReviewActivity.class);
					BoxActivity.this.startActivity(reviewIntent);
	    		}
			}
		});
	}
	
	private void updateGUI()
	{
		Integer[] cardsCount = new Integer[30];
		for(int i = 0; i < 30; i++)
			cardsCount[i] = 0;
    	LocalDate localDate = new LocalDate();
    	
		for(int step = 1; step <= 5; step++)
		{
			List<BoxCard> boxCards = DatabaseManager.getSingleton(this).getCards(step);
			Iterator<BoxCard> it_boxCard = boxCards.iterator();
			switch(step)
			{
				case 1:
				{
					cardsCount[0] = boxCards.size();
					break;
				}
				case 2:
				{
//					cardsCount[1] = 0; cardsCount[2] = 0;
					while(it_boxCard.hasNext())
					{
						BoxCard boxCard = it_boxCard.next();
						LocalDate reviewDate = Helper.getLocalDate(boxCard.getNextReviewDate());
						int delta = Days.daysBetween(localDate, reviewDate).getDays();
						if(delta <= 0)
						{
							cardsCount[2]++;
						}else
						{
							int room = 1 + Math.max(2 - delta, 0);
							cardsCount[room]++;
						}
					}
					break;
				}
				case 3:
				{
//					cardsCount[3] = 0; cardsCount[4] = 0; cardsCount[5] = 0; 
//					cardsCount[6] = 0; cardsCount[7] = 0;
					while(it_boxCard.hasNext())
					{
						BoxCard boxCard = it_boxCard.next();
						LocalDate reviewDate = Helper.getLocalDate(boxCard.getNextReviewDate());
						int delta = Days.daysBetween(localDate, reviewDate).getDays();
						if(delta <= 0)
						{
							cardsCount[7]++;
						}else
						{
							int room = 3 + Math.max(5 - delta, 0);
							cardsCount[room]++;
						}
					}
					break;
				}
				case 4:
				{
//					cardsCount[8] = 0; cardsCount[9] = 0; cardsCount[10] = 0;
//					cardsCount[11] = 0; cardsCount[12] = 0; cardsCount[13] = 0;
//					cardsCount[14] = 0; cardsCount[15] = 0;
					while(it_boxCard.hasNext())
					{
						BoxCard boxCard = it_boxCard.next();
						LocalDate reviewDate = Helper.getLocalDate(boxCard.getNextReviewDate());
						int delta = Days.daysBetween(localDate, reviewDate).getDays();
						if(delta <= 0)
						{
							cardsCount[15]++;
						}else
						{
							int room = 8 + Math.max(8 - delta, 0);
							cardsCount[room]++;
						}
					}
					break;
				}
				case 5:
				{
//					cardsCount[16] = 0; cardsCount[17] = 0; cardsCount[18] = 0;
//					cardsCount[19] = 0; cardsCount[20] = 0; cardsCount[21] = 0;
//					cardsCount[22] = 0; cardsCount[23] = 0; cardsCount[24] = 0;
//					cardsCount[25] = 0; cardsCount[26] = 0; cardsCount[27] = 0;
//					cardsCount[28] = 0; cardsCount[29] = 0;
					while(it_boxCard.hasNext())
					{
						BoxCard boxCard = it_boxCard.next();
						LocalDate reviewDate = Helper.getLocalDate(boxCard.getNextReviewDate());
						int delta = Days.daysBetween(localDate, reviewDate).getDays();
						if(delta <= 0)
						{
							cardsCount[29]++;
						}else
						{
							int room = 16 + Math.max(14 - delta, 0);
							cardsCount[room]++;
						}
					}
					break;
				}
			}
		}
		for(int i = 0; i < 30; i++)
		{
			String info = "Room " + (i + 1) + " - " + cardsCount[i] + " card(s)"; 
			((TextView)findViewById(_rooms[i])).setText(info);
		}
	}
	
    // references to the rooms
    private Integer[] _rooms = {
    		R.id.text_view_room_1, R.id.text_view_room_2,
    		R.id.text_view_room_3, R.id.text_view_room_4,
    		R.id.text_view_room_5, R.id.text_view_room_6,
    		R.id.text_view_room_7, R.id.text_view_room_8,
    		R.id.text_view_room_9, R.id.text_view_room_10,
    		R.id.text_view_room_11, R.id.text_view_room_12,
    		R.id.text_view_room_13, R.id.text_view_room_14,
    		R.id.text_view_room_15, R.id.text_view_room_16,
    		R.id.text_view_room_17, R.id.text_view_room_18,
    		R.id.text_view_room_19, R.id.text_view_room_20,
    		R.id.text_view_room_21, R.id.text_view_room_22,
    		R.id.text_view_room_23, R.id.text_view_room_24,
    		R.id.text_view_room_25, R.id.text_view_room_26,
    		R.id.text_view_room_27, R.id.text_view_room_28,
    		R.id.text_view_room_29, R.id.text_view_room_30
    };
}
