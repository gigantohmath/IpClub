package ipclub.com.ipclub.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ipclub.com.ipclub.R;


public class LoadingView extends View {

    int centerX;
    int centerY;
    int radius;

    float newBallx = 0;
    float newBally = 0;

    int angle = 360;

    private int w;
    private int h;

    private int mWidth;
    private int mHeight;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onDraw(final Canvas canv) {

        centerX = w/2;
        centerY = h/2;
        radius = w/7;

        Paint mCirclePaint = new Paint();

        mCirclePaint.setColor(Color.LTGRAY);
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        canv.drawCircle(centerX, centerY, radius, mCirclePaint);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        int cx = (mWidth - bmp.getWidth()) >> 1;
        int cy = (mHeight - bmp.getHeight()) >> 1;

        canv.drawBitmap(bmp, cx, cy, mCirclePaint);

        newBallx = (float) (Math.sin(Math.toRadians(angle))*radius)+centerX;
        newBally = (float) (Math.cos(Math.toRadians(angle))*radius)+centerY;

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.WHITE/*Color.parseColor("#036166")*/);
        canv.drawCircle(newBallx,  newBally, 10, mCirclePaint);

        angle -=10;
        if(angle == 0){
            angle = 360;
        }

        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }
}
