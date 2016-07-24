package ir.goodluckapps.leitnerbox;

public class CardPackage {
	private boolean _isCustomPackage;
	private int _id;
	private String _name;
	private int _cardsCount;
	
	public CardPackage()
	{
		this._isCustomPackage = true;
		this._id = 0;
		this._name = "";
		this._cardsCount = 0;
	}
	
	public CardPackage(boolean isCustomPackage, int id, String name, int cardsCount)
	{
		this._isCustomPackage = isCustomPackage;
		this._id = id;
		this._name = name;
		this._cardsCount = cardsCount;
	}
	
	public void setCustomPackage(boolean isCustomPackage)
	{
		this._isCustomPackage = isCustomPackage;
	}
	
	public boolean isCustomPackage()
	{
		return this._isCustomPackage;
	}
	
	public void setId(int id)
	{
		this._id = id;
	}
	
	public int getId()
	{
		return this._id;
	}
	
	public void setName(String name)
	{
		this._name = name;
	}
	
	public String getName()
	{
		return this._name;
	}
	
	public void setCardsCount(int cardsCount)
	{
		this._cardsCount = cardsCount;
	}
	
	public int getCardsCount()
	{
		return this._cardsCount;
	}
}
