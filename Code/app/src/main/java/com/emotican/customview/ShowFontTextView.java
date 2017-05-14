package com.emotican.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.emotican.emotican.R;

public class ShowFontTextView extends AppCompatTextView {
    private Context context;
    private String fontPath;

    public ShowFontTextView(Context context) {
        super(context);
    }
    public ShowFontTextView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        init(attr);
    }
    public ShowFontTextView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        this.context = context;
        init(attr);
    }

    private void init(AttributeSet attrs) {
        // 获取自定义组件的属性
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.font_text);
        try {
            fontPath = types.getString(R.styleable.font_text_font_path);
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
    }

    /**
     * 重写方法，使得textView绝对正方
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    /**
     * 重写方法，使得textView背景上有方框和十字
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int backColor = ContextCompat.getColor(getContext(), R.color.colorGray);
        float width = getResources().getDimension(R.dimen.view_tiny_strok_width);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);

        DashPathEffect pathEffect = new DashPathEffect(new float[] {8, 10}, 0);
        Path path = new Path();
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(backColor);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect);

        path.moveTo(0, getHeight() / 2);
        path.lineTo(getWidth(), getHeight() / 2);
        paint.setPathEffect(pathEffect);
        canvas.drawPath(path, paint);
        path = new Path();
        path.moveTo(getWidth() / 2, 0);
        path.lineTo(getWidth() / 2, getHeight());
        canvas.drawPath(path, paint);

        super.onDraw(canvas);
    }
}
