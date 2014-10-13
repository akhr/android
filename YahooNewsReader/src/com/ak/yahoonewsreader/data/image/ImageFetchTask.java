package com.ak.yahoonewsreader.data.image;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.ak.yahoonewsreader.util.Logger;

class ImageFetchTask extends AsyncTask<Void, Void, Bitmap>  {

	private URL mImageURL = null;
	private final WeakReference<ImageView> imageViewReference;

	public ImageFetchTask(URL imageURL, ImageView imageView) {
		this.mImageURL = imageURL;
		imageViewReference = new WeakReference<ImageView>(imageView);
	}

	public URL getImageURL() {
		return mImageURL;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		if(!isCancelled()){
			return downloadBitmap(mImageURL);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if(bitmap != null){
			ImageDownloadManager.getInstance().addBitmapToCache(mImageURL, bitmap);
		}
		if (imageViewReference != null) {
			ImageView imageView = (ImageView) imageViewReference.get();
			ImageFetchTask imageFetchTask = ImageDownloadManager.getImageFetchTaskFromView(imageView);
			if((this == imageFetchTask)){
				if((imageView != null) && (bitmap != null)) {
					imageView.setImageBitmap(bitmap);
				} 
			}
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled(Bitmap result) {
		super.onCancelled(result);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private Bitmap downloadBitmap(URL imageURL) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		HttpGet httpGet = new HttpGet(imageURL.toString());

		try {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if ( entity != null) {
					final Bitmap bitmap = BitmapFactory.decodeStream(entity.getContent());
					return bitmap;
				}
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			Logger.error("Error fetching image", e);
		}
		return null;
	}
}

