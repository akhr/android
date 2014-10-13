package com.ak.yahoonewsreader.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsItem {
	
	private String mTitle;
	private String mPublished;
	private String mUUID;
	private String mPublisher;
	private List<NewsItemImage> mImageList; 
	private String mSummary;
	private URL mLink;
	
	public NewsItem(String title, String uuid){
		this.mTitle = title;
		this.mUUID = uuid;
		mImageList = new ArrayList<NewsItem.NewsItemImage>();
	}
	
	public String getPublished() {
		return mPublished;
	}

	public void setPublished(String mPublished) {
		this.mPublished = mPublished;
	}

	public String getPublisher() {
		return mPublisher;
	}

	public void setPublisher(String mPublisher) {
		this.mPublisher = mPublisher;
	}

	public String getSummary() {
		return mSummary;
	}

	public void setSummary(String mSummary) {
		this.mSummary = mSummary;
	}

	public URL getLink() {
		return mLink;
	}

	public void setLink(String link) {
		try {
			URL url = new URL(link);
			this.mLink = url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return mTitle;
	}

	public String getUUID() {
		return mUUID;
	}

	public List<NewsItemImage> getImageList() {
		return mImageList;
	}
	
	public void addImage(NewsItemImage image){
		if(mImageList != null){
			mImageList.add(image);
		}else{
			mImageList = new ArrayList<NewsItem.NewsItemImage>();
			mImageList.add(image);
		}
	}

	public static class NewsItemImage {
		
		private int mWidth;
		private int mHeight;
		private URL mLink;
		
		public NewsItemImage(int mWidth, int mHeight, URL mLink) {
			this.mWidth = mWidth;
			this.mHeight = mHeight;
			this.mLink = mLink;
		}
		public int getmWidth() {
			return mWidth;
		}
		public int getmHeight() {
			return mHeight;
		}
		public URL getmLink() {
			return mLink;
		}
		
	}
}
