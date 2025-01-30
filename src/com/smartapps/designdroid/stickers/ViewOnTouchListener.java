package com.smartapps.designdroid.stickers;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.smaprtapps.designdroid.R;

class ViewOnTouchListener implements View.OnTouchListener {
	Point pushPoint;
	int lastImgLeft;
	int lastImgTop;
	FrameLayout.LayoutParams viewLP;
	FrameLayout.LayoutParams pushBtnLP;

	FrameLayout.LayoutParams cancelBtnLP;

	int lastPushBtnLeft;
	int lastPushBtnTop;
	private View mPushView;

	int lastCancelBtnLeft;
	int lastCancelBtnTop;
	private View mCancelView;

	ViewOnTouchListener(View mPushView, View mCancelView) {
		this.mPushView = mPushView;
		this.mCancelView = mCancelView;

	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (null == viewLP) {
				viewLP = (FrameLayout.LayoutParams) view.getLayoutParams();
			}
			if (null == pushBtnLP) {
				pushBtnLP = (FrameLayout.LayoutParams) mPushView
						.getLayoutParams();
			}
			if (null == cancelBtnLP) {
				cancelBtnLP = (FrameLayout.LayoutParams) mCancelView
						.getLayoutParams();
			}
			view.setBackgroundResource(R.drawable.dotted);
			pushPoint = getRawPoint(event);
			lastImgLeft = viewLP.leftMargin;
			lastImgTop = viewLP.topMargin;
			lastPushBtnLeft = pushBtnLP.leftMargin;
			lastPushBtnTop = pushBtnLP.topMargin;
			lastCancelBtnLeft = cancelBtnLP.leftMargin;
			lastCancelBtnTop = cancelBtnLP.topMargin;
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
			
			cancelBtnLP.leftMargin = (int) (lastCancelBtnLeft + moveX);
			cancelBtnLP.topMargin = (int) (lastCancelBtnTop + moveY);
			mCancelView.setLayoutParams(cancelBtnLP);

			mPushView.setVisibility(View.VISIBLE);
			mCancelView.setVisibility(View.VISIBLE);
			
			break;

		}
		return false;
	}

	private Point getRawPoint(MotionEvent event) {
		return new Point((int) event.getRawX(), (int) event.getRawY());
	}
}
