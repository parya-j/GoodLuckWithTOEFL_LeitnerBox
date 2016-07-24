package ir.goodluckapps.leitnerbox.activities;

import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.DatabaseManager;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.widgets.ScreenSlidePageFragment;

import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PreviewCardsActivity extends FragmentActivity  {

	private TextView _text_view_side;
    private ViewPager _Pager;
    private PagerAdapter _PagerAdapter;
    private int _packageId;
    private int _cardsCount;
    public static boolean isFront = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_preview_cards);
        _Pager = (ViewPager) findViewById(R.id.pager);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
        	this.onBackPressed();
        	return;
        }
        	
        _packageId = extras.getInt("PackageId");
        int selectedItem = extras.getInt("selectedItem");
        _cardsCount = DatabaseManager.getSingleton(this).getAllBuiltInCardsCount(_packageId);
        List<Card> cards = DatabaseManager.getSingleton(this).getAllBuiltInCards(_packageId);
        _PagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), cards, _cardsCount);
        _Pager.setAdapter(_PagerAdapter);
        _Pager.setCurrentItem(selectedItem);
        _text_view_side = (TextView)findViewById(R.id.text_view_side); 
		Typeface type_andika = Typeface.createFromAsset(getAssets(),"fonts/MTCORSVA.TTF");
		_text_view_side.setTypeface(type_andika);
        Button button_gotoOtherSide = (Button)findViewById(R.id.button_next_side);
        button_gotoOtherSide.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((ScreenSlidePagerAdapter) _PagerAdapter).gotoOtherSide(_Pager.getCurrentItem(), _text_view_side);
			}
		});
        
        Button button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PreviewCardsActivity.this.onBackPressed();
			}
		});
        
        _Pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				ScreenSlidePageFragment fragment = (ScreenSlidePageFragment) _PagerAdapter.instantiateItem(_Pager, position);
	        	if(fragment == null)
	        		return;
	        	fragment.refreshSide();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
    }

    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    	private int _pageCounts;
    	private List<Card> _cards;
    	private ScreenSlidePageFragment _currentFragment;
    	public ScreenSlidePagerAdapter(FragmentManager fm, List<Card> cards, int pageCounts) {
            super(fm);
            _pageCounts = pageCounts;
            _cards = cards;
            isFront = true;
        }
        
        @Override
        public Fragment getItem(int position) {
            Card card = _cards.get(position);
            ScreenSlidePageFragment SSPF = new ScreenSlidePageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Front", card.getFront());
            bundle.putString("Back", card.getBack());
            SSPF.setArguments(bundle);
        	return SSPF;
        }
        
        public void gotoOtherSide(int position, TextView text_view_side)
        {
        	_currentFragment = (ScreenSlidePageFragment) this.instantiateItem(_Pager, position);
        	
        	if(_currentFragment == null)
        		return;
        	
        	isFront = !isFront;
        	_currentFragment.refreshSide();
        	if(isFront)
        		text_view_side.setText("Front");
        	else
        		text_view_side.setText("Back");
        }

        @Override
        public int getCount() {
            return _pageCounts;
        }
    }
}