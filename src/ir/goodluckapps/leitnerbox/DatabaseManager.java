package ir.goodluckapps.leitnerbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseManager extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "db_leitnerbox.sqlite";
    private static final int DATABASE_VERSION = 1;
    //----------------------------------------------------------------
    private static final String TABLE_BUILTIN_CARD_PACKAGES = "cards_builtin_packages";
    private static final String TABLE_CUSTOM_CARD_PACKAGES = "cards_custom_packages";
    private static final String TABLE_CUSTOM_CARDS = "custom_cards";
    private static final String TABLE_BUILTIN_CARDS = "builtin_cards";
    private static final String TABLE_BOX_CARDS = "box_cards";
	//----------------------------------------------------------------
    private static DatabaseManager _databaseManager;
    public static DatabaseManager getSingleton(Context context)
    {
    	if(_databaseManager == null)
    		_databaseManager = new DatabaseManager(context);
    	return _databaseManager;
    }
    //----------------------------------------------------------------
    public DatabaseManager(Context context)
    {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public List<BoxCard> getCards(int step)
    {
    	String query_getTodayCards = "SELECT * FROM box_cards WHERE step = " + step;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getTodayCards, null);
    	List<BoxCard> boxCards = new ArrayList<BoxCard>();
    	if(cursor == null || cursor.getCount() == 0)
    		return boxCards;
    	
    	if(cursor.moveToFirst())
    	{
    		BoxCard boxCard;
    		do
    		{
    			boxCard = new BoxCard(cursor.getInt(0)
    					, cursor.getInt(1)
    					, (cursor.getInt(2) == 1 ? true : false)
    					, cursor.getInt(3)
    					, cursor.getLong(4)
    					, cursor.getLong(5)
    					, cursor.getLong(6));
    			
    			boxCards.add(boxCard);    			
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return boxCards;
    }
    //----------------------------------------------------------------
    public void getSingleBoxCard(BoxCard boxCard, Card card, long boxCardId)
    {
    	String query_getTodayCards = "SELECT * FROM box_cards JOIN custom_cards ON box_cards.card_id = custom_cards.id WHERE box_cards.id = " + boxCardId;
    	query_getTodayCards += " UNION SELECT box_cards.*, builtin_cards.id, builtin_cards.package_id, ('/'||builtin_cards.front_pronounce||'/'||'<'||builtin_cards.front_content||'>'||':'||builtin_cards.front_part||':') AS front, ('<'||builtin_cards.back_eng||'>'||':'||builtin_cards.back_fa||':') AS back FROM box_cards JOIN builtin_cards ON box_cards.card_id = builtin_cards.id WHERE box_cards.id = " + boxCardId;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getTodayCards, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return;
    	
    	if(cursor.moveToFirst())
    	{
			boxCard.setId(cursor.getInt(0));
			boxCard.setStep(cursor.getInt(1));
			boxCard.setCustomCard(cursor.getInt(2) == 1 ? true : false);
			boxCard.setCardId(cursor.getInt(3));
			boxCard.setFirstEnterDate(cursor.getLong(4));
			boxCard.setLastReviewDate(cursor.getLong(5));
			boxCard.setNextReviewDate(cursor.getLong(6));
			
			card.setCustomCard(cursor.getInt(2) == 1 ? true : false);
			card.setId(cursor.getInt(7));
			card.setPackageId(cursor.getInt(8));
			card.setFront(cursor.getString(9));
			card.setBack(cursor.getString(10));
    	}
    	cursor.close();    	
    }
    //----------------------------------------------------------------
    public void getSingleTodayCard(BoxCard boxCard, Card card)
    {
    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
    	Date lastOfDay = calendar.getTime();
    	lastOfDay.setHours(23);
    	lastOfDay.setMinutes(59);
    	lastOfDay.setSeconds(59);
    	
    	long last = lastOfDay.getTime();
    	
    	String query_getTodayCards = "SELECT * FROM box_cards JOIN custom_cards ON box_cards.card_id = custom_cards.id WHERE box_cards.step < 6 AND box_cards.is_custom_card = '1' AND box_cards.next_review_date <= " + last;
    	query_getTodayCards += " UNION SELECT box_cards.*, builtin_cards.id, builtin_cards.package_id, ('/'||builtin_cards.front_pronounce||'/'||'<'||builtin_cards.front_content||'>'||':'||builtin_cards.front_part||':') AS front, ('<'||builtin_cards.back_eng||'>'||'/'||builtin_cards.back_fa||'/') AS back FROM box_cards JOIN builtin_cards ON box_cards.card_id = builtin_cards.id WHERE box_cards.step < 6 AND box_cards.is_custom_card = '0' AND box_cards.next_review_date <= " + last + " LIMIT 1";
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getTodayCards, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return;
    	
    	if(cursor.moveToFirst())
    	{
    			boxCard.setId(cursor.getInt(0));
    			boxCard.setStep(cursor.getInt(1));
    			boxCard.setCustomCard(cursor.getInt(2) == 1 ? true : false);
    			boxCard.setCardId(cursor.getInt(3));
    			boxCard.setFirstEnterDate(cursor.getLong(4));
    			boxCard.setLastReviewDate(cursor.getLong(5));
    			boxCard.setNextReviewDate(cursor.getLong(6));
    			    			
    			card.setCustomCard(cursor.getInt(2) == 1 ? true : false);
    			card.setId(cursor.getInt(7));
    			card.setPackageId(cursor.getInt(8));
    			card.setFront(cursor.getString(9));
    			card.setBack(cursor.getString(10));
    	}
    	cursor.close();
    }
    //----------------------------------------------------------------
    public void getTodayCards(List<BoxCard> boxCards, List<Card> cards)
    {
    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
    	Date lastOfDay = calendar.getTime();
    	lastOfDay.setHours(23);
    	lastOfDay.setMinutes(59);
    	lastOfDay.setSeconds(59);
    	
    	long last = lastOfDay.getTime();
    	
    	String query_getTodayCards = "SELECT * FROM box_cards JOIN custom_cards ON box_cards.card_id = custom_cards.id WHERE box_cards.step < 6 AND box_cards.is_custom_card = '1' AND box_cards.next_review_date <= " + last;
    	query_getTodayCards += " UNION SELECT box_cards.*, builtin_cards.id, builtin_cards.package_id, ('/'||builtin_cards.front_pronounce||'/'||'<'||builtin_cards.front_content||'>'||':'||builtin_cards.front_part||':') AS front, ('<'||builtin_cards.back_eng||'>'||'/'||builtin_cards.back_fa||'/') AS back FROM box_cards JOIN builtin_cards ON box_cards.card_id = builtin_cards.id WHERE box_cards.step < 6 AND box_cards.is_custom_card = '0' AND box_cards.next_review_date <= " + last;
//    	query_getTodayCards += " UNION SELECT * FROM box_cards JOIN builtin_cards ON box_cards.card_id = builtin_cards.id WHERE box_cards.step < 6 AND box_cards.is_custom_card = '0' AND box_cards.next_review_date <= " + last;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getTodayCards, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return;
    	
    	if(cursor.moveToFirst())
    	{
    		BoxCard boxCard;
    		Card card;
    		do
    		{
    			boxCard = new BoxCard(cursor.getInt(0)
    					, cursor.getInt(1)
    					, (cursor.getInt(2) == 1 ? true : false)
    					, cursor.getInt(3)
    					, cursor.getLong(4)
    					, cursor.getLong(5)
    					, cursor.getLong(6));
    			
    			boxCards.add(boxCard);
    			
    			card = new Card((cursor.getInt(2) == 1 ? true : false)
    					, cursor.getInt(7)
    					, cursor.getInt(8)
    					, cursor.getString(9)
    					, cursor.getString(10));
    			
    			cards.add(card);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    }
    //----------------------------------------------------------------
    public int getTodayCardsCount()
    {
    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
    	Date lastOfDay = calendar.getTime();
    	lastOfDay.setHours(23);
    	lastOfDay.setMinutes(59);
    	lastOfDay.setSeconds(59);
    	
    	long last = lastOfDay.getTime();
    	
    	String query_getTodayCards = "SELECT count(id) FROM box_cards WHERE box_cards.step < 6 AND box_cards.next_review_date <= " + last;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getTodayCards, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return 0;
    	
    	if(cursor.moveToFirst())
    	{
    		int p = cursor.getInt(0);
    		cursor.close();
    		return p;
    	}
    	else
    	{
    		cursor.close();
    		return 0;
    	}
    }
    //----------------------------------------------------------------
    public void updateCardInBox(BoxCard boxCard)
    {
    	String query_updateCard = "UPDATE " + TABLE_BOX_CARDS + " SET 'step' = '" + boxCard.getStep() + "'" 
    			+ ", 'is_custom_card' = '" + (boxCard.isCustomCard() ? 1 : 0) + "'"
    			+ ", 'card_id' = '" + boxCard.getCardId() + "'"
    			+ ", 'first_enter_date' = '" + boxCard.getFirstEnterDate() + "'"
    			+ ", 'last_review_date' = '" + boxCard.getLastReviewDate() + "'"
    			+ ", 'next_review_date' = '" + boxCard.getNextReviewDate() + "'"
    			+ " WHERE id = '" + boxCard.getId() + "'";
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL(query_updateCard);
    }
    //----------------------------------------------------------------
    public void addCardsToBox(Iterator<Card> it_cards)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
    	long nowDate = calendar.getTime().getTime();
    	long nextDate = nowDate + 86400000; // plus One day
    	while(it_cards.hasNext())
    	{
    		Card card = it_cards.next();
    		ContentValues initialValues = new ContentValues();
    		initialValues.put("step", 1);
    		initialValues.put("is_custom_card", (card.isCustomCard() ? 1 : 0));
    		initialValues.put("card_id", card.getId());
    		initialValues.put("first_enter_date", nowDate);
    		initialValues.put("last_review_date", nowDate);
    		initialValues.put("next_review_date", nextDate); // next day
    		db.insert(TABLE_BOX_CARDS, null, initialValues);
    	}
    }
    //----------------------------------------------------------------
    public boolean isAvable_CustomCardPackageName(String name)
    {
    	String query_getCardPackage = "SELECT id FROM " + TABLE_CUSTOM_CARD_PACKAGES + " WHERE name = '" + name + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCardPackage, null);
    	boolean res = false;
    	if(cursor == null)
    		return false;
    	if(cursor.getCount() == 0)
    		res = true;
    	cursor.close();
    	return res;
    }
    public boolean addCustomCardPackage(CardPackage cardPackage)
    {
    	if(cardPackage != null && isAvable_CustomCardPackageName(cardPackage.getName()))
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		ContentValues initialValues = new ContentValues();
    		initialValues.put("name", cardPackage.getName());
    		initialValues.put("cards_count", cardPackage.getCardsCount());
    		db.insert(TABLE_CUSTOM_CARD_PACKAGES, null, initialValues);
    		return true;
    	}
    	return false;
    }
    public void updateCustomCardsCount(int packageId)
    {
    	int cardsCount = this.getCustomCardsCount(packageId);
    	String query_updateCustomCardsCount = "UPDATE " + TABLE_CUSTOM_CARD_PACKAGES + " SET 'cards_count' = '" + cardsCount + "' WHERE id = '" + packageId + "'";
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL(query_updateCustomCardsCount);
    }
    public List<CardPackage> getCustomCardPackages()
    {
    	String query_getCardPackages = "SELECT * FROM " + TABLE_CUSTOM_CARD_PACKAGES;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCardPackages, null);
    	List<CardPackage> cardPackages = new ArrayList<CardPackage>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cardPackages;
    	
    	if(cursor.moveToFirst())
    	{
    		CardPackage cardPackage;
    		do
    		{
    			cardPackage = new CardPackage(true, cursor.getInt(0)
    														  , cursor.getString(1)
    														  , cursor.getInt(2));
    			cardPackages.add(cardPackage);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cardPackages;
    }
    //----------------------------------------------------------------
    public boolean addPremiumCard(int id, int packageId, String frontContent, String frontPronounce, String frontPart, String backEng, String backFa)
    {
    	    if(isPremiumCardExist(id, packageId))
    	    	return true;
    		SQLiteDatabase db = this.getWritableDatabase();
    		ContentValues initialValues = new ContentValues();
    		initialValues.put("id", id);
    		initialValues.put("package_id", packageId);
    		initialValues.put("front_content", frontContent);
    		initialValues.put("front_pronounce", frontPronounce);
    		initialValues.put("front_part", frontPart);
    		initialValues.put("back_eng", backEng);
    		initialValues.put("back_fa", backFa);
    		db.insert(TABLE_BUILTIN_CARDS, null, initialValues);
    		this.updateBuiltinCardsCount(packageId);
    		return true;
    }
    
    public void addPremiumCardPackage(CardPackage cardPackage, int price)
    {
    	if(cardPackage != null && !isTherePackagePremium(cardPackage.getId()))
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		ContentValues initialValues = new ContentValues();
    		initialValues.put("id", cardPackage.getId());
    		initialValues.put("name", cardPackage.getName());
    		initialValues.put("cards_count", cardPackage.getCardsCount());
    		initialValues.put("price", price);
    		initialValues.put("premium", 0);
    		db.insert(TABLE_BUILTIN_CARD_PACKAGES, null, initialValues);
    	}
    }
    
    public boolean isPremiumCardExist(int id, int packageId)
    {
    	String query_isPremiumCardExist = "SELECT count(*) FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = ? AND id = ?";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_isPremiumCardExist, new String[]{String.valueOf(packageId),String.valueOf(id)});
    	if(cursor == null)
    		return false;
    	if(cursor.moveToFirst())
    		if(cursor.getInt(0) > 0)
    		{
    			cursor.close();
    			return true;
    		}
    	return false;
    }
    
    public int getBuiltinCardsCount(int packageId)
    {
    	String query_getBuiltinCardsCount = "SELECT count(*) FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getBuiltinCardsCount, null);
    	int count = 0;
    	if(cursor == null)
    		return 0;
    	if(cursor.moveToFirst())
    		count = cursor.getInt(0);
    	cursor.close();
    	return count;
    }
    
    public void updateBuiltinCardsCount(int packageId)
    {
    	int cardsCount = this.getBuiltinCardsCount(packageId);
    	String query_updateBuiltinCardsCount = "UPDATE " + TABLE_BUILTIN_CARD_PACKAGES + " SET 'cards_count' = '" + cardsCount + "' WHERE id = '" + packageId + "'";
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL(query_updateBuiltinCardsCount);
    }
    
    public void setPremiumPackage(int packageId)
    {
    	String query_updatePremiumPackage = "UPDATE " + TABLE_BUILTIN_CARD_PACKAGES + " SET 'premium' = '" + 1 + "' WHERE id = '" + packageId + "'";
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL(query_updatePremiumPackage);    	
    }
    
    public CardPackage getBuiltInCardPackage(int packageId)
    {
    	String query_getCardPackage = "SELECT * FROM " + TABLE_BUILTIN_CARD_PACKAGES + " WHERE id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCardPackage, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return new CardPackage();
    	CardPackage cardPackage = new CardPackage();
    	if(cursor.moveToFirst())
    	{
    		cardPackage = new CardPackage(false, cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
    	}
    	cursor.close();
    	return cardPackage;
    }
    
    public boolean isTherePackagePremium(int packageId)
    {
    	String query_packagePremium = "Select count(*) FROM " + TABLE_BUILTIN_CARD_PACKAGES + " WHERE id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_packagePremium, null);
    	if(cursor == null)
    		return false;

    	if(cursor.moveToFirst())
    		if(cursor.getInt(0) > 0)
    			return true;
    	return false;
    }
    
    public boolean isPackagePremium(int packageId)
    {
    	String query_packagePremium = "Select count(*) FROM " + TABLE_BUILTIN_CARD_PACKAGES + " WHERE id = '" + packageId + "' AND premium = '1'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_packagePremium, null);
    	if(cursor == null)
    		return false;

    	if(cursor.moveToFirst())
    		if(cursor.getInt(0) > 0)
    			return true;
    	return false;
    }
    
    public List<CardPackage> getAllAvailablePackages()
    {
    	List<CardPackage> allPackages = new ArrayList<CardPackage>(); 
    	allPackages.addAll(this.getBuiltInCardPackages());
    	allPackages.addAll(this.getCustomCardPackages());
    	return allPackages;
    }
    
    public List<CardPackage> getBuiltInCardPackages()
    {
    	String query_getCardPackages = "SELECT * FROM " + TABLE_BUILTIN_CARD_PACKAGES + " ORDER BY id";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCardPackages, null);
    	List<CardPackage> cardPackages = new ArrayList<CardPackage>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cardPackages;
    	
    	if(cursor.moveToFirst())
    	{
    		CardPackage cardPackage;
    		do
    		{
    			cardPackage = new CardPackage(false, cursor.getInt(0)
    														  , cursor.getString(1)
    														  , cursor.getInt(2));
    			cardPackages.add(cardPackage);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cardPackages;
    }
    public int getAllBuiltInCardsCount(int packageId)
    {
    	String query_getBuiltInCardsCount = "SELECT count(*) FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getBuiltInCardsCount, null);
    	int count = 0;
    	if(cursor == null)
    		return 0;
    	if(cursor.moveToFirst())
    		count = cursor.getInt(0);
    	cursor.close();
    	return count;
    }
    
    public List<Card> getBuiltInCardsOutBox(int packageId)
    {
    	String query_getBuiltInCards = "SELECT id, package_id, ('/'||front_pronounce||'/'||'<'||front_content||'>'||':'||front_part||':') AS front, ('<'||back_eng||'>'||':'||back_fa||':') AS back FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = '" + packageId + "' AND id NOT IN (SELECT card_id FROM " + TABLE_BOX_CARDS + " WHERE is_custom_card = '0')";
//    	String query_getBuiltInCards = "SELECT id, package_id, front_content AS front, back_eng AS back FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = '" + packageId + "' AND id NOT IN (SELECT card_id FROM " + TABLE_BOX_CARDS + " WHERE is_custom_card = '0')";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getBuiltInCards, null);
    	List<Card> cards = new ArrayList<Card>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cards;
    	
    	if(cursor.moveToFirst())
    	{
    		Card card;
    		do
    		{
    			card = new Card(false, cursor.getInt(0)
    								, cursor.getInt(1)
    								, cursor.getString(2)
    								, cursor.getString(3));
    			cards.add(card);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cards;
    }
    
    public List<Card> getAllBuiltInCards(int packageId)
    {
    	String query_getAllBuiltInCards = "SELECT id, package_id, ('/'||front_pronounce||'/'||'<'||front_content||'>'||':'||front_part||':') AS front, ('<'||back_eng||'>'||'/'||back_fa||'/') AS back FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = '" + packageId + "'";
//    	String query_getAllBuiltInCards = "SELECT * FROM " + TABLE_BUILTIN_CARDS + " WHERE package_id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getAllBuiltInCards, null);
    	List<Card> cards = new ArrayList<Card>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cards;
    	
    	if(cursor.moveToFirst())
    	{
    		Card card;
    		do
    		{
    			card = new Card(false, cursor.getInt(0)
    								, cursor.getInt(1)
    								, cursor.getString(2)
    								, cursor.getString(3));
    			cards.add(card);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cards;
    }
    
    public List<Card> getBuiltInCards(int packageId)
    {
    	String query_getBuiltInCards = "SELECT id, package_id, ('/'||front_pronounce||'/'||'<'||front_content||'>'||':'||front_part||':') AS front, ('<'||back_eng||'>'||':'||back_fa||':') AS back FROM " + TABLE_BUILTIN_CARDS + " LEFT JOIN " + TABLE_BOX_CARDS + " ON " + TABLE_BUILTIN_CARDS + "._id != " + TABLE_BOX_CARDS + ".card_id WHERE " + TABLE_BUILTIN_CARDS + ".package_id = '" + packageId + "'";
//    	String query_getBuiltInCards = "SELECT * FROM " + TABLE_BUILTIN_CARDS + " LEFT JOIN " + TABLE_BOX_CARDS + " ON " + TABLE_BUILTIN_CARDS + "._id != " + TABLE_BOX_CARDS + ".card_id WHERE " + TABLE_BUILTIN_CARDS + ".package_id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getBuiltInCards, null);
    	List<Card> cards = new ArrayList<Card>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cards;
    	
    	if(cursor.moveToFirst())
    	{
    		Card card;
    		do
    		{
    			card = new Card(false, cursor.getInt(0)
    								, cursor.getInt(1)
    								, cursor.getString(2)
    								, cursor.getString(3));
    			cards.add(card);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cards;
    }
    public Card getBuiltInCard(int cardId)
    {
    	String query_getBuiltInCard = "SELECT * FROM " + TABLE_BUILTIN_CARDS + " WHERE id = '" + cardId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getBuiltInCard, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return new Card();
    	Card card = new Card();
    	if(cursor.moveToFirst())
    	{
			card = new Card(false, cursor.getInt(0)
					, cursor.getInt(1)
					, cursor.getString(2)
					, cursor.getString(3));
    	}
    	return card;
    }
    //----------------------------------------------------------------
    public boolean addCustomCard(Card card)
    {
    	if(card != null)
    	{
    		SQLiteDatabase db = this.getWritableDatabase();
    		ContentValues initialValues = new ContentValues();
    		initialValues.put("package_id", card.getPackageId());
    		initialValues.put("front", card.getFront());
    		initialValues.put("back", card.getBack());
    		db.insert(TABLE_CUSTOM_CARDS, null, initialValues);
    		this.updateCustomCardsCount(card.getPackageId());
    		return true;
    	}
        return false;
    }
    public int getCustomCardsCount(int packageId)
    {
    	String query_getCustomCardsCount = "SELECT count(*) FROM " + TABLE_CUSTOM_CARDS + " WHERE package_id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCustomCardsCount, null);
    	int count = 0;
    	if(cursor == null)
    		return 0;
    	if(cursor.moveToFirst())
    		count = cursor.getInt(0);
    	cursor.close();
    	return count;
    }
    
    public List<Card> getCustomCardsOutBox(int packageId)
    {
    	String query_getCustomCards = "SELECT * FROM " + TABLE_CUSTOM_CARDS + " WHERE package_id = '" + packageId + "' AND id NOT IN (SELECT card_id FROM " + TABLE_BOX_CARDS + " WHERE is_custom_card = '1')";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCustomCards, null);
    	List<Card> cards = new ArrayList<Card>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cards;
    	
    	if(cursor.moveToFirst())
    	{
    		Card card;
    		do
    		{
    			card = new Card(true, cursor.getInt(0)
    								, cursor.getInt(1)
    								, cursor.getString(2)
    								, cursor.getString(3));
    			cards.add(card);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cards;
    }
    
    public List<Card> getCustomCards(int packageId)
    {
    	String query_getCustomCards = "SELECT * FROM " + TABLE_CUSTOM_CARDS + " WHERE package_id = '" + packageId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCustomCards, null);
    	List<Card> cards = new ArrayList<Card>();
    	if(cursor == null || cursor.getCount() == 0)
    		return cards;
    	
    	if(cursor.moveToFirst())
    	{
    		Card card;
    		do
    		{
    			card = new Card(true, cursor.getInt(0)
    								, cursor.getInt(1)
    								, cursor.getString(2)
    								, cursor.getString(3));
    			cards.add(card);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return cards;
    }
    
    public boolean updateCustomCard(Card card)
    {
    	if(card == null)
    		return false;
    	
    	String query_updateCustomCard = "UPDATE " + TABLE_CUSTOM_CARDS + " SET 'front' = '" + card.getFront() + "' , 'back' = '" + card.getBack() + "' WHERE id = '" + card.getId() + "'";
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL(query_updateCustomCard);
    	return true;
    }
    public Card getCustomCard(int cardId)
    {
    	String query_getCustomCard = "SELECT * FROM " + TABLE_CUSTOM_CARDS + " WHERE id = '" + cardId + "'";
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(query_getCustomCard, null);
    	if(cursor == null || cursor.getCount() == 0)
    		return new Card();
    	Card card = new Card();
    	if(cursor.moveToFirst())
    	{
			card = new Card(true, cursor.getInt(0)
					, cursor.getInt(1)
					, cursor.getString(2)
					, cursor.getString(3));
    	}
    	return card;
    }
}
