package com.example.usmall.customone.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.usmall.customone.R;

/**
 * Created by jh on 2016/10/25.
 */
public class CustomImageView extends View {

    /*
    * 控件中的图片
    * */
    private Bitmap mImage;
    /*
    * 图片的缩放模式
    * */
    private int mImageScale;
    private static final int IMAGE_SCALE_FITXY = 0;
    private static final int IMAGE_SCALE_CENTER = 1;
    /*
    * 字体的颜色
    * */
    private int mTextColor;
    /*
    * 字体的大小
    * */
    private int mTextSize;
    /*
    * 控制整体布局
    * */
    private Rect rect;
    private Paint mPaint;
    /*
    * 对文本的约束
    * */
    private Rect mTextBounds;
    private String mTitle;
    /*
    * 控件的宽高
    * */
    private int mWidth;
    private int mHeight;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /*
    * 在此构造方法中初始化所有自定义类型
    * */
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性类型数组
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        //获取属性的数量
        int n = a.getIndexCount();
        //循环遍历所有的属性并通过switch进行分支判断
        for (int i = 0; i < n; i++) {
            //获取所有的属性
            int attr = a.getIndex(i);
            //进行分支判断
            switch (attr) {
                case R.styleable.CustomImageView_image:
                    //通过Bitmap工厂解析图片资源id
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_titleText:
                    //获取文本
                    mTitle = a.getString(i);
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    //图片的比例缩放类型
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    //获取文本颜色
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    //获取文本尺寸
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        //矩形
        rect = new Rect();
        mPaint = new Paint();
        mTextBounds = new Rect();
        mPaint.setTextSize(mTextSize);
        //计算绘制字体所需要的范围
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBounds);
    }

    //重写测量方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
        * 设置宽度
        * */

        //获取mode size 设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        //对测量模式进行判断
        if (specMode == MeasureSpec.EXACTLY) {
            Log.e("xxx", "EXACTLY");
            mWidth = specSize;
        } else {
            //有图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            //由字体决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextBounds.width();
            if (specMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.min(desire, specSize);
                Log.e("xxx", "AT_MOST");
            }
        }

        /*
        * 设置高度
        * */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBounds.height();
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desire, specSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    /*
    * 重写onDraw()方法
    * */

    @Override
    protected void onDraw(Canvas canvas) {
        /*
        * 绘制边框
        * */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        //绘制
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        //矩形边框值
        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        //颜色和缩放类型
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        /*
        * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
        * */
        if (mTextBounds.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitle, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            //绘制文本
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            //正常情况下将字体进行居中显示
            canvas.drawText(mTitle, mWidth / 2 - mTextBounds.width() * 1.0f / 2, mHeight - getPaddingBottom(), mPaint);
        }
        //取消使用掉的块
        rect.bottom -= mTextBounds.height();
        //判断图片缩放类型
        if(mImageScale == IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }else{
            //计算居中的矩形范围
            rect.left = mWidth/2 - mImage.getWidth() / 2;
            rect.right = mWidth/2 + mImage.getWidth()/2;
            rect.top = (mHeight - mTextBounds.height())/2 - mImage.getHeight()/2;
            rect.bottom = (mHeight - mTextBounds.height())/2 + mImage.getHeight()/2;
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }

    }
}
