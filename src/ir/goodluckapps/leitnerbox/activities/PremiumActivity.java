package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.AsyncHTTPConnection;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.util.IabHelper;
import ir.goodluckapps.util.Inventory;
import ir.goodluckapps.util.IabResult;
import ir.goodluckapps.util.Purchase;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PremiumActivity extends Activity{
	
	// Debug tag, for logging
	static final String TAG = "LeitnerBox";

	// SKUs for our products: the premium upgrade (non-consumable)
	static final String SKU_PREMIUM_504 = "504";
	static final String SKU_PREMIUM_GoodLuck_Level_One = "GoodLuck_Words_Level_1";
	static final String SKU_PREMIUM_GoodLuck_Level_Two = "GoodLuck_Words_Level_2";
	static final String SKU_PREMIUM_GoodLuck_Level_Three = "GoodLuck_Words_Level_3";
	
	// Does the user have the premium upgrade?
	boolean mIsPremium_504 = false;
	boolean mIsPremium_GoodLuck_One = false;
	boolean mIsPremium_GoodLuck_Two = false;
	boolean mIsPremium_GoodLuck_Three = false;
	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 1394;

	// The helper object
	IabHelper mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_premium);
		
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		TextView TV_Cards_504 = (TextView)findViewById(R.id.text_view_cards_504);
		TV_Cards_504.setTypeface(type_andika);
		
		TextView TV_Cards_GoodLuck_Level_One = (TextView)findViewById(R.id.text_view_cards_goodluck_level_one);
		TV_Cards_GoodLuck_Level_One.setTypeface(type_andika);

		TextView TV_Cards_GoodLuck_Level_Two = (TextView)findViewById(R.id.text_view_cards_goodluck_level_two);
		TV_Cards_GoodLuck_Level_Two.setTypeface(type_andika);
		
		TextView TV_Cards_GoodLuck_Level_Three = (TextView)findViewById(R.id.text_view_cards_goodluck_level_three);
		TV_Cards_GoodLuck_Level_Three.setTypeface(type_andika);
		
		RelativeLayout RL_Cards_504 = (RelativeLayout)findViewById(R.id.RL_Cards_504);
		RL_Cards_504.setVisibility(View.GONE);
		View progressBar_504 = RL_Cards_504.findViewById(R.id.progressBar);
		progressBar_504.setVisibility(View.GONE);
		
		RelativeLayout RL_Cards_GoodLuck_Level_One = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_One);
		RL_Cards_GoodLuck_Level_One.setVisibility(View.GONE);
		View progressBar_goodluck_level_one = RL_Cards_GoodLuck_Level_One.findViewById(R.id.progressBar);
		progressBar_goodluck_level_one.setVisibility(View.GONE);
		
		RelativeLayout RL_Cards_GoodLuck_Level_Two = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_two);
		RL_Cards_GoodLuck_Level_Two.setVisibility(View.GONE);
		View progressBar_goodluck_level_two = RL_Cards_GoodLuck_Level_Two.findViewById(R.id.progressBar);
		progressBar_goodluck_level_two.setVisibility(View.GONE);
		
		RelativeLayout RL_Cards_GoodLuck_Level_Three = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_three);
		RL_Cards_GoodLuck_Level_Three.setVisibility(View.GONE);
		View progressBar_goodluck_level_three = RL_Cards_GoodLuck_Level_Three.findViewById(R.id.progressBar);
		progressBar_goodluck_level_three.setVisibility(View.GONE);
				
		String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDQbJUZ71TGQD/PGWf6Yru2ewjH1UWjc7sc/lufECk4hm5aNpGbR7XR7mxzf/Epmv4+TtulvggK2HcjYVLhdsacvdPZbcK7VEUm7OjaUNHkYGwNeojTL1EM13mk4YnKov3ppFce2wntYVt4LbpzV5jCuTVrFfs90Q8JnR5pGGIy35adomvWC6LduYmimUv/6DEkahFeN+i4K9CpZ7agpaQMV1R92pN4VwIqxTSGl6ECAwEAAQ==";
		// You can find it in your Bazaar console, in the Dealers section. 
		// It is recommended to add more security than just pasting it in your source code;
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
		    public void onIabSetupFinished(IabResult result) {
		        Log.d(TAG, "Setup finished.");

		        if (!result.isSuccess()) {
		            // Oh noes, there was a problem.
		            Log.d(TAG, "Problem setting up In-app Billing: " + result);
		        }
		        
		        // Hooray, IAB is fully set up!
		        mHelper.queryInventoryAsync(mGotInventoryListener);
		    }
		});
	}
	
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                ((TextView)PremiumActivity.this.findViewById(R.id.text_view_wait)).setText("Failed to load data. Please make sure the last version of Bazaar is installed and check the network connection.");
                return;
            }
            else {
                Log.d(TAG, "Query inventory was successful.");
                // does the user have the premium upgrade?
                PremiumActivity.this.findViewById(R.id.text_view_wait).setVisibility(View.GONE);
                
                mIsPremium_504 = inventory.hasPurchase(SKU_PREMIUM_504);
                RelativeLayout RL_Cards_504 = (RelativeLayout)findViewById(R.id.RL_Cards_504);
                RL_Cards_504.setVisibility(View.VISIBLE);
                if(mIsPremium_504)
                {
                	((TextView)RL_Cards_504.findViewById(R.id.text_view_packageName)).setText("Purchased");
                }
                final boolean isDownloaded_504 = DatabaseManager.getSingleton(PremiumActivity.this).isPackagePremium(2); // 2 => 504
                if(isDownloaded_504)
    	            ((TextView)RL_Cards_504.findViewById(R.id.text_view_packageName)).setText("Downloaded");
                else
                {
					if(AsyncHTTPConnection.isRunning() && AsyncHTTPConnection.getPackageName().equals(SKU_PREMIUM_504))
						AsyncHTTPConnection.setViews(RL_Cards_504);
    	            RL_Cards_504.setOnClickListener(new View.OnClickListener() {
    					@Override
    					public void onClick(View v) {
    						if(AsyncHTTPConnection.isRunning())
    						{
    							Toast.makeText(PremiumActivity.this, "Another package is downloading. Please wait...", Toast.LENGTH_LONG).show();
    						}else if(!isDownloaded_504)
    							mHelper.launchPurchaseFlow(PremiumActivity.this, SKU_PREMIUM_504, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
    					}
    				});
                }
                
                // --------------------------
                mIsPremium_GoodLuck_One = inventory.hasPurchase(SKU_PREMIUM_GoodLuck_Level_One);
                RelativeLayout RL_Cards_GoodLuck_Level_One = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_One);
                RL_Cards_GoodLuck_Level_One.setVisibility(View.VISIBLE);
                if(mIsPremium_GoodLuck_One)
                {
                	((TextView)RL_Cards_GoodLuck_Level_One.findViewById(R.id.text_view_packageName)).setText("Purchased");
                }
                final boolean isDownloaded_goodLuck_level_one = DatabaseManager.getSingleton(PremiumActivity.this).isPackagePremium(3); // 3 => goodluck - level 1
                if(isDownloaded_goodLuck_level_one)
    	            ((TextView)RL_Cards_GoodLuck_Level_One.findViewById(R.id.text_view_packageName)).setText("Downloaded");
                else
                {
					if(AsyncHTTPConnection.getPackageName().equals(SKU_PREMIUM_GoodLuck_Level_One))
						AsyncHTTPConnection.setViews(RL_Cards_GoodLuck_Level_One);
                	RL_Cards_GoodLuck_Level_One.setOnClickListener(new View.OnClickListener() {
    					@Override
    					public void onClick(View v) {
    						if(AsyncHTTPConnection.isRunning())
    						{
    							Toast.makeText(PremiumActivity.this, "Another package is downloading. Please wait...", Toast.LENGTH_LONG).show();
    						}else if(!isDownloaded_goodLuck_level_one)
    							mHelper.launchPurchaseFlow(PremiumActivity.this, SKU_PREMIUM_GoodLuck_Level_One, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
    					}
    				});
                }
                // --------------------------
                mIsPremium_GoodLuck_Two = inventory.hasPurchase(SKU_PREMIUM_GoodLuck_Level_Two);
                RelativeLayout RL_Cards_GoodLuck_Level_Two = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_two);
                RL_Cards_GoodLuck_Level_Two.setVisibility(View.VISIBLE);
                if(mIsPremium_GoodLuck_Two)
                {
                	((TextView)RL_Cards_GoodLuck_Level_Two.findViewById(R.id.text_view_packageName)).setText("Purchased");
                }
                final boolean isDownloaded_goodLuck_level_two = DatabaseManager.getSingleton(PremiumActivity.this).isPackagePremium(4); // 4 => goodluck - level 2
                if(isDownloaded_goodLuck_level_two)
    	            ((TextView)RL_Cards_GoodLuck_Level_Two.findViewById(R.id.text_view_packageName)).setText("Downloaded");
                else
                {
					if(AsyncHTTPConnection.getPackageName().equals(SKU_PREMIUM_GoodLuck_Level_Two))
						AsyncHTTPConnection.setViews(RL_Cards_GoodLuck_Level_Two);
                	RL_Cards_GoodLuck_Level_Two.setOnClickListener(new View.OnClickListener() {
    					@Override
    					public void onClick(View v) {
    						if(AsyncHTTPConnection.isRunning())
    						{
    							Toast.makeText(PremiumActivity.this, "Another package is downloading. Please wait...", Toast.LENGTH_LONG).show();
    						}else if(!isDownloaded_goodLuck_level_two)
    							mHelper.launchPurchaseFlow(PremiumActivity.this, SKU_PREMIUM_GoodLuck_Level_Two, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
    					}
    				});
                }
                // --------------------------
                mIsPremium_GoodLuck_Three = inventory.hasPurchase(SKU_PREMIUM_GoodLuck_Level_Three);
                RelativeLayout RL_Cards_GoodLuck_Level_Three = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_three);
                RL_Cards_GoodLuck_Level_Three.setVisibility(View.VISIBLE);
                if(mIsPremium_GoodLuck_Three)
                {
                	((TextView)RL_Cards_GoodLuck_Level_Three.findViewById(R.id.text_view_packageName)).setText("Purchased");
                }
                final boolean isDownloaded_goodLuck_level_three = DatabaseManager.getSingleton(PremiumActivity.this).isPackagePremium(5); // 4 => goodluck - level 2
                if(isDownloaded_goodLuck_level_three)
    	            ((TextView)RL_Cards_GoodLuck_Level_Three.findViewById(R.id.text_view_packageName)).setText("Downloaded");
                else
                {
					if(AsyncHTTPConnection.getPackageName().equals(SKU_PREMIUM_GoodLuck_Level_Three))
						AsyncHTTPConnection.setViews(RL_Cards_GoodLuck_Level_Three);
                	RL_Cards_GoodLuck_Level_Three.setOnClickListener(new View.OnClickListener() {
    					@Override
    					public void onClick(View v) {
    						if(AsyncHTTPConnection.isRunning())
    						{
    							Toast.makeText(PremiumActivity.this, "Another package is downloading. Please wait...", Toast.LENGTH_LONG).show();
    						}else if(!isDownloaded_goodLuck_level_three)
    							mHelper.launchPurchaseFlow(PremiumActivity.this, SKU_PREMIUM_GoodLuck_Level_Three, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
    					}
    				});
                }
                // --------------------------   
            }
        }
    };
    
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                return;
            }
            else if (purchase.getSku().equals(SKU_PREMIUM_504)) {
            	Log.d(TAG, "Congradulation 504:)" );
        		RelativeLayout RL_Cards_504 = (RelativeLayout)findViewById(R.id.RL_Cards_504);
        		RL_Cards_504.setClickable(false);
            	Helper.upgradeTo(PremiumActivity.this.getApplicationContext(), SKU_PREMIUM_504, RL_Cards_504, 504 - 60);
            }else if (purchase.getSku().equals(SKU_PREMIUM_GoodLuck_Level_One))
            {
            	Log.d(TAG, "Congradulation goodluck - level 1:)" );
        		RelativeLayout RL_Cards_GoodLuck_Level_One = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_One);
        		RL_Cards_GoodLuck_Level_One.setClickable(false);
            	Helper.upgradeTo(PremiumActivity.this.getApplicationContext(), SKU_PREMIUM_GoodLuck_Level_One, RL_Cards_GoodLuck_Level_One, 558);
            }else if (purchase.getSku().equals(SKU_PREMIUM_GoodLuck_Level_Two))
            {
            	Log.d(TAG, "Congradulation goodluck - level 2:)" );
        		RelativeLayout RL_Cards_GoodLuck_Level_Two = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_two);
        		RL_Cards_GoodLuck_Level_Two.setClickable(false);
            	Helper.upgradeTo(PremiumActivity.this.getApplicationContext(), SKU_PREMIUM_GoodLuck_Level_Two, RL_Cards_GoodLuck_Level_Two, 480);
            }else if (purchase.getSku().equals(SKU_PREMIUM_GoodLuck_Level_Three))
            {
            	Log.d(TAG, "Congradulation goodluck - level 3:)" );
        		RelativeLayout RL_Cards_GoodLuck_Level_Three = (RelativeLayout)findViewById(R.id.RL_Cards_GoodLuck_Level_three);
        		RL_Cards_GoodLuck_Level_Three.setClickable(false);
            	Helper.upgradeTo(PremiumActivity.this.getApplicationContext(), SKU_PREMIUM_GoodLuck_Level_Three, RL_Cards_GoodLuck_Level_Three, 409);
            }
        }
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
}
