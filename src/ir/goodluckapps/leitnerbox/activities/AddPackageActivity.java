package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.CardPackage;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddPackageActivity extends Activity {

	private MediaPlayer _mediaPlayer;
	private DatabaseManager _databaseManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_package);
		_databaseManager = DatabaseManager.getSingleton(this);
		
		setStyle();
		setClicks();
	}
	
	private void setClicks()
	{
        int res_id = this.getResources().getIdentifier("button_click", "raw", this.getPackageName());
        _mediaPlayer = MediaPlayer.create(this, res_id);
        Button Btn_Add_Package = (Button)findViewById(R.id.button_add_package);
        Btn_Add_Package.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				_mediaPlayer.start();
				EditText input_packageName = (EditText)findViewById(R.id.input_package_name);
				String package_name = input_packageName.getText().toString().trim();
				if(package_name.equals(""))
				{
					TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
					TV_Error.setText("Please enter a name for the new package.");
					TV_Error.setVisibility(View.VISIBLE);
				}else
				{
					CardPackage cardPackage = new CardPackage(true, 0, package_name, 0);
					if(_databaseManager.addCustomCardPackage(cardPackage))
					{
						AddPackageActivity.this.setResult(RESULT_CANCELED);
						AddPackageActivity.this.finish();
					}
					else
					{
						TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
						TV_Error.setText("There is a package with this name, plaese choose another name.");
						TV_Error.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}
	
	private void setStyle()
	{
		TextView TV_Error = (TextView)findViewById(R.id.text_view_error);
		TV_Error.setText("There is a package with this name, plaese choose another name.");
		TV_Error.setVisibility(View.INVISIBLE);
	}

}
