package com.smartapps.designdroid.text;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode.Callback;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.smaprtapps.designdroid.R;
import com.smartapps.designdroid.frames.FrameActivity;
import com.smartapps.designdroid.home.SaveIMG;
import com.smartapps.designdroid.home.VirtualStore;

//import com.smartapps.design.inflater.PushBtnTouchListener;

public class TextActivity extends ActionBarActivity {

	// interface views
	private Button categoryButtonsNames;
	ImageView morebuttonmenu, toolButtons;
	FrameLayout closebutton, applybutton;
	private LinearLayout toolslinearview, categorytypesLinear, moreMenuList;
	private EditText editTextView;
	private FrameLayout Mainlinearview;
	private FrameLayout Frameview;
	private ColorPicker picker;
	private SVBar svBar;
	private OpacityBar opacityBar;
	private SaturationBar saturationBar;
	private ValueBar valueBar;
	Bitmap bmp;
	public static boolean isKeyboardShown;
	InputMethodManager imm;

	private View textInflateView, getTextView, moremenulist;
	private SeekBar fontresizeseekbar, rotateSeekbar, opacity_seekbar;
	private TableRow TBfortoolsButtons, TBforcatList, TBRowcategory;
	private TableRow.LayoutParams paramfortoolsicons;
	private TextView TVcatlist;
	private PopupWindow popupwindow;
	private View popupView;
	HorizontalScrollView horizontalScroller;

	private int counter = 300; // view edit text id
	private String[] FontsCategory, FontsSubCategory;
	private int[] buttonToolsIcon;
	private Typeface face;
	int color, Rcolor = 255, Gcolor = 255, Bcolor = 255, shadowFlag = 0;
	private String categoryname = "Fonts", lang; // change to set your
													// category type
	// name
	private String filepath, mainpathFiles;
	private int countkey = 1;
	public static Context context;
	ImageView imgHolder;

