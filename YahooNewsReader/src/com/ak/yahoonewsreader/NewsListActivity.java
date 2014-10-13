package com.ak.yahoonewsreader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ak.yahoonewsreader.data.NewsItem;
import com.ak.yahoonewsreader.data.NewsItemsFetch;
import com.ak.yahoonewsreader.data.image.ImageDownloadManager;

public class NewsListActivity extends Activity {


	private List<NewsItem> mData;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void init(){
		mData = new ArrayList<NewsItem>();
		mListView = (ListView) findViewById(R.id.listView);
		fetchData();
		mListView.setAdapter(new NewsListViewAdapter());
		/*Button btn = (Button) findViewById(R.id.get_data_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});*/
	}

	private void fetchData(){
		DataFetchCallBack callBack = new DataFetchCallBack();
		NewsItemsFetch fetchTask = new NewsItemsFetch(callBack);
		fetchTask.execute();
	}

	private class NewsListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.listview_row, parent, false);
			}
			
			TextView textView = (TextView) convertView.findViewById(R.id.newsTitleTV);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.newsthumbNailIV);
			
			NewsItem item = (NewsItem) getItem(position);
			textView.setText(item.getTitle());
			
//			loadNewsThumbNail(item.getImageList().get(0).getmLink(), imageView);
			
			return convertView;
		}
		
		private void loadNewsThumbNail(URL iconURL, ImageView imgView){
			if(iconURL != null)
				ImageDownloadManager.getInstance().loadImage(iconURL, imgView, true);
		}
	}
	
	private void updateDataModel(List<NewsItem> newsItems){
		this.mData = newsItems;
		((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
	}

	private class DataFetchCallBack implements NewsItemsFetch.CallBack{

		@Override
		public void onSuccess(List<NewsItem> newsItems) {
			updateDataModel(newsItems);
		}

		@Override
		public void onFailure(String errorMsg) {

		}

	}

}
