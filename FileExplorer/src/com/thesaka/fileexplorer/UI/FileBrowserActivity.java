package com.thesaka.fileexplorer.UI;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thesaka.fileexplorer.R;
import com.thesaka.fileexplorer.common.Logger;

public class FileBrowserActivity extends ActionBarActivity {

	private static final String PKCS12_EXT = ".p12";
	private static final String HIDDEN_DIRECTORIES = ".";

	private List<String> mPathList;
	private Stack<DirectoryLevel> mDirectoryLevel;
	private List<FileNode> mFileList;
	private File mCurrentDirectory;
	private String[] mHomeDirectories;

	private Intent receivedIntent;
	private String receivedAction;
	private CertFileFilter mFileFilter;
	private DirectoryObserver mDirectoryObserver;

	private ListView mListView;

	private enum DirectoryLevel{
		HOME,
		CHILD;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.debug(FileBrowserActivity.class, "onCreate()");
		setContentView(R.layout.file_browser);

//		receivedIntent = getIntent();
//		receivedAction = receivedIntent != null ? receivedIntent.getAction():null;

		if(!isExternalStorageReadable()){
			Logger.debug(FileBrowserActivity.class, "SD card not mounted - finishing FileBrowserActivity");
			finish();
			showToast(R.string.sd_card_not_mounted);
		}

		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Logger.debug(FileBrowserActivity.class, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.debug(FileBrowserActivity.class, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.debug(FileBrowserActivity.class, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.debug(FileBrowserActivity.class, "onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.debug(FileBrowserActivity.class, "onDestroy()");
		mDirectoryObserver.stopWatching();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Logger.debug(FileBrowserActivity.class, "onConfigurationChanged()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.debug(FileBrowserActivity.class, "onSaveInstanceState()");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Logger.debug(FileBrowserActivity.class, "onRestoreInstanceState()");
	}

	@Override
	public void onBackPressed() {
		Logger.debug(FileBrowserActivity.class, "onBackPressed()");
		this.finish();
	}

	@Override
	public boolean onSupportNavigateUp() {
		Logger.debug(FileBrowserActivity.class, "onSupportNavigateUp()");
		loadParentDirectory();
		return true;
	}

	private void init(){
		mPathList = new ArrayList<String>();
		mDirectoryLevel = new Stack<DirectoryLevel>();
		mFileList = new ArrayList<FileNode>();
		mFileFilter = new CertFileFilter();

		setHomeDirectory();

		mListView = (ListView) findViewById(R.id.listView);
		mListView.setAdapter(createFileListAdapter());
		mListView.setOnItemClickListener(mOnItemClickListener);

		mDirectoryObserver = new DirectoryObserver();
		loadHomeDirectory();
	}

	private ArrayAdapter<FileNode> createFileListAdapter(){
		return new ArrayAdapter<FileNode>(FileBrowserActivity.this,
				android.R.layout.select_dialog_item,
				android.R.id.text1,
				mFileList){

			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				File file = mFileList.get(position).file;

				if(file == null){
					textView.setText(getString(R.string.no_file));
				}

				if(position == 0 && mDirectoryLevel.peek() == DirectoryLevel.HOME){
					textView.setText(getString(R.string.internal_storage));
				}

				int drawableID = 0;
				if (mFileList.get(position).iconResId != -1) {
					drawableID = mFileList.get(position).iconResId;
				}
				textView.setCompoundDrawablesWithIntrinsicBounds(drawableID, 0, 0, 0);
				textView.setEllipsize(TruncateAt.MARQUEE);
				int dp2 = (int) (2 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp2);
				textView.setTextColor(Color.BLACK);
				textView.setBackgroundColor(Color.WHITE);
				return view;
			}

		};
	}

	private void setHomeDirectory(){
		mHomeDirectories = getStorageDirectories();
	}

	private void parsePathListFromPath(){
		mPathList.clear();
		if(mCurrentDirectory != null){
			String pathString = mCurrentDirectory.getAbsolutePath();
			String[] directories = pathString.split(File.separator);
			for(String directory : directories){
				mPathList.add(directory);
			}
		}
	}

	private String getPathFromPathList(){
		if(mPathList != null && !mPathList.isEmpty()){
			StringBuilder sB = new StringBuilder(mPathList.get(0));
			sB.append(File.separator);
			for(int i=1; i<mPathList.size(); i++){
				sB.append(mPathList.get(i));
				sB.append(File.separator);
			}
			return sB.toString();
		}
		return null;
	}

	private void loadHomeDirectory(){
		if(mDirectoryLevel.isEmpty())
			mDirectoryLevel.push(DirectoryLevel.HOME);
		loadFileListUI();
	}

	private void loadParentDirectory(){
		mDirectoryObserver.stopWatching();
		if(mDirectoryLevel.peek() != DirectoryLevel.HOME && mPathList.size()>0){
			mDirectoryLevel.pop();
			mPathList.remove(mPathList.size()-1);
			mCurrentDirectory = new File(getPathFromPathList());
			loadFileListUI();
			mDirectoryObserver.startWatching();
		}else if(mDirectoryLevel.peek() == DirectoryLevel.HOME){
			loadHomeDirectory();
		}else{
			this.finish();
		}
	}

	private void loadChildDirectory(File selectedFile){
		mDirectoryObserver.stopWatching();
		mDirectoryLevel.push(DirectoryLevel.CHILD);
		mCurrentDirectory = selectedFile;
		if(mPathList.isEmpty())
			parsePathListFromPath();
		else
			mPathList.add(selectedFile.getName());
		loadFileListUI();
		mDirectoryObserver.startWatching();
	}

	private boolean loadHomeFileList(){
		mFileList.clear();
		if(mHomeDirectories[0] == null || TextUtils.isEmpty(mHomeDirectories[0])){
			mHomeDirectories = getStorageDirectories();
		}

		for(String path : mHomeDirectories){
			File file = new File(path);
			if(file.exists() && file.isDirectory() && file.canRead()){
				FileNode fileNode = new FileNode(file, R.drawable.ic_folder_icon);
				mFileList.add(fileNode);
			}
		}
		return true;
	}

	private boolean loadFileList() {
		mFileList.clear();

		if (mCurrentDirectory != null && mCurrentDirectory.exists() && mCurrentDirectory.isDirectory() && mCurrentDirectory.canRead()) {
			String[] fList = mCurrentDirectory.list(mFileFilter);
			for (String dir : fList) {
				File file = new File(mCurrentDirectory, dir);
				int drawableID = R.drawable.ic_file_icon;
				boolean canRead = file.canRead();
				if (file.isDirectory()) {
					if (canRead) {
						drawableID = R.drawable.ic_folder_icon;
					} else {
						drawableID = R.drawable.ic_folder_disabled_icon;
					}
				}
				mFileList.add(new FileNode(file, drawableID));
			}

			if (mFileList.size() == 0) {
				mFileList.add(new FileNode(null, -1));
			}else{
				Collections.sort(mFileList);
			}
			return true;
		}else{
			Logger.error(FileBrowserActivity.class, mCurrentDirectory + " cant be read");
			showToast(String.format(getString(R.string.not_readable), mCurrentDirectory.getName()));
			return false;
		}

	}

	private void loadFileListUI(){
		boolean isFileListLoaded;

		if(mDirectoryLevel.peek() == DirectoryLevel.HOME)
			isFileListLoaded = loadHomeFileList();
		else
			isFileListLoaded = loadFileList();

		if(isFileListLoaded){
			((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
			updateActionBarHeader();
		}
	}

	private void updateActionBarHeader(){
		if(mDirectoryLevel.peek() == DirectoryLevel.HOME){
			setTitle("Home");
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}else{
			setTitle(mCurrentDirectory.getAbsolutePath());
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private void handleListSelection(int position){
		File selectedFile = mFileList.get(position).file;
		if(selectedFile == null){
			showToast(R.string.no_file);
			return;
		}

		if(selectedFile.isDirectory()){
			if(selectedFile.canRead()){
				loadChildDirectory(selectedFile);
			}else{
				showToast(R.string.directory_not_readable);
			}
		}else{
			if(isFileAcceptable(selectedFile)){
				finishAndReturnFile(selectedFile);
			}else{
				showToast("Selected File : "+selectedFile.getName());
			}
		}
	}

	private void finishAndReturnFile(File selectedFile){
		Intent intent = new Intent();
	 
		// TODO Call the Content Handler to handle this file type
		
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	private void showToast(String msg){
		Toast.makeText(FileBrowserActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private void showToast(int  resId){
		Toast.makeText(FileBrowserActivity.this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	protected boolean isFileAcceptable(File file) {
		return file.getAbsolutePath().endsWith(PKCS12_EXT);
	}

	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			handleListSelection(position);
		}
	};

	private String[] getStorageDirectories(){
		String[] output;
		output = getStorageDirectories_method_1();
		if(output[0] == null || TextUtils.isEmpty(output[0])){
			output = getStorageDirectories_Fallback_1();
		}
		return output;
	}

	/**
	 * Source : http://stackoverflow.com/questions/11281010/how-can-i-get-external-sd-card-path-for-android-4-0 
	 **/
	public static String[] getStorageDirectories_method_1()
	{
		final Set<String> output = new HashSet<String>();

		final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
		final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
		final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");

		if(TextUtils.isEmpty(rawEmulatedStorageTarget))
		{
			if(TextUtils.isEmpty(rawExternalStorage))
				// EXTERNAL_STORAGE undefined; falling back to default.
				output.add("/storage/sdcard0");
			else
				output.add(rawExternalStorage);
		}
		else
		{

			final String rawUserId;
			if(Build.VERSION.SDK_INT < 17) // Build.VERSION_CODES.JELLY_BEAN_MR1
				rawUserId = "";
			else
			{
				final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				final String[] folders = path.split(File.separator);
				final String lastFolder = folders[folders.length - 1];
				boolean isDigit = false;
				try
				{
					Integer.valueOf(lastFolder);
					isDigit = true;
				}
				catch(NumberFormatException ignored)
				{
				}
				rawUserId = isDigit ? lastFolder : "";
			}

			// /storage/emulated/0[1,2,...]
			if(TextUtils.isEmpty(rawUserId))
			{
				output.add(rawEmulatedStorageTarget);
			}
			else
			{
				output.add(rawEmulatedStorageTarget + File.separator + rawUserId);
			}
		}

		// Add all secondary storages
		if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
		{
			final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
			Collections.addAll(output, rawSecondaryStorages);
		}
		return output.toArray(new String[output.size()]);
	}

	private String[] getStorageDirectories_Fallback_1(){
		String[] output = new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()};
		return output;
	}

	private class FileNode implements Comparable<FileNode>{

		File file;
		int iconResId;

		FileNode(File file, int resId){
			this.file = file;
			this.iconResId = resId;
		}

		@Override
		public int compareTo(FileNode another) {
			if(another.file == null)
				return -1;
			return this.file.getName().toLowerCase().compareTo(another.file.getName().toLowerCase());
		}

		@Override
		public String toString() {
			if(file != null)
				return file.getName().toString();
			else{
				return "";
			}
		}
	}

	private class CertFileFilter implements FilenameFilter{
		@Override
		public boolean accept(File dir, String filename) {
			File selectedFile = new File(dir, filename);
			if(selectedFile.isDirectory() && filename.startsWith(HIDDEN_DIRECTORIES, 0))
				return true;   //Also show hidden files
			return true;
		}
	}

	private class DirectoryObserver {

		FileObserver fileObserver;

		void startWatching(){
			if(mCurrentDirectory != null && mCurrentDirectory.exists() && mCurrentDirectory.isDirectory()){
				if(fileObserver != null){
					fileObserver.stopWatching();
					fileObserver = null;
				}

				fileObserver = new FileObserver(mCurrentDirectory.getAbsolutePath()) {
					@Override
					public void onEvent(int event, String path) {
						event &= FileObserver.ALL_EVENTS;
						switch (event) {
						case FileObserver.CREATE:
						case FileObserver.DELETE:
							if(shdTakeAction(path)){
								Logger.debug(FileBrowserActivity.class, "File/Dir created/deleted");
								runOnUiThread(new Runnable() {
									public void run() {
										loadFileListUI();
									}
								});
							}
							break;
						case DELETE_SELF:
						case MOVE_SELF:
							Logger.debug(FileBrowserActivity.class, "Monitored dir moved/deleted");
							runOnUiThread(new Runnable() {
								public void run() {
									loadParentDirectory();
								}
							});
							break;
						case MOVED_FROM:
						case MOVED_TO:
							if(shdTakeAction(path)){
								Logger.debug(FileBrowserActivity.class, "File/Dir moved to/from");
								runOnUiThread(new Runnable() {
									public void run() {
										loadFileListUI();
									}
								});
							}
							break;
						}
					}
				};
				Logger.debug(FileBrowserActivity.class, "startWatching() - "+mCurrentDirectory.getAbsolutePath());
				fileObserver.startWatching();
			}
		}

		void stopWatching(){
			if(fileObserver != null){
				Logger.debug(FileBrowserActivity.class, "stopWatching() - "+mCurrentDirectory.getAbsolutePath());
				fileObserver.stopWatching();
				fileObserver = null;
			}
		}

		private boolean shdTakeAction(String path){
			if(path != null && !TextUtils.isEmpty(path)){
				File file = new File(mCurrentDirectory.getAbsoluteFile(), path);
				if(file != null && file.exists())
					return file.isFile() ? isFileAcceptable(file):true;
			}
			return true;
		}
	}

}

