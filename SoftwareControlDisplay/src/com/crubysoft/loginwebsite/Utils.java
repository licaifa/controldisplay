package com.crubysoft.loginwebsite;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
/*
 * @crubysoft cpanda
 */
public class Utils {

	public final static String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static String appPath = "cinematixvp";

	public static boolean createDir(String createPath) {
		File dirFile = new File(createPath);
		if (!dirFile.exists()) {
			if (!dirFile.mkdir())
				return false;
		}
		return true;
	}
	
	public static void deleteFile(String dirName){
		File dirFile = new File(dirName);
		if(dirFile.exists())
			dirFile.delete();
	}
	public static String getFileName(String path){
		File dirFile = new File(path);
		if(!dirFile.exists()){
			if(!dirFile.mkdir())
				return null;
		}
		
		File[] files = dirFile.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.toLowerCase().endsWith(".mp4"); 
			}
		});

		if(files.length == 0)
			return null;
		
		File lastModifiedFile = files[0];
		for(File file : files){
			if(lastModifiedFile.lastModified() > file.lastModified())
				lastModifiedFile = file;
		}
		
		String fileName = lastModifiedFile.toString();
		return fileName;
	}

	public static String getAppPath(){
			return appPath;
	}
	
	public static void setAppPath(String path){
		if(path!=null)
			appPath = path;
	}
	public static ArrayList<String> getPlayList(){
		ArrayList<String> playList = new ArrayList<String>();
		File dirFile = new File(sdcardPath + appPath);
		if(!dirFile.exists())
			if(!dirFile.mkdir())
				return null;
		File[] files = dirFile.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String filename){
				return filename.toLowerCase().endsWith(".mp4");
			}
		});

		for(File file : files){
			if(!file.exists())
				return null;
			String tmpPath = file.getAbsolutePath();
			playList.add(tmpPath);			
		}
		return playList;
	}
	/** Method to check whether external media available and writable. */

	public static boolean checkExternalMedia() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		boolean stat;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			stat = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// Can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			stat = false;
		} else {
			// Can't read or write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			stat = false;
		}
		return stat;
	}
	
	public static void deleteDir(String deletePath, String restoreFile){
		File dirFile = new File(deletePath);
		if(dirFile.exists()){
			File[]files= dirFile.listFiles();
			if(files == null)
				return;
			for(File file : files){
				if(file.isDirectory())
					deleteDir(file.getPath(), restoreFile);
				else{
					if(!file.getName().equals(restoreFile))
							file.delete();
					}
				}
			}
		}
}
