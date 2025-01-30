package com.smartapps.designdroid.tools;

import java.io.File;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.home.SaveIMG;
import com.smartapps.designdroid.home.VirtualStore;

public class ToolsEdit extends ActionBarActivity {

	LinearLayout crop, rotateRight, rotateLeft, resize, flipHoriz, flipVertic;
	ImageView cancel, submit;
	CropImageView cropImageView;
	ImageView finalLayout;
	RelativeLayout relative;
	boolean isCroppingFlag;
	int counter;
	boolean isPressed = false;
	ImageView cropImage;
	SharedPreferences prefs;
	Bitmap bmp;

	// type definition
	public static final int FLIP_VERTICAL = 1;
	public static final int FLIP_HORIZONTAL = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_v2);
		//
		this.getSupportActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.custom_action_bar, null);

		TextView titleTextView = (TextView) v
				.findViewById(R.id.custom_action_bar_title);
		titleTextView.setText(this.getTitle());

		this.getSupportActionBar().setCustomView(v);
		//
		prefs = getSharedPreferences("designdroidprefs", MODE_PRIVATE);
		crop = (LinearLayout) findViewById(R.id.crop);
		resize = (LinearLayout) findViewById(R.id.resize);
		rotateRight = (LinearLayout) findViewById(R.id.rotate_right);
		rotateLeft = (LinearLayout) findViewById(R.id.rotate_left);
		flipHoriz = (LinearLayout) findViewById(R.id.flip_horizontal);
		flipVertic = (LinearLayout) findViewById(R.id.flip_vert);
		cancel = (ImageView) findViewById(R.id.btn_cancel);
		submit = (ImageView) findViewById(R.id.btn_submit);

		cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		cropImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		cropImage = (ImageView) findViewById(R.id.crop_imgs);

		finalLayout = (ImageView) findViewById(R.id.final_layout);
		finalLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// Bitmap bmp = VirtualStore.originBitmap;

		File file = new File(prefs.getString("saving_path",
				"/sdcard/designdroid/")
				+ "/"
				+ "temp_folder"
				+ "/"
				+ VirtualStore.counter + ".png");
		bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

		float f = getResources().getDisplayMetrics().density;

		int bitmapWidth = (int) (bmp.getWidth() / f);
		int bimtapHeight = (int) (bmp.getHeight() / f);

		// bmp = Bitmap.createScaledBitmap(bmp, bitmapWidth, bimtapHeight,
		// false);

		finalLayout.setImageBitmap(bmp);

		/*
		 * finalLayout.setBackground(new BitmapDrawable(getResources(),
		 * VirtualStore.originBitmap));
		 */
		// editableLayout = (LinearLayout) findViewById(R.id.editable_layout);

		relative = (RelativeLayout) findViewById(R.id.top_bar);
		// Start-Cropping action
		crop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cropImage.setImageDrawable(getResources().getDrawable(
						R.drawable.crop_btn_selected));

				counter = 1;
				disabelCtrls();
				finalLayout.setVisibility(View.GONE);
				relative.setVisibility(View.VISIBLE);
				cropImageView.setVisibility(View.VISIBLE);

				Bitmap bitmap = ((BitmapDrawable) finalLayout.getDrawable())
						.getBitmap();
				cropImageView.setImageBitmap(bitmap);
			}
		});
		// End-Cropping action
		//
		resize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// counter = 6;
				// disabelCtrls();
				showResizeDialog();
			}
		});
		//
		// Start-Rotating action
		rotateRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// relative.setVisibility(View.VISIBLE);
				// resizingEditableLayout(finalLayout.getWidth(),
				// finalLayout.getHeight());
				// counter = 3;
				// disabelCtrls();
				// finalLayout.setVisibility(View.GONE);
				// editableLayout.setVisibility(View.VISIBLE);
				// if (editableLayout.getBackground() == null)
				// editableLayout.setBackground(finalLayout.getBackground());
				Bitmap bitmap = ((BitmapDrawable) finalLayout.getDrawable())
						.getBitmap();
				Bitmap rotatedBitmap = rotateImage(bitmap, 90);
				/*
				 * Drawable background = new BitmapDrawable(getResources(),
				 * rotatedBitmap);
				 */
				finalLayout.setImageBitmap(rotatedBitmap);
			}
		});
		rotateLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// relative.setVisibility(View.VISIBLE);
				// resizingEditableLayout(finalLayout.getWidth(),
				// finalLayout.getHeight());
				// counter = 2;
				// disabelCtrls();
				// finalLayout.setVisibility(View.GONE);
				// editableLayout.setVisibility(View.VISIBLE);
				// if (editableLayout.getBackground() == null)
				// editableLayout.setBackground(finalLayout.getBackground());
				Bitmap bitmap = ((BitmapDrawable) finalLayout.getDrawable())
						.getBitmap();
				Bitmap rotatedBitmap = rotateImage(bitmap, -90);
				/*
				 * Drawable background = new BitmapDrawable(getResources(),
				 * rotatedBitmap);
				 */
				finalLayout.setImageBitmap(rotatedBitmap);
			}
		});
		// End-Rotating action
		// Start-Flipping action
		flipHoriz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// relative.setVisibility(View.VISIBLE);
				// resizingEditableLayout(finalLayout.getWidth(),
				// finalLayout.getHeight());
				// counter = 4;
				// disabelCtrls();
				// finalLayout.setVisibility(View.GONE);
				// editableLayout.setVisibility(View.VISIBLE);
				// if (editableLayout.getBackground() == null)
				// editableLayout.setBackground(finalLayout.getBackground());
				Bitmap bitmap = ((BitmapDrawable) finalLayout.getDrawable())
						.getBitmap();
				Bitmap flippedBitmap = flip(bitmap, 2);
				/*
				 * Drawable background = new BitmapDrawable(getResources(),
				 * flippedBitmap);
				 */
				finalLayout.setImageBitmap(flippedBitmap);
			}
		});
		flipVertic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// relative.setVisibility(View.VISIBLE);
				// resizingEditableLayout(finalLayout.getWidth(),
				// finalLayout.getHeight());
				// counter = 5;
				// disabelCtrls();
				// finalLayout.setVisibility(View.GONE);
				// editableLayout.setVisibility(View.VISIBLE);
				// if (editableLayout.getBackground() == null)
				// editableLayout.setBackground(finalLayout.getBackground());
				Bitmap bitmap = ((BitmapDrawable) finalLayout.getDrawable())
						.getBitmap();
				Bitmap flippedBitmap = flip(bitmap, 1);
				/*
				 * Drawable background = new BitmapDrawable(getResources(),
				 * flippedBitmap);
				 */
				finalLayout.setImageBitmap(flippedBitmap);
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// relative.setVisibility(View.INVISIBLE);
				if (counter == 1) {
					cropImage.setImageDrawable(getResources().getDrawable(
							R.drawable.crop_btn_unselected));
					finalLayout.setVisibility(View.VISIBLE);
					cropImageView.setVisibility(View.GONE);

					@SuppressWarnings("deprecation")
					float f = getResources().getDisplayMetrics().density;

					int bitmapWidth = (int) (cropImageView.getCroppedImage()
							.getWidth() / f);
					int bimtapHeight = (int) (cropImageView.getCroppedImage()
							.getHeight() / f);

					Bitmap bmp = cropImageView.getCroppedImage();

					finalLayout.setImageBitmap(bmp);
					enableCtrls();
					counter = 0;
				} else {
					// save & close screen
					new SaveIMG(ToolsEdit.this, prefs)
							.execute(((BitmapDrawable) finalLayout
									.getDrawable()).getBitmap());
					setResult(1);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// relative.setVisibility(View.INVISIBLE);
				if (counter == 1) {
					cropImage.setImageDrawable(getResources().getDrawable(
							R.drawable.crop_btn_unselected));
					finalLayout.setVisibility(View.VISIBLE);
					cropImageView.setVisibility(View.GONE);
					enableCtrls();
					counter = 0;
				} else
					// close screen
					finish();
			}
		});

	}

	public static Bitmap flip(Bitmap src, int type) {
		// create new matrix for transformation
		Matrix matrix = new Matrix();
		// if vertical
		if (type == FLIP_VERTICAL) {
			// y = y * -1
			matrix.preScale(1.0f, -1.0f);
		}
		// if horizonal
		else if (type == FLIP_HORIZONTAL) {
			// x = x * -1
			matrix.preScale(-1.0f, 1.0f);
			// unknown type
		} else {
			return null;
		}

		// return transformed image
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
	}

	int originWidth, originHeight;
	boolean isWidth, isHeight;

	private void showResizeDialog() {
		final Dialog dialog = new Dialog(ToolsEdit.this);
		if (originWidth == 0)
			originWidth = bmp.getWidth();
		if (originHeight == 0)
			originHeight = bmp.getHeight();
		dialog.setContentView(R.layout.resizing_dialog);
		dialog.setTitle(getString(R.string.reszing_pic));
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
		dialog.show();
		// set the custom dialog components - text, image and button
		final EditText width = (EditText) dialog.findViewById(R.id.txt_width);
		final EditText height = (EditText) dialog.findViewById(R.id.txt_height);
		width.setText(originWidth + "");
		height.setText(originHeight + "");

		width.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// original height / original width * new width = new height
				isWidth = true;
				if (s.length() != 0 && !isHeight) {
					int newHeight = Math.round(((float) originHeight
							/ originWidth * Integer.parseInt(s + "")));
					height.setText(newHeight + "");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				isWidth = false;
			}
		});

		height.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// orignal width / orignal height * new height = new width
				isHeight = true;
				if (s.length() != 0 && !isWidth) {
					int newWidth = Math.round(((float) originWidth
							/ originHeight * Integer.parseInt(s + "")));
					width.setText(newWidth + "");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				isHeight = false;
			}
		});
		// int height = (int)
		// TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, <HEIGHT>,
		// getResources().getDisplayMetrics());
		Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);
		// if button is clicked, close the custom dialog
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// android.view.ViewGroup.LayoutParams params = finalLayout
				// .getLayoutParams();
				// Changes the height and width to the specified *pixels*
				// params.height =
				// Integer.parseInt(height.getText().toString());
				// params.width = Integer.parseInt(width.getText().toString());
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp,
						Integer.parseInt(width.getText().toString()),
						Integer.parseInt(height.getText().toString()), false);
				finalLayout.setImageBitmap(scaledBitmap);
				originHeight = scaledBitmap.getHeight();
				originWidth = scaledBitmap.getWidth();
				dialog.dismiss();
				enableCtrls();
			}
		});
		Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
		// if button is clicked, close the custom dialog
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				enableCtrls();
			}
		});
	}

	private void enableCtrls() {
		resize.setEnabled(true);
		crop.setEnabled(true);
		rotateLeft.setEnabled(true);
		rotateRight.setEnabled(true);
		flipHoriz.setEnabled(true);
		flipVertic.setEnabled(true);
	}

	private void disabelCtrls() {
		switch (counter) {
		case 1:
			resize.setEnabled(false);
			rotateLeft.setEnabled(false);
			rotateRight.setEnabled(false);
			flipHoriz.setEnabled(false);
			flipVertic.setEnabled(false);
			break;
		case 2:
			crop.setEnabled(false);
			resize.setEnabled(false);
			rotateRight.setEnabled(false);
			flipHoriz.setEnabled(false);
			flipVertic.setEnabled(false);
			break;
		case 3:
			crop.setEnabled(false);
			resize.setEnabled(false);
			rotateLeft.setEnabled(false);
			flipHoriz.setEnabled(false);
			flipVertic.setEnabled(false);
			break;
		case 4:
			crop.setEnabled(false);
			resize.setEnabled(false);
			rotateLeft.setEnabled(false);
			rotateRight.setEnabled(false);
			flipVertic.setEnabled(false);
			break;
		case 5:
			crop.setEnabled(false);
			resize.setEnabled(false);
			rotateRight.setEnabled(false);
			rotateLeft.setEnabled(false);
			flipHoriz.setEnabled(false);
			break;
		case 6:
			crop.setEnabled(false);
			rotateRight.setEnabled(false);
			rotateLeft.setEnabled(false);
			flipHoriz.setEnabled(false);
			flipVertic.setEnabled(false);
			break;
		default:
			break;
		}
	}

	private void resizingEditableLayout(int width, int height) {
		/*
		 * android.view.ViewGroup.LayoutParams params = editableLayout
		 * .getLayoutParams(); // Changes the height and width to the specified
		 * *pixels* params.height = height; params.width = width;
		 */
	}

	public Bitmap rotateImage(Bitmap src, float degree) {
		// create new matrix object
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);
		// return new bitmap rotated using matrix
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
	}

}
