package com.smartapps.designdroid.home;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ListView;
import android.widget.Toast;

import com.smaprtapps.designdroid.R;

public class VirtualStore {
	public static int counter = -1;
	public static Bitmap originBitmap;
	public static int progressFlag = 0, downloadingItemID = -1;
	public static boolean isDownloading;

	// save image as png
	public static boolean saveImageAsPng(Context context,
			SharedPreferences prefs, Bitmap bm) {
		OutputStream stream = null;
		String imgTyp = ".png";
		try {

			File folder = new File(prefs.getString("saving_path",
					"/sdcard/designdroid/") + "/" + "temp_folder");
			if (!folder.exists())
				folder.mkdir();

			stream = new FileOutputStream(prefs.getString("saving_path",
					"/sdcard/designdroid/")
					+ "/"
					+ "temp_folder"
					+ "/"
					+ ++counter + imgTyp);

			bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();
			return true;
		} catch (FileNotFoundException e) {
			Toast.makeText(context, context.getString(R.string.not_saved),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Toast.makeText(context, context.getString(R.string.not_saved),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		}
	}
}
