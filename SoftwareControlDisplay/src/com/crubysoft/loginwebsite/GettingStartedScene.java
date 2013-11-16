package com.crubysoft.loginwebsite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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

public class GettingStartedScene extends Activity implements android.view.GestureDetector.OnGestureListener, OnClickListener{

	private GestureDetector gd;
	private Button btn[] = new Button[12];
	private EditText userInput;
	private int mScreenW, mScreenH;
	private LinearLayout lay_numberPad, lay_accessDenied;
	private static String PASSWD = "00000";
	private Handler handle = new Handler();
	private Runnable running = null;
	private static String password = null, touchSecs = null;
	private SharedPreferences prvPref;
	private SharedPreferences.Editor prevEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			    WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.getting_started);

		prvPref = PreferenceManager.getDefaultSharedPreferences(this);
		prevEditor = prvPref.edit();
		password = prvPref.getString("PASSWORD", PASSWD);
		touchSecs = prvPref.getString("TOUCH_SECS", "0");
		Log.e("dddd", "ddd: " + PASSWD);
//		prevEditor.putString("PASSWORD", PASSWD);
		mScreenW = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		mScreenH = getWindow().getWindowManager().getDefaultDisplay().getHeight();

		lay_accessDenied = (LinearLayout)findViewById(R.id.lay_accessDenied);
		lay_numberPad = (LinearLayout)findViewById(R.id.lay_numberPad);

		userInput = (EditText)findViewById(R.id.numberPadTxt);
		userInput.setFocusable(false);
		userInput.setHeight((int)(mScreenH * 0.1));
		
		userInput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(userInput.getText().toString().length() < 1)
					userInput.setBackgroundResource(R.drawable.edit);
				else
					userInput.setBackgroundResource(0);
			}
		});
		
		for(int i = 0; i < btn.length; i ++ ){
			btn[i] = (Button)findViewById(R.id.button1 + i);
			btn[i].setWidth((int)(mScreenW * 0.2));
			btn[i].setHeight((int)(mScreenH * 0.1));
			btn[i].setTag(i);
			btn[i].setOnClickListener(this);
		}
		//initialize the Gesture Detector
        gd = new GestureDetector(this);
        gd.setIsLongpressEnabled(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		running = new Runnable(){
			@Override
			public void run() {
				CheckPASSWD();
			}
		};
		try{
			int secs = Integer.parseInt(touchSecs);
			handle.postDelayed(running, (secs - 1) * 1000);
		}catch(NumberFormatException ex){
			Log.e("touchSecs", "touchSecs: " + touchSecs);
		}
	}

	private void CheckPASSWD(){
		lay_numberPad.setVisibility(View.VISIBLE);
		lay_accessDenied.setVisibility(View.INVISIBLE);
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
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
		case 8:
			addToArray(tag + 1);
			break;
		case 10:	
			addToArray(0);
			break;
		case 9:
			int sLen = userInput.length();
			if(sLen > 0){
				String selection = userInput.getText().toString().substring(0, sLen - 1);
//				String result = userInput.getText().toString().replaceFirst(selection, "");
				userInput.setText(selection);
				userInput.setSelection(userInput.getText().length());
			}
			break;
		case 11:
			String tmpPass = userInput.getText().toString();
			if(tmpPass.equalsIgnoreCase(password) ||tmpPass.equalsIgnoreCase(PASSWD) ){
				Intent scene = new Intent(this, SetupScene.class);
				startActivity(scene);
				finish();
				lay_numberPad.setVisibility(View.VISIBLE);
				lay_accessDenied.setVisibility(View.INVISIBLE);
			}else{
				lay_numberPad.setVisibility(View.INVISIBLE);
				lay_accessDenied.setVisibility(View.VISIBLE);
				running = new Runnable(){
					@Override
					public void run() {
						lay_accessDenied.setVisibility(View.INVISIBLE);
						userInput.setText("");
						userInput.setBackgroundResource(R.drawable.edit);

					}
				};
				handle.postDelayed(running, 1000);
			}
			
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(running != null)
			handle.removeCallbacks(running);
	}

	public void addToArray(int number){
		userInput.append(number + "");
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}
}

