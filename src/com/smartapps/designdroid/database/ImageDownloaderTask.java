package com.smartapps.designdroid.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.database.DatabaseAdapter;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;

	ProgressBar bar;
	int counter;
	Context context;
	boolean isThumb;
	int subCatID;

	public ImageDownloaderTask(ImageView imageView, ProgressBar progBar,
			Context context, boolean isThumb, int subCatID) {
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.bar = progBar;
		this.context = context;
		this.isThumb = isThumb;
		this.subCatID = subCatID;
	}



	@Override
	protected Bitmap doInBackground(String... params) {
		return downloadBitmap(params[0]);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				if (bitmap != null) {
					if (imageView != null)
						imageView.setImageBitmap(bitmap);

					if (this.bar != null)
						this.bar.setVisibility(View.GONE);
				} else {
					Drawable placeholder = imageView.getContext()
							.getResources().getDrawable(R.drawable.ic_launcher);
					// imageView.setImageDrawable(placeholder);
				}
			}
		}
	}

	private Bitmap downloadBitmap(String url) {
		HttpURLConnection urlConnection = null;
		try {
			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			int statusCode = urlConnection.getResponseCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			InputStream inputStream = urlConnection.getInputStream();
			if (inputStream != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				saveImageAsPng(bitmap);
				return bitmap;
			}
		} catch (Exception e) {
			urlConnection.disconnect();
			Log.w("ImageDownloader", "Error downloading image from " + url);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	private boolean saveImageAsPng(Bitmap bm) {
		OutputStream stream = null;
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		String path = dbAdapter.getStrPreferences("saving_path");
		final Random random = new java.util.Random();
		String imgTyp = ".png";
		try {
			File folder = new File(path + "/" + "shopimg" + "/"
					+ random.nextInt() + imgTyp);

			stream = new FileOutputStream(folder.getAbsolutePath());

			bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();
			if (isThumb)
				dbAdapter.insertimagepath("ThumbImg", folder.getAbsolutePath(),
						subCatID);
			else
				dbAdapter.insertimagepath("PrevImg", folder.getAbsolutePath(),
						subCatID);
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