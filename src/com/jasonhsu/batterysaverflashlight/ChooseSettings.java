package com.jasonhsu.batterysaverflashlight;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChooseSettings extends Activity {
	
	// The screen you see after you click Submit
	private LinearLayout FlashlightScreen;
	TextView StatusWifi;
	TextView StatusGPS;
	TextView StatusBluetooth;
	
	int n_brightness;
	int color_code;
	
	
	// @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // Set up layout from /res/layout/settings.xml
        setContentView(R.layout.settings);

        // Get brightness value from the SeekBar
        SeekBar BrightnessBar = (SeekBar)findViewById(R.id.SeekBarBrightness);
        final TextView BrightnessValue = (TextView)findViewById(R.id.TextViewBrightnessLevel);
        
        // When the user changes the brightness value
        BrightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			
			// Executes when the user changes the value in the SeekBar
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// Update the displayed brightness value according to the SeekBar
				BrightnessValue.setText(String.valueOf(progress));
					
			}
			
		});
        
        
        
		// Executes when the user clicks on Submit
		Button Button1;
		Button1 = (Button) findViewById(R.id.ButtonSubmit);
				
		Button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Status of inputs
		        // Get the brightness setting
			    n_brightness = Integer.parseInt(BrightnessValue.getText().toString());
			    
			    // Convert the brightness setting into possible RGB values
			    int shade_red = rgb (n_brightness, 0, 0);
			    int shade_green = rgb (0, n_brightness, 0);
			    int shade_yellow = rgb (n_brightness, n_brightness, 0);
			    int shade_white = rgb (n_brightness, n_brightness, n_brightness);
			    
			    // Check which color is selected
				RadioButton IsRed = (RadioButton) findViewById(R.id.ButtonRed);
			    RadioButton IsGreen = (RadioButton) findViewById(R.id.ButtonGreen);
			    RadioButton IsYellow = (RadioButton) findViewById(R.id.ButtonYellow);

			    // Pick the appropriate RGB value based on the color selected
			    if (IsRed.isChecked()) {
					color_code = shade_red;
				}
				else if (IsGreen.isChecked()) {
					color_code = shade_green;
				}
				else if (IsYellow.isChecked()) {
					color_code = shade_yellow;
				}
				else {
					color_code = shade_white;
				}

				CreateFlashlightScreen();
			    

				setContentView(FlashlightScreen); // Displays new screen

				// Remove the title bar
		    	//requestWindowFeature(Window.FEATURE_NO_TITLE);
				
			}
		});
				
    }
    
    private int rgb (int r_local, int g_local, int b_local) {
    	int r_rgb = r_local * 17;
    	int g_rgb = g_local * 17;
    	int b_rgb = b_local * 17;
    	int total = r_rgb*16*16*16*16 + g_rgb*16*16 + b_rgb;
    	return total;
    }
    
    private void CreateFlashlightScreen() {
    	// Provides the appropriate screen color and brightness
    	FlashlightScreen = new LinearLayout(this);
    	String hex_color = String.format("#%06X", (0xFFFFFF & color_code));
    	FlashlightScreen.setBackgroundColor(Color.parseColor(hex_color));
    	
    	// Provide brightness setting
    	TextView TextViewBright = new TextView (this);
    	TextViewBright.setText("Brightness: " + String.valueOf(n_brightness));
    	FlashlightScreen.addView(TextViewBright);
    	
    	// Wifi status
    	// NOTE: This does NOT work in the AVD, only on an actual device.
    	StatusWifi = new TextView (this);
    	this.registerReceiver(this.WifiStateChangedReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    	FlashlightScreen.addView(StatusWifi);
    	
    	// Black text for bright background, white text for dark background
    	if (n_brightness > 7) {
    		TextViewBright.setTextColor(getResources().getColor(R.color.black));
    		StatusWifi.setTextColor(getResources().getColor(R.color.black));
    	}
    	else {
    		TextViewBright.setTextColor(getResources().getColor(R.color.white_bright));
    		StatusWifi.setTextColor(getResources().getColor(R.color.white_bright));
    	}
    	

    }
    
    private BroadcastReceiver WifiStateChangedReceiver
    = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE , 
					WifiManager.WIFI_STATE_UNKNOWN);
			
			switch(extraWifiState){
			case WifiManager.WIFI_STATE_DISABLED:
				StatusWifi.setText("\nWiFi Status: Disabled");
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				StatusWifi.setText("\nWiFi Status: Disabling");
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				StatusWifi.setText("\nWifi Status: Enabled");
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				StatusWifi.setText("\nWifi Status: Enabling");
				break;
			case WifiManager.WIFI_STATE_UNKNOWN:
				StatusWifi.setText("\nWiFi Status: Unknown");
				break;
			}
			
		}};

}
