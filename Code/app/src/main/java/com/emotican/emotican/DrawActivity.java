package com.emotican.emotican;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.emotican.API.LocalAPI;
import com.emotican.customview.ImageEditor;

public class DrawActivity extends AppCompatActivity {
    private static final int NUMBER_BUTTONS = 6;

    private String[] allMoulds_label, allMoulds_secondary_labels;
    private boolean[] allMoulds_isHot;
    private Bitmap[] allMoulds_image;
    private RelativeLayout layoutLabels, layoutScroll;
    private RelativeLayout[] labArray = new RelativeLayout[NUMBER_BUTTONS];
    private LinearLayout[] scrollArray = new LinearLayout[NUMBER_BUTTONS];
    private ImageEditor imageEditor;
    private ImageButton[] buttons = new ImageButton[NUMBER_BUTTONS];
    private ImageButton backward, save, undo, redo, layerUp, layerDown;
    private ImageView helpImage;

    private static void logD(String msg) {
        Log.d("DrawActivity", msg);
    }
    private static void logE(String msg) {
        Log.e("DrawActivity", msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        //initMoulds();
        initViews();
        initLabelAndScroll();
    }

    private void initLabelAndScroll() {

    }

    /**
     * do not use in testing
     */
    private void initMoulds() {
        Object[] objects = LocalAPI.getAllMoulds();
        allMoulds_label = (String[]) objects[0];
        allMoulds_secondary_labels = (String[]) objects[1];
        allMoulds_isHot = (boolean[]) objects[2];
        allMoulds_image = (Bitmap[]) objects[3];
    }

    private void initViews() {
        imageEditor = (ImageEditor) findViewById(R.id.image_edit);

        helpImage = (ImageView) findViewById(R.id.draw_help_image);

        layoutLabels = (RelativeLayout) findViewById(R.id.labels_layout);
        layoutScroll = (RelativeLayout) findViewById(R.id.scroll_layout);

        backward = (ImageButton) findViewById(R.id.backward);
        save = (ImageButton) findViewById(R.id.save);
        undo = (ImageButton) findViewById(R.id.undo);
        redo = (ImageButton) findViewById(R.id.redo);
        layerUp = (ImageButton) findViewById(R.id.layerUp);
        layerDown = (ImageButton) findViewById(R.id.layerDown);
        setEnable(undo, false);
        setEnable(redo, false);
        setEnable(layerUp, false);
        setEnable(layerDown, false);

        buttons[0] = (ImageButton) findViewById(R.id.mould);
        buttons[1] = (ImageButton) findViewById(R.id.shape);
        buttons[2] = (ImageButton) findViewById(R.id.photo);
        buttons[3] = (ImageButton) findViewById(R.id.magic);
        buttons[4] = (ImageButton) findViewById(R.id.character);
        buttons[5] = (ImageButton) findViewById(R.id.effect);
    }

    private void setEnable(ImageButton button, boolean enable) {
        if (!enable) { //disable
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorGray2));
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ripple_bg_nolimit_trans_small));
            else //less than version 16
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.ripple_bg_nolimit_trans_small));
            button.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGray));
        }
    }

    private void setViewVisible(View view, boolean visible) {
        if (visible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }
}
