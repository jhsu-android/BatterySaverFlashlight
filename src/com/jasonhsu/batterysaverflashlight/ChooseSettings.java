package com.jasonhsu.batterysaverflashlight;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class ChooseSettings extends Activity {
	TextView TextViewWifi1, TextViewWifi2;
	
	// Bluetooth configuration and status 
	TextView TextViewBluetooth1, TextViewBluetooth2;
	
	// GPS configuration and status 
	TextView TextViewGPS1, TextViewGPS2;
	
	TextView TextViewNotes;
	
	// For brightness settings
	private int n_brightness;
	int n_red, n_green, n_yellow, n_white;
	int color_code;
	SeekBar BrightnessBar;
	TextView TextViewRed, TextViewGreen, TextViewYellow, TextViewWhite;
	TextView TextViewBrightness;
	
	// For flashlight screen
	private LinearLayout FlashlightScreen;
	
	ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        // Update WiFi, Bluetooth, and GPS status
        UpdateStatus1 ();
        
        // Automatically update the Wifi, Bluetooth, and GPS
        //ScheduledExecutorService scheduledExecutorService;
        //scheduledExecutorService = Executors.newScheduledThreadPool(1);
      
        //scheduledExecutorService.scheduleWithFixedDelay( new Runnable(){
			//@Override
			//public void run() {
				//handler_update.sendMessage(handler_update.obtainMessage());
			//}
		//},1,1,TimeUnit.SECONDS);
                
        // When you enable WiFi
        Button ButtonWifiOn = (Button)findViewById(R.id.buttonWIFIon);
        ButtonWifiOn.setOnClickListener (new Button.OnClickListener () {
        	@Override
			public void onClick(View arg0) {
				WifiManager wifiManager = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(true);
        	}
        });
        	
        // When you disable WiFi
        Button ButtonWifiOff = (Button)findViewById(R.id.buttonWIFIoff);
        ButtonWifiOff.setOnClickListener (new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
				WifiManager wifiManager = (WifiManager)getBaseContext().getSystemService(Context.WIFI_SERVICE);
				wifiManager.setWifiEnabled(false);
        	}
        });
        
        // When you enable Bluetooth
        Button ButtonBluetoothOn = (Button)findViewById(R.id.buttonBLUETOOTHon);
        ButtonBluetoothOn.setOnClickListener(new Button.OnClickListener () {
        	public void onClick(View arg0) {
        		// Show that the system is in the process of enabling Bluetooth
        		TextViewBluetooth1 = (TextView)findViewById(R.id.textViewBLUETOOTHstatus);
        		TextViewBluetooth1.setText("Enabling");
        		TextViewBluetooth1.setTextColor(getResources().getColor(R.color.yellow_bright));
        		
        		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        		mBluetoothAdapter.enable();
        		UpdateBluetoothStatus1 ();
        	}
        });
        
        // When you disable Bluetooth
        Button ButtonBluetoothOff = (Button)findViewById(R.id.buttonBLUETOOTHoff);
        ButtonBluetoothOff.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View arg0) {
        		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        		mBluetoothAdapter.disable();
        		UpdateBluetoothStatus1 ();
        	}
        });
        
        // When you change GPS
        Button ButtonGPSOn = (Button)findViewById(R.id.buttonGPSchange);
        ButtonGPSOn.setOnClickListener(new Button.OnClickListener () {
        	@Override
        	public void onClick(View arg0) {
        		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        		startActivity(intent);
        		UpdateGPSstatus1 ();
        	}
        });
        
        // Brightness Bar and Its Effects
        BrightnessBar = (SeekBar)findViewById(R.id.seekBarBrightness);
        TextViewRed = (TextView)findViewById(R.id.textViewRed);       
        TextViewGreen = (TextView)findViewById(R.id.textViewGreen);
        TextViewYellow = (TextView)findViewById(R.id.textViewYellow);
        TextViewWhite = (TextView)findViewById(R.id.textViewWhite);
        TextViewBrightness = (TextView)findViewById(R.id.textViewBrightness);
        UpdateBrightness();
        BrightnessBar.setOnSeekBarChangeListener(seekBarChangeListener);
        
        // Buttons for entering flashlight mode
    	Button ButtonRed, ButtonGreen, ButtonYellow, ButtonWhite;
    	ButtonRed = (Button) findViewById(R.id.buttonRed);
    	ButtonGreen = (Button) findViewById(R.id.buttonGreen);
    	ButtonYellow = (Button) findViewById(R.id.buttonYellow);
    	ButtonWhite = (Button) findViewById(R.id.buttonWhite);
    	
    	// When you click on Red
        ButtonRed.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick (View v) {
        		StartFlashlightScreen();
        		FlashlightScreen.setBackgroundColor(red_bk(n_brightness));
        		FlashlightScreenInfo();
        	}
        });
        
    	// When you click on Green
        ButtonGreen.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick (View v) {
        		StartFlashlightScreen();
        		FlashlightScreen.setBackgroundColor(green_bk(n_brightness));
        		FlashlightScreenInfo();
        	}
        });
        
    	// When you click on Yellow
        ButtonYellow.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick (View v) {
        		StartFlashlightScreen();
        		FlashlightScreen.setBackgroundColor(yellow_bk(n_brightness));
        		FlashlightScreenInfo();
        	}
        });
        
    	// When you click on White
        ButtonWhite.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick (View v) {
        		StartFlashlightScreen();
        		FlashlightScreen.setBackgroundColor(white_bk(n_brightness));
        		FlashlightScreenInfo();
        	}
        });


    }
    
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener 
    		= new SeekBar.OnSeekBarChangeListener () {
    	
    	@Override
    	public void onProgressChanged (SeekBar seekbar, int progress, boolean fromUser) {
    		UpdateBrightness();
    	}
    	
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
    };
    
    private int rgb (int r_local, int g_local, int b_local) {
    	int r_rgb = r_local * 17;
    	int g_rgb = g_local * 17;
    	int b_rgb = b_local * 17;
    	int total = r_rgb*16*16*16*16 + g_rgb*16*16 + b_rgb;
    	return total;
    }
    
    private String rgb_hex (int r_local, int g_local, int b_local) {
    	int rgb_local = rgb (r_local, g_local, b_local);
    	String result = String.format("#%06X", (0xFFFFFF & rgb_local));
    	return result;
    }
    
    private int rgb_bk (int r_local, int g_local, int b_local) {
    	String hex_local = rgb_hex (r_local, g_local, b_local);
    	int result = Color.parseColor(hex_local);
    	return result;
    }
    
    private int red_bk (int n_local) {
    	int result = rgb_bk (n_local, 0, 0);
    	return result;
    }
    
    private int green_bk (int n_local) {
    	int result = rgb_bk (0, n_local, 0);
    	return result;
    }
    
    private int yellow_bk (int n_local) {
    	int result = rgb_bk (n_local, n_local, 0);
    	return result;
    }
    
    private int white_bk (int n_local) {
    	int result = rgb_bk (n_local, n_local, n_local);
    	return result;
    }


    private void UpdateBrightness () {
    	n_brightness = BrightnessBar.getProgress();
    	
    	n_red = red_bk (n_brightness);
    	n_green = green_bk (n_brightness);
    	n_yellow = yellow_bk (n_brightness);
    	n_white = white_bk (n_brightness);
    	
    	TextViewRed.setBackgroundColor(n_red);
    	TextViewRed.setTextColor(n_red);
    	
    	TextViewGreen.setBackgroundColor(n_green);
    	TextViewGreen.setTextColor(n_green);
    	
    	TextViewYellow.setBackgroundColor(n_yellow);
    	TextViewYellow.setTextColor(n_yellow);
    	
    	TextViewWhite.setBackgroundColor(n_white);
    	TextViewWhite.setTextColor(n_white);
    	
    	TextViewBrightness.setText(String.valueOf(n_brightness));
    }
    
    private void StartFlashlightScreen () {
    	// Creates new screen
    	FlashlightScreen = new LinearLayout(this);
    	FlashlightScreen.setOrientation(LinearLayout.VERTICAL);
    	
    	setContentView(FlashlightScreen); // Displays new screen
    }
    
    private void FlashlightScreenInfo () {
    	// Brightness setting
    	TextView TextViewBright = new TextView (this);
    	TextViewBright.setText("Brightness: " + String.valueOf(n_brightness));
    	FlashlightScreen.addView(TextViewBright);
    	
    	// WiFi status
    	TextViewWifi2 = new TextView (this);
    	this.registerReceiver(this.WifiStateChangedReceiver2, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    	FlashlightScreen.addView(TextViewWifi2);
    	
    	// Bluetooth status
    	TextViewBluetooth2 = new TextView (this);
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    // Device does not support Bluetooth
    		TextViewBluetooth2.setText("Bluetooth Status: N/A");
    	}
    	else { 
    		if (mBluetoothAdapter.isEnabled()) {
    			TextViewBluetooth2.setText("Bluetooth Status: Enabled");
    		}
    		else {
    			TextViewBluetooth2.setText("Bluetooth Status: Disabled, SAVING POWER");
    		}
    	}
    	FlashlightScreen.addView(TextViewBluetooth2);
    	
    	// GPS status
    	TextViewGPS2 = new TextView (this);
    	ContentResolver contentResolver = getBaseContext().getContentResolver();
        boolean GPS_TF = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (GPS_TF) {
        	TextViewGPS2.setText("GPS Status: Enabled");
        }
        else {
        	TextViewGPS2.setText("GPS Status: Disabled, SAVING POWER");
        }
    	FlashlightScreen.addView(TextViewGPS2);
    	
    	// Add other notes
    	TextViewNotes = new TextView (this);
    	TextViewNotes.setText("");
    	FlashlightScreen.addView(TextViewNotes);
    	
    	
    	// Black text for bright background, white text for dark background
    	if (n_brightness > 7) {
    		TextViewBright.setTextColor(getResources().getColor(R.color.black));
    		TextViewWifi2.setTextColor(getResources().getColor(R.color.black));
    		TextViewBluetooth2.setTextColor(getResources().getColor(R.color.black));
    		TextViewGPS2.setTextColor(getResources().getColor(R.color.black));
    		TextViewNotes.setTextColor(getResources().getColor(R.color.black));
    	}
    	else {
    		TextViewBright.setTextColor(getResources().getColor(R.color.white_bright));
    		TextViewWifi2.setTextColor(getResources().getColor(R.color.white_bright));
    		TextViewBluetooth2.setTextColor(getResources().getColor(R.color.white_bright));
    		TextViewGPS2.setTextColor(getResources().getColor(R.color.white_bright));
    		TextViewNotes.setTextColor(getResources().getColor(R.color.white_bright));
    	}
    	
    	
    }
    
	private BroadcastReceiver WifiStateChangedReceiver1
	= new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
   
			int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
					WifiManager.WIFI_STATE_UNKNOWN);
   
			
			switch(extraWifiState){
			case WifiManager.WIFI_STATE_DISABLED:
				TextViewWifi1.setText ("Disabled");
				TextViewWifi1.setTextColor(getResources().getColor(R.color.aqua_bright));
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				TextViewWifi1.setText("Disabling");
				TextViewWifi1.setTextColor(getResources().getColor(R.color.yellow_bright));
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				TextViewWifi1.setText("Enabled");
				TextViewWifi1.setTextColor(getResources().getColor(R.color.red_bright));
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				TextViewWifi1.setText("Enabling");
				TextViewWifi1.setTextColor(getResources().getColor(R.color.yellow_bright));
				break;
			case WifiManager.WIFI_STATE_UNKNOWN:
				TextViewWifi1.setText("Unknown");
				TextViewWifi1.setTextColor(getResources().getColor(R.color.yellow_bright));
				break;
			}
   
		}};
		
		private BroadcastReceiver WifiStateChangedReceiver2
		= new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
	   
				int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
						WifiManager.WIFI_STATE_UNKNOWN);
	   
				
				switch(extraWifiState){
				case WifiManager.WIFI_STATE_DISABLED:
					TextViewWifi2.setText ("WiFi Status: Disabled, SAVING POWER");
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					TextViewWifi2.setText("WiFi Status: Disabling");
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					TextViewWifi2.setText("WiFi Status: Enabled");
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					TextViewWifi2.setText("WiFi Status: Enabling");
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					TextViewWifi2.setText("WiFi Status: Unknown");
					break;
				}
	   
			}};
	
	private void UpdateWifiStatus1 () {
		TextViewWifi1 = (TextView)findViewById(R.id.textViewWIFIstatus);
		this.registerReceiver(this.WifiStateChangedReceiver1,
				new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
	}
	
	private void UpdateBluetoothStatus1 () {
		TextViewBluetooth1 = (TextView)findViewById(R.id.textViewBLUETOOTHstatus);
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    // Device does not support Bluetooth
    		TextViewBluetooth1.setText("N/A");
    		TextViewBluetooth1.setTextColor(getResources().getColor(R.color.aqua_bright));
    	}
    	else { 
    		if (mBluetoothAdapter.isEnabled()) {
    			TextViewBluetooth1.setText("Enabled");
    			TextViewBluetooth1.setTextColor(getResources().getColor(R.color.red_bright));
    		}
    		else if (mBluetoothAdapter.isDiscovering()) {
    			TextViewBluetooth1.setText("Enabling");
    			TextViewBluetooth1.setTextColor(getResources().getColor(R.color.yellow_bright));
    		}
    		else {
    			TextViewBluetooth1.setText("Disabled");
    			TextViewBluetooth1.setTextColor(getResources().getColor(R.color.aqua_bright));
    		}
    	}
	}
    
	private void UpdateGPSstatus1 () {
		TextViewGPS1 = (TextView)findViewById(R.id.textViewGPSstatus);
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		boolean GPS_TF = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
		if (GPS_TF) {
			TextViewGPS1.setText("Enabled");
			TextViewGPS1.setTextColor(getResources().getColor(R.color.red_bright));
		}
		else {
			TextViewGPS1.setText("Disabled");
			TextViewGPS1.setTextColor(getResources().getColor(R.color.aqua_bright));
		}
	}
	
	private void UpdateStatus1 () {
		UpdateWifiStatus1();
		UpdateBluetoothStatus1();
		UpdateGPSstatus1();
	}
	
    Handler handler_update = new Handler(){ 
    	@Override
    	public void handleMessage(Message msg) {
    		UpdateStatus1();
    	}
    };
}

// WiFi configuration and status 
// Based on
// http://android-er.blogspot.com/2011/01/turn-wifi-onoff-using.html

// GPS configuration and status
// Based on 
// http://android-coding.blogspot.com/2011/04/detect-gps-onoff-status-using.html
// http: //androidfreakers.blogspot.com/2011/09/enable-or-disable-gps-in-android.html 
// http://www.vogella.com/articles/AndroidLocationAPI/article.html#checklocationapi

// Bluetooth configuration and status based on
// From http://androiddesk.wordpress.com/2012/05/14/bluetooth-in-android/

// Automatic status updates based on
// http://android-coding.blogspot.com/2012/06/javautilconcurrentscheduledexecutorserv.html

// Brightness bar and its effects based on  
// http://android-er.blogspot.com/2009/08/change-background-color-by-seekbar.html

// NOTE: Using ScheduledExecutorService to automatically update the
// WiFi, Bluetooth, and GPS status destabilized the app and caused it to
// quit unexpectedly.  Apparently, there was a memory leak.