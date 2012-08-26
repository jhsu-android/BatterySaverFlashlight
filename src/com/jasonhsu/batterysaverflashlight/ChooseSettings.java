package com.jasonhsu.batterysaverflashlight;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChooseSettings extends Activity {
	
	private int n_brightness;
	int n_red, n_green, n_yellow, n_white;
	int color_code;
	SeekBar BrightnessBar;
	TextView TextViewRed, TextViewGreen, TextViewYellow, TextViewWhite;
	TextView TextViewBrightness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        // Brightness Bar and Its Effects
        // Based on 
        // http://android-er.blogspot.com/2009/08/change-background-color-by-seekbar.html
        BrightnessBar = (SeekBar)findViewById(R.id.seekBarBrightness);
        TextViewRed = (TextView)findViewById(R.id.textViewRed);       
        TextViewGreen = (TextView)findViewById(R.id.textViewGreen);
        TextViewYellow = (TextView)findViewById(R.id.textViewYellow);
        TextViewWhite = (TextView)findViewById(R.id.textViewWhite);
        TextViewBrightness = (TextView)findViewById(R.id.textViewBrightness);
        UpdateBrightness();
        BrightnessBar.setOnSeekBarChangeListener(seekBarChangeListener);
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
    
    private int red_bk (int n_brightness) {
    	int result = rgb_bk (n_brightness, 0, 0);
    	return result;
    }
    
    private int green_bk (int n_brightness) {
    	int result = rgb_bk (0, n_brightness, 0);
    	return result;
    }
    
    private int yellow_bk (int n_brightness) {
    	int result = rgb_bk (n_brightness, n_brightness, 0);
    	return result;
    }
    
    private int white_bk (int n_brightness) {
    	int result = rgb_bk (n_brightness, n_brightness, n_brightness);
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
}
