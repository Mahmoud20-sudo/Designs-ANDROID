package com.smartapps.designdroid.home;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.smaprtapps.designdroid.R;

public class TestActivity extends ActionBarActivity{
	public class Home extends ActionBarActivity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
		}
	}
}
