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

public class AddCardActivity extends Activity {

	private MediaPlayer _mediaPlayer;
	private DatabaseManager _databaseManager;
	private int _packageId;
	private String _packageName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_card);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	_packageName = extras.getString("PackageName");
            _packageId = extras.getInt("PackageId");
        }
		_databaseManager = DatabaseManager.getSingleton(this);
		
		setStyle();
		setClicks();
	}
	
	private void setClicks()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
        Button Btn_Add_Card = (Button)findViewById(R.id.button_add_card);
        Btn_Add_Card.setOnClickListener(new View.OnClickListener() {
			
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
					Card card = new Card(true, 0, _packageId, frontContent, backContent);
					if(_databaseManager.addCustomCard(card))
					{
						AddCardActivity.this.setResult(RESULT_CANCELED);
						AddCardActivity.this.finish();
					}
					else
					{
						TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
						TV_Error.setText("There is a problem in creating the new card. Please try again.");
						TV_Error.setVisibility(View.VISIBLE);
					}
				}
			}
		});
        
        Button Btn_Preview_Card = (Button)findViewById(R.id.button_review);
        Btn_Preview_Card.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				Intent nextIntent = new Intent(AddCardActivity.this, PreviewCardActivity.class);
				EditText input_frontContent = (EditText)findViewById(R.id.input_card_front);
				String frontContent = input_frontContent.getText().toString().trim();
				EditText input_backContent = (EditText)findViewById(R.id.input_card_back);
				String backContent = input_backContent.getText().toString().trim();
				nextIntent.putExtra("Front", frontContent);
				nextIntent.putExtra("Back", backContent);
				nextIntent.putExtra("CardId", 0);
				AddCardActivity.this.startActivityForResult(nextIntent, 1);
			}
		});
	}
	
	private void setStyle()
	{
		TextView TT_Title = (TextView)findViewById(R.id.title_text);
		TT_Title.setText("Add a new card to " + _packageName);
		TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
		TV_Error.setText("Please fill the front and back contents.");
		TV_Error.setVisibility(View.INVISIBLE);
	}

}
