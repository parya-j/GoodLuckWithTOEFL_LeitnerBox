package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditCardActivity extends Activity {

	private MediaPlayer _mediaPlayer;
	private int _cardId;
	private boolean _isCustomPackage = true;
	private Card _card;
	private DatabaseManager _databaseManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_card);
		_databaseManager = DatabaseManager.getSingleton(this);
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			_isCustomPackage = extras.getBoolean("isCustomPackage");
			_cardId = extras.getInt("CardId");
		}
		if(_isCustomPackage)
			_card = _databaseManager.getCustomCard(_cardId);
		else
			_card = _databaseManager.getBuiltInCard(_cardId);
		
		setStyle();
		setClicks();
	}
	
	private void setClicks()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
        Button button_preview = (Button)findViewById(R.id.button_review);
        button_preview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				Intent nextIntent = new Intent(EditCardActivity.this, PreviewCardActivity.class);
				EditText input_frontContent = (EditText)findViewById(R.id.input_card_front);
				String frontContent = input_frontContent.getText().toString().trim();
				EditText input_backContent = (EditText)findViewById(R.id.input_card_back);
				String backContent = input_backContent.getText().toString().trim();
				nextIntent.putExtra("Front", frontContent);
				nextIntent.putExtra("Back", backContent);
				nextIntent.putExtra("CardId", _cardId);
				EditCardActivity.this.startActivity(nextIntent);
			}
		});
		Button button_edit = (Button)findViewById(R.id.button_edit_card);
		button_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				EditText input_frontContent = (EditText)findViewById(R.id.input_card_front);
				String frontContent = input_frontContent.getText().toString().trim();
				EditText input_backContent = (EditText)findViewById(R.id.input_card_back);
				String backContent = input_backContent.getText().toString().trim();
				if(frontContent.equals("") || backContent.equals(""))
				{
					TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
					TV_Error.setText("Please fill the front and back contents.");
					TV_Error.setVisibility(View.VISIBLE);
				}else
				{
					_card.setFront(frontContent);
					_card.setBack(backContent);
					if(_isCustomPackage)
					{
						if(_databaseManager.updateCustomCard(_card))
						{
							EditCardActivity.this.setResult(RESULT_CANCELED);
							EditCardActivity.this.finish();
						}
						else
						{
							TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
							TV_Error.setText("There is a problem in creating the new card. Please try again.");
							TV_Error.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});
	}
	
	private void setStyle()
	{
		EditText ET_front = (EditText)findViewById(R.id.input_card_front);
		ET_front.setText(_card.getFront());

		EditText ET_back = (EditText)findViewById(R.id.input_card_back);
		ET_back.setText(_card.getBack());
		
		TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
		TV_Error.setVisibility(View.INVISIBLE);
	}

}
