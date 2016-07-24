package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.R;
import android.R.color;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;


public class CardsActivity extends TabActivity {
	
	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cards);
		setStyle();
	}
	
	private void setStyle()
	{
		tabHost = getTabHost();
		
        View view_tab_bultInCards = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView label_bultInCards = (TextView) view_tab_bultInCards.findViewById(R.id.lbTabTitle);
        label_bultInCards.setText("Built In Cards");
        Intent bultInCardsIntent = new Intent(this, BuiltInPackagesActivity.class);
        tabHost.addTab(tabHost.newTabSpec("tab_bultInCards").setIndicator(view_tab_bultInCards).setContent(bultInCardsIntent));

        View view_tab_CustomCards = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        TextView label_CustomCards = (TextView) view_tab_CustomCards.findViewById(R.id.lbTabTitle);
        label_CustomCards.setText("Custom Cards");
        Intent customCardsIntent = new Intent(this, CustomPackagesActivity.class);
        tabHost.addTab(tabHost.newTabSpec("tab_customCards").setIndicator(view_tab_CustomCards).setContent(customCardsIntent));
        
		View tabView = tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab());
		View LinearLayout_tab = tabView.findViewById(R.id.LinearLayout_tab);
		LinearLayout_tab.setBackgroundColor(Color.parseColor("#0b78a8"));
		TextView title_view = (TextView) LinearLayout_tab.findViewById(R.id.lbTabTitle);
		title_view.setTextColor(Color.WHITE);	
		
		
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {				
					
					for(int ind = 0; ind < tabHost.getTabWidget().getTabCount(); ind++)
					{
						
						View tabView = tabHost.getTabWidget().getChildTabViewAt(ind);
						View LinearLayout_tab = tabView.findViewById(R.id.LinearLayout_tab);
						LinearLayout_tab.setBackgroundColor(color.white);
						TextView title_view = (TextView) LinearLayout_tab.findViewById(R.id.lbTabTitle);
						title_view.setTextColor(Color.parseColor("#151515"));
					}
					View tabView = tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab());
					View LinearLayout_tab = tabView.findViewById(R.id.LinearLayout_tab);
					LinearLayout_tab.setBackgroundColor(Color.parseColor("#0b78a8"));
					TextView title_view = (TextView) LinearLayout_tab.findViewById(R.id.lbTabTitle);
					title_view.setTextColor(Color.WHITE);
			}
		});
	}

}
