package com.smartapps.designdroid.stickers;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.frames.FrameActivity;
import com.smartapps.designdroid.home.SaveIMG;
import com.smartapps.designdroid.home.VirtualStore;
import com.smartapps.designdroid.text.TextActivity;

public class StikcersActivity extends ActionBarActivity {
	/**
	 * Called when the activity is first created.
	 */

	SingleFingerView single;
	private com.smartapps.designdroid.stickers.SingleFingerView co;
	private Context context;
	private float _1dp;
	public static TypedArray aa;
	Bitmap bmp;
	ImageView effectimg;
	private String filepath, mainpathFiles;
	private String categoryname = "Stickers", lang;
	SharedPreferences prefs;
	ImageView morebuttonmenu;
	private TextView TVcatlist;
	private TableRow TBforcatList, TBRowcategory;
	private PopupWindow popupwindow;
	private View popupView;
	ImageView categoryButtonsNames;
	FrameLayout cancelBtn, applyBtn;
	Bitmap stickersImg[], stickersThumbnails[];
	private String[] stickersCategory, stickersSubCategory;
	private LinearLayout categorytypesLinear, moreMenuList;
	ImageView innerImg;
	SeekBar rotateSeekbar, hue_seekbar;
	FrameLayout container;
	HorizontalScrollView horizontalScroller;
	private com.smartapps.designdroid.database.DatabaseAdapter AdDB;
	int workAreaWidth, workAreaHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stickers);
		context = this;
		this._1dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
				context.getResources().getDisplayMetrics());

		lang = Locale.getDefault().getLanguage();

		this.getSupportActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.custom_action_bar, null);

		TextView titleTextView = (TextView) v
				.findViewById(R.id.custom_action_bar_title);
		titleTextView.setText(this.getTitle());

		this.getSupportActionBar().setCustomView(v);

		horizontalScroller = (HorizontalScrollView) findViewById(R.id.horizonatl_scroller);
		cancelBtn = (FrameLayout) findViewById(R.id.btn_cancel);
		applyBtn = (FrameLayout) findViewById(R.id.btn_submit);
		innerImg = (ImageView) findViewById(R.id.final_layout);
		innerImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		container = (FrameLayout) findViewById(R.id.relative_layout);
		final FrameLayout headerLayout = (FrameLayout) findViewById(R.id.frame);

		ViewTreeObserver observer = headerLayout.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				int headerLayoutHeight = headerLayout.getHeight();
				int headerLayoutWidth = headerLayout.getWidth();

				workAreaHeight = headerLayoutHeight;
				workAreaWidth = headerLayoutWidth;

				Display display = getWindowManager().getDefaultDisplay();
				DisplayMetrics outMetrics = new DisplayMetrics();
				display.getMetrics(outMetrics);

				float dpHeight = outMetrics.heightPixels;
				float dpWidth = outMetrics.widthPixels;

				headerLayout.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
			}
		});

		categorytypesLinear = (LinearLayout) findViewById(R.id.categorytypesLinear);
		prefs = getSharedPreferences("designdroidprefs", MODE_PRIVATE);
		mainpathFiles = prefs.getString("saving_path", "");

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
		LoadCategroyButtons(stickersCategory[0]);

		ViewTreeObserver vto = innerImg.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				// Remove after the first run so it doesn't fire forever
				innerImg.getViewTreeObserver().removeOnPreDrawListener(this);
				int finalHeight = innerImg.getMeasuredHeight();
				int finalWidth = innerImg.getMeasuredWidth();
				// tv.setText("Height: " + finalHeight + " Width: " +
				// finalWidth);
				// int a = Math.round((float) bmp.getHeight() / finalHeight);

				Display display = getWindowManager().getDefaultDisplay();
				DisplayMetrics outMetrics = new DisplayMetrics();
				display.getMetrics(outMetrics);

				float dpHeight = outMetrics.heightPixels;
				float dpWidth = outMetrics.widthPixels;

				int newWidth = bmp.getWidth();
				int newheight = finalHeight;
				if (bmp.getHeight() > bmp.getWidth())
					newWidth = bmp.getWidth() * finalHeight / bmp.getHeight();

				else if (bmp.getWidth() >= dpWidth) {
					newWidth = (int) dpWidth;
					newheight = bmp.getHeight() * newWidth / bmp.getWidth();
				}
				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) container
						.getLayoutParams();
				params.height = newheight;
				params.width = newWidth;
				container.setLayoutParams(params);

				// imgHolder.getViewTreeObserver().removeOnPreDrawListener(this);
				return true;
			}
		});

		innerImg.setImageBitmap(bmp);

		morebuttonmenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Rehab
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
					for (int i = 0; i < stickersCategory.length; i++) {
						TBforcatList = new TableRow(StikcersActivity.this);
						TVcatlist = new TextView(StikcersActivity.this);
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
									.getsubcategoryArabicName(stickersCategory[i]
											.toString().trim()));

						} else {
							TVcatlist.setText(stickersCategory[i].toString());
						}

						TBforcatList.addView(TVcatlist);
						moreMenuList.addView(TBforcatList);
						shopbuttonid = i;
					}

					TBforcatList = new TableRow(StikcersActivity.this);
					TVcatlist = new TextView(StikcersActivity.this);
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
					for (int w = 0; w < ((stickersCategory.length) + 1); w++) {
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

				removeborderfromlinearview();
				/*
				 * FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
				 * container .getLayoutParams(); params.height =
				 * bmp.getHeight(); params.width = bmp.getWidth();
				 * container.setLayoutParams(params);
				 */

				container.setDrawingCacheEnabled(true);
				container.buildDrawingCache();

				Bitmap bitmap = container.getDrawingCache();

				setResult(1);

				new SaveIMG(StikcersActivity.this, prefs).execute(bitmap);

			}
		});
		container.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeborderfromlinearview();
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
				String getcatnameformtextview = getTextView.getText()
						.toString();

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
					// open shop activity
					Toast.makeText(getApplicationContext(),
							"This features not available in this demo version",
							Toast.LENGTH_SHORT).show();
				}
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
		if (stickersSubCategory.length == 0) {

		} else {
			categorytypesLinear.removeAllViews();
			for (int i = 0; i < stickersSubCategory.length; i++) {
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
				categoryButtonsNames.setImageBitmap(stickersThumbnails[i]);
				// categoryButtonsNames.setTextSize(20);
				categorytypesLinear.addView(categoryButtonsNames);
			}

		}

		// set events on buttons
		for (int w = 0; w < stickersSubCategory.length; w++) {
			getCategoryTypessButton(stickersImg[w], w + 3000);

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
				removeborderfromlinearview();
				SingleFingerView.mImageBitmap = frameImg;
				co = new SingleFingerView(StikcersActivity.this);
				co.setId(200);
				co.bringToFront();
				container.addView(co);
				container.bringToFront();

			}
		});

	}

	private void getSubDirFolderCategory(String categoryname, String subname) {
		String filepath = mainpathFiles + "/" + categoryname + "/"
				+ subname.toString();
		File subDir = new File(filepath + File.separator);
		File[] files = subDir.listFiles();
		stickersSubCategory = new String[files.length];
		stickersImg = new Bitmap[files.length];
		stickersThumbnails = new Bitmap[files.length];
		for (int i = 0; i < files.length; i++) {
			final int THUMBNAIL_SIZE = 45;
			stickersSubCategory[i] = files[i].getName();
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			Bitmap frameImg = BitmapFactory.decodeFile(
					files[i].getAbsolutePath(), bmOptions);
			Bitmap frameThumb = Bitmap.createScaledBitmap(frameImg,
					((int) convertDpToPixel(THUMBNAIL_SIZE)),
					((int) convertDpToPixel(THUMBNAIL_SIZE)), false);
			// bitmap =
			// Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
			stickersThumbnails[i] = frameThumb;
			stickersImg[i] = frameImg;
		}
	}

	// to get catagory folders for the more button type (fonts , templates, ..)
	private void getDirFolderCategory(String categoryname) {
		filepath = mainpathFiles + "/" + categoryname.toString();
		File dir = new File(filepath + File.separator);
		File[] files = dir.listFiles();
		stickersCategory = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			stickersCategory[i] = files[i].getName();
		}
	}

	public float convertDpToPixel(float dp) {
		Resources resources = getApplicationContext().getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	private void removeborderfromlinearview() {
		// TODO Auto-generated method stub
		int childcount = container.getChildCount();
		for (int i = 1; i < childcount; i++) {
			View linearview = container.getChildAt(i);
			View textlinerlayoutview = ((ViewGroup) linearview).getChildAt(0);
			View image1 = ((ViewGroup) textlinerlayoutview).getChildAt(1);
			View image2 = ((ViewGroup) textlinerlayoutview).getChildAt(2);
			View image3 = ((ViewGroup) textlinerlayoutview).getChildAt(3);
			if (image1 != null) { // check with Mahmood
				image1.setVisibility(View.GONE);
				image2.setBackgroundResource(0);
				image3.setVisibility(View.GONE);
			}
			try {
				// editTextView = (EditText) linearview;
				// editTextView.setBackgroundResource(0);
			} catch (ClassCastException e) {
				continue;
			}
		}
	}
}