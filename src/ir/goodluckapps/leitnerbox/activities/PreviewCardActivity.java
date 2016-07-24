package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.R;
import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PreviewCardActivity extends Activity {
	private String _front;
	private String _back;
	private boolean _isFront = true;
	private MediaPlayer _mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_preview_card);
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			_front = extras.getString("Front");
			_back = extras.getString("Back");
		}
		setBaseStyle();
		setStyle();
		setClicks();
	}
	
	private void setClicks()
	{
		Button button_goNextSide = (Button)findViewById(R.id.button_next_side);
		button_goNextSide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				_isFront = !_isFront;
				setStyle();
			}
		});
		
		Button button_edit = (Button)findViewById(R.id.button_edit);
		button_edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				PreviewCardActivity.this.onBackPressed();
			}
		});
	}
	private void setBaseStyle()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		TextView TV_Side = (TextView)findViewById(R.id.text_view_side);
		TV_Side.setTypeface(type_andika);
	}
	private void setStyle()
	{
		TextView TV_Content = (TextView)findViewById(R.id.text_view_content);
		TextView TV_Side = (TextView)findViewById(R.id.text_view_side);
		if(_isFront)
		{
			TV_Content.setText(_front);
			TV_Side.setText("Front Side");
		}else
		{
			TV_Content.setText(_back);
			TV_Side.setText("Back Side");
		}
	}

}
