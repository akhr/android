package com.ak.yahoonewsreader.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ak.yahoonewsreader.data.NewsItem.NewsItemImage;
import com.ak.yahoonewsreader.util.Logger;

public class NewsItemsParser {

	private static final String TAG_RESULTS = "result";
	private static final String TAG_ITEMS = "items";
	private static final String TAG_TITLE = "title";
	private static final String TAG_PUBLISHED = "published";
	private static final String TAG_UUID = "uuid";
	private static final String TAG_PUBLISHER = "publisher";
	private static final String TAG_IMAGES = "images";
	private static final String TAG_WIDTH = "width";
	private static final String TAG_HEIGTH = "height";
	private static final String TAG_URL = "url";
	private static final String TAG_LINK = "link";
	private static final String TAG_SUMMARY = "summary";

	public static List<NewsItem> parse(String jsonString){

		JSONObject jsonObject = null;
		List<NewsItem> newsItemslist = null;

		try {
			jsonObject = new JSONObject(jsonString.toString());
		} catch (JSONException e) {
			Logger.error("Invalid JSON 1 - Skipping parsing", e);
			e.printStackTrace();
			return null;
		}

		if(jsonObject != null){
			try {
				JSONObject results = jsonObject.getJSONObject(TAG_RESULTS);
				JSONArray items = results.getJSONArray(TAG_ITEMS);
				newsItemslist = new ArrayList<NewsItem>();
				for (int i = 0; i < items.length(); i++) {
					try{
						JSONObject item = items.getJSONObject(i);
						String title = item.getString(TAG_TITLE);
						String uuid = item.getString(TAG_UUID);
						NewsItem newsItem = new NewsItem(title, uuid);
						newsItem.setPublished(item.getString(TAG_PUBLISHED));
						newsItem.setPublisher(item.getString(TAG_PUBLISHER));
						newsItem.setSummary(item.getString(TAG_SUMMARY));
						newsItem.setLink(item.getString(TAG_LINK));
						List<NewsItemImage> images = new ArrayList<NewsItem.NewsItemImage>();
						JSONArray imagelist = item.getJSONArray(TAG_IMAGES);
						for (int j = 0; j < imagelist.length(); j++) {
							try{
								JSONObject image = imagelist.getJSONObject(i);
								int width = Integer.valueOf(image.getString(TAG_WIDTH));
								int heigth = Integer.valueOf(image.getString(TAG_HEIGTH));
								URL url = new URL(image.getString(TAG_URL));
								NewsItemImage imageObj = new NewsItemImage(width, heigth, url);
								images.add(imageObj);
							}catch(JSONException e){
								Logger.error("Invalid Image JSON object - Skipping Image", e);
								continue;
							}catch (NumberFormatException e) {
								Logger.error("Invalid Image SIZE - Skipping Image", e);
								continue;
							} catch (MalformedURLException e) {
								Logger.error("Invalid Image URL - Skipping Image", e);
								continue;
							}
						}
						newsItemslist.add(newsItem);
					}catch (JSONException e) {
						Logger.error("Invalid News Item JSON Object - Skipping News Item", e);
						e.printStackTrace();
						continue;
					}
				}
			} catch (JSONException e) {
				Logger.error("Invalid JSON 2 - Skipping parsing", e);
				e.printStackTrace();
			}
		}
		return newsItemslist;
	}

}
