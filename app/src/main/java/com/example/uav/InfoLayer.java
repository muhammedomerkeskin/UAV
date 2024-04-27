package com.example.uav;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Date;

public class InfoLayer extends View {


    Date startDate;
    private int centerX;
    private int centerY;
    private int height;
    protected float mAltitude;
    protected float mDroneVerticalDistance;
    protected int mAltitude_max = 0;
    protected int mAltitude_min = 0;
    private float mAltitudePixel;
    protected float mAzimuth;
    private float mAzimuthPixel;
    private Context mContext;
    private float mDensity;
    private Paint mPaintLine;
    private Paint mPaintLineAzimuth;

    private Paint mPaintLineAltitude;
    private Paint mPaintLineCenter;
    private Paint mPaintTextCenter;
    private Paint mPaintTextLeft;
    private Paint mPaintTextRight;

    private Paint mPaintTextOrientationAlert, mPaintTextMaxMinAlert, mPaintTextZoneAlert, mPaintTextRangeAlert;
    private Paint test;
    protected float mPitch;
    private float mPitchPixel;
    protected float mRoll;
    protected float mRange;

    //values
    protected int AltMinWarning;
    protected int AltMaxWarning;
    protected int AttitudeLimit;
    protected int HATMinWarning;
    protected int HATMaxWarning;
    protected int larStatus;
    protected int larRangeMax;
    protected int larRangeMin;

    protected double LRF, gimbalPitch, gimbalYaw, azimuthRadian;

    //
    private float mRangePixel;
    private float m_10;
    private float m_11;
    private float m_12;
    private float m_120;

    private float m_126;
    private float m_140;
    private float m_15;
    private float m_24;
    private float m_200;
    private float m_30;
    private float m_3;
    private float m_4;
    private float m_40;
    private float m_45;
    private float m_5;
    private float m_50;
    private float m_55;
    private float m_60;
    private float m_65;
    private float m_70;
    private float m_75;
    private float m_8;
    private float m_85;
    private float m_7;
    private float m_9;
    private SharedPreferences settings;
    private int width;
    Long timer = System.currentTimeMillis();
    Long timer2 = System.currentTimeMillis();

    public InfoLayer(Context context) {
        super(context);
        init(context);
    }

    public InfoLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InfoLayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
        this.m_4 = 4.0f * this.mDensity;
        this.mAzimuthPixel = this.mDensity * 2.6f;
        this.mPaintLineAzimuth = new Paint(1);
        this.mPaintLineAzimuth.setStyle(Paint.Style.STROKE);
        this.mPaintLineAzimuth.setColor(Color.YELLOW);
        this.mPaintLineAzimuth.setStrokeWidth(2.0f * this.mDensity);
        this.mPaintLineAzimuth.setShadowLayer(13,  // Gölge yarıçapı
                0,   // X ekseni gölge ofseti
                0,   // Y ekseni gölge ofseti
                Color.BLACK  // Gölge rengi
        );

        this.settings = PreferenceManager.getDefaultSharedPreferences(this.mContext);


    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        this.height = getMeasuredHeight();
        this.width = getMeasuredWidth();
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.mPaintLine.setStrokeWidth(this.mDensity * Float.valueOf(this.settings.getString(this.mContext.getString(Integer.parseInt("2")), "2")).floatValue());
        float font_size = Float.valueOf(this.settings.getString(this.mContext.getString(Integer.parseInt("14")), "14")).floatValue();
        this.mPaintTextCenter.setTextSize(this.mDensity * font_size);
        this.mPaintTextRight.setTextSize(this.mDensity * font_size);
        this.mPaintTextLeft.setTextSize(this.mDensity * font_size);
        drawAzimuth(canvas, (int) this.mAzimuth * 2);


        if (this.AttitudeLimit == 1) {
            canvas.drawText("DURUŞ UYARISI", ((float) this.centerX), (float) this.centerY + this.m_140, this.mPaintTextOrientationAlert);
        } else {
            canvas.drawText("", ((float) this.centerX), (float) this.centerY + this.m_140, this.mPaintTextOrientationAlert);
        }

