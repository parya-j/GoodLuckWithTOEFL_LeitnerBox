package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.widgets.TickableCardsArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AddToBoxActivity extends Activity {

	private boolean _isCustomPackage = true;
	private int _packageId;
	private String _packageName;
	private List<Card> _cards;
	private TickableCardsArrayAdapter _adaptor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_to_box);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			_isCustomPackage = extras.getBoolean("isCustomPackage");
			_packageId = extras.getInt("packageId");
			_packageName = extras.getString("packageName");
			
			// Pay attention we should ignore the cards which are currently in the box.
			if(_isCustomPackage)
				_cards = DatabaseManager.getSingleton(this).getCustomCardsOutBox(_packageId);
			else
				_cards = DatabaseManager.getSingleton(this).getBuiltInCardsOutBox(_packageId);
		}
		
		if(_cards == null)
			_cards = new ArrayList<Card>();
		
		setStyle();
		setClicks();
	}
	
	private void setClicks()
	{
		TextView buttonAddToBox = (TextView)findViewById(R.id.button_add_to_box);
		buttonAddToBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final List<Card> selectedCards = _adaptor.getSelectedCards();
        		final Dialog dialog = new Dialog(AddToBoxActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Note");
                ((TextView)dialog.findViewById(R.id.textView_term)).setText("You are going to add " + selectedCards.size() + " new cards. Are you sure?");
                Button btn_yes = (Button) dialog.findViewById(R.id.Yes);
                Button btn_no = (Button) dialog.findViewById(R.id.No);
                
                btn_no.setOnClickListener(new View.OnClickListener() {
        	        public void onClick(View arg0) {
        	        	dialog.dismiss();
        	        }
        	    });
                
                btn_yes.setOnClickListener(new View.OnClickListener() {
        	        public void onClick(View arg0) {
        	        	dialog.dismiss();
        	        	DatabaseManager.getSingleton(AddToBoxActivity.this).addCardsToBox(selectedCards.iterator());
        	        	AddToBoxActivity.this.onBackPressed();
        	        }
        	    });
                
                dialog.show();
			}
		});
	}
	
	private void setStyle()
	{
		TextView title_text = (TextView)findViewById(R.id.title_text);
		title_text.setText(_packageName);
		Card[] cards = new Card[_cards.size()];
		cards = _cards.toArray(cards);
		_adaptor = new TickableCardsArrayAdapter(this, cards);
		
		ListView listViewCards = (ListView)findViewById(R.id.List_view_cards);
		listViewCards.setAdapter(_adaptor);
	}

}
