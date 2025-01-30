package com.smartapps.designdroid.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.frames.FrameActivity;
import com.smartapps.designdroid.home.VirtualStore;
import com.smartapps.designdroid.stickers.StikcersActivity;
import com.smartapps.designdroid.text.TextActivity;
import com.smartapps.designdroid.tools.ToolsEdit;

public class MainEditor extends ActionBarActivity {

	LinearLayout frameBtn, stickersBtn, effectBtn, addTxtBtn, toolsBtn,
			cancelBtn, saveBtn;
	FrameLayout resetBtn, backBtn, nextBtn, shareBtn;
	SharedPreferences prefs;
	ProgressBar progBar;
	ImageView imgHolder;
	public static int FRAMES_ACTIVITY = 1;
	public static int STICKERS_ACTIVITY = 2;
	public static int TEXT_ACTIVITY = 3;
	public static int EFFECTS_ACTIVITY = 4;
	public static int TOOLS_ACTIVITY = 5;
	public static boolean isMainImg;
	int step = 10;
	int max = 100;
	int min = 0;
	int imageQuality;
	int rad;
	String imgTyp;
	boolean isSaved;
	Handler handler;
	File savingFolder;
	ProgressDialog progDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		this.getSupportActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.custom_action_bar, null);

		TextView titleTextView = (TextView) v
				.findViewById(R.id.custom_action_bar_title);
		titleTextView.setText(this.getTitle());

		this.getSupportActionBar().setCustomView(v);

		prefs = getSharedPreferences("designdroidprefs", MODE_PRIVATE);

		File folder = new File(prefs.getString("saving_path",
				"/sdcard/designdroid/") + "/" + "temp_folder");
		if (!folder.exists())
			folder.mkdir();

		savingFolder = new File(Environment.getExternalStorageDirectory() + "/"
				+ "Design");

		handler = new Handler();

		progDialog = new ProgressDialog(MainEditor.this);
		progDialog.setMessage(MainEditor.this
				.getString(R.string.saveprog_title));
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(true);

		imgHolder = (ImageView) findViewById(R.id.image_holder);
		imgHolder.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		frameBtn = (LinearLayout) findViewById(R.id.frame_btn);
		stickersBtn = (LinearLayout) findViewById(R.id.stck_btn);
		effectBtn = (LinearLayout) findViewById(R.id.effect_btn);
		addTxtBtn = (LinearLayout) findViewById(R.id.txt_btn);
		toolsBtn = (LinearLayout) findViewById(R.id.tools_btn);
		cancelBtn = (LinearLayout) findViewById(R.id.cancel_btn);
		saveBtn = (LinearLayout) findViewById(R.id.save_btn);

		resetBtn = (FrameLayout) findViewById(R.id.reset_btn);
		backBtn = (FrameLayout) findViewById(R.id.back_btn);
		nextBtn = (FrameLayout) findViewById(R.id.next_btn);
		shareBtn = (FrameLayout) findViewById(R.id.share_btn);

		progBar = (ProgressBar) findViewById(R.id.imgholder_progbar);

		// Drawable editableImage = new
		// BitmapDrawable(getResources(),VirtualStore.originBitmap);
		Intent intent = getIntent();
		if (intent.getStringExtra("selectedimg_uri") != null
				&& !intent.getBooleanExtra("isCaptured", false))
			new LoadImg().execute(Uri.parse(intent
					.getStringExtra("selectedimg_uri")));
		else if (intent.getStringExtra("capturedimg_path") != null
				&& intent.getBooleanExtra("isCaptured", false)) {
			Bitmap result = BitmapFactory.decodeFile(intent
					.getStringExtra("capturedimg_path"));
			imgHolder.setImageBitmap(result);
			// VirtualStore.saveImageAsPng(getApplicationContext(), prefs,
			// result);
			isMainImg = true;
			// new SaveIMG(MainEditor.this, prefs).execute(result);
			try {
				copyFile(
						new File(intent.getStringExtra("capturedimg_path")),
						new File(prefs.getString("saving_path",
								"/sdcard/designdroid/")
								+ "/"
								+ "temp_folder"
								+ "/" + ++VirtualStore.counter + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// bmp = VirtualStore.originBitmap;
			File file = new File(prefs.getString("saving_path",
					"/sdcard/designdroid/")
					+ "/"
					+ "temp_folder"
					+ "/"
					+ VirtualStore.counter + ".png");
			Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
			imgHolder.setImageBitmap(bmp);
		}

		frameBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent frameIntent = new Intent(MainEditor.this,
						FrameActivity.class);
				startActivityForResult(frameIntent, 1);
			}
		});

		stickersBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent stcksIntent = new Intent(MainEditor.this,
						StikcersActivity.class);
				startActivityForResult(stcksIntent, 7);

			}
		});

		effectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();

			}
		});

		addTxtBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"This features not available in this demo version",
						Toast.LENGTH_SHORT).show();

			}
		});

		toolsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toolsIntent = new Intent(MainEditor.this,
						ToolsEdit.class);
				startActivityForResult(toolsIntent, 5);

			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCloseDialog();
			}
		});

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSavingDialog();
			}
		});

		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				while (VirtualStore.counter > 0) {
					File file = new File(prefs.getString("saving_path",
							"/sdcard/designdroid/")
							+ "/"
							+ "/"
							+ VirtualStore.counter + ".png");
					if (file.exists())
						file.delete();
					VirtualStore.counter--;
				}

				resetBtn.setEnabled(false);
				nextBtn.setEnabled(false);
				backBtn.setEnabled(false);
				File file = new File(prefs.getString("saving_path",
						"/sdcard/designdroid/")
						+ "/"
						+ "temp_folder"
						+ "/"
						+ VirtualStore.counter + ".png");
				imgHolder.setImageBitmap(BitmapFactory.decodeFile(file
						.getAbsolutePath()));
			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextBtn.setEnabled(true);

				VirtualStore.counter--;

				File file = new File(prefs.getString("saving_path",
						"/sdcard/designdroid/")
						+ "/"
						+ "temp_folder"
						+ "/"
						+ VirtualStore.counter + ".png");
				if (file.length() != 0)
					imgHolder.setImageBitmap(BitmapFactory.decodeFile(file
							.getAbsolutePath()));
				else {
					VirtualStore.counter++;
					// backBtn.setEnabled(false);
					return;
				}

				if (VirtualStore.counter - 1 < 0) {
					backBtn.setEnabled(false);
				}

			}
		});

		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backBtn.setEnabled(true);
				VirtualStore.counter++;
				File file = new File(prefs.getString("saving_path",
						"/sdcard/designdroid/")
						+ "/"
						+ "temp_folder"
						+ "/"
						+ VirtualStore.counter + ".png");
				if (file.length() != 0)
					imgHolder.setImageBitmap(BitmapFactory.decodeFile(file
							.getAbsolutePath()));
				else {
					VirtualStore.counter--;
					// nextBtn.setEnabled(false);
					return;
				}

				if (new File(prefs.getString("saving_path",
						"/sdcard/designdroid/")
						+ "/"
						+ "temp_folder"
						+ "/"
						+ (VirtualStore.counter + 1) + ".png")
						.getAbsoluteFile().length() == 0) {
					nextBtn.setEnabled(false);
				}
			}
		});

		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareImage();
			}
		});
	}

	// share img
	private void shareImage() {
		Toast.makeText(getApplicationContext(),
				"This features not available in this demo version",
				Toast.LENGTH_SHORT).show();
	}

	private void refreshGallery(String filePath) {
		// Tell the media scanner about the new file so that it is
		// immediately available to the user.
		File file = new File(filePath);
		MediaScannerConnection.scanFile(this, new String[] { file.toString() },
				null, new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.i("ExternalStorage", "Scanned " + path + ":");
						Log.i("ExternalStorage", "-> uri=" + uri);
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 1) {
			File file = new File(prefs.getString("saving_path",
					"/sdcard/designdroid/")
					+ "/"
					+ "temp_folder"
					+ "/"
					+ VirtualStore.counter + ".png");

			Bitmap result = BitmapFactory.decodeFile(file.getAbsolutePath());

			imgHolder.setImageBitmap(result);
			nextBtn.setEnabled(true);
			backBtn.setEnabled(true);
			resetBtn.setEnabled(true);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		File file = new File(prefs.getString("saving_path",
				"/sdcard/designdroid/") + "/" + "temp_folder");
		VirtualStore.counter = -1;
		DeleteRecursive(file);
	}

	// delete temp images
	void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);

		fileOrDirectory.delete();
	}

	public class LoadImg extends AsyncTask<Uri, Uri, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			imgHolder.setImageBitmap(result);
			progBar.setVisibility(View.GONE);
			/*
			 * isMainImg = true; new SaveIMG(MainEditor.this,
			 * prefs).execute(result);
			 */
			// VirtualStore.saveImageAsPng(getApplicationContext(), prefs,
			// result);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		}

		@Override
		protected void onPreExecute() {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
					WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			progBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Bitmap doInBackground(Uri... params) {

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			// Get the cursor
			Cursor cursor = getContentResolver().query(params[0],
					filePathColumn, null, null, null);
			// Move to first row
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String imgDecodableString = cursor.getString(columnIndex);
			cursor.close();
			Bitmap result = BitmapFactory.decodeFile(imgDecodableString);
			try {
				copyFile(
						new File(getPath(params[0])),
						new File(prefs.getString("saving_path",
								"/sdcard/designdroid/")
								+ "/"
								+ "temp_folder"
								+ "/" + ++VirtualStore.counter + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// VirtualStore.originBitmap = ;
			return result;
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor == null)
			return null;
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String s = cursor.getString(column_index);
		cursor.close();
		return s;
	}

	private void showCloseDialog() {
		final Dialog dialog = new Dialog(MainEditor.this);
		dialog.setContentView(R.layout.closing_dialog);
		dialog.setTitle(getString(R.string.confirm_close));
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
		dialog.show();

		Button okBtn = (Button) dialog.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);
		// if button is clicked, close the custom dialog
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	Random random = new Random();

	private void showSavingDialog() {
		final Dialog dialog = new Dialog(MainEditor.this);
		dialog.setContentView(R.layout.saving_dialog);
		dialog.setTitle(getString(R.string.saving_title));
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
		dialog.show();

		Spinner locationBar = (Spinner) dialog.findViewById(R.id.folder_loc);
		final EditText fileNameTxt = (EditText) dialog
				.findViewById(R.id.txt_file_name);
		fileNameTxt.setText("Design_" + random.nextInt());
		fileNameTxt.setTextColor(Color.parseColor("#e2651c"));

		ArrayList<String> location = new ArrayList<String>(1);
		location.add(savingFolder.getAbsolutePath());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, location);
		locationBar.setAdapter(adapter); // this will set list of values to
											// spinner

		locationBar.setSelection(location.indexOf(0));// set selected value

		final Spinner imgTypSpinner = (Spinner) dialog
				.findViewById(R.id.img_type_spinner);

		ArrayAdapter<String> imgTypeAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, getResources().getStringArray(
						R.array.img_type));

		imgTypSpinner.setAdapter(imgTypeAdapter);
		final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);
		seekBar.setMax((max - min) / step);

		imgTypSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> adapterView,
							View view, int i, long l) {
						if (imgTypSpinner.getSelectedItemPosition() == 0)
							seekBar.setEnabled(true);
						else if (imgTypSpinner.getSelectedItemPosition() == 1)
							seekBar.setEnabled(false);
					}

					public void onNothingSelected(AdapterView<?> adapterView) {
						return;
					}
				});
		// image quality seekbar value listener
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Ex :
				// And finnaly when you want to retrieve the
				// value in the range you
				// wanted in the first place -> [3-5]
				//
				// if progress = 13 -> value = 3 + (13 * 0.1) =
				// 4.3
				imageQuality = min + (progress * step);

			}
		});

		Button okBtn = (Button) dialog.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				progDialog.show();

				if (!savingFolder.exists()) {
					savingFolder.mkdir();
				}

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						File file = new File(prefs.getString("saving_path",
								"/sdcard/designdroid/")
								+ "/"
								+ "temp_folder"
								+ "/" + VirtualStore.counter + ".png");
						if (imgTypSpinner.getSelectedItemPosition() == 0) {

							isSaved = saveImageAsJbj(BitmapFactory
									.decodeFile(file.getAbsolutePath()),
									fileNameTxt.getText().toString());
						}

						else if (imgTypSpinner.getSelectedItemPosition() == 1) {
							isSaved = saveImageAsPng(BitmapFactory
									.decodeFile(file.getAbsolutePath()),
									fileNameTxt.getText().toString());
						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (isSaved) {
									Toast.makeText(getApplicationContext(),
											getString(R.string.saved),
											Toast.LENGTH_SHORT).show();
								} else
									Toast.makeText(getApplicationContext(),
											getString(R.string.not_saved),
											Toast.LENGTH_SHORT).show();
								progDialog.dismiss();
								dialog.dismiss();
							}
						});
					}
				}).start();
				// finish();
			}
		});
		Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);
		// if button is clicked, close the custom dialog
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	// save image as png
	private boolean saveImageAsPng(Bitmap bm, String imgNam) {
		OutputStream stream = null;
		imgTyp = ".png";
		try {
			stream = new FileOutputStream(savingFolder.getAbsolutePath() + "/"
					+ imgNam + imgTyp);
			bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
			stream.close();
			refreshGallery(savingFolder.getAbsolutePath() + imgNam + imgTyp);
			return true;
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.not_saved), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.not_saved), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		}
	}

	// save image as jbg
	private boolean saveImageAsJbj(Bitmap bm, String imgName) {
		imgTyp = ".jpeg";
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(savingFolder.getAbsolutePath() + "/"
					+ imgName + imgTyp);
			if (imageQuality == 0)
				imageQuality += 10;
			bm.compress(Bitmap.CompressFormat.JPEG, imageQuality, stream);
			stream.close();
			// refreshGallery(savingFolder.getAbsolutePath() + imgName +
			// imgTyp);
			return true;
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.not_saved), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.not_saved), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		}
	}

	private void copyFile(File sourceFile, File destFile) throws IOException {
		if (!sourceFile.exists()) {
			return;
		}

		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}

	}
}
