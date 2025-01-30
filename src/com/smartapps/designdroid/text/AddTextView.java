package com.smartapps.designdroid.text;

import java.io.File;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.smaprtapps.designdroid.R;
//import com.smartapps.design.inflater.PushBtnTouchListener;

public class AddTextView extends Activity {

	// interface views
	private Button closebutton, applybutton, toolButtons, categoryButtonsNames,
			morebuttonmenu;
	private LinearLayout toolslinearview, categorytypesLinear, moreMenuList;
	private ViewGroup mRrootLayout;
	private EditText editTextView;
	private LinearLayout textlinerlayout;
	private RelativeLayout Mainlinearview;
	private ColorPicker picker;
	private SVBar svBar;
	private OpacityBar opacityBar;
	private SaturationBar saturationBar;
	private ValueBar valueBar;

	private View textInflateView, getTextView, moremenulist;
	private SeekBar fontresizeseekbar, rotateSeekbar, opacity_seekbar;
	private TableRow TBfortoolsButtons, TBforcatList, TBRowcategory;
	private TableRow.LayoutParams paramfortoolsicons;
	private TextView TVcatlist;
	private PopupWindow popupwindow;
	private View popupView;

	private int counter = 300; // view edit text id
	private String[] FontsCategory, FontsSubCategory;
	private int[] buttonToolsIcon;
	private Typeface face;
	int color, Rcolor = 255, Gcolor = 255, Bcolor = 255, shadowFlag = 0;
	private String categoryname = "Fonts"; // change to set your category type
											// name
	private String filepath, mainpathFiles;

