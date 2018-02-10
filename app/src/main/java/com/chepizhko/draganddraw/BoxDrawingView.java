package com.chepizhko.draganddraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    // Методы, вызываемые для объекта Paint, определяют характеристики вывода: должны ли фигуры заполняться,
    // каким шрифтом должен выводиться текст, каким цветом должны выводиться линии и т. д.
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Используется при создании представления в коде
    public BoxDrawingView(Context context) {
        this(context, null);
    }
    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Прямоугольники рисуются полупрозрачным красным цветом (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        // Фон закрашивается серовато-белым цветом
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Заполнение фона
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxen) {
            // для каждого прямоугольника в списке мы определяем значения left, right, top и bottom по двум точкам.
            // Значения left и top будут минимальными, а bottom и right — максимальными
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            // После вычисления параметров вызов метода Canvas.drawRect(…) рисует красный прямоугольник на экране
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
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
