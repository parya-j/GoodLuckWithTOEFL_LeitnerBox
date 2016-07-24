package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private MediaPlayer _mediaPlayer;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setStyle();
		setClicks();
		Helper.setAlarm(this);
		
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Feedback");
        ((TextView)dialog.findViewById(R.id.textView_term)).setText("Would you like to leave any feedback?");
        Button btn_yes = (Button) dialog.findViewById(R.id.Yes);
        btn_yes.setText("Sure");
        Button btn_no = (Button) dialog.findViewById(R.id.No);
        btn_no.setText("No");

        btn_yes.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir/app/ir.goodluckapps.leitnerbox"));
	        	startActivity(browserIntent);
	        	dialog.dismiss();
	        	MainActivity.this.virtualOnBackProcess();
	        }
	    }); 
        btn_no.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	dialog.dismiss();
	        	MainActivity.this.virtualOnBackProcess();
	        }
	    });
	}
	
	private void setClicks()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
        
		RelativeLayout RL_Cards = (RelativeLayout)findViewById(R.id.RL_Cards);
		RL_Cards.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				Intent nextIntent = new Intent(MainActivity.this, CardsActivity.class);
				MainActivity.this.startActivity(nextIntent);
			}
		});
		
		RelativeLayout RL_Box = (RelativeLayout)findViewById(R.id.RL_Boxes);
		RL_Box.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_mediaPlayer.start();
				Intent nextIntent = new Intent(MainActivity.this, BoxActivity.class);
				MainActivity.this.startActivity(nextIntent);
			}
		});
	
		RelativeLayout RL_Premium = (RelativeLayout)findViewById(R.id.RL_Premium);
		RL_Premium.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mediaPlayer.start();
				Intent nextIntent = new Intent(MainActivity.this, PremiumActivity.class);
				MainActivity.this.startActivity(nextIntent);				
			}
		});
		
		RelativeLayout RL_About = (RelativeLayout)findViewById(R.id.RL_About);
		RL_About.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_mediaPlayer.start();
				Intent nextIntent = new Intent(MainActivity.this, AboutActivity.class);
				MainActivity.this.startActivity(nextIntent);				
			}
		});
	
		View LL_Share = findViewById(R.id.share);
        LL_Share.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	_mediaPlayer.start();
	        	Intent intent = new Intent(Intent.ACTION_SEND);
	        	intent.setType("text/plain");
	        	intent.putExtra(Intent.EXTRA_TEXT, "I'm using \"Good Luck with TOEFL - Leitner Box\" and I strongly suggest you to try it. \n\nOfficial website:\nwww.goodluckapps.ir \n\nCafe Bazar:\nwww.cafebazaar.ir/app/ir.goodluckapps.leitnerbox");
	        	startActivity(Intent.createChooser(intent, "Share with"));
	        }
	    });		
	}
	
	private void setStyle()
	{
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		TextView TV_Boxes = (TextView)findViewById(R.id.text_view_boxes);
		TextView TV_Cards = (TextView)findViewById(R.id.text_view_cards);
		TextView TV_Settings = (TextView)findViewById(R.id.text_view_settings);
		TextView TV_About = (TextView)findViewById(R.id.text_view_about);
		TV_Boxes.setTypeface(type_andika);
		TV_Cards.setTypeface(type_andika);
		TV_Settings.setTypeface(type_andika);
		TV_About.setTypeface(type_andika);
	}

	@Override
	public void onBackPressed()
	{
		dialog.show();
	}
	public void virtualOnBackProcess()
	{
		super.onBackPressed();
	}
}
