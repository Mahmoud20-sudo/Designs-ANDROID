package com.smartapps.designdroid.frames;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.home.SaveIMG;
import com.smartapps.designdroid.home.VirtualStore;

public class FrameActivity extends ActionBarActivity implements
		OnSeekBarChangeListener {
	Bitmap bmp;
	ImageView frame;
	public static Matrix matrix;
	public static final int FLIP_VERTICAL = 1;
	public static final int FLIP_HORIZONTAL = 2;
	private String filepath, mainpathFiles;
	private String categoryname = "Frames", lang;
	SharedPreferences prefs;
	ImageView morebuttonmenu;
	private TextView TVcatlist;
	private Button shopBtn;
	private TableRow TBfortoolsButtons, TBforcatList, TBRowcategory;
	private PopupWindow popupwindow;
	private View popupView;
	ImageView categoryButtonsNames;
	FrameLayout resetBtn, rotateBtn, hFlip, vFlip, cancelBtn, applyBtn;
	Bitmap framesImgs[], framesThumbnails[];
	private String[] FramesCategory, FramesSubCategory;
	private LinearLayout toolslinearview, categorytypesLinear, moreMenuList;
	ImageView innerImg;
	SeekBar rotateSeekbar;
	RelativeLayout container;
	HorizontalScrollView horizontalScroller;
	private com.smartapps.designdroid.database.DatabaseAdapter AdDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frames);

		lang = Locale.getDefault().getLanguage();

		this.getSupportActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.custom_action_bar, null);

		TextView titleTextView = (TextView) v
				.findViewById(R.id.custom_action_bar_title);
		titleTextView.setText(this.getTitle());

		this.getSupportActionBar().setCustomView(v);

		horizontalScroller = (HorizontalScrollView) findViewById(R.id.horizonatl_scroller);
		innerImg = (ImageView) findViewById(R.id.final_layout);
		innerImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// img.setPadding(0, 0, 0, 0);
		// bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sp);
		// BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
		categorytypesLinear = (LinearLayout) findViewById(R.id.categorytypesLinear);
		prefs = getSharedPreferences("designdroidprefs", MODE_PRIVATE);
		mainpathFiles = prefs.getString("saving_path", "");

		resetBtn = (FrameLayout) findViewById(R.id.reset_btn);
		rotateBtn = (FrameLayout) findViewById(R.id.rotate_btn);
		hFlip = (FrameLayout) findViewById(R.id.hflip_btn);
		vFlip = (FrameLayout) findViewById(R.id.vflip_btn);
		cancelBtn = (FrameLayout) findViewById(R.id.btn_cancel);
		applyBtn = (FrameLayout) findViewById(R.id.btn_submit);
		rotateSeekbar = (SeekBar) findViewById(R.id.rotate_seekbar);
		rotateSeekbar.setOnSeekBarChangeListener(this);
		rotateSeekbar.setMax(360);

		// bmp = VirtualStore.originBitmap;
		File file = new File(prefs.getString("saving_path",
				"/sdcard/designdroid/")
				+ "/"
				+ "temp_folder"
				+ "/"
				+ VirtualStore.counter + ".png");
		bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

		AdDB = new com.smartapps.designdroid.database.DatabaseAdapter(this);

		// main path for folders of app
		mainpathFiles = AdDB.getStrPreferences("saving_path");

		// get the directory category
		getDirFolderCategory(categoryname);

		TBRowcategory = (TableRow) findViewById(R.id.TBRowcategory);
		morebuttonmenu = (ImageView) findViewById(R.id.morebuttonmenu);
		LoadCategroyButtons(FramesCategory[0]);
		/*
		 * bmp = Bitmap.createScaledBitmap(VirtualStore.originBitmap, (int)
		 * (VirtualStore.originBitmap.getWidth() * density), (int)
		 * (VirtualStore.originBitmap.getHeight() * density), false);
		 */

		// BitmapFactory.Options dimensions = new BitmapFactory.Options();
		// dimensions.inJustDecodeBounds = true;
		container = (RelativeLayout) findViewById(R.id.relative_layout);

		// float wscale = (float) width / (float) bmp.getWidth();
		// float hscale = (float) height / (float) bmp.getHeight();

		/*
		 * wscale = 0.34f; hscale = 0.34f;
		 */

		/*
		 * Toast.makeText( getApplicationContext(), wscale + "",
		 * Toast.LENGTH_SHORT) .show();
		 */
		innerImg.setImageBitmap(bmp);

		matrix = new Matrix();
		setImageMatrix();
		innerImg.setScaleType(ImageView.ScaleType.MATRIX);

		innerImg.setOnTouchListener(new OnTouch());
		/*
		 * Button bm = (Button) findViewById(R.id.button1);
		 * bm.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub frame = (ImageView) findViewById(R.id.imageView_frame);
		 * frame.setLayerType(View.LAYER_TYPE_SOFTWARE, null); Bitmap bmps =
		 * BitmapFactory.decodeResource(getResources(), R.drawable.frame11);
		 * Bitmap finals = Bitmap.createScaledBitmap(bmps, bmp.getWidth(),
		 * bmp.getHeight(), false);
		 * 
		 * // BitmapDrawable bitmapDrawable = new BitmapDrawable(finals);
		 * frame.setImageBitmap(finals); } });
		 */

		/*
		 * Button bm2 = (Button) findViewById(R.id.button2);
		 * bm2.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Bitmap bitmap = ((BitmapDrawable) frame.getDrawable())
		 * .getBitmap(); Bitmap flippedBitmap = flip(bitmap, 1);
		 * frame.setImageBitmap(flippedBitmap); } });
		 */

		horizontalScroller.getViewTreeObserver().addOnScrollChangedListener(
				new OnScrollChangedListener() {

					@Override
					public void onScrollChanged() {

						int scrollX = horizontalScroller.getScrollX(); // for
																		// horizontalScrollView
						int scrollY = horizontalScroller.getScrollY(); // for
																		// verticalScrollView
						// DO SOMETHING WITH THE SCROLL COORDINATES

						if (scrollX > scrollY)
							Toast.makeText(getApplicationContext(), "right",
									Toast.LENGTH_SHORT);
						else
							Toast.makeText(getApplicationContext(), "left",
									Toast.LENGTH_SHORT);

					}
				});

		morebuttonmenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (popupwindow == null) {
					morebuttonmenu
							.setImageResource(R.drawable.moreicon_selected);
					LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
							.getSystemService(LAYOUT_INFLATER_SERVICE);
					popupView = layoutInflater.inflate(R.layout.moremenu, null);
					popupwindow = new PopupWindow(popupView,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					moreMenuList = (LinearLayout) popupView
							.findViewById(R.id.moreMenuList);
					int shopbuttonid = 0;
					// create list of category
					for (int i = 0; i < FramesCategory.length; i++) {
						TBforcatList = new TableRow(FrameActivity.this);
						TVcatlist = new TextView(FrameActivity.this);
						TVcatlist.setId(5000 + i);
						TVcatlist.setTextSize(15);
						XmlResourceParser xpp = getResources().getXml(
								R.drawable.category_txt);
						ColorStateList cl;
						try {
							cl = ColorStateList.createFromXml(getResources(),
									xpp);
							TVcatlist.setTextColor(cl);
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
								TableRow.LayoutParams.WRAP_CONTENT,
								TableRow.LayoutParams.WRAP_CONTENT);
						// layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
						layoutParams.setMargins((int) convertDpToPixel(10),
								(int) convertDpToPixel(10),
								(int) convertDpToPixel(10),
								(int) convertDpToPixel(10));
						TVcatlist.setLayoutParams(layoutParams);

						// change subcategory name to arabic
						if (lang.equals("ar")) {
							TVcatlist.setText(AdDB
									.getsubcategoryArabicName(FramesCategory[i]
											.toString().trim()));

						} else {
							TVcatlist.setText(FramesCategory[i].toString());
						}

						TBforcatList.addView(TVcatlist);
						moreMenuList.addView(TBforcatList);
						shopbuttonid = i;
					}

					TBforcatList = new TableRow(FrameActivity.this);
					TVcatlist = new TextView(FrameActivity.this);
					TVcatlist.setId(5000 + shopbuttonid + 1);
					TVcatlist.setTextSize(15);
					TVcatlist.setTextColor(Color.WHITE);
					TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
							TableRow.LayoutParams.WRAP_CONTENT,
							TableRow.LayoutParams.WRAP_CONTENT);

					layoutParams.setMargins((int) convertDpToPixel(10),
							(int) convertDpToPixel(10),
							(int) convertDpToPixel(10), 0);
					TVcatlist.setLayoutParams(layoutParams);
					// TVcatlist.setText("Shop");
					TVcatlist.setBackground(getResources().getDrawable(
							R.drawable.small_shop_btn));
					TBforcatList.addView(TVcatlist);
					moreMenuList.addView(TBforcatList);

					popupwindow.showAsDropDown(TBRowcategory, 0, 10);
					// morebuttonmenu.setEnabled(false);
					for (int w = 0; w < ((FramesCategory.length) + 1); w++) {
						getTVcatlistname(w + 5000);

					}
				} else {
					if (!popupwindow.isShowing()) {
						popupwindow.showAsDropDown(TBRowcategory, 0, 10);
						morebuttonmenu
								.setImageResource(R.drawable.moreicon_selected);
					} else {
						popupwindow.dismiss();
						morebuttonmenu
								.setImageResource(R.drawable.moreicon_unselected);
					}
				}
			}

		});

		resetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// innerImg.setImageBitmap(enterdImg);
				File file = new File(prefs.getString("saving_path",
						"/sdcard/designdroid/")
						+ "/"
						+ "temp_folder"
						+ "/"
						+ VirtualStore.counter + ".png");
				innerImg.setImageBitmap(null);
				frame.setImageBitmap(null);
				innerImg.setImageBitmap(BitmapFactory.decodeFile(file
						.getAbsolutePath()));
				setImageMatrix();
			}
		});
		rotateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rotateSeekbar.getVisibility() == View.GONE)
					rotateSeekbar.setVisibility(View.VISIBLE);
				else
					rotateSeekbar.setVisibility(View.GONE);
			}
		});
		vFlip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Bitmap bitmap = ((BitmapDrawable) frame.getDrawable())
							.getBitmap();
					Bitmap flippedBitmap = flip(bitmap, 2);
					frame.setImageBitmap(flippedBitmap);
				} catch (NullPointerException e) {
				}
			}
		});
		hFlip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Bitmap bitmap = ((BitmapDrawable) frame.getDrawable())
							.getBitmap();
					Bitmap flippedBitmap = flip(bitmap, 1);
					frame.setImageBitmap(flippedBitmap);
				} catch (NullPointerException e) {
				}
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		applyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				container.setDrawingCacheEnabled(true);
				Bitmap bitmap = container.getDrawingCache();
				setResult(1);
				new SaveIMG(FrameActivity.this, prefs).execute(bitmap);
			}
		});
	}

	// event for popupmenu window of categories
	public void getTVcatlistname(final int ButtonID) {

		final TextView getTextView = (TextView) popupView
				.findViewById(ButtonID);

		getTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// String gettext = getTextView.getText().toString();
				horizontalScroller.scrollTo(0, 0);
				int Butid = v.getId();
				// to change subcategory to arabic
				String getcatnameformtextview;
				if (lang.equals("ar")) {
					getcatnameformtextview = AdDB
							.getsubcategoryEnglishName(getTextView.getText()
									.toString().trim());
					if (getcatnameformtextview.contains("\n")) {
						getcatnameformtextview = getcatnameformtextview
								.replace("\n", "");
					}
				} else {
					getcatnameformtextview = (getTextView.getText().toString());
				}
				if (!getcatnameformtextview.equals("")) {
					// load subcategorys
					LoadCategroyButtons(getcatnameformtextview);
				} else {
					// open shop
					Toast.makeText(getApplicationContext(),
							"This features not available in this demo version",
							Toast.LENGTH_SHORT).show();
				}
				// morebuttonmenu.setEnabled(true);
				morebuttonmenu.setImageResource(R.drawable.moreicon_unselected);
				popupwindow.dismiss();
			}
		});
	}

	private void LoadCategroyButtons(String subcatname) {
		// TODO Auto-generated method stub
		// category Buttons Rehab
		String fontfacefile = mainpathFiles + "/Frames/"
				+ subcatname.toString();

		getSubDirFolderCategory(categoryname, subcatname);
		// check if folder id empaty
		if (FramesSubCategory.length == 0) {

		} else {
			categorytypesLinear.removeAllViews();
			for (int i = 0; i < FramesSubCategory.length; i++) {
				/*
				 * face = Typeface.createFromFile(fontfacefile + "/" +
				 * FramesSubCategory[i]);
				 */
				categoryButtonsNames = new ImageView(this);
				LayoutParams params = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins((int) convertDpToPixel(5),
						(int) convertDpToPixel(5), (int) convertDpToPixel(5),
						(int) convertDpToPixel(5));
				categoryButtonsNames.setLayoutParams(params);
				categoryButtonsNames.setId(i + 3000);
				/*
				 * categoryButtonsNames
				 * .setBackgroundResource(R.color.moremenucolor);
				 */
				// categoryButtonsNames.setText("sample");
				// categoryButtonsNames.setTypeface(face);
				categoryButtonsNames.setImageBitmap(framesThumbnails[i]);
				// categoryButtonsNames.setTextSize(20);
				categorytypesLinear.addView(categoryButtonsNames);
			}

		}

		// set events on buttons
		for (int w = 0; w < FramesSubCategory.length; w++) {
			getCategoryTypessButton(framesImgs[w], w + 3000);

		}

	}

	public void getCategoryTypessButton(final Bitmap frameImg,
			final int ButtonID) {

		final ImageView getTextView = (ImageView) findViewById(ButtonID);

		getTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// set the frame to the image view
				// editTextView.setTypeface(getTextView.getTypeface());
				frame = (ImageView) findViewById(R.id.imageView_frame);
				frame.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				Bitmap bmps = frameImg;
				Bitmap finals = Bitmap.createScaledBitmap(bmps, bmp.getWidth(),
						bmp.getHeight(), false);

				// BitmapDrawable bitmapDrawable = new BitmapDrawable(finals);
				frame.setImageBitmap(finals);
			}
		});

	}

	private void getSubDirFolderCategory(String categoryname, String subname) {
		String filepath = mainpathFiles + "/" + categoryname + "/"
				+ subname.toString();
		File subDir = new File(filepath + File.separator);
		File[] files = subDir.listFiles();
		FramesSubCategory = new String[files.length];
		framesImgs = new Bitmap[files.length];
		framesThumbnails = new Bitmap[files.length];
		for (int i = 0; i < files.length; i++) {
			final int THUMBNAIL_SIZE = 45;
			FramesSubCategory[i] = files[i].getName();
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			Bitmap frameImg = BitmapFactory.decodeFile(
					files[i].getAbsolutePath(), bmOptions);
			Bitmap frameThumb = Bitmap.createScaledBitmap(frameImg,
					((int) convertDpToPixel(THUMBNAIL_SIZE)),
					((int) convertDpToPixel(THUMBNAIL_SIZE)), false);
			// bitmap =
			// Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
			framesThumbnails[i] = frameThumb;
			framesImgs[i] = frameImg;
		}
	}

	public float convertDpToPixel(float dp) {
		Resources resources = getApplicationContext().getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
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

	// to get catagory folders for the more button type (fonts , templates, ..)
	private void getDirFolderCategory(String categoryname) {
		filepath = mainpathFiles + "/" + categoryname.toString();
		File dir = new File(filepath + File.separator);
		File[] files = dir.listFiles();
		FramesCategory = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			FramesCategory[i] = files[i].getName();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		// rotateImage((int) Math.round(progress * 3.6));
		innerImg.setRotation(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	private void setImageMatrix() {
		container.measure(container.getWidth(), container.getHeight());

		int height = bmp.getHeight();
		int width = bmp.getWidth();

		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float dpHeight = outMetrics.heightPixels;
		float dpWidth = outMetrics.widthPixels;
		// float value = (float) rv.getMeasuredHeight() / density;
		// float frameVlaue = (float) frameLayout.getMeasuredHeight() / density;

		if ((height >= dpHeight) || (width >= dpWidth)) {

			float wscale = (float) dpWidth / (float) width;
			float hscale = (float) (dpHeight * 0.7f) / (float) height;
			//
			matrix = new Matrix();

			if (width > height) {
				matrix.postScale(wscale, wscale);
			} else {
				matrix.postScale(hscale, hscale);
			}
		}// 346*610
			// matrix.postScale(0.8f, 0.8f);
		innerImg.setImageMatrix(matrix);
		/*
		 * innerImg.setRotation(0); rotateSeekbar.setProgress(0);
		 */
	}
}
