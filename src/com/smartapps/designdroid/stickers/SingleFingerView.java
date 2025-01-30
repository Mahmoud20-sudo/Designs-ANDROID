package com.smartapps.designdroid.stickers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.smaprtapps.designdroid.R;

public class SingleFingerView extends LinearLayout {
	private ImageView mView;
	private ImageView mPushView;
	private ImageView mCancelView;
	private float _1dp;
	private boolean mCenterInParent = true;
	private Drawable mImageDrawable, mPushImageDrawable, mCancelImageDrawable;
	private float mImageHeight, mImageWidth, mPushImageHeight, mPushImageWidth,
			mCancelImageHeight, mCancelImageWidth;
	private int mLeft = 0, mTop = 0;
	private FrameLayout frameview;
	private int counter = 1000;
	public static Bitmap mImageBitmap;

	public SingleFingerView(Context context) {
		this(context, null, 0);
	}

	public SingleFingerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SingleFingerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this._1dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
				context.getResources().getDisplayMetrics());
		this.parseAttr(context, attrs);
		View mRoot = View.inflate(context, R.layout.sticker_inflator, null);
		addView(mRoot, -1, -1);

		frameview = (FrameLayout) mRoot.findViewById(R.id.frameview);
		mPushView = new ImageView(context);
		mPushView.setId(counter++);
		mPushView.setImageResource(R.drawable.rotate_scale_sbtn);
		mView = new ImageView(context);
		mView.setId(2000 + counter++);
		mView.setImageResource(R.drawable.crown);
		mCancelView = new ImageView(context);
		mCancelView.setId(10000 + counter++);
		mCancelView.setBackgroundResource(R.drawable.del_sbtn);

		frameview.addView(mPushView);
		frameview.addView(mView);
		// mCancelView.setVisibility(View.INVISIBLE);
		frameview.addView(mCancelView);

		mPushView.setFocusable(true);
		mPushView.setClickable(true);
		mPushView.setFocusableInTouchMode(true);

		mView.setFocusable(true);
		mView.setClickable(true);
		mView.setFocusableInTouchMode(true);

		mCancelView.setClickable(true);

		// mPushView = (ImageView) mRoot.findViewById(R.id.push_view);
		// mView = (ImageView) mRoot.findViewById(R.id.view);
		mPushView.setOnTouchListener(new PushBtnTouchListener(mView,
				mCancelView));
		mView.setOnTouchListener(new ViewOnTouchListener(mPushView, mCancelView));
		initForSingleFingerView();
	/*	mView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
				mCancelView.setVisibility(View.VISIBLE);
				mPushView.setVisibility(View.VISIBLE);
			}
		});*/
	}

	private void parseAttr(Context context, AttributeSet attrs) {
		TypedArray a;
		if (null == attrs) {
			a = StikcersActivity.aa;
			if (a != null) {
				int n = a.getIndexCount();
				for (int i = 0; i < n; i++) {
					int attr = a.getIndex(i);
					if (attr == R.styleable.SingleFingerView_centerInParent) {
						this.mCenterInParent = a.getBoolean(attr, true);
					} else if (attr == R.styleable.SingleFingerView_image) {
						this.mImageDrawable = a.getDrawable(attr);
					} else if (attr == R.styleable.SingleFingerView_image_height) {
						this.mImageHeight = a.getDimension(attr, 200 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_image_width) {
						this.mImageWidth = a.getDimension(attr, 200 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_push_image) {
						this.mPushImageDrawable = a.getDrawable(attr);
					} else if (attr == R.styleable.SingleFingerView_push_image_width) {
						this.mPushImageWidth = a.getDimension(attr, 30 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_push_image_height) {
						this.mPushImageHeight = a.getDimension(attr, 30 * _1dp);
					}

					else if (attr == R.styleable.SingleFingerView_cancel_image) {
						this.mCancelImageDrawable = a.getDrawable(attr);
					} else if (attr == R.styleable.SingleFingerView_cancel_image_width) {
						this.mCancelImageWidth = a
								.getDimension(attr, 20 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_cancel_image_height) {
						this.mCancelImageHeight = a.getDimension(attr,
								20 * _1dp);

					} else if (attr == R.styleable.SingleFingerView_left) {
						this.mLeft = (int) a.getDimension(attr, 0 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_top) {
						this.mTop = (int) a.getDimension(attr, 0 * _1dp);
					}
				}
			}
		} else {
			a = context.obtainStyledAttributes(attrs,
					R.styleable.SingleFingerView);
			StikcersActivity.aa = a;

			if (a != null) {
				int n = a.getIndexCount();
				for (int i = 0; i < n; i++) {
					int attr = a.getIndex(i);
					if (attr == R.styleable.SingleFingerView_centerInParent) {
						this.mCenterInParent = a.getBoolean(attr, true);
					} else if (attr == R.styleable.SingleFingerView_image) {
						this.mImageDrawable = a.getDrawable(attr);
					} else if (attr == R.styleable.SingleFingerView_image_height) {
						this.mImageHeight = a.getDimension(attr, 200 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_image_width) {
						this.mImageWidth = a.getDimension(attr, 200 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_push_image) {
						this.mPushImageDrawable = a.getDrawable(attr);
					} else if (attr == R.styleable.SingleFingerView_push_image_width) {
						this.mPushImageWidth = a.getDimension(attr, 50 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_push_image_height) {
						this.mPushImageHeight = a.getDimension(attr, 50 * _1dp);
					}

					else if (attr == R.styleable.SingleFingerView_cancel_image) {
						this.mCancelImageDrawable = a.getDrawable(attr);
					} else if (attr == R.styleable.SingleFingerView_cancel_image_width) {
						this.mCancelImageWidth = a
								.getDimension(attr, 20 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_cancel_image_height) {
						this.mCancelImageHeight = a.getDimension(attr,
								20 * _1dp);

					} else if (attr == R.styleable.SingleFingerView_left) {
						this.mLeft = (int) a.getDimension(attr, 0 * _1dp);
					} else if (attr == R.styleable.SingleFingerView_top) {
						this.mTop = (int) a.getDimension(attr, 0 * _1dp);
					}
				}
			}
		}
	}

	private void initForSingleFingerView() {
		/*
		 * ViewTreeObserver vto2 = mView.getViewTreeObserver();
		 * vto2.addOnGlobalLayoutListener(new
		 * ViewTreeObserver.OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() { FrameLayout.LayoutParams
		 * viewLP = (FrameLayout.LayoutParams) mView.getLayoutParams();
		 * FrameLayout.LayoutParams pushViewLP = (FrameLayout.LayoutParams)
		 * mPushView.getLayoutParams(); pushViewLP.width = (int)
		 * mPushImageWidth; pushViewLP.height = (int) mPushImageHeight;
		 * pushViewLP.leftMargin = (viewLP.leftMargin + mView.getWidth()) -
		 * mPushView.getWidth() / 2; pushViewLP.topMargin = (viewLP.topMargin +
		 * mView.getHeight()) - mPushView.getWidth() / 2;
		 * mPushView.setLayoutParams(pushViewLP); } });
		 */
	}

	private void setViewToAttr(int pWidth, int pHeight) {
		if (null != mImageBitmap) {
			this.mView.setImageBitmap(mImageBitmap);
			mView.setBackgroundResource(R.drawable.dotted);
		}
		if (null != mPushImageDrawable) {
			this.mPushView.setImageDrawable(mPushImageDrawable);
		}
		if (null != mCancelImageDrawable) {
			this.mCancelView.setImageDrawable(mCancelImageDrawable);
		}
		FrameLayout.LayoutParams viewLP = (FrameLayout.LayoutParams) this.mView
				.getLayoutParams();
		viewLP.width = (int) mImageWidth;
		viewLP.height = (int) mImageHeight;
		int left = 0, top = 0;
		if (mCenterInParent) {
			left = pWidth / 2 - viewLP.width / 2;
			top = pHeight / 2 - viewLP.height / 2;
		} else {
			if (mLeft > 0)
				left = mLeft;
			if (mTop > 0)
				top = mTop;
		}
		viewLP.leftMargin = left;
		viewLP.topMargin = top;
		this.mView.setLayoutParams(viewLP);

		FrameLayout.LayoutParams pushViewLP = (FrameLayout.LayoutParams) mPushView
				.getLayoutParams();
		pushViewLP.width = (int) mPushImageWidth;
		pushViewLP.height = (int) mPushImageHeight;
		pushViewLP.leftMargin = (int) (viewLP.leftMargin + mImageWidth - mPushImageWidth / 2);
		pushViewLP.topMargin = (int) (viewLP.topMargin + mImageHeight - mPushImageHeight / 2);
		mPushView.setLayoutParams(pushViewLP);

		FrameLayout.LayoutParams cancelViewLP = (FrameLayout.LayoutParams) mCancelView
				.getLayoutParams();
		cancelViewLP.width = (int) mCancelImageWidth;
		cancelViewLP.height = (int) mCancelImageHeight;
		cancelViewLP.leftMargin = (int) (viewLP.leftMargin - mCancelImageWidth / 2);
		cancelViewLP.topMargin = (int) (viewLP.topMargin - mCancelImageHeight / 2);
		mCancelView.setLayoutParams(cancelViewLP);

		mCancelView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				frameview.removeView(mView);
				frameview.removeView(mPushView);
				frameview.removeView(mCancelView);
		
				Toast.makeText(getContext(), mView.getId() + "",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setParamsForView(widthMeasureSpec, heightMeasureSpec);
	}

	private boolean hasSetParamsForView = false;

	private void setParamsForView(int widthMeasureSpec, int heightMeasureSpec) {
		ViewGroup.LayoutParams layoutParams = getLayoutParams();
		if (null != layoutParams && !hasSetParamsForView) {
			System.out.println("AAAAAAAAAAAAAAAAAAA setParamsForView");
			hasSetParamsForView = true;
			int width;
			if ((getLayoutParams().width == LayoutParams.MATCH_PARENT)) {
				width = MeasureSpec.getSize(widthMeasureSpec);
			} else {
				width = getLayoutParams().width;
			}
			int height;
			if ((getLayoutParams().height == LayoutParams.MATCH_PARENT)) {
				height = MeasureSpec.getSize(heightMeasureSpec);
			} else {
				height = getLayoutParams().height;
			}
			setViewToAttr(width, height);
		}
	}
}
