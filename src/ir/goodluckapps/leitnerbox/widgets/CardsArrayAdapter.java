package ir.goodluckapps.leitnerbox.widgets;

import java.util.List;

import ir.goodluckapps.leitnerbox.BuiltinCard;
import ir.goodluckapps.leitnerbox.Card;
import ir.goodluckapps.leitnerbox.Helper;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.activities.PreviewCardsActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CardsArrayAdapter extends ArrayAdapter<Card>{
	
	static class ViewHolder
	{
		TextView cardFront;
	}	
	
	private final Context _context;
	
	public CardsArrayAdapter(Context context)
	{
		super(context, R.layout.element_custom_card);
		_context = context;
	}
	
	public CardsArrayAdapter(Context context, List<Card> values)
	{
		super(context, R.layout.element_custom_card, values);
		_context = context;
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
			rowView = inflater.inflate(R.layout.element_custom_card, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.cardFront = (TextView)rowView.findViewById(R.id.text_view_front);
			rowView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		BuiltinCard builtinCard = Helper.convertToBuitinCard(card);
		String front = builtinCard.getFrontContent();
		if(front.length() > 15)
			front = front.substring(0, 15) + "...";
		viewHolder.cardFront.setText(front);
		viewHolder.cardFront.setTag(card);

		OnClickListnerExtended onClickListnerExtended = new OnClickListnerExtended();
		onClickListnerExtended.setIndex(position);
		viewHolder.cardFront.setOnClickListener(onClickListnerExtended);

		return rowView;
	}
	
	private class OnClickListnerExtended implements View.OnClickListener {
		private int _index = 0;
		public void setIndex(int index)
		{
			_index = index;
		}
		
		@Override
		public void onClick(View arg0) {
			Card card = (Card)arg0.getTag();
			if(card != null)
			{
				Intent nextIntent = new Intent(_context, PreviewCardsActivity.class);
				nextIntent.putExtra("Front", card.getFront());
				nextIntent.putExtra("Back", card.getBack());
				nextIntent.putExtra("CardId", card.getId());
				nextIntent.putExtra("PackageId", card.getPackageId());
				nextIntent.putExtra("selectedItem", _index);
				_context.startActivity(nextIntent);
			}
		}
	}
}
