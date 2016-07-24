package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.CardPackage;
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

public class CustomPackagesActivity extends Activity {

	private DatabaseManager _databaseManager;
	private MediaPlayer _mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_custompackages);
		_databaseManager = DatabaseManager.getSingleton(this);
		
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
		RelativeLayout RL_Add_Package = (RelativeLayout)findViewById(R.id.RL_Add_Package);
		RL_Add_Package.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent nextIntent = new Intent(CustomPackagesActivity.this, AddPackageActivity.class);
				CustomPackagesActivity.this.startActivityForResult(nextIntent, 1);
				_mediaPlayer.start();
			}
		});
	}
	
	private void setStyle()
	{
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		TextView TV_Cards_add = (TextView)findViewById(R.id.text_view_add);
		TV_Cards_add.setTypeface(type_andika);
		
		List<CardPackage> cardPackages = _databaseManager.getCustomCardPackages();
		int cardPackagesCount = cardPackages.size();
		LinearLayout LL_CustomPackages = (LinearLayout)findViewById(R.id.LL_custom_packages);
		LL_CustomPackages.removeAllViews();
		for(int index = 0; index < cardPackagesCount; index++)
		{
			View child = getLayoutInflater().inflate(R.layout.element_custom_card_package, LL_CustomPackages, false);
			TextView TV_PackageName = (TextView)child.findViewById(R.id.text_view_packageName);
			TV_PackageName.setText(cardPackages.get(index).getName());
			TV_PackageName.setTypeface(type_andika);
			TextView TV_CardsCount = (TextView)child.findViewById(R.id.text_view_cardsCount);
			TV_CardsCount.setText(cardPackages.get(index).getCardsCount() + " Cards");
			TV_CardsCount.setTypeface(type_andika);
			RelativeLayout RL_Cards = (RelativeLayout)child.findViewById(R.id.RL_Cards);
			RL_Cards.setTag(cardPackages.get(index));
			RL_Cards.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent nextIntent = new Intent(CustomPackagesActivity.this, CustomCardsActivity.class);
					CardPackage cardPackage = (CardPackage)arg0.getTag();
					if(cardPackage != null)
					{
						nextIntent.putExtra("PackageName", cardPackage.getName());
						nextIntent.putExtra("PackageId", cardPackage.getId());
						CustomPackagesActivity.this.startActivityForResult(nextIntent, 1);
						_mediaPlayer.start();
					}
				}
			});
			LL_CustomPackages.addView(child);
		}
	}

}
