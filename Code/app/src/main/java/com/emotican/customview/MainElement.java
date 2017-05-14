package com.emotican.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emotican.emotican.DrawActivity;
import com.emotican.emotican.R;

public class MainElement extends LinearLayout {
    private ImageView image;
    private Bitmap bitmap;
    private TextView text;
    private int width, type;
    private Activity pointerTop;
    private GestureDetector mGestureDetector;

    public static final int TYPE_NEW = 1, TYPE_NET = 2, TYPE_LOCAL = 3;

    public MainElement(Activity top, Context fatherContext, Point posInGrid, Bitmap image, String text, int windowWidth, int type) {
        super(fatherContext);
        this.pointerTop = top;
        this.bitmap = image;
        this.width = windowWidth;
        this.type = type;
        initThis(posInGrid);
        initImageView(fatherContext, image);
        initTextView(fatherContext, text);

        mGestureDetector = new GestureDetector(fatherContext, new ElementListener());
        mGestureDetector.setOnDoubleTapListener(new ElementListener());
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    private void initImageView(Context context, Bitmap image) {
        this.image = new ImageView(context);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 5;
        this.image.setLayoutParams(lp);
        this.image.setImageBitmap(image);
        this.addView(this.image);
    }

    private void initTextView(Context context, String text) {
        this.text = new TextView(context);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        this.text.setLayoutParams(lp);
        this.text.setText(text);
        this.addView(this.text);
    }

    private void initThis(Point posInGrid) {
        final int padding = (int) getResources().getDimension(R.dimen.large_margin);
        final int w = (width - 3 * padding) / 2;
        GridLayout.Spec row = GridLayout.spec(posInGrid.x);
        GridLayout.Spec column = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            column = GridLayout.spec(posInGrid.y, 1f);
        }
        else {
            column = GridLayout.spec(posInGrid.y);
        }
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(row, column);
        lp.height = (int) getResources().getDimension(R.dimen.main_element_height);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            lp.width = w;
        }
        lp.setMargins(posInGrid.x==1 ? padding/2 : padding, padding, posInGrid.x==0 ? padding/2 : padding, padding);
        this.setLayoutParams(lp);
    }

    public class ElementListener extends GestureDetector.SimpleOnGestureListener {
        public ElementListener() {
            super();
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (type == TYPE_NEW) {
                openDrawing();
            }
            else {
                openShare();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            openTooltip(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            openDrawing();
            return false;
        }
    }

    private void openShare() {
        /**
         * 打开分享到
         */
    }

    private void openDrawing() {
        Intent intent = new Intent(pointerTop, DrawActivity.class);
        intent.putExtra(this.text.getText().toString(), this.bitmap);
        pointerTop.startActivity(intent);
    }

    private void openTooltip(MotionEvent e) {
        /**
         * 弹出选项
         */
    }
}
