package ir.goodluckapps.leitnerbox;

public class Card {
	private boolean _isCustomCard;
	private int _id;
	private int _packageId;
	private String _front;
	private String _back;
	
	public Card()
	{
		this._isCustomCard = true;
		this._id = 0;
		this._packageId = 0;
		this._front = "Front Side";
		this._back = "Back Side";
	}
	
	public Card(boolean isCustomCard, int id, int packageId, String front, String back)
	{
		this._isCustomCard = isCustomCard;
		this._id = id;
		this._packageId = packageId;
		this._front = front;
		this._back = back;
	}
	
	public void setCustomCard(boolean isCustomCard)
	{
		this._isCustomCard = isCustomCard;
	}
	
	public boolean isCustomCard()
	{
		return this._isCustomCard;
	}
	
	public void setId(int id)
	{
		this._id = id;
	}
	
	public int getId()
	{
		return this._id;
	}
	
	public void setPackageId(int packageId)
	{
		this._packageId = packageId;
	}
	
	public int getPackageId()
	{
		return this._packageId;
	}
	
	public void setFront(String front)
	{
		this._front = front;
	}
	
	public String getFront()
	{
		return this._front;
	}
	
	public void setBack(String back)
	{
		this._back = back;
	}
	
	public String getBack()
	{
		return this._back;
	}
}
