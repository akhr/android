package com.ak.yahoonewsreader.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.ak.yahoonewsreader.util.Logger;

public class NewsItemsFetch extends AsyncTask<Void, Void, List<NewsItem>>{

	private static final String FEED_URL = "http://mhr.yql.yahoo.com/v1/newsfeed?all_content=1";

	private CallBack mCallback;
	
	public NewsItemsFetch(CallBack mCallback) {
		super();
		this.mCallback = mCallback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected List<NewsItem> doInBackground(Void... params) {
		URL url = null;
		List<NewsItem> items = null;
		try {
			url = new URL(FEED_URL);
		} catch (MalformedURLException e) {
			Logger.error("Feed URL is invalid", e);
			e.printStackTrace();
		}
		if(url != null){
			String resultString = fetchData(url);
			items = NewsItemsParser.parse(resultString);
		}
		return items;
	}

	@Override
	protected void onPostExecute(List<NewsItem> result) {
		mCallback.onSuccess(result);
	}

	private String fetchData(URL url){

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(FEED_URL);
		String responseString = null;

		try {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if ( entity != null) {
					// insert to database
					/*		      ContentValues values = new ContentValues();*/
					responseString = readInputStreamAsString(entity.getContent());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;
	}
	
	public static String readInputStreamAsString(InputStream in) 
		    throws IOException {

		    BufferedInputStream bis = new BufferedInputStream(in);
		    ByteArrayOutputStream buf = new ByteArrayOutputStream();
		    int result = bis.read();
		    while(result != -1) {
		      byte b = (byte)result;
		      buf.write(b);
		      result = bis.read();
		    }        
		    return buf.toString();
		}

	
	public interface CallBack{
		public void onSuccess(List<NewsItem> newsItems);
		public void onFailure(String errorMsg);
	}

}
