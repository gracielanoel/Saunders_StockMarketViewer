package com.example.graci.saunders_stocksearchapp;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by graci on 5/13/2016.
 */
public class news_underliner extends TextView {
    private Rect mRect;
    private Paint mPaint;
    private int mColor;
    private float density;
    private float mStrokeWidth;

    public news_underliner(Context context) {
        this(context, null, 0);
        this.setPaintFlags(this.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    public news_underliner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setPaintFlags(this.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    public news_underliner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setPaintFlags(this.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        //init(context, attrs, defStyleAttr);
    }

}
