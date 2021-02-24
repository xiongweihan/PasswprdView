package com.hj.passwprdview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * 类似支付宝样式的密码输入框，可以自由选择，，输入框是分开的，还是连在一起的，
 * 设置
 */
public class PasswordEditText extends AppCompatEditText {
    private Context mContext;
    //密码位数
    private int count = 6;
    /**
     * 绘制的edittext 的宽高
     */
    private int width, height;

    /**
     * 分割线的宽度，用来计算分割点的坐标
     */
    private int divingWidth;

    /**
     * 输入位置
     */
    private int editPosition = 0;

    /**
     * 密码框中第一个黑色圆心坐标
     */
    private int startX, startY;

    /**
     * 黑色圆心半径
     */
    private int radius = 12;

    private RectF rectF = new RectF();


    /**
     * 绘制的宽度
     */
    private int borderWidth = 10;
    private int lineWidth = 2;

    /**
     * 输入框圆角
     */
    private int roundAngle = 20;

    /**
     * 输入框，边框，黑色圆心画笔
     */
    private Paint borderPaint, divingPaint, circlePaint;

    private int borderColor = Color.GRAY;
    private int divingColor = Color.GRAY;
    private int circleColor = Color.BLACK;
    /**
     * 输入框类型 0--默认，连着的，1--有间隔的
     */
    private int inputBoxType = 0;


    public PasswordEditText(@NonNull Context context) {
        this(context, null);
    }

    public PasswordEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttributeSet(attrs);
        initPaint();
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(count)});
        this.setCursorVisible(false);
    }

    private void initPaint() {
        //抗锯齿
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setAntiAlias(true);
        //画笔宽度
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);

        divingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        divingPaint.setAntiAlias(true);
        divingPaint.setStrokeWidth(lineWidth);
        divingPaint.setStyle(Paint.Style.FILL);
        divingPaint.setColor(divingColor);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(radius);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);
    }

    private void setAttributeSet(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        count = array.getInteger(R.styleable.PasswordEditText_inputMaxNumber, count);
        roundAngle = array.getDimensionPixelOffset(R.styleable.PasswordEditText_roundAngle, roundAngle);
        borderColor = array.getColor(R.styleable.PasswordEditText_borderColor, borderColor);
        divingColor = array.getColor(R.styleable.PasswordEditText_divingColor, divingColor);
        circleColor = array.getColor(R.styleable.PasswordEditText_circleColor, circleColor);
        borderWidth = array.getDimensionPixelOffset(R.styleable.PasswordEditText_borderWidth, borderWidth);
        lineWidth = array.getDimensionPixelOffset(R.styleable.PasswordEditText_divingWidth, lineWidth);
        radius = array.getDimensionPixelOffset(R.styleable.PasswordEditText_circleRadius, radius);
        inputBoxType = array.getInt(R.styleable.PasswordEditText_inputBoxType, inputBoxType);
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取宽高坐标等数据
        height = h;
        width = w;
        //分割线宽度
        divingWidth = w / count;
        //密码黑色圆坐标
        startX = w / count / 2;
        startY = h / 2;
        rectF.set(0, 0, w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (inputBoxType == 0) {
            drawContinuous(canvas);
        } else {
            drawInterval(canvas);
        }
    }

    /**
     * 画分割开的输入框
     */
    @SuppressLint("NewApi")
    private void drawInterval(Canvas canvas) {
        //绘制圆角矩形
        for (int i = 0; i < count; i++) {

            /**
             * 两个输入框之间的间隔
             */
            int marge = 40;
            if (i == count - 1) {//如果是最后一个
                canvas.drawRoundRect((float) (width - width / count + marge / 2), (float) (borderWidth / 2), (float) (width - marge / 2), (float) (height - borderWidth / 2), roundAngle, roundAngle, borderPaint);
                continue;
            }
            //其他的
            canvas.drawRoundRect((float) ((width / count) * i + marge / 2), (float) (borderWidth / 2), (float) ((width / count) * (i + 1) - marge / 2), (float) (height - borderWidth / 2), roundAngle, roundAngle, borderPaint);
        }

        //绘制黑色圆点
        for (int i = 0; i < editPosition; i++) {
            canvas.drawCircle(startX * (2 * i + 1), startY, radius, circlePaint);
        }

    }

    /**
     * 画连续再一起的输入框
     */
    private void drawContinuous(Canvas canvas) {
        //绘制圆角矩形
        canvas.drawRoundRect(rectF, roundAngle, roundAngle, borderPaint);
        //绘制分割线
        for (int i = 0; i < count; i++) {
            canvas.drawLine(divingWidth * i, 0, divingWidth * i, height, divingPaint);
        }
        //绘制黑色小圆点
        for (int i = 0; i < editPosition; i++) {
            canvas.drawCircle(startX * (2 * i + 1), startY, radius, circlePaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        // 获取当前输入位置
        editPosition = text.length();
    }
}
