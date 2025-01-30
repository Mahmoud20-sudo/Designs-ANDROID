package com.smartapps.designdroid.home;

import java.io.File;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.database.DatabaseAdapter;
import com.smartapps.designdroid.main.MainEditor;

public class Home extends ActionBarActivity {

	private static final int SELECT_PICTURE = 1;
	static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	LinearLayout cameraBtn, galleryBtn, shopBtn, backgroundBtn, sysBtn,
			helpBtn, aboutBtn, templateBtn;
	SharedPreferences prefs;
	Intent mainScreenIntent;
	BuildFoldersHierarchy buildFolds;
	DatabaseAdapter AdDB;
	// ProgressBar homeBar;
	public static Window homeWindo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// for database work
		setContentView(R.layout.home);
		// homeBar = (ProgressBar) findViewById(R.id.home_bar);
		homeWindo = getWindow();
		AdDB = new DatabaseAdapter(this);
		AdDB.createDataBase();
		// shared preference for holding application categories folder path
		prefs = getSharedPreferences("designdroidprefs", MODE_PRIVATE);
		// this class builds designdroid folders structure in background

		// check if folder structure is done before
		if (AdDB.getStrPreferences("saving_path").equals("A")) {
			buildFolds = new BuildFoldersHierarchy(this, prefs);
		}

		if (!prefs.contains("show_help"))
			prefs.edit().putBoolean("show_help", true).commit();

		/*
		 * if (prefs.getBoolean("show_help", false)) { //Intent intent = new
		 * Intent(Home.this, HelpActivity.class); //startActivity(intent); }
		 */

		this.getSupportActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.custom_action_bar, null);

		TextView titleTextView = (TextView) v
				.findViewById(R.id.custom_action_bar_title);
		titleTextView.setText(this.getTitle());

		this.getSupportActionBar().setCustomView(v);
		// to get categories path11
		// prefs.getString("designdroid_path", "");
		mainScreenIntent = new Intent(Home.this, MainEditor.class);

		cameraBtn = (LinearLayout) findViewById(R.id.camera_btn);
		backgroundBtn = (LinearLayout) findViewById(R.id.background_btn);
		shopBtn = (LinearLayout) findViewById(R.id.shop_btn);
		galleryBtn = (LinearLayout) findViewById(R.id.gallery_btn);
		templateBtn = (LinearLayout) findViewById(R.id.template_btn);

		sysBtn = (LinearLayout) findViewById(R.id.sys_btn);
		helpBtn = (LinearLayout) findViewById(R.id.help_btn);
		aboutBtn = (LinearLayout) findViewById(R.id.about_btn);

		cameraBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File file = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "image.jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent,
						CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
			}
		});

		galleryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent galleryIntent = new Intent(Intent.ACTION_PICK);
				galleryIntent.setType("image/*");
				startActivityForResult(galleryIntent, SELECT_PICTURE);
				/*
				 * Toast.makeText(getApplicationContext(),
				 * prefs.getString("saving_path", ""),
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		});

		templateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent galleryIntent = new Intent(Intent.ACTION_PICK);
				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();
			}
		});
		backgroundBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent galleryIntent = new Intent(Intent.ACTION_PICK);
				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();
			}
		});
		shopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * Intent shopIntent = new Intent(Home.this, ShopTabs.class);
				 * startActivity(shopIntent);
				 */
				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();
			}
		});

		helpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();
			}
		});

		aboutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();
			}
		});

		sysBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent galleryIntent = new Intent(Intent.ACTION_PICK);
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			// When an Image is picked
			if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK
					&& null != data) {
				// Get the Image from data
				try {
					Uri selectedImage = data.getData();
					mainScreenIntent.putExtra("selectedimg_uri",
							selectedImage.toString());
					mainScreenIntent.putExtra("isCaptured", false);
					// mainScreenIntent.putExtra("img", imgDecodableString);
					startActivity(mainScreenIntent);

				} catch (Exception e) {
					Toast.makeText(this, "Something went wrong",
							Toast.LENGTH_LONG).show();
				}
			}
			if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE
					&& resultCode == RESULT_OK) {

				// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				File file = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "image.jpg");

				mainScreenIntent.putExtra("isCaptured", true);
				mainScreenIntent.putExtra("capturedimg_path", file
						.getAbsolutePath().toString());

				startActivity(mainScreenIntent);
			}
		}
	}
}
