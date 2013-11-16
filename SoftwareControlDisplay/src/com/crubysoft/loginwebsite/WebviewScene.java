package com.crubysoft.loginwebsite;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

/*
 * @crubysoft cpanda
 */
public class WebviewScene extends Activity implements OnClickListener,
		OnTouchListener {
	// private static String ServerURL1 = "http://url2it.com/ioqdt";
	private static String ServerURL = "http://www.aaronvanderzwan.com/maximage/examples/basic.html";
	private static String url;
	private SharedPreferences prvPref;
	private SharedPreferences.Editor prevEditor;

	private static final int INPUT_ERROR = 10;
	private static final int EXTERNAL_MEDIA = 4;
	private static final int EXTERNAL = 7;
	private String localPath;
	private ProgressBar progressbar;
	private EditText urlTxt;
	private CountDownTimer mainTimer;
	private LinearLayout webViewLay;
	private MyWebView webview;
	private ProgressBar webviewPgr;
	private AbsoluteLayout mainLay;
	private static final int DIALOG_NO_INTERNET = 5;

	private Button btn[] = new Button[12];
	private EditText userInput;
	private int mScreenW, mScreenH;
	private LinearLayout lay_numberPad, lay_accessDenied;
	private Handler handle = new Handler();
	private Runnable running = null;

	private Timer timer;
	private TimerTask task;
	private static long period = 30;

	private static String password = null, touchSecs = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		prvPref = PreferenceManager.getDefaultSharedPreferences(this);
		prevEditor = prvPref.edit();
		url = prvPref.getString("CUSTOM_URL", "http://");
		String refreshSecs = prvPref.getString("REFRESH_SECS", "0");
		password = prvPref.getString("PASSWORD", "");
		touchSecs = prvPref.getString("TOUCH_SECS", "0");
		
		Log.e("exception", "refreshSecs_webview:" + refreshSecs);
		Log.e("exception", "password_ webview:" + password);
		Log.e("exception", "touchSecs_ webview:" + touchSecs);
		
		
		try {
			period = Integer.parseInt(refreshSecs);
			Log.e("exception", "invalid period:" + period);
		} catch (NumberFormatException ex) {
			Log.e("exception", "invalid period:" + period);
		}

		// url = url.substring(url.toLowerCase().lastIndexOf("http://") + 7);

		if (timer != null) {
			timer.purge();
			timer.cancel();
			timer = null;
		}

		if (task != null) {
			task.cancel();
			task = null;
		}

		mainLay = (AbsoluteLayout) findViewById(R.id.mainLay);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		webViewLay = (LinearLayout) findViewById(R.id.webViewLay);
		webview = new MyWebView(this);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		webViewLay.addView(webview, param);
		webviewPgr = (ProgressBar) findViewById(R.id.webviewPgr);
		webviewPgr.setVisibility(View.GONE);
		// if (serverURL.isEmpty())
		// showDialog(SETTING_DLG);
		showeb(url);

		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				showeb(url);
			}
		};
		if(period != 0)
			timer.schedule(task, period * 1000, period * 1000);

		// number pad
		mScreenW = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		mScreenH = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();

		lay_accessDenied = (LinearLayout) findViewById(R.id.lay_accessDenied);
		lay_numberPad = (LinearLayout) findViewById(R.id.lay_numberPad);

		userInput = (EditText) findViewById(R.id.numberPadTxt);
		userInput.setFocusable(false);
		userInput.setHeight((int) (mScreenH * 0.1));
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
		for (int i = 0; i < btn.length; i++) {
			btn[i] = (Button) findViewById(R.id.button1 + i);
			btn[i].setWidth((int) (mScreenW * 0.2));
			btn[i].setHeight((int) (mScreenH * 0.1));
			btn[i].setTag(i);
			btn[i].setOnClickListener(this);
		}

	}

	private void showeb(String webUrl) {
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLoadsImagesAutomatically(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.setLongClickable(true);
		// webview.setInitialScale(1);
		webview.setWebViewClient(new MyWebViewClient());
		webview.loadUrl(url);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			webviewPgr.setVisibility(View.GONE);
			webviewPgr.setProgress(100);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			webviewPgr.setVisibility(View.VISIBLE);
			webviewPgr.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("serverurl", url);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DIALOG_NO_INTERNET:
			Dialog alertDlg = new AlertDialog.Builder(this)
					.setTitle(R.string.no_internet_title)
					.setIcon(android.R.drawable.stat_sys_warning)
					.setMessage(R.string.no_internet_message)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final Intent intent = new Intent(
											Intent.ACTION_MAIN, null);
									intent.addCategory(Intent.CATEGORY_LAUNCHER);
									final ComponentName cn = new ComponentName(
											"com.android.settings",
											"com.android.settings.wifi.WifiSettings");
									intent.setComponent(cn);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							}).create();
			return alertDlg;

		case INPUT_ERROR:
			Dialog errorDlg = new AlertDialog.Builder(this)
					.setTitle("Try Again!")
					.setMessage("Both fields are required")
					.setCancelable(false).setPositiveButton("OK", null)
					.create();

			return errorDlg;

		case EXTERNAL_MEDIA:
			Dialog dlgBuilder = new AlertDialog.Builder(this)
					.setTitle("No External Media")
					.setIcon(android.R.drawable.stat_sys_warning)
					.setMessage(
							"There is No External Media. \n Please insert External Media and open the app.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).create();

			return dlgBuilder;

		case EXTERNAL:
			Dialog d = new AlertDialog.Builder(this)
					.setTitle(Utils.sdcardPath)
					.setIcon(android.R.drawable.stat_sys_warning)
					.setMessage(
							"Current file is" + localPath + ": \n"
									+ " App Path : " + Utils.getAppPath())
					.setCancelable(false).setPositiveButton("OK", null)
					.create();

			return d;
		default:
			return null;
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
			return true;
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mainTimer != null) {
			mainTimer.cancel();
			mainTimer = null;
		}
		if (running != null)
			handle.removeCallbacks(running);
		if (timer != null)
			timer.cancel();
	}

	private String getDeviceID() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phoneSN = tm.getDeviceId();// .getLine1Number();
		return phoneSN;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveBtn:
			String tmpUrl = urlTxt.getText().toString();
			prevEditor = prvPref.edit();
			prevEditor.putString("serverurl", tmpUrl);
			url = tmpUrl;
			if (tmpUrl.length() < 1 || !tmpUrl.contains("http://")) {
				AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
				dlgBuilder
						.setTitle("Try Again!")
						.setMessage(
								"The field is incorrect. \n Be sure you're using web url for you")
						.setCancelable(false).setPositiveButton("OK", null)
						.create().show();
			} else {
				prevEditor.commit();
				webview.clearCache(true);
				webview.clearHistory();
				webview.setWebViewClient(new MyWebViewClient());
				webview.loadUrl(url);
			}
			break;
		}

		int tag = Integer.parseInt(String.valueOf(v.getTag()));
		switch (tag) {
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
			if (sLen > 0) {
				String selection = userInput.getText().toString().substring(0, sLen - 1);
//				String result = userInput.getText().toString()
//						.replace(selection, "");
				userInput.setText(selection);
				userInput.setSelection(userInput.getText().length());
			}
			break;
		case 11:
			String tmpPass = userInput.getText().toString();
			if (tmpPass.equalsIgnoreCase(password)) {
				Intent scene = new Intent(this, SetupScene.class);
				startActivity(scene);
				finish();
				lay_numberPad.setVisibility(View.VISIBLE);
				lay_accessDenied.setVisibility(View.INVISIBLE);
			} else {
				lay_numberPad.setVisibility(View.INVISIBLE);
				lay_accessDenied.setVisibility(View.VISIBLE);
				running = new Runnable() {
					@Override
					public void run() {
						lay_accessDenied.setVisibility(View.INVISIBLE);
						webViewLay.setVisibility(View.VISIBLE);
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

	public void addToArray(int number) {
		userInput.append(number + "");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// switch(v.getId()){
			// case R.id.surface_view:
			// if(!mediaCtrl.isShown())
			// mVideoView.setMediaController(mediaCtrl);
			// break;
			// }
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			progressbar.setVisibility(View.GONE);
			if (mainTimer != null) {
				mainTimer.cancel();
				mainTimer = null;
			}
		}
		return false;
	}

	public class MyWebView extends WebView {
		int x, y;

		public MyWebView(Context context) {
			super(context);
			this.setLongClickable(true);
			this.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
							progressbar.getWidth(), progressbar.getHeight(), x,
							y);
					mainLay.removeAllViews();
					mainLay.addView(progressbar, params);
					progressbar.setVisibility(View.VISIBLE);

					int secs = Integer.parseInt(touchSecs);
					Log.e("dddd", "totuchc secs:" +  secs);
					mainTimer = new CountDownTimer(secs * 1000, 100) {
						@Override
						public void onTick(long millisUntilFinished) {
						}
						@Override
						public void onFinish() {
							progressbar.setVisibility(View.GONE);
							if(password.isEmpty()){
								Intent scene = new Intent(WebviewScene.this, SetupScene.class);
								startActivity(scene);
								finish();
							}else
								CheckPASSWD();
						}
					};
					mainTimer.start();
					return false;
				}
			});
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			x = (int) event.getX() - progressbar.getWidth() / 2;
			y = (int) event.getY() - progressbar.getHeight() / 2;
			if (event.getAction() == MotionEvent.ACTION_UP) {
				progressbar.setVisibility(View.GONE);
				if (mainTimer != null) {
					mainTimer.cancel();
					mainTimer = null;
				}
			}
			return super.onTouchEvent(event);
		}
	}

	private void CheckPASSWD() {
		webviewPgr.setVisibility(View.INVISIBLE);
		webViewLay.setVisibility(View.INVISIBLE);
		lay_numberPad.setVisibility(View.VISIBLE);
		lay_accessDenied.setVisibility(View.INVISIBLE);
	}
}
