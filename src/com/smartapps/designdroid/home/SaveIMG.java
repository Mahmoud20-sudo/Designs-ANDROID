package com.smartapps.designdroid.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.sax.StartElementListener;

import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.main.MainEditor;

public class SaveIMG extends AsyncTask<Bitmap, Bitmap, Bitmap> {

	Context context;
	SharedPreferences prefs;
	ProgressDialog progDialog;
	public static boolean saveFlag = false;

	public SaveIMG(Context context, SharedPreferences prefs) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.prefs = prefs;
		progDialog = new ProgressDialog(context);
		progDialog.setMessage(context.getString(R.string.saveprog_title));
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(true);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		progDialog.dismiss();
		if (saveFlag) {
			Intent mainIntent = new Intent(((Activity) context),
					MainEditor.class);
			((Activity) context).startActivity(mainIntent);
			saveFlag = false;
		}
		((Activity) context).finish();
	}

	@Override
	protected void onPreExecute() {
		progDialog.show();
	}

	@Override
	protected Bitmap doInBackground(Bitmap... params) {
		VirtualStore.saveImageAsPng(context, prefs, params[0]);
		return null;
	}
}
