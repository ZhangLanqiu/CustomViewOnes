package com.example.usmall.customone.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.usmall.customone.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Created by jh on 2016/10/25.
 */
public class CustomTitleView extends View {
    /*
      文本
     */
    private String mTitleText;
    /*
   文本的颜色
    * */
    private int mTitleTextColor;
    /*
    文本的尺寸
    * */
    private int mTitleTextSize;
    /*
    * 绘制时控制文本范围
    * */
    private Paint mPaint;
    private Rect mRect;

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*
        获取我们自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = a.getString(attr);
                    break;

                case R.styleable.CustomTitleView_titleTextColor:
                    //默认颜色设置为黑色
                    mTitleTextColor = a.getColor(i, Color.BLACK);
                    break;

                case R.styleable.CustomTitleView_titleTextSize:
                    //默认设置为16sp,TypeValue也可以把sp转换为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                            getResources().getDisplayMetrics()));

                    break;
            }
        }
        //释放
        a.recycle();
        /*
        获得回执文本的宽和高
         */
        //创建画笔
        mPaint = new Paint();
        //设置文本尺寸
        mPaint.setTextSize(mTitleTextSize);
        //创建一个矩形
        mRect = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);

        //添加点击事件
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                postInvalidate();
            }
        });
    }


    //重写onDraw方法
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        //通过画布绘制范围
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, 0+getPaddingLeft(), getHeight() / 2 + mRect.height() / 2, mPaint);
        Log.i("msgs", "getWidth = " + getWidth() + "\n" + "mRect.width = " + mRect.width() + "\n" + "getHeight() = " + getHeight() + "\n" + "mRect.height = " + mRect.height());
    }

    /*
    * 重写测量方法
    * */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        //进行判断
        if (widthMode == MeasureSpec.EXACTLY) {
            //等于EXACTLY模式的宽度
            width = widthSize;
        } else {
            //用画笔设置字体尺寸
            mPaint.setTextSize(mTitleTextSize);
            //获取文本范围
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            //获取Bounds的宽度
            float textWidth = mRect.width();
            //这个是我们期望的值
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingLeft() + textHeight + getPaddingRight());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }

        //创建StringBuffer
        StringBuffer sb = new StringBuffer();
        for (Integer s : set) {
            sb.append("" + s);
        }
        return sb.toString();
    }
}
