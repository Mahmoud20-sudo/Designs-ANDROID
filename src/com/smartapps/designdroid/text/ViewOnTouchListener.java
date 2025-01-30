package com.smartapps.designdroid.text;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class ViewOnTouchListener implements View.OnTouchListener {
	Point pushPoint;
	int lastImgLeft;
	int lastImgTop;
	FrameLayout.LayoutParams viewLP;
	FrameLayout.LayoutParams pushBtnLP;
	int lastPushBtnLeft;
	int lastPushBtnTop;
	private View mPushView;

	public ViewOnTouchListener(View mPushView) {
		this.mPushView = mPushView;

	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// update view to be like the old one
		// view = ((ViewGroup)view).getChildAt(0);
		EditText et = (EditText) view;

		InputMethodManager imm = (InputMethodManager) TextActivity.context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		//boolean isShown = imm.isAcceptingText();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (null == viewLP) {
				viewLP = (FrameLayout.LayoutParams) view.getLayoutParams();
			}
			if (null == pushBtnLP) {
				pushBtnLP = (FrameLayout.LayoutParams) mPushView
						.getLayoutParams();
			}
			pushPoint = getRawPoint(event);
			lastImgLeft = viewLP.leftMargin;
			lastImgTop = viewLP.topMargin;
			lastPushBtnLeft = pushBtnLP.leftMargin;
			lastPushBtnTop = pushBtnLP.topMargin;

			// et.setFocusable(false);
			break;
		case MotionEvent.ACTION_MOVE:
			Point newPoint = getRawPoint(event);
			float moveX = newPoint.x - pushPoint.x;
			float moveY = newPoint.y - pushPoint.y;

			viewLP.leftMargin = (int) (lastImgLeft + moveX);
			viewLP.topMargin = (int) (lastImgTop + moveY);
			view.setLayoutParams(viewLP);

			pushBtnLP.leftMargin = (int) (lastPushBtnLeft + moveX);
			pushBtnLP.topMargin = (int) (lastPushBtnTop + moveY);
			mPushView.setLayoutParams(pushBtnLP);
			if (TextActivity.isKeyboardShown) {
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				//TextActivity.isKeyboardShown = false ;
			}
			// et.setFocusable(true);
			break;
		case MotionEvent.ACTION_UP:
			/*if (TextActivity.isKeyboardShown) {
				imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
			}*/
			// et.setFocusable(true); 
			break;
		default:
			// et.setFocusable(true);
			break;
		}

		return false;
	}

	private Point getRawPoint(MotionEvent event) {
		return new Point((int) event.getRawX(), (int) event.getRawY());
	}
}
