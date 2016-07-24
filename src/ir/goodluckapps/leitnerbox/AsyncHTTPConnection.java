package ir.goodluckapps.leitnerbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncHTTPConnection extends AsyncTask<Void, Integer, Void>
{
	private String _url = "";
	private HttpURLConnection _connection;
	private Context _context;
	private View _packageView;
	private ProgressBar _progresBar;
	private String _packageName;
	private String _displayName;
	private int _recordNumber;
	private int _packageId;
	private int _price;
	private static AsyncHTTPConnection _asyncHTTPConnection = null;
	
	public static boolean isRunning()
	{
		if(_asyncHTTPConnection == null)
			return false;
		return _asyncHTTPConnection.getStatus().equals(AsyncTask.Status.RUNNING);
	}
	
	public static String getPackageName()
	{
		if(_asyncHTTPConnection == null)
			return "";
		return _asyncHTTPConnection._packageName;
	}
	
	public static void singleExecute(Context context, int packageId, String packageName, String displayName, View packageView, int recordNumber, int price)
	{
		_asyncHTTPConnection = new AsyncHTTPConnection(context, packageId, packageName, displayName, packageView, recordNumber, price);
		_asyncHTTPConnection.execute();
//		if(_asyncHTTPConnection == null)
//		{
//			_asyncHTTPConnection = new AsyncHTTPConnection(context, packageId, packageName, displayName, packageView, recordNumber, price);
//			_asyncHTTPConnection.execute();
//		}else
//		{
//			setViews(packageView);
//		}
	}
	
	public static void setViews(View packageView)
	{
		if(_asyncHTTPConnection != null)
		{
			_asyncHTTPConnection._packageView = packageView;
			_asyncHTTPConnection._progresBar = (ProgressBar) _asyncHTTPConnection._packageView.findViewById(R.id.progressBar);
			if(_asyncHTTPConnection.getStatus().equals(AsyncTask.Status.RUNNING))
				_asyncHTTPConnection._progresBar.setVisibility(View.VISIBLE);	
		}
	}
	
	public AsyncHTTPConnection(Context context, int packageId, String packageName, String displayName, View packageView, int recordNumber, int price)
	{
		_recordNumber = recordNumber;
		_context = context;
		_packageView = packageView;
		_packageId = packageId;
		_packageName = packageName;
		_displayName = displayName;
		_price = price;
		_url = "http://www.goodluckapps.ir/apps/leitnerbox/packages/" + _packageName + ".png";
	}

	@Override
	protected void onPreExecute()
	{
		_progresBar = (ProgressBar) _packageView.findViewById(R.id.progressBar);
		_progresBar.setVisibility(View.VISIBLE);
		CardPackage cardPackage = new CardPackage(false, _packageId, _displayName, _recordNumber);
		DatabaseManager.getSingleton(_context).addPremiumCardPackage(cardPackage, _price);
	}
	
	@Override
    protected void onProgressUpdate(Integer... progress) {
        _progresBar.setProgress(progress[0]);
    }

	@Override
	protected void onCancelled()
	{
		_progresBar.setVisibility(View.GONE);
		((TextView)_packageView.findViewById(R.id.text_view_packageName)).setText("Failed");		
	}
	
	@Override
	protected void onPostExecute(Void arg)
	{
		_progresBar.setVisibility(View.GONE);
		((TextView)_packageView.findViewById(R.id.text_view_packageName)).setText("Downloaded");
		DatabaseManager.getSingleton(_context).setPremiumPackage(_packageId);
	}
	
	@Override
	protected Void doInBackground(Void... arg0)
	{
		try {
			URL url = new URL(_url);
			_connection = (HttpURLConnection)url.openConnection();
			_connection.setDoOutput(false);
			_connection.setDoInput(true);
			_connection.setRequestProperty("Accept-Charset", "UTF-8");
			_connection.setRequestMethod("GET");
			_connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			_connection.setRequestProperty("Cache-Control","no-cache");
	          BufferedReader reader = null;
	          reader = new BufferedReader(new InputStreamReader(_connection.getInputStream()));
	          String line = "";
	          int ind = 0;
	          while((line = reader.readLine()) != null)
	          {
	        	  String[] feilds = line.split("\t");
	        	  if(feilds.length < 7)
	        		  continue;
	        	  int packageId = Integer.parseInt(feilds[0]);
	        	  int id = Integer.parseInt(feilds[1]);
	        	  String frontContent = feilds[2];
	        	  String frontPronounce = feilds[3];
	        	  String frontPart = feilds[4];
	        	  String backEng = feilds[5];
	        	  String backFa = feilds[6];
	        	  DatabaseManager.getSingleton(_context).addPremiumCard(id, packageId, frontContent, frontPronounce, frontPart, backEng, backFa);
	        	  publishProgress((int)(((float)ind / _recordNumber) * 100));
	        	  ind++;
	          }
	          
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			this.cancel(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
			this.cancel(true);
		} catch (IOException e) {
			e.printStackTrace();
			this.cancel(true);
		}
		return null;
	}
}
