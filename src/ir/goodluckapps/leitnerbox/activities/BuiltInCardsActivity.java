package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.widgets.CardsArrayAdapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class BuiltInCardsActivity extends Activity {

	private int _packageId;
	private String _packageName;
	private int _cardsCount;
	private DatabaseManager _databaseManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_builtincards);
		_databaseManager = DatabaseManager.getSingleton(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	_packageName = extras.getString("PackageName");
            _packageId = extras.getInt("PackageId");
        }
		setStyle();
//		setClicks();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
        	setStyle();
        }
    }//onActivityResult
    
	private void setStyle()
	{
		TextView TV_CardsCount = (TextView)findViewById(R.id.text_view_cards_count);
		_cardsCount = _databaseManager.getAllBuiltInCardsCount(_packageId);
		TV_CardsCount.setText("This package includes " + _cardsCount + " cards.");
		
		TextView TV_title = (TextView)findViewById(R.id.title_text);
		TV_title.setText(_packageName);
		
		ListView LV_CustomCarts = (ListView)findViewById(R.id.List_view_cards);
		List<Card> cards = _databaseManager.getAllBuiltInCards(_packageId);
		CardsArrayAdapter cardsAdapter = new CardsArrayAdapter(BuiltInCardsActivity.this, cards);
		LV_CustomCarts.setAdapter(cardsAdapter);
	}
}
