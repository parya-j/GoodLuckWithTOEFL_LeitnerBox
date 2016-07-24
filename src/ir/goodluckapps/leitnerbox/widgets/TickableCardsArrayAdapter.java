package ir.goodluckapps.leitnerbox.widgets;

import java.util.ArrayList;
import java.util.List;

import ir.goodluckapps.leitnerbox.BuiltinCard;
import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TickableCardsArrayAdapter extends ArrayAdapter<Card>{
	
	static class ViewHolder
	{
		TextView cardFront;
		CheckBox checkBox;
	}	
	
	private final Context _context;
	private List<Card> _selectedCards;
	
	public TickableCardsArrayAdapter(Context context)
	{
		super(context, R.layout.element_tickable_word);
		_context = context;
		_selectedCards = new ArrayList<Card>();
	}
	
	public TickableCardsArrayAdapter(Context context, Card[] values)
	{
		super(context, R.layout.element_tickable_word, values);
		_context = context;
		_selectedCards = new ArrayList<Card>();
	}
	 
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		final ViewHolder viewHolder;
		Card card = getItem(position);
		
		if(rowView == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.element_tickable_word, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.cardFront = (TextView)rowView.findViewById(R.id.text_view_front);
			viewHolder.checkBox = (CheckBox)rowView.findViewById(R.id.check_box_word);
			rowView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		BuiltinCard builtinCard = Helper.convertToBuitinCard(card);
		String minimizeName = builtinCard.getFrontContent();
		if(minimizeName.length() > 25)
			minimizeName = minimizeName.substring(0, 22) + " ...";
		viewHolder.cardFront.setText(minimizeName);
		
		viewHolder.checkBox.setTag(card);
		if(_selectedCards.contains(card))
			viewHolder.checkBox.setChecked(true);
		else
			viewHolder.checkBox.setChecked(false);
		
		viewHolder.cardFront.setOnClickListener(new OnClickListenerCardFront(viewHolder.checkBox));
		
		viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListenerCheckBox(viewHolder.checkBox));
		
		return rowView;
	}
	
	public List<Card> getSelectedCards()
	{
		return _selectedCards;
	}
	
	private class OnClickListenerCardFront implements View.OnClickListener {
		private CheckBox _checkBox;
		
		public OnClickListenerCardFront(CheckBox checkBox)
		{
			_checkBox = checkBox;
		}
		
		@Override
		public void onClick(View v) {
			_checkBox.setChecked(!_checkBox.isChecked());
		}
	}; 
	
	private class OnCheckedChangeListenerCheckBox implements OnCheckedChangeListener
	{
		private CheckBox _checkBox;
		
		public OnCheckedChangeListenerCheckBox(CheckBox checkBox)
		{
			_checkBox = checkBox;
		}
		
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	Card card = (Card)_checkBox.getTag();
        	if (isChecked) {
                if (!_selectedCards.contains(card))
                	_selectedCards.add(card);
            }else{
            	_selectedCards.remove(card);
            }

        }
	}
}
