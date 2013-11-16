package com.crubysoft.loginwebsite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {
	
	private SharedPreferences prvPref;
	private SharedPreferences.Editor prevEditor;
	private static int CD_SETTING_STATE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		
		prvPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		prevEditor = prvPref.edit();
		CD_SETTING_STATE = prvPref.getInt("CD_SETTING_STATE", 0);
		CD_SETTING_STATE = 0;
		Log.e("xxxxxxx", "xxxx:" + CD_SETTING_STATE);
		boolean status = prvPref.getBoolean("status", false);
		if(!status){
			prevEditor = prvPref.edit();
			prevEditor.putBoolean("status", true);
			prevEditor.commit();
			startThread(3000);//5000
		}else{
			setIntent();
		}
	}

	private void startThread(final int MiliSeconds) {
		Thread welcomeThread = new Thread() {
			int wait = 0;

			@Override
			public void run() {
				try {
					super.run();
					while (wait < MiliSeconds) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
					System.out.println("miniPC=" + e);
				} finally {
					CD_SETTING_STATE = 0;
					setIntent();
				}
			}
		};
		welcomeThread.start();
	}

	private void setIntent() {
		switch(CD_SETTING_STATE){
		case 0:
			startActivity(new Intent(SplashActivity.this, GettingStartedScene.class));
			break;
		case 1:
			startActivity(new Intent(SplashActivity.this, WebviewScene.class));
			break;
		case 2:
			startActivity(new Intent(SplashActivity.this, DropBoxScene.class));
			break;
		case 3:
			startActivity(new Intent(SplashActivity.this, WebviewScene.class));
			break;
		}
		finish();
	}
}