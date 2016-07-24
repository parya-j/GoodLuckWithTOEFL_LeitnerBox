package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomCardsActivity extends Activity {

	private int _packageId;
	private String _packageName;
	private int _cardsCount;
	
	private MediaPlayer _mediaPlayer;
	private DatabaseManager _databaseManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_customcards);
		_databaseManager = DatabaseManager.getSingleton(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	_packageName = extras.getString("PackageName");
            _packageId = extras.getInt("PackageId");
        }
		setStyle();
		setClicks();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
        	setStyle();
        }
    }//onActivityResult
    
	private void setClicks()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
		RelativeLayout RL_Add_Card = (RelativeLayout)findViewById(R.id.RL_Add_Card);
		RL_Add_Card.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent nextIntent = new Intent(CustomCardsActivity.this, AddCardActivity.class);
				nextIntent.putExtra("PackageName", _packageName);
				nextIntent.putExtra("PackageId", _packageId);
				CustomCardsActivity.this.startActivityForResult(nextIntent, 1);
				_mediaPlayer.start();
			}
		});
	}
	
	private void setStyle()
	{
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		TextView TV_Cards_add = (TextView)findViewById(R.id.text_view_add);
		TV_Cards_add.setTypeface(type_andika);
		TextView TV_title = (TextView)findViewById(R.id.title_text);
		_cardsCount = _databaseManager.getCustomCardsCount(_packageId);
		TV_title.setText(_packageName + " - " + _cardsCount + " Cards");
		
		LinearLayout LL_CustomCarts = (LinearLayout)findViewById(R.id.LL_custom_cards);
		LL_CustomCarts.removeAllViews();
		List<Card> cards = _databaseManager.getCustomCards(_packageId);
		for(int index = 0; index < _cardsCount; index++)
		{
			View child = getLayoutInflater().inflate(R.layout.element_custom_card, LL_CustomCarts, false);
			TextView TV_Front = (TextView)child.findViewById(R.id.text_view_front);
			String front = cards.get(index).getFront();
			if(front.length() > 15)
				front = front.substring(0, 15) + "...";
			TV_Front.setText(front);
//			TV_Front.setTypeface(type_andika);
			TV_Front.setTag(cards.get(index));
			TV_Front.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent nextIntent = new Intent(CustomCardsActivity.this, EditCardActivity.class);
					Card card = (Card)arg0.getTag();
					if(card != null)
					{
						nextIntent.putExtra("isCustomPackage", true);
						nextIntent.putExtra("CardId", card.getId());
						CustomCardsActivity.this.startActivityForResult(nextIntent, 1);
						_mediaPlayer.start();
					}
				}
			});
			LL_CustomCarts.addView(child);
		}
	}

}
