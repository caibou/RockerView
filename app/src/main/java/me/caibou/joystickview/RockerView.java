package me.caibou.joystickview;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author caibou
 */
public abstract class RockerView extends View {

    private Region edgeRegion = new Region();

    private Point centerPoint = new Point();
    private int radius;

    public RockerView(Context context) {
        this(context, null);
    }

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public Point centerPoint(){
        return new Point(centerPoint);
    }

    private void initialize() {
        radius = radius();
        centerPoint.x = radius;
        centerPoint.y = radius;
        initialTouchRange();
    }

    private void initialTouchRange() {
        Path edgeRulePath = new Path();
        edgeRulePath.addCircle(centerPoint.x, centerPoint.y, radius, Path.Direction.CW);
        Region globalRegion = new Region(centerPoint.x - radius, centerPoint.y - radius,
                centerPoint.x + radius, centerPoint.y + radius);
        edgeRegion.setPath(edgeRulePath, globalRegion);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        double angle = Math.atan2(y, x);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (edgeRegion.contains((int) x, (int) y)) {
                    actionDown(x, y, angle);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(x, y, angle);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                actionUp(x, y, angle);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sideLength = radius() * 2;
        setMeasuredDimension(sideLength, sideLength);
    }

    protected void actionDown(float x, float y, double angle) {
    }

    protected void actionMove(float x, float y, double angle) {
    }

    protected void actionUp(float x, float y, double angle) {
    }

    /**
     * @return
     */
    public abstract int radius();
}
