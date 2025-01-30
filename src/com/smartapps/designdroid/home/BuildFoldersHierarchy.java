package com.smartapps.designdroid.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.smaprtapps.designdroid.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BuildFoldersHierarchy {

	private static final String CATEGORYFOLDER = "designdroid/categories";
	private ArrayList<ArrayList<Object>> getallsubcategory;
	// private static final String APPFOLDER = "designdroid";
	private Context context;
	SharedPreferences prefs;
	Editor editor;
	ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private com.smartapps.designdroid.database.DatabaseAdapter AdDB;

	public BuildFoldersHierarchy(Context context, SharedPreferences prefs) {
		this.context = context;
		this.prefs = prefs;
		AdDB = new com.smartapps.designdroid.database.DatabaseAdapter(context);

		Home.homeWindo.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

		new BuildFoldersStructure().execute();
		editor = prefs.edit();
	}

	private void copyFolder(boolean isUsingSDCard) {
		// "Name" is the name of your folder!
		AssetManager assetManager = context.getAssets();
		String[] files = null;
		// We can read and write the media
		// Checking file on assets subfolder
		try {
			files = assetManager.list(CATEGORYFOLDER);
		} catch (IOException e) {
			Log.e("ERROR", "Failed to get asset file list.", e);
		}
		// Analyzing all file on assets subfolder
		for (String mainfolder : files) {
			try {
				files = assetManager.list(CATEGORYFOLDER + "/" + mainfolder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String internalfoldername : files) {
				try {
					files = assetManager.list(CATEGORYFOLDER + "/" + mainfolder
							+ "/" + internalfoldername);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (String filename : files) {
					// First: checking if there is already a target folder
					if (isUsingSDCard) {
						// SAVE IN EXTERNAL STORAGE
						File folder = new File(
								Environment.getExternalStorageDirectory() + "/"
										+ "." + CATEGORYFOLDER + "/"
										+ mainfolder + "/" + internalfoldername);
						boolean success = true;
						if (!folder.exists()) {
							success = folder.mkdirs();
						}
						if (success) {
							// Moving all the files on external SD
							editor.putString("saving_path",
									Environment.getExternalStorageDirectory()
											+ "/" + "." + CATEGORYFOLDER);

							editor.commit();
							copyAssets(folder, CATEGORYFOLDER + "/"
									+ mainfolder + "/" + internalfoldername);
							counter++;
							publishProgress("" + counter);

						} else {
							// Do something else on failure
						}
					} else {
						// SAVE IN INTERNAL STORAG
						File myDir = new File(context.getFilesDir() + "/"
								+ CATEGORYFOLDER + "/" + mainfolder + "/"
								+ internalfoldername);
						boolean success = true;
						if (!myDir.exists()) {
							success = myDir.mkdirs();
						}
						if (success) {
							// Moving all the files on external SD
							editor.putString("saving_path",
									context.getFilesDir() + "/"
											+ CATEGORYFOLDER);
							editor.commit();

							copyAssets(myDir, CATEGORYFOLDER + "/" + mainfolder
									+ "/" + internalfoldername);

							Drawable.createFromPath(myDir.getPath() + "/"
									+ filename);
							counter++;
							publishProgress("" + counter);

						} else {
							// Do something else on failure
						}

					}
				}
			}
		}

	}

	int counter;

	private void copyAssets(File folder, String fileName) {
		AssetManager assetManager = context.getAssets();
		String[] files = null;
		try {
			files = assetManager.list(fileName);
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(fileName + "/" + filename);
				File outFile = new File(folder, filename);
				out = new FileOutputStream(outFile);
				copyFile(in, out);
			} catch (IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// NOOP
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// NOOP
					}
				}
			}
		}
	}

	private void publishProgress(String... progress) {
		// TODO Auto-generated method stub
		mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private boolean checkStorage() {
		// Get the external storage's state
		String state = Environment.getExternalStorageState();

		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// Storage is available and writeable
			// externalStorageAvailable = externalStorageWriteable = true;
			return true;
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			// Storage is only readable
			return false;
		} else {
			// Storage is neither readable nor writeable
			return false;
		}
	}

	public class BuildFoldersStructure extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (checkStorage()) {
				// use SDCard
				copyFolder(true);
			} else {
				copyFolder(false);
			}

			return null;
		}

		@Override
		protected void onPreExecute() {

			mProgressDialog = new ProgressDialog(context);
			// Set your progress dialog Title
			mProgressDialog.setTitle("Progress Bar Tutorial");
			// Set your progress dialog Message
			mProgressDialog.setMessage("Downloading, Please Wait!");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Show progress dialog
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			mProgressDialog.setContentView(R.layout.preparing_dialog);
		}

		@Override
		protected void onPostExecute(Void result) {

			// add path to the db
			mProgressDialog.dismiss();
			Home.homeWindo
					.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			String path = prefs.getString("saving_path", null);
			AdDB.updateStrPreferences("saving_path", path);

			// create folder to put images for shop in it.
			File folder = new File(path + "/shopimg");
			if (!folder.isDirectory()) {
				folder.mkdir();
				copyAssets(folder, CATEGORYFOLDER + "/" + "shopimg");
			}

			File savingFolder = new File(
					Environment.getExternalStorageDirectory() + "/" + "Design");
			if (!savingFolder.exists()) {
				savingFolder.mkdir();
			}

			createmissedfolder(path);

		}
	}

	private void createmissedfolder(String path) {
		getallsubcategory = new ArrayList<ArrayList<Object>>();
		getallsubcategory.addAll(AdDB.getAllSubCategory());
		int foldernumber = getallsubcategory.size();
		for (int i = 0; i < foldernumber; i++) {
			int subcategoryid = Integer.valueOf(getallsubcategory.get(i).get(6)
					.toString());
			/*
			 * String Categoryname = AdDB.getcategorybyID(Integer
			 * .valueOf(getallsubcategory.get(i).get(2).toString()));
			 */
			String foldername = getallsubcategory.get(i).get(1).toString();

			String thumbimagename = foldername + "thumb" + subcategoryid
					+ ".png";
			String previmagename = foldername + "prev" + subcategoryid + ".png";
			AdDB.updateimagpath("ThumbImg",
					path + "/shopimg/" + thumbimagename, subcategoryid);
			AdDB.updateimagpath("PrevImg", path + "/shopimg/" + previmagename,
					subcategoryid);
		}

		// /storage/emulated/0/.designdroid/categories/shopimg/Backgroundthumb0.png
	}

}
