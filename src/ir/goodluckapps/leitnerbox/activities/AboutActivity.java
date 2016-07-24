package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class AboutActivity extends Activity {
	   @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        //Remove title bar
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_about);
	        final MediaPlayer _media_player;
	        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
	        _media_player = MediaPlayer.create(this, res_id);
	        LinearLayout LL_Share = (LinearLayout)findViewById(R.id.share);
	        ImageView IV_Logo = (ImageView) findViewById(R.id.logo);
	        IV_Logo.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
		        	_media_player.start();
		        	Intent intent = new Intent(Intent.ACTION_SEND);
		        	intent.setType("text/plain");
		        	intent.putExtra(Intent.EXTRA_TEXT, "I'm using \"Good Luck with TOEFL - Leitner Box\" and I strongly suggest you to try it. \n\nOfficial website:\nwww.goodluckapps.ir \n\nCafe Bazar:\nwww.cafebazaar.ir/app/ir.goodluckapps.leitnerbox");
		        	startActivity(Intent.createChooser(intent, "Share with"));
		        }
		    }); 
	        LL_Share.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
		        	_media_player.start();
		        	Intent intent = new Intent(Intent.ACTION_SEND);
		        	intent.setType("text/plain");
		        	intent.putExtra(Intent.EXTRA_TEXT, "I'm using \"Good Luck with TOEFL - Leitner Box\" and I strongly suggest you to try it. \n\nOfficial website:\nwww.goodluckapps.ir \n\nCafe Bazar:\nwww.cafebazaar.ir/app/ir.goodluckapps.leitnerbox");
		        	startActivity(Intent.createChooser(intent, "Share with"));
		        }
		    });
	        
	        Button button_website = (Button)findViewById(R.id.button_website);
	        button_website.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
		        	_media_player.start();
		        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goodluckapps.ir"));
		        	startActivity(browserIntent);
		        }
		    });
	        
	        Button button_bazar = (Button)findViewById(R.id.button_bazar);
	        button_bazar.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
		        	_media_player.start();
		        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir/app/ir.goodluckapps.leitnerbox"));
		        	startActivity(browserIntent);
		        }
		    });
	        
	        Button button_thanks = (Button)findViewById(R.id.button_thanks);
	        button_thanks.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View arg0) {
		        	_media_player.start();		        	
		        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/goodluckapps"));
		        	startActivity(browserIntent);
		        }
		    });
	    }
}