        if (this.HATMinWarning == 1) {
            this.mPaintLineAltitude.setColor(Color.YELLOW);
        } else {
            this.mPaintLineAltitude.setColor(Color.WHITE);
        }

        if (this.HATMaxWarning == 1) {
            this.mPaintLineAltitude.setColor(Color.YELLOW);
        } else {
            this.mPaintLineAltitude.setColor(Color.WHITE);
        }

    }







    private void draw_LEFT_RIGHT_TURN(Canvas canvas, int Z) {
        Z = Z / 20;
        if (Z < -5) {
            canvas.drawText(Math.abs(Z) + "° SOLA DÖN", ((float) this.centerX) - this.m_50 * 6, (float) this.centerY + this.m_5 - 360, this.mPaintTextCenter);
        }
        if (5 < Z) {
            canvas.drawText(Math.abs(Z) + "° SAĞA DÖN", ((float) this.centerX) + this.m_50 * 6, (float) this.centerY + this.m_5 - 360, this.mPaintTextCenter);

        }

    }

    void drawAzimuth(Canvas canvas, int indicator) {
        int ind2 = indicator;
        if (indicator > 100) {
            indicator = 105;
        }
        if (indicator < -100) {
            indicator = -105;
        }
        canvas.drawLine(centerX - this.m_126 * 2, m_5, centerX + this.m_126 * 2, m_5, this.mPaintLineAzimuth);
        for (int i = 0; i < 51; i++) {
            if (i % 10 == 0) {
                canvas.drawLine(centerX - (m_5 * i), m_5, centerX - (m_5 * i), m_5 + (m_15), this.mPaintLineAzimuth);
                canvas.drawLine(centerX + (m_5 * i), m_5, centerX + (m_5 * i), m_5 + (m_15), this.mPaintLineAzimuth);
                if (i == 0) {
                    canvas.drawText("" + 0 + "°", centerX + (m_5 * i) - m_3, m_30 + this.m_5, this.mPaintTextLeft);
                } else {
                    canvas.drawText("" + i / 10 + "°", centerX + (m_5 * i) - m_3, m_30 + this.m_5, this.mPaintTextLeft);
                    canvas.drawText("-" + i / 10 + "°", centerX - (m_5 * i) - m_7, m_30 + this.m_5, this.mPaintTextLeft);
                }

            } else {
                canvas.drawLine(centerX - (m_5 * i), m_5, centerX - (m_5 * i), m_5 + (m_10), this.mPaintLineAzimuth);
                canvas.drawLine(centerX + (m_5 * i), m_5, centerX + (m_5 * i), m_5 + (m_10), this.mPaintLineAzimuth);
            }

        }
        Paint color = new Paint();
        ind2 = ind2 / 2;
        if (-10 < ind2 && ind2 < 10)
            color.setColor(Color.GREEN);
        if ((-20 <= ind2 && -10 >= ind2) || (ind2 >= 10 && ind2 <= 20))
            color.setColor(Color.YELLOW);
        if ((-50 <= ind2 && -20 >= ind2) || (ind2 >= 20 && ind2 <= 50))
            color.setColor(Color.YELLOW); // RED?
        if (ind2 > 50 || ind2 < -50) {
            color.setColor(Color.RED);
        }

        color.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(((float) (centerX + ((float) (indicator) * this.mDensity) / 0.4)), this.m_40);
        path.lineTo(((float) (centerX + ((float) (indicator) * this.mDensity) / 0.4)) - this.m_5, this.m_50);
        path.lineTo(((float) (centerX + ((float) (indicator) * this.mDensity) / 0.4)) + this.m_5, this.m_50);
        path.moveTo(((float) (centerX + ((float) (indicator) * this.mDensity) / 0.4)), this.m_40);
        path.close();

        canvas.drawPath(path, mPaintLine);
        canvas.drawPath(path, color);
    }
}