	// these matrices will be used to move and zoom image
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	// we can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	// remember some things for zooming
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private float d = 0f;
	private float newRot = 0f;
	private float[] lastEvent = null;
	SharedPreferences prefs;
	int workAreaWidth, workAreaHeight;
	private com.smartapps.designdroid.database.DatabaseAdapter AdDB;
	int paramsWidth, paramsHeight;
	int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_master_view);

		lang = Locale.getDefault().getLanguage();

		this.getSupportActionBar().setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.custom_action_bar, null);

		TextView titleTextView = (TextView) v
				.findViewById(R.id.custom_action_bar_title);
		titleTextView.setText(this.getTitle());

		this.getSupportActionBar().setCustomView(v);

		context = this;
		AdDB = new com.smartapps.designdroid.database.DatabaseAdapter(this);

		prefs = getSharedPreferences("designdroidprefs", MODE_PRIVATE);
		// main path for folders of app
		mainpathFiles = AdDB.getStrPreferences("saving_path");

		// get the directory category
		getDirFolderCategory(categoryname);
		initialization();
	}

	private void initialization() {
		closebutton = (FrameLayout) findViewById(R.id.closebutton);
		applybutton = (FrameLayout) findViewById(R.id.applybutton);
		morebuttonmenu = (ImageView) findViewById(R.id.morebuttonmenu);
		TBRowcategory = (TableRow) findViewById(R.id.TBRowcategory);
		toolslinearview = (LinearLayout) findViewById(R.id.toolslinearview);
		Mainlinearview = (FrameLayout) findViewById(R.id.Mainlinearview); // hold
																			// the
																			// view
																			// which
																			// came
																			// from
																			// inflater
		Frameview = (FrameLayout) findViewById(R.id.work_area);
		fontresizeseekbar = (SeekBar) findViewById(R.id.fontresizeSeekbar);
		rotateSeekbar = (SeekBar) findViewById(R.id.rotateSeekbar);
		opacity_seekbar = (SeekBar) findViewById(R.id.opcity_seekbar);

		final FrameLayout headerLayout = (FrameLayout) findViewById(R.id.work_area);
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
		imgHolder = (ImageView) findViewById(R.id.imgHolder);

		buttonToolsIcon = new int[] { R.drawable.addtexticonoff,
				R.drawable.coloriconoff, R.drawable.transiconoff,
				R.drawable.rotateiconoff, R.drawable.scaleiconoff,
				R.drawable.shadowiconoff, R.drawable.hflipiconoff,
				R.drawable.vflipiconoff };
		paramfortoolsicons = new TableRow.LayoutParams(150,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		horizontalScroller = (HorizontalScrollView) findViewById(R.id.horizonatl_scroller);

		// create tools buttons
		CreateToolsButton();
		// to load category buttons
		LoadCategroyButtons(FontsCategory[0]);
		// create edit text view
		createEditTextView();

		File file = new File(prefs.getString("saving_path",
				"/sdcard/designdroid/")
				+ "/"
				+ "temp_folder"
				+ "/"
				+ VirtualStore.counter + ".png");

		bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

		ViewTreeObserver vto = imgHolder.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				// Remove after the first run so it doesn't fire forever
				imgHolder.getViewTreeObserver().removeOnPreDrawListener(this);
				int finalHeight = imgHolder.getMeasuredHeight();
				int finalWidth = imgHolder.getMeasuredWidth();
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

				// 2688*1520
				// 1345*760
				// 1345*1080

				paramsWidth = newWidth;
				paramsHeight = newheight;

				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) Mainlinearview
						.getLayoutParams();
				params.height = newheight;
				params.width = newWidth;
				Mainlinearview.setLayoutParams(params);

				// imgHolder.getViewTreeObserver().removeOnPreDrawListener(this);
				return true;
			}
		});

		imgHolder.setImageBitmap(bmp);

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
					for (int i = 0; i < FontsCategory.length; i++) {
						TBforcatList = new TableRow(TextActivity.this);
						TVcatlist = new TextView(TextActivity.this);
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
									.getsubcategoryArabicName(FontsCategory[i]
											.toString().trim()));

						} else {
							TVcatlist.setText(FontsCategory[i].toString());
						}

						TBforcatList.addView(TVcatlist);
						moreMenuList.addView(TBforcatList);
						shopbuttonid = i;
					}

					TBforcatList = new TableRow(TextActivity.this);
					TVcatlist = new TextView(TextActivity.this);
					TVcatlist.setId(5000 + shopbuttonid + 1);
					TVcatlist.setTextSize(15);
					TVcatlist.setTextColor(Color.WHITE);
					TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
							TableRow.LayoutParams.WRAP_CONTENT,
							TableRow.LayoutParams.WRAP_CONTENT);

					layoutParams.setMargins((int) convertDpToPixel(10),
							(int) convertDpToPixel(10),
							(int) convertDpToPixel(10), 0);
					layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
					TVcatlist.setLayoutParams(layoutParams);
					// TVcatlist.setText("Shop");
					TVcatlist.setBackground(getResources().getDrawable(
							R.drawable.small_shop_btn));
					TBforcatList.addView(TVcatlist);
					moreMenuList.addView(TBforcatList);

					popupwindow.showAsDropDown(TBRowcategory, 0, 10);
					// morebuttonmenu.setEnabled(false);
					for (int w = 0; w < ((FontsCategory.length) + 1); w++) {
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

		rotateSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				editTextView.setRotation(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

		});
		fontresizeseekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						editTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
								progress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

				});

		// seek bar for opacity

		opacity_seekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						// need 3 color RGB for set in color to set
						int textcolor = editTextView.getCurrentTextColor();
						editTextView.setTextColor(Color.argb(progress,
								Color.red(textcolor), Color.green(textcolor),
								Color.blue(textcolor)));

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

				});

		closebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		applybutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				removeborderfromlinearview();
				removeHintfromlinearview();
				editTextView.setCursorVisible(false);
				// Mainlinearview.setVisibility(View.INVISIBLE);
				Mainlinearview.setDrawingCacheEnabled(true);
				Mainlinearview.buildDrawingCache();

				Bitmap bitmap = Mainlinearview.getDrawingCache();
				setResult(1);
				new SaveIMG(TextActivity.this, prefs).execute(bitmap);
			}
		});
	}

	private void CreateToolsButton() {
		for (int i = 0; i < buttonToolsIcon.length; i++) {
			TBfortoolsButtons = new TableRow(this);
			toolButtons = new ImageView(this);
			toolButtons.setId(i);
			toolButtons.setImageResource(buttonToolsIcon[i]);
			TBfortoolsButtons
					.setBackgroundResource(R.drawable.toolsiconsborder);
			TBfortoolsButtons.setPadding(35, 5, 35, 8);
			TBfortoolsButtons.addView(toolButtons);
			toolslinearview.addView(TBfortoolsButtons);

		}

		// set events on tools buttons
		for (int w = 0; w < buttonToolsIcon.length; w++) {
			getToolsButton(w);

		}
	}

	private void createEditTextView() {
		counter++;

		editTextView = new EditText(TextActivity.this);
		editTextView.setId(counter);

		editTextView.setClickable(true);
		editTextView.setFocusable(true);
		editTextView.setTextSize(20);

		editTextView.setBackground(null);

		Mainlinearview.addView(editTextView);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		editTextView.setLayoutParams(layoutParams);
		editTextView.setGravity(Gravity.CENTER);

		// editTextView.setGravity(Gravity.CENTER_VERTICAL
		// * | Gravity.CENTER_HORIZONTAL);

		removeborderfromlinearview();
		editTextView.setHint("Enter your text!");
		editTextView.setHintTextColor(Color.WHITE);
		editTextView.setBackgroundResource(R.drawable.border);
		editTextView.setOnTouchListener(new ViewOnTouchListener(editTextView));

		// change in the text
		prefs.edit().putInt("editTextViewID", editTextView.getId()).commit();

		gettextview(counter);

	}

	public void getToolsButton(final int ButtonID) {

		final ImageView getTextView = (ImageView) findViewById(ButtonID);

		getTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// String gettext = getTextView.getText().toString();
				int Butid = v.getId();
				if (Butid == 0) // means add text button
				{
					// for UI design
					getTextView.setImageResource(R.drawable.addtexticon);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);
					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

					// create your text view on click add button
					createEditTextView();

				} else if (Butid == 1) // means color text button
				{
					// for UI design
					getTextView.setImageResource(R.drawable.coloricon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					shadowFlag = 0; // means affect text only
					showColorPickerDialog();

				} else if (Butid == 2) // means opacity text button
				{
					// for UI design
					getTextView.setImageResource(R.drawable.transicon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.VISIBLE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					opacity_seekbar.setProgress(255);

				} else if (Butid == 3) // means rotation text button
				{
					// for UI design
					getTextView.setImageResource(R.drawable.rotateicon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);

					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.VISIBLE);
					opacity_seekbar.setVisibility(View.GONE);
					rotateSeekbar.setProgress(Integer
							.valueOf((int) editTextView.getRotation()));

				} else if (Butid == 4) // means font size button
				{
					// for UI design
					getTextView.setImageResource(R.drawable.scaleicon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);

					fontresizeseekbar.setVisibility(View.VISIBLE);
					opacity_seekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					fontresizeseekbar.setProgress(Integer
							.valueOf((int) editTextView.getTextSize()));

				} else if (Butid == 5) { // means shadow button
					// for UI design
					getTextView.setImageResource(R.drawable.shadowicon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					shadowFlag = 1; // means affect shadow only
					showColorPickerDialog();

				} else if (Butid == 6) { // means h_filp button
					// for UI design
					getTextView.setImageResource(R.drawable.hflipicon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(7))
							.setImageResource(R.drawable.vflipiconoff);
					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

					if (editTextView.getRotationX() == 180) {
						editTextView.setRotationX(360);
					} else {
						editTextView.setRotationX(180);
					}

				} else { // means v_filp button
					getTextView.setImageResource(R.drawable.vflipicon);
					((ImageView) findViewById(0))
							.setImageResource(R.drawable.addtexticonoff);
					((ImageView) findViewById(1))
							.setImageResource(R.drawable.coloriconoff);
					((ImageView) findViewById(2))
							.setImageResource(R.drawable.transiconoff);
					((ImageView) findViewById(3))
							.setImageResource(R.drawable.rotateiconoff);
					((ImageView) findViewById(4))
							.setImageResource(R.drawable.scaleiconoff);
					((ImageView) findViewById(5))
							.setImageResource(R.drawable.shadowiconoff);
					((ImageView) findViewById(6))
							.setImageResource(R.drawable.hflipiconoff);
					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

					if (editTextView.getRotationY() == 180) {
						editTextView.setRotationY(360);
					} else {
						editTextView.setRotationY(180);
					}
				}
			}
		});

		Mainlinearview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// to remove all linerLayouy background
				removeborderfromlinearview();

			}

		});
	}

	// event for selected font type or any button in the category types
	public void getCategoryTypessButton(final int ButtonID) {

		final Button getTextView = (Button) findViewById(ButtonID);

		getTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// set the font typ face to the edit text view
				editTextView.setTypeface(getTextView.getTypeface());
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
					// open shop activity
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

	public void gettextview(final int textid) {

		editTextView = (EditText) findViewById(textid);

		editTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int getid = prefs.getInt("editTextViewID", editTextView.getId());

				// countkey++;

				isKeyboardShown = false;
				imm = (InputMethodManager) TextActivity.context
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				if (imm.isAcceptingText()) {
					isKeyboardShown = true;

					System.out.println("is shown");
				} else {
					isKeyboardShown = false;
					System.out.println("not shown");
				}

				if (getid == editTextView.getId()) {
					countkey++;
				} else {
					prefs.edit().putInt("editTextViewID", editTextView.getId())
							.commit();
					countkey = 0;
				}
				removeborderfromlinearview();

				editTextView.setOnTouchListener(null);
				editTextView.setFocusable(true);
				editTextView.setEnabled(true);
				editTextView.setOnTouchListener(new ViewOnTouchListener(
						editTextView));
				// TODO Auto-generated method stub
				editTextView = (EditText) v;
				// editTextView.setEnabled(true);
				if (editTextView.getText().equals("Enter your text!")) {
					editTextView.setText("");
				}

				rotateSeekbar.setProgress(Integer.valueOf((int) editTextView
						.getRotation()));
				fontresizeseekbar.setProgress(Integer
						.valueOf((int) editTextView.getTextSize()));
				editTextView.setBackgroundResource(R.drawable.border);

				System.out.println("clicked");
				editTextView.requestFocus();

				if (countkey == 3) {
					imm.showSoftInputFromInputMethod(
							editTextView.getWindowToken(), 0);
					countkey = 0;
				} else {
					imm.hideSoftInputFromWindow(editTextView.getWindowToken(),
							0);
				}

			}

		});
		editTextView.setCustomSelectionActionModeCallback(new Callback() {

			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			public void onDestroyActionMode(ActionMode mode) {
			}

			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(android.view.ActionMode mode,
					MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onCreateActionMode(android.view.ActionMode mode,
					Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(android.view.ActionMode mode) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onPrepareActionMode(android.view.ActionMode mode,
					Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
		});

	}

	private void removeborderfromlinearview() {
		// TODO Auto-generated method stub
		int childcount = Mainlinearview.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View linearview = Mainlinearview.getChildAt(i);
			// View textlinerlayoutview = ((ViewGroup)
			// linearview).getChildAt(0);
			try {
				editTextView = (EditText) linearview;
				editTextView.setBackgroundResource(0);
			} catch (ClassCastException e) {
				continue;
			}
		}
	}

	private void removeHintfromlinearview() {
		// TODO Auto-generated method stub
		int childcount = Mainlinearview.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View linearview = Mainlinearview.getChildAt(i);
			// View textlinerlayoutview = ((ViewGroup)
			// linearview).getChildAt(0);
			try {
				editTextView = (EditText) linearview;
				editTextView.setHint("");
			} catch (ClassCastException e) {
				continue;
			}
		}
	}

	private void LoadCategroyButtons(String subcatname) {
		// TODO Auto-generated method stub
		// category Buttons Rehab
		String fontfacefile = mainpathFiles + "/Fonts/" + subcatname.toString();

		getSubDirFolderCategory(categoryname, subcatname);
		// check if folder id empaty
		if (FontsSubCategory.length == 0) {

		} else {
			categorytypesLinear.removeAllViews();
			for (int i = 0; i < FontsSubCategory.length; i++) {
				face = Typeface.createFromFile(fontfacefile + "/"
						+ FontsSubCategory[i]);
				categoryButtonsNames = new Button(this);
				categoryButtonsNames.setId(i + 3000);
				XmlResourceParser xpp = getResources().getXml(
						R.drawable.category_txt);
				ColorStateList cl;
				try {
					cl = ColorStateList.createFromXml(getResources(), xpp);
					categoryButtonsNames.setTextColor(cl);
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				categoryButtonsNames
						.setBackgroundResource(R.color.moremenucolor);
				if (subcatname.contains("Arabic")
						|| subcatname.contains("ÚÑÈí")) {
					categoryButtonsNames.setText("äãæÐÌ");
				} else {
					categoryButtonsNames.setText("sample");
				}
				categoryButtonsNames.setTypeface(face);
				categoryButtonsNames.setTextSize(20);
				categorytypesLinear.addView(categoryButtonsNames);
			}

		}

		// set events on buttons
		for (int w = 0; w < FontsSubCategory.length; w++) {
			getCategoryTypessButton(w + 3000);

		}

	}

	/**
	 * Determine the space between the first two fingers
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * Calculate the mid point of the first two fingers
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * Calculate the degree to be rotated by.
	 * 
	 * @param event
	 * @return Degrees
	 */
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	private int showColorPickerDialog() {
		AlertDialog.Builder colorDialogBuilder = new AlertDialog.Builder(
				TextActivity.this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.color_picker, null);
		picker = (ColorPicker) dialogview.findViewById(R.id.picker);
		svBar = (SVBar) dialogview.findViewById(R.id.svbar);
		opacityBar = (OpacityBar) dialogview.findViewById(R.id.opacitybar);
		saturationBar = (SaturationBar) dialogview
				.findViewById(R.id.saturationbar);
		valueBar = (ValueBar) dialogview.findViewById(R.id.valuebar);

		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.addSaturationBar(saturationBar);
		picker.addValueBar(valueBar);

		picker.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void onColorChanged(int color) {
				Rcolor = Color.red(color);
				Gcolor = Color.green(color);
				Bcolor = Color.blue(color);

			}
		});
		colorDialogBuilder.setTitle(getString(R.string.choose_color));
		colorDialogBuilder.setView(dialogview);

		colorDialogBuilder.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("", "Color :" + picker.getColor());
						// colorPickerView.setTextColor(picker.getColor());
						picker.setOldCenterColor(picker.getColor());
						if (shadowFlag == 0) {
							editTextView.setTextColor(picker.getColor());
						} else {
							editTextView.setShadowLayer(2, 5, 5,
									picker.getColor());
						}
						color = picker.getColor();

						picker.setOldCenterColor(picker.getColor());

					}
				});
		colorDialogBuilder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog colorPickerDialog = colorDialogBuilder.create();
		colorPickerDialog.show();

		return color;
	}

	// to get catagory folders for the more button type (fonts , templates, ..)
	private void getDirFolderCategory(String categoryname) {
		filepath = mainpathFiles + "/" + categoryname.toString();
		File dir = new File(filepath + File.separator);
		File[] files = dir.listFiles();
		FontsCategory = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			FontsCategory[i] = files[i].getName();
		}
	}

	// to get Subcatagory folders for the category of more button type (fonts
	// type , templates kind, ..)
	private void getSubDirFolderCategory(String categoryname, String subname) {
		String filepath = mainpathFiles + "/" + categoryname + "/"
				+ subname.toString();
		File subDir = new File(filepath + File.separator);
		File[] files = subDir.listFiles();
		FontsSubCategory = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			FontsSubCategory[i] = files[i].getName();
		}
	}

	public float convertDpToPixel(float dp) {
		Resources resources = getApplicationContext().getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	@Override
	public void onBackPressed() {
		if (isKeyboardShown) {
			isKeyboardShown = false;
			imm.hideSoftInputFromWindow(editTextView.getWindowToken(), 0);
			countkey = 0;
		} else
			finish();
	}
}
