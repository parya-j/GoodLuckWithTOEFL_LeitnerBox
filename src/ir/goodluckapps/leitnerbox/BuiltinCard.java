package ir.goodluckapps.leitnerbox;

public class BuiltinCard {
	private int _id;
	private int _packageId;
	private String _frontPronounce;
	private String _frontContent;
	private String _frontPart;
	private String _backEng;
	private String _backFa;
	
	public BuiltinCard()
	{
		this.setId(0);
		this.setPackageId(0);
		this.setFrontPronounce("");
		this.setFrontContent("");
		this.setFrontPart("");
		this.setBackEng("");
		this.setBackFa("");
	}
	
	public BuiltinCard(int id, int packageId, String frontPronounce, String frontContent, String frontPart, String backEng, String backFa)
	{
		this.setId(id);
		this.setPackageId(packageId);
		this.setFrontPronounce(frontPronounce);
		this.setFrontContent(frontContent);
		this.setFrontPart(frontPart);
		this.setBackEng(backEng);
		this.setBackFa(backFa);
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public int getPackageId() {
		return _packageId;
	}

	public void setPackageId(int packageId) {
		this._packageId = packageId;
	}

	public String getFrontPronounce() {
		return _frontPronounce;
	}

	public void setFrontPronounce(String frontPronounce) {
		this._frontPronounce = frontPronounce;
	}

	public String getFrontContent() {
		return _frontContent;
	}

	public void setFrontContent(String frontContent) {
		this._frontContent = frontContent;
	}

	public String getFrontPart() {
		return _frontPart;
	}

	public void setFrontPart(String frontPart) {
		this._frontPart = frontPart;
	}

	public String getBackEng() {
		return _backEng;
	}

	public void setBackEng(String backEng) {
		this._backEng = backEng;
	}

	public String getBackFa() {
		return _backFa;
	}

	public void setBackFa(String backFa) {
		this._backFa = backFa;
	}
}
