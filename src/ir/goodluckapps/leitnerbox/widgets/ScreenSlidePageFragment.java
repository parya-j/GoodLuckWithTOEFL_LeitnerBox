package ir.goodluckapps.leitnerbox.widgets;

import ir.goodluckapps.leitnerbox.BuiltinCard;
import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.activities.PreviewCardsActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {
	private String _front;
	private String _back;
	private BuiltinCard _builtinCard;
	private TextView _TV_pronounce;
	private TextView _TV_Content;
	private TextView _TV_part;
	private ViewGroup _rootView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        _rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        Bundle args = getArguments();
        _TV_pronounce = (TextView)_rootView.findViewById(R.id.text_view_content_pronounce);
        _TV_Content = (TextView)_rootView.findViewById(R.id.text_view_content);
        _TV_Content.setMovementMethod(new ScrollingMovementMethod());
        _TV_part = (TextView)_rootView.findViewById(R.id.text_view_content_part);

        if(args != null)
        {
        	_front = args.getString("Front");
        	_back = args.getString("Back");
        	Card card = new Card();
        	card.setFront(_front);
        	card.setBack(_back);
        	card.setCustomCard(false);
        	_builtinCard = Helper.convertToBuitinCard(card);
        	refreshSide();
        }
        return _rootView;
    }
    
    public void refreshSide()
    {
    	if(_TV_pronounce == null)
    		_TV_pronounce = (TextView)getView().findViewById(R.id.text_view_content_pronounce);
    	if(_TV_Content == null)
    	{
    		_TV_Content = (TextView)getView().findViewById(R.id.text_view_content);
    		_TV_Content.setMovementMethod(new ScrollingMovementMethod());
    	}
    	if(_TV_part == null)
    		_TV_part = (TextView)getView().findViewById(R.id.text_view_content_part);

    	if(PreviewCardsActivity.isFront)
    	{
    		_TV_pronounce.setText(_builtinCard.getFrontPronounce());
    		_TV_Content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    		_TV_Content.setText(_builtinCard.getFrontContent());
    		_TV_part.setText(_builtinCard.getFrontPart());
    		_TV_Content.scrollTo(0, 0);
    	}
    	else
    	{
    		_TV_pronounce.setText("");
    		String enters = "";
    		if(!_builtinCard.getBackEng().trim().equals("") && !_builtinCard.getBackFa().trim().equals(""))
    			enters = "\n\n";
    		SpannableString sp = new SpannableString(_builtinCard.getBackFa() + enters + _builtinCard.getBackEng());
    		sp.setSpan(new RelativeSizeSpan(0.9f), 0, _builtinCard.getBackFa().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		_TV_Content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    		_TV_Content.setText(sp);
    		_TV_part.setText("");
    	}
    }
}
