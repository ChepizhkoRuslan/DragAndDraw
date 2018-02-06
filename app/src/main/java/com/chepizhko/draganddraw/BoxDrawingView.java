package com.chepizhko.draganddraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();

    // Используется при создании представления в коде
    public BoxDrawingView(Context context) {
        this(context, null);
    }
    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // PointF — предоставленный Android класс-контейнер, где координаты X и Y упаковываются вместе
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                // Сброс текущего состояния
                // При каждом получении события ACTION_DOWN в поле mCurrentBox сохраняется новый объект Box с базовой точкой,
                // соответствующей позиции события
                mCurrentBox = new Box(current);
                // объект Box добавляется в массив прямоугольников
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    // В процессе перемещения пальца по экрану приложение обновляет mCurrentBox.mCurrent
                    mCurrentBox.setCurrent(current);
                    // invalidate() заставляет BoxDrawingView перерисовать себя, чтобы пользователь видел прямоугольник в процессе перетаскивания
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                // поле mCurrentBox обнуляется для завершения операции
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                // поле mCurrentBox обнуляется для завершения операции
                mCurrentBox = null;
                break;
        }
        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }
}
