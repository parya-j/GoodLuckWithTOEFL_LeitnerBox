package ir.goodluckapps.leitnerbox.widgets;

import ir.goodluckapps.leitnerbox.CardPackage;
import ir.goodluckapps.leitnerbox.R;
import ir.goodluckapps.leitnerbox.activities.AddToBoxActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BoxCardPackageArrayAdapter extends ArrayAdapter<CardPackage>{
	
	static class ViewHolder
	{
		TextView cardsCount;
		TextView packageName;
		CardPackage cPackage;
	}	
	
	private final Context _context;
	private final Typeface _type_andika;
	private final Dialog _dialog;
	public BoxCardPackageArrayAdapter(Context context, Dialog dialog)
	{
		super(context, R.layout.element_custom_card_package);
		_context = context;
		_type_andika = Typeface.createFromAsset(_context.getAssets(),"fonts/MTCORSVA.TTF");
		_dialog = dialog;
	}
	
	public BoxCardPackageArrayAdapter(Context context, CardPackage[] values, Dialog dialog)
	{
		super(context, R.layout.element_custom_card_package, values);
		_context = context;
		_type_andika = Typeface.createFromAsset(_context.getAssets(),"fonts/MTCORSVA.TTF");
		_dialog = dialog;
	}
	 
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		ViewHolder viewHolder;
		CardPackage cardPackage = getItem(position);
		
		if(rowView == null)
		{
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.element_custom_card_package, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.packageName = (TextView)rowView.findViewById(R.id.text_view_packageName);
			viewHolder.cardsCount = (TextView)rowView.findViewById(R.id.text_view_cardsCount);
			viewHolder.cPackage = cardPackage;
			rowView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		String minimizeName = cardPackage.getName();
		if(minimizeName.length() > 25)
			minimizeName = minimizeName.substring(0, 22) + " ...";
		viewHolder.packageName.setText(minimizeName);
		viewHolder.packageName.setTypeface(_type_andika);
		viewHolder.packageName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		viewHolder.cardsCount.setText(String.valueOf(cardPackage.getCardsCount()) + " Cards");
		viewHolder.cardsCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		
		rowView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewHolder holder = (ViewHolder)v.getTag();
				Intent nextIntent = new Intent(_context, AddToBoxActivity.class);
				nextIntent.putExtra("isCustomPackage", holder.cPackage.isCustomPackage());
				nextIntent.putExtra("packageId", holder.cPackage.getId());
				nextIntent.putExtra("packageName", holder.cPackage.getName());
				_context.startActivity(nextIntent);
				_dialog.dismiss();
			}
		});
		
		return rowView;
	}
}
