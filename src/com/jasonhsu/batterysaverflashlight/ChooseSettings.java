package com.jasonhsu.batterysaverflashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChooseSettings extends Activity {
	
	// The screen you see after you click Submit
	private LinearLayout FlashlightScreen;
	private LinearLayout StatusColor;
	private LinearLayout StatusWifi;
	private LinearLayout StatusGPS;
	private LinearLayout StatusBluetooth;
	
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
			    int n_brightness = Integer.parseInt(BrightnessValue.getText().toString());
			    
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

			    
				FlashlightScreenBackground();
			    AddStatusColor();
			    
				
				
				setContentView(FlashlightScreen); // Displays new screen


				
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
    
    private void AddStatusColor() {
    	FlashlightScreen = new LinearLayout(this);
    	
    	FlashlightScreen.setLayoutParams(new LayoutParams (LayoutParams.FILL_PARENT,
    		LayoutParams.WRAP_CONTENT));
    	FlashlightScreen.setOrientation(LinearLayout.VERTICAL);
    	
    	TextView TextView1 = new TextView (this);
    	TextView1.setText(String.valueOf(color_code));
	    
	    FlashlightScreen.addView(TextView1);
	    FlashlightScreen.setBackgroundColor(color_code);
    }
    private void FlashlightScreenBackground() {
    	FlashlightScreen = new LinearLayout(this);
    	FlashlightScreen.setBackgroundColor(color_code);
    }

}
