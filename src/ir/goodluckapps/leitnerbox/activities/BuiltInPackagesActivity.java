package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.CardPackage;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuiltInPackagesActivity extends Activity {

	private MediaPlayer _mediaPlayer;
	private DatabaseManager _databaseManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bultinpackages);
		_databaseManager = DatabaseManager.getSingleton(this);
		setStyle();
		setClicks();
	}
	
	private void setClicks()
	{
		RelativeLayout RL_Cards_504 = (RelativeLayout)findViewById(R.id.RL_Cards_504);
		CardPackage cardPackage_504 = _databaseManager.getBuiltInCardPackage(2);
		RL_Cards_504.setTag(cardPackage_504);
		RL_Cards_504.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent nextIntent = new Intent(BuiltInPackagesActivity.this, BuiltInCardsActivity.class);
					CardPackage cardPackage = (CardPackage)arg0.getTag();
					if(cardPackage != null)
					{
						nextIntent.putExtra("PackageName", cardPackage.getName());
						nextIntent.putExtra("PackageId", cardPackage.getId());
						BuiltInPackagesActivity.this.startActivityForResult(nextIntent, 1);
						_mediaPlayer.start();
					}
				}
			});
		    RelativeLayout RL_Cards_GoodLuck_One = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_One);
			RL_Cards_GoodLuck_One.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!DatabaseManager.getSingleton(BuiltInPackagesActivity.this).isPackagePremium(3))
					{
				        final Dialog dialog = new Dialog(BuiltInPackagesActivity.this);
				        dialog.setContentView(R.layout.custom_dialog);
				        dialog.setTitle("Premium package");
				        ((TextView)dialog.findViewById(R.id.textView_term)).setText("Would you like to buy this package?");
				        Button btn_yes = (Button) dialog.findViewById(R.id.Yes);
				        btn_yes.setText("Sure");
				        Button btn_no = (Button) dialog.findViewById(R.id.No);
				        btn_no.setText("No");
	
				        btn_yes.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View arg0) {
								Intent nextIntent = new Intent(BuiltInPackagesActivity.this, PremiumActivity.class);
								BuiltInPackagesActivity.this.startActivity(nextIntent);
					        	dialog.dismiss();
					        }
					    }); 
				        btn_no.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View arg0) {
					        	dialog.dismiss();
					        }
					    });
				        dialog.show();
					}else
					{
						CardPackage cardPackage = _databaseManager.getBuiltInCardPackage(3);
						Intent nextIntent = new Intent(BuiltInPackagesActivity.this, BuiltInCardsActivity.class);
						if(cardPackage != null)
						{
							nextIntent.putExtra("PackageName", cardPackage.getName());
							nextIntent.putExtra("PackageId", cardPackage.getId());
							BuiltInPackagesActivity.this.startActivityForResult(nextIntent, 1);
							_mediaPlayer.start();
						}
					}
				}
			});
			
		    RelativeLayout RL_Cards_GoodLuck_Two = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_two);
			RL_Cards_GoodLuck_Two.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!DatabaseManager.getSingleton(BuiltInPackagesActivity.this).isPackagePremium(4))
					{
				        final Dialog dialog = new Dialog(BuiltInPackagesActivity.this);
				        dialog.setContentView(R.layout.custom_dialog);
				        dialog.setTitle("Premium package");
				        ((TextView)dialog.findViewById(R.id.textView_term)).setText("Would you like to buy this package?");
				        Button btn_yes = (Button) dialog.findViewById(R.id.Yes);
				        btn_yes.setText("Sure");
				        Button btn_no = (Button) dialog.findViewById(R.id.No);
				        btn_no.setText("No");
	
				        btn_yes.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View arg0) {
								Intent nextIntent = new Intent(BuiltInPackagesActivity.this, PremiumActivity.class);
								BuiltInPackagesActivity.this.startActivity(nextIntent);
					        	dialog.dismiss();
					        }
					    }); 
				        btn_no.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View arg0) {
					        	dialog.dismiss();
					        }
					    });
				        dialog.show();
					}else
					{
						CardPackage cardPackage = _databaseManager.getBuiltInCardPackage(4);
						Intent nextIntent = new Intent(BuiltInPackagesActivity.this, BuiltInCardsActivity.class);
						if(cardPackage != null)
						{
							nextIntent.putExtra("PackageName", cardPackage.getName());
							nextIntent.putExtra("PackageId", cardPackage.getId());
							BuiltInPackagesActivity.this.startActivityForResult(nextIntent, 2);
							_mediaPlayer.start();
						}
					}
				}
			});

		    RelativeLayout RL_Cards_GoodLuck_Three = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_three);
		    RL_Cards_GoodLuck_Three.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!DatabaseManager.getSingleton(BuiltInPackagesActivity.this).isPackagePremium(5))
					{
				        final Dialog dialog = new Dialog(BuiltInPackagesActivity.this);
				        dialog.setContentView(R.layout.custom_dialog);
				        dialog.setTitle("Premium package");
				        ((TextView)dialog.findViewById(R.id.textView_term)).setText("Would you like to buy this package?");
				        Button btn_yes = (Button) dialog.findViewById(R.id.Yes);
				        btn_yes.setText("Sure");
				        Button btn_no = (Button) dialog.findViewById(R.id.No);
				        btn_no.setText("No");
	
				        btn_yes.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View arg0) {
								Intent nextIntent = new Intent(BuiltInPackagesActivity.this, PremiumActivity.class);
								BuiltInPackagesActivity.this.startActivity(nextIntent);
					        	dialog.dismiss();
					        }
					    }); 
				        btn_no.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View arg0) {
					        	dialog.dismiss();
					        }
					    });
				        dialog.show();
					}else
					{
						CardPackage cardPackage = _databaseManager.getBuiltInCardPackage(5);
						Intent nextIntent = new Intent(BuiltInPackagesActivity.this, BuiltInCardsActivity.class);
						if(cardPackage != null)
						{
							nextIntent.putExtra("PackageName", cardPackage.getName());
							nextIntent.putExtra("PackageId", cardPackage.getId());
							BuiltInPackagesActivity.this.startActivityForResult(nextIntent, 3);
							_mediaPlayer.start();
						}
					}
				}
			});
	}
	
	private void setStyle()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		
		TextView TV_Cards_504 = (TextView)findViewById(R.id.text_view_cards_504);
		TV_Cards_504.setTypeface(type_andika);
		
		TextView TV_Cards_GoodLuck_Level_One = (TextView)findViewById(R.id.text_view_cards_goodluck_level_one);
		TV_Cards_GoodLuck_Level_One.setTypeface(type_andika);

		TextView TV_Cards_GoodLuck_Level_Two = (TextView)findViewById(R.id.text_view_cards_goodluck_level_two);
		TV_Cards_GoodLuck_Level_Two.setTypeface(type_andika);
		
		TextView TV_Cards_GoodLuck_Level_Three = (TextView)findViewById(R.id.text_view_cards_goodluck_level_three);
		TV_Cards_GoodLuck_Level_Three.setTypeface(type_andika);
	}

}