	public static Context context;

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
	private com.smartapps.designdroid.database.DatabaseAdapter AdDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_master_view);
		context = this;
		 AdDB = new  com.smartapps.designdroid.database.DatabaseAdapter(this);

		// main path for folders of app
		mainpathFiles = AdDB.getStrPreferences("saving_path");
		
		// get the directory category
				getDirFolderCategory(categoryname);
		initialization();

		

	}

	private void initialization() {
		closebutton = (Button) findViewById(R.id.closebutton);
		applybutton = (Button) findViewById(R.id.applybutton);
		morebuttonmenu = (Button) findViewById(R.id.morebuttonmenu);
		TBRowcategory = (TableRow) findViewById(R.id.TBRowcategory);
		toolslinearview = (LinearLayout) findViewById(R.id.toolslinearview);
		Mainlinearview = (RelativeLayout) findViewById(R.id.Mainlinearview); // hold
																				// the
																				// view
																				// which
																				// came
																				// from
																				// inflater
		fontresizeseekbar = (SeekBar) findViewById(R.id.fontresizeSeekbar);
		rotateSeekbar = (SeekBar) findViewById(R.id.rotateSeekbar);
		opacity_seekbar = (SeekBar) findViewById(R.id.opcity_seekbar);

		categorytypesLinear = (LinearLayout) findViewById(R.id.categorytypesLinear);

		buttonToolsIcon = new int[] { R.drawable.addtexticonoff,
				R.drawable.coloriconoff, R.drawable.transiconoff,
				R.drawable.strokiconoff, R.drawable.rotateiconoff,
				R.drawable.scaleiconoff, R.drawable.shadowiconoff,
				R.drawable.hflipiconoff, R.drawable.vflipiconoff };
		paramfortoolsicons = new TableRow.LayoutParams(150,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// create tools buttons
		CreateToolsButton();
		// to load category buttons
		LoadCategroyButtons(FontsCategory[0]); 
		// create edit text view
		createEditTextView(); 

		morebuttonmenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Rehab
				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				popupView = layoutInflater.inflate(R.layout.moremenu, null);
				popupwindow = new PopupWindow(popupView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				moreMenuList = (LinearLayout) popupView
						.findViewById(R.id.moreMenuList);
				int shopbuttonid = 0;
				// create list of category
				for (int i = 0; i < FontsCategory.length; i++) {
					TBforcatList = new TableRow(AddTextView.this);
					TVcatlist = new TextView(AddTextView.this);
					TVcatlist.setId(5000 + i);
					TVcatlist.setTextSize(25);
					TVcatlist.setText(FontsCategory[i].toString());
					TBforcatList.addView(TVcatlist);
					moreMenuList.addView(TBforcatList);
					shopbuttonid = i;
				}

				TBforcatList = new TableRow(AddTextView.this);
				TVcatlist = new TextView(AddTextView.this);
				TVcatlist.setId(5000 + shopbuttonid + 1);
				TVcatlist.setTextSize(25);
				TVcatlist.setText("Shop");
				TBforcatList.addView(TVcatlist);
				moreMenuList.addView(TBforcatList);

				popupwindow.showAsDropDown(TBRowcategory, 0, 10);
				morebuttonmenu.setEnabled(false);
				for (int w = 0; w < ((FontsCategory.length) + 1); w++) {
					getTVcatlistname(w + 5000);

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
		opacity_seekbar.setProgress(100);
		opacity_seekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						// need 3 color RGB for set in color to set
						editTextView.setTextColor(Color.argb(progress, Rcolor,
								Gcolor, Bcolor));

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
	}

	private void CreateToolsButton() {
		for (int i = 0; i < buttonToolsIcon.length; i++) {
			TBfortoolsButtons = new TableRow(this);
			toolButtons = new Button(this);
			toolButtons.setId(i);
			toolButtons.setBackgroundResource(buttonToolsIcon[i]);
			TBfortoolsButtons
					.setBackgroundResource(R.drawable.toolsiconsborder);
			TBfortoolsButtons.setPadding(25, 0, 25, 0);
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

		editTextView = new EditText(AddTextView.this);
		editTextView.setId(counter);
		
		editTextView.setText("Enter your text!");
		editTextView.setBackgroundResource(R.drawable.border);
		editTextView.setClickable(true);
		editTextView.setFocusable(true);
		editTextView.setTextSize(20);

		Mainlinearview.addView(editTextView);
		removeborderfromlinearview();
		editTextView.setOnTouchListener(new ViewOnTouchListener(editTextView));

		gettextview(counter);

	}

	public void getToolsButton(final int ButtonID) {

		final Button getTextView = (Button) findViewById(ButtonID);

		getTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// String gettext = getTextView.getText().toString();
				int Butid = v.getId();
				if (Butid == 0) // means add text button
				{
					// for UI design
					getTextView.setBackgroundResource(R.drawable.addtexticon);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);
					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

					// create your text view on click add button
					createEditTextView();

				} else if (Butid == 1) // means color text button
				{
					// for UI design
					getTextView.setBackgroundResource(R.drawable.coloricon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					shadowFlag = 0; // means affect text only
					showColorPickerDialog();

				} else if (Butid == 2) // means opacity text button
				{
					// for UI design
					getTextView.setBackgroundResource(R.drawable.transicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.VISIBLE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

				} else if (Butid == 3) // means Stroke text button
				{
					// for UI design
					getTextView.setBackgroundResource(R.drawable.strokicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

				} else if (Butid == 4) // means rotation text button
				{
					// for UI design
					getTextView.setBackgroundResource(R.drawable.rotateicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);

					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.VISIBLE);
					opacity_seekbar.setVisibility(View.GONE);

				} else if (Butid == 5) // means font size button
				{
					// for UI design
					getTextView.setBackgroundResource(R.drawable.scaleicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);

					fontresizeseekbar.setVisibility(View.VISIBLE);
					opacity_seekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					float getfontsize = editTextView.getTextSize();
					fontresizeseekbar.setProgress((int) getfontsize);

				} else if (Butid == 6) { // means shadow button
					// for UI design
					getTextView.setBackgroundResource(R.drawable.shadowicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);

					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);
					shadowFlag = 1; // means affect shadow only
					showColorPickerDialog();

				} else if (Butid == 7) { // means h_filp button
					// for UI design
					getTextView.setBackgroundResource(R.drawable.hflipicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(8).setBackgroundResource(
							R.drawable.vflipiconoff);
					opacity_seekbar.setVisibility(View.GONE);
					fontresizeseekbar.setVisibility(View.GONE);
					rotateSeekbar.setVisibility(View.GONE);

					if (editTextView.getRotationX() == 180) {
						editTextView.setRotationX(360);
					} else {
						editTextView.setRotationX(180);
					}

				} else { // means v_filp button
					getTextView.setBackgroundResource(R.drawable.vflipicon);
					findViewById(0).setBackgroundResource(
							R.drawable.addtexticonoff);
					findViewById(1).setBackgroundResource(
							R.drawable.coloriconoff);
					findViewById(2).setBackgroundResource(
							R.drawable.transiconoff);
					findViewById(3).setBackgroundResource(
							R.drawable.strokiconoff);
					findViewById(4).setBackgroundResource(
							R.drawable.rotateiconoff);
					findViewById(5).setBackgroundResource(
							R.drawable.scaleiconoff);
					findViewById(6).setBackgroundResource(
							R.drawable.shadowiconoff);
					findViewById(7).setBackgroundResource(
							R.drawable.hflipiconoff);
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
				int Butid = v.getId();
				String getcatnameformtextview = getTextView.getText()
						.toString();
				if (!getcatnameformtextview.equals("shop")) {
					// load subcategorys
					LoadCategroyButtons(getcatnameformtextview);
				} else {
					// open shop activity

				}
				morebuttonmenu.setEnabled(true);
				popupwindow.dismiss();
			}
		});
	}

	public void gettextview(final int textid) {

		editTextView = (EditText) findViewById(textid);

		editTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				removeborderfromlinearview();

				editTextView.setOnTouchListener(null);
				editTextView.setOnTouchListener(new ViewOnTouchListener(
						editTextView));
				// TODO Auto-generated method stub
				editTextView = (EditText) v;
				// editTextView.setEnabled(true);
				if (editTextView.getText().equals("Enter your text!")) {
					editTextView.setText("");
				}
				editTextView.setBackgroundResource(R.drawable.border);

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
			editTextView = (EditText) linearview;
			editTextView.setBackgroundResource(0);
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
				categoryButtonsNames
						.setBackgroundResource(R.color.moremenucolor);
				categoryButtonsNames.setText("sample");
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

	/*
	 * // to move resize and scall public boolean onTouch(View v, MotionEvent
	 * event) { // handle touch events here LinearLayout view = (LinearLayout)
	 * v; switch (event.getAction() & MotionEvent.ACTION_MASK) { case
	 * MotionEvent.ACTION_DOWN: savedMatrix.set(matrix); start.set(event.getX(),
	 * event.getY()); mode = DRAG; lastEvent = null; break; case
	 * MotionEvent.ACTION_POINTER_DOWN: oldDist = spacing(event); if (oldDist >
	 * 10f) { savedMatrix.set(matrix); midPoint(mid, event); mode = ZOOM; }
	 * lastEvent = new float[4]; lastEvent[0] = event.getX(0); lastEvent[1] =
	 * event.getX(1); lastEvent[2] = event.getY(0); lastEvent[3] =
	 * event.getY(1); d = rotation(event); break; case MotionEvent.ACTION_UP:
	 * case MotionEvent.ACTION_POINTER_UP: mode = NONE; lastEvent = null; break;
	 * case MotionEvent.ACTION_MOVE: if (mode == DRAG) {
	 * matrix.set(savedMatrix); float dx = event.getX() - start.x; float dy =
	 * event.getY() - start.y; matrix.postTranslate(dx, dy); } else if (mode ==
	 * ZOOM) { float newDist = spacing(event); if (newDist > 10f) {
	 * matrix.set(savedMatrix); float scale = (newDist / oldDist);
	 * matrix.postScale(scale, scale, mid.x, mid.y); } if (lastEvent != null &&
	 * event.getPointerCount() == 2) { newRot = rotation(event); float r =
	 * newRot - d; float[] values = new float[9]; matrix.getValues(values);
	 * float tx = values[2]; float ty = values[5]; float sx = values[0]; float
	 * xc = (view.getWidth() / 2) * sx; float yc = (view.getHeight() / 2) * sx;
	 * 
	 * matrix.postRotate(r, tx + xc, ty + yc); } } break; }
	 * 
	 * float[] values = new float[9]; matrix.getValues(values); float globalX =
	 * values[Matrix.MTRANS_X]; float globalY = values[Matrix.MTRANS_Y]; float
	 * globalR = values[Matrix.MPERSP_2];
	 * 
	 * // calculate real scale float scalex = values[Matrix.MSCALE_X]; float
	 * scaley = values[Matrix.MSCALE_Y]; float skewy = values[Matrix.MSKEW_Y];
	 * float rScale = (float) Math.sqrt(scalex * scalex + skewy * skewy);
	 * 
	 * // calculate the degree of rotation float rAngle =
	 * Math.round(Math.atan2(values[Matrix.MSKEW_X], values[Matrix.MSCALE_X]) *
	 * (180 / Math.PI));
	 * 
	 * view.setX(globalX); view.setY(globalY); // view.setRotation(globalR); //
	 * view.setRotationY(45); view.setScaleX(scalex); view.setScaleY(scaley);
	 * return true; }
	 */

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
				AddTextView.this);
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
							editTextView.setShadowLayer(2, 5, 5, picker.getColor());
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
}
