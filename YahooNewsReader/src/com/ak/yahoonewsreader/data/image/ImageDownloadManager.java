package com.ak.yahoonewsreader.data.image;

import java.lang.ref.WeakReference;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;


public class ImageDownloadManager {

	private static ImageDownloadManager INSTANCE;

	private ImageDownloadManager() {
	}

	public static ImageDownloadManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ImageDownloadManager();
		}
		return INSTANCE;
	}

	public void loadImage(URL url, ImageView imageView, boolean useExecutor){
		download(url, imageView, useExecutor);
	}

	public void loadImage(Bitmap bitmap, ImageView imageView){
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
		}
	}

	private void download(URL url, ImageView imageView, boolean useExecutor){
		ImageFetchTask task = new ImageFetchTask(url, imageView);
		DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
		imageView.setImageDrawable(downloadedDrawable);
		if (Build.VERSION.SDK_INT >= 11 && useExecutor) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			task.execute();
		}
	}

	public static ImageFetchTask getImageFetchTaskFromView(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	protected void addBitmapToCache(URL url, Bitmap bitmap) {
		
	}

	protected Bitmap getBitmapFromCache(URL url) {
		return null;
	}

	public void clearCache() {
		//clear Cache.
	}

	static class DownloadedDrawable extends BitmapDrawable {

		private final WeakReference<ImageFetchTask> imageFetchTaskRef;

		public DownloadedDrawable(ImageFetchTask imageFetchTask) {
			imageFetchTaskRef = new WeakReference<ImageFetchTask>(imageFetchTask);
		}

		public ImageFetchTask getBitmapDownloaderTask() {
			return imageFetchTaskRef.get();
		}
	}


}
