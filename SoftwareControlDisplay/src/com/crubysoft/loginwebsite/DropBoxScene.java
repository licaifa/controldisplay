package com.crubysoft.loginwebsite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DropBoxScene extends Activity implements OnClickListener{

	private int mScreenW, mScreenH;
	private static String PASSWD = "00000";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dropbox);

		mScreenW = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		mScreenH = getWindow().getWindowManager().getDefaultDisplay().getHeight();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}


	@Override
	public void onClick(View v) {
		int tag = Integer.parseInt(String.valueOf(v.getTag()));
		switch(tag){
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

}

