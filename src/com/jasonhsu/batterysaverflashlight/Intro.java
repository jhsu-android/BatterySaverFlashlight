package com.jasonhsu.batterysaverflashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Intro extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
    }
    
    Button button; // Initializes button
    
    public void showOptions(View v) {
    	startActivity(new Intent(this, ChooseSettings.class));
    	}

    
}
