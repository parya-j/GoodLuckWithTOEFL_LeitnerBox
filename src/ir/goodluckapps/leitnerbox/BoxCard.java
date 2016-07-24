package ir.goodluckapps.leitnerbox;

public class BoxCard {
	private int _id;
	private int _step;
	private boolean _isCustomCard;
	private int _cardId;
	private long _firstEnterDate;
	private long _lastReviewDate;
	private long _nextReviewDate;
	
	public BoxCard()
	{
		setId(0);
		setStep(0);
		setCustomCard(true);
		setCardId(0);
		setFirstEnterDate(0);
		setLastReviewDate(0);
		setNextReviewDate(0);
	}

	public BoxCard(int id, int step, boolean isCustomCard, int cardId, long firstEnterDate, long lastReviewDate,  long nextReviewDate)
	{
		setId(id);
		setStep(step);
		setCustomCard(isCustomCard);
		setCardId(cardId);
		setFirstEnterDate(firstEnterDate);
		setLastReviewDate(lastReviewDate);
		setNextReviewDate(nextReviewDate);
	}
	
	public BoxCard(int step, boolean isCustomCard, int cardId, long firstEnterDate, long lastReviewDate, long nextReviewDate)
	{
		setId(0);
		setStep(step);
		setCustomCard(isCustomCard);
		setCardId(cardId);
		setFirstEnterDate(firstEnterDate);
		setLastReviewDate(lastReviewDate);
		setNextReviewDate(nextReviewDate);
	}
	

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public int getStep() {
		return _step;
	}

	public void setStep(int step) {
		this._step = step;
	}

	public boolean isCustomCard() {
		return _isCustomCard;
	}

	public void setCustomCard(boolean isCustomCard) {
		this._isCustomCard = isCustomCard;
	}

	public int getCardId() {
		return _cardId;
	}

	public void setCardId(int cardId) {
		this._cardId = cardId;
	}

	public long getFirstEnterDate() {
		return _firstEnterDate;
	}

	public void setFirstEnterDate(long firstEnterDate) {
		this._firstEnterDate = firstEnterDate;
	}

	public long getLastReviewDate() {
		return _lastReviewDate;
	}

	public void setLastReviewDate(long lastReviewDate) {
		this._lastReviewDate = lastReviewDate;
	}
	
	public long getNextReviewDate() {
		return _nextReviewDate;
	}

	public void setNextReviewDate(long nextReviewDate) {
		this._nextReviewDate = nextReviewDate;
	}
}
