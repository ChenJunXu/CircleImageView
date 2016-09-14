package com.xu.circleimageview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xu.circleimageview.R;

/**
 * 自定义圆形组件
 *
 * @author chenjunxu
 * @date 16/9/13
 */
public class CircleImageView extends ImageView {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 矩阵
     */
    private Matrix mMatrix;
    /**
     * 默认圆角半径为10
     */
    private final static int BORDER_RADIUS_DEFAULT = 10;
    /**
     * 图片圆角
     */
    private int mBorderRadius;
    /**
     * 半径
     */
    private float mRadius;
    /**
     * 取长宽中较小值
     */
    private int minLength;
    /**
     * 位图渲染
     */
    private BitmapShader mBitmapShader;


    public CircleImageView(Context context) {
        super(context, null, 0);
        init(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mBorderRadius = typedArray.getDimensionPixelSize(R.styleable.CircleImageView_radius, BORDER_RADIUS_DEFAULT);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取半径
        minLength = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = minLength / 2.0f;

        // 重新设置宽高
        setMeasuredDimension(minLength, minLength);

        initDraw();
    }

    /**
     * 处理图片
     */
    private void initDraw() {
        Drawable drawable = getDrawable();

        // 若没有图片资源，则不操作
        if (drawable == null) {
            return;
        }

        // 获取图片的位图
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        // 压缩图片
        float mDrawScale;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            mDrawScale = minLength * 1.0f / bitmap.getHeight();
            mMatrix.setScale(mDrawScale, mDrawScale);
            mMatrix.postTranslate(
                    -(bitmap.getWidth() * mDrawScale / 2 - mRadius), 0);
        } else {
            mDrawScale = minLength * 1.0f / bitmap.getWidth();
            mMatrix.setScale(mDrawScale, mDrawScale);
            mMatrix.postTranslate(0,
                    -(bitmap.getHeight() * mDrawScale / 2 - mRadius));
        }

        // 颜色渲染
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }
}
