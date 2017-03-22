package com.example.xcho.x_drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    private DrawingTools drawPath;
    private Paint drawPaint, bitmapPaint;
    private Bitmap bitmap;
    private Rect rect;
    private List<DrawingTools> pathList;

    private int brushColor = Color.BLACK;
    private int brushSize = 15;
    private boolean isCanvasEmpty = true;


    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setStyle(Paint.Style.STROKE);
        pathList = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                drawPath = new DrawingTools(brushColor, brushSize);
                drawPaint.setStrokeWidth(getBrushSize());
                drawPaint.setColor(getBrushColor());
                drawPath.moveTo(event.getX(), event.getY());
                drawPath.lineTo(event.getX(), event.getY() + (0.1f));
                pathList.add(drawPath);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(event.getX(), event.getY());
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, rect, bitmapPaint);
        }
        if (isCanvasEmpty()) {
            canvas.drawColor(getResources().getColor(R.color.backgroundColor));
        }
        for (DrawingTools path1 : pathList) {
            drawPaint.setColor(path1.getBrushColor());
            drawPaint.setStrokeWidth(path1.getBrushSize());
            canvas.drawPath(path1, drawPaint);
        }
    }

    public Bitmap asBitmap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap drawingCache = Bitmap.createBitmap(getDrawingCache());
        setDrawingCacheEnabled(false);
        return drawingCache;
    }

    public void setBitmap(Bitmap bitmap) {
        rect = new Rect(0, 0, getWidth(), getHeight());
        bitmapPaint = new Paint();
        bitmapPaint.setFilterBitmap(true);
        this.bitmap = bitmap;
        invalidate();
    }

    public void undoExtion() {
        if (pathList.size() != 0) {
            pathList.remove(pathList.size() - 1);
            invalidate();
        }
    }

    public boolean isCanvasUse() {
        return pathList.size() != 0;
    }

    public void newPage() {
        pathList.clear();
        bitmap = null;
        setBackgroundResource(R.color.backgroundColor);
        invalidate();
    }

    public boolean isCanvasEmpty() {
        return isCanvasEmpty;
    }

    public void setCanvasEmpty(boolean canvasEmpty) {
        isCanvasEmpty = canvasEmpty;
    }

    public int getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(int brushColor) {
        this.brushColor = brushColor;
        invalidate();
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
        invalidate();
    }
}
