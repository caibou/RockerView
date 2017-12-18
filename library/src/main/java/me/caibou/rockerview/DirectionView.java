package me.caibou.rockerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author caibou
 */
public class DirectionView extends RockerView {

    private static final int INSIDE_CIRCLE = 30;
    private static final int INDICATOR_SWEEP_ANGLE = 90;

    private int sideLength;
    private int inSideLength;
    private int turnPosLength;
    private int edgeRadius;
    private int buttonRadius;
    private float startAngle = 337.5f;

    private Point centerPoint = new Point();

    private Paint paint = new Paint();

    private Path edgePath = new Path();
    private Path directPath = new Path();
    private Path indicatorPath = new Path();
    private RectF indicatorRectF = new RectF();
    private boolean pressedStatus = false;

    public DirectionView(Context context) {
        this(context, null);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeData(context, attrs);
    }

    private void initializeData(Context context, AttributeSet attrs) {
        edgeRadius = 200;
        buttonRadius = 180;
        sideLength = 120;
        inSideLength = (int) Math.sqrt(Math.pow(buttonRadius, 2) - Math.pow(sideLength / 2, 2));
        turnPosLength = inSideLength - sideLength / 2;

        centerPoint.x = edgeRadius;
        centerPoint.y = edgeRadius;

        indicatorRectF.set(centerPoint.x - INSIDE_CIRCLE, centerPoint.y - INSIDE_CIRCLE,
                centerPoint.x + INSIDE_CIRCLE, centerPoint.y + INSIDE_CIRCLE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        drawEdge(canvas);
        drawDirectButton(canvas);
        if (pressedStatus){
            drawIndicator(canvas);
        }

    }

    protected void drawEdge(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        edgePath.reset();
        edgePath.addCircle(centerPoint.x, centerPoint.y, edgeRadius, Path.Direction.CW);

        canvas.drawPath(edgePath, paint);
    }

    protected void drawDirectButton(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        directPath.reset();
        directPath.moveTo(-inSideLength + centerPoint.x, -sideLength / 2 + centerPoint.y);
        directPath.rLineTo(turnPosLength, 0);
        directPath.rLineTo(0, -turnPosLength);
        directPath.rLineTo(sideLength, 0);
        directPath.rLineTo(0, turnPosLength);
        directPath.rLineTo(turnPosLength, 0);
        directPath.rLineTo(0, sideLength);
        directPath.rLineTo(-turnPosLength, 0);
        directPath.rLineTo(0, turnPosLength);
        directPath.rLineTo(-sideLength, 0);
        directPath.rLineTo(0, -turnPosLength);
        directPath.rLineTo(-turnPosLength, 0);
        directPath.rLineTo(0, -sideLength);

        directPath.addCircle(centerPoint.x, centerPoint.y, INSIDE_CIRCLE, Path.Direction.CW);

        canvas.drawPath(directPath, paint);
    }

    protected void drawIndicator(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        indicatorPath.reset();
        indicatorPath.addArc(indicatorRectF, startAngle, INDICATOR_SWEEP_ANGLE);
        canvas.drawPath(indicatorPath, paint);
    }

    @Override
    protected void actionDown(float x, float y, double angle) {
        pressedStatus = true;
        updateIndicator(angle);
    }

    @Override
    protected void actionMove(float x, float y, double angle) {
        updateIndicator(angle);
    }

    @Override
    protected void actionUp(float x, float y, double angle) {
        resetIndicator();
    }

    private void resetIndicator() {
        pressedStatus = false;
        invalidate();
    }

    private void updateIndicator(double angle) {
        if (range(angle, 337.5, 360) || range(angle, 0, 22.5)) {
            startAngle = 315.0f;
        } else if (range(angle, 22.5, 67.5)) {
            startAngle = 0.0f;
        } else if (range(angle, 67.5, 112.5)) {
            startAngle = 45.0f;
        } else if (range(angle, 112.5, 157.5)) {
            startAngle = 90.0f;
        } else if (range(angle, 157.5, 202.5)) {
            startAngle = 135.0f;
        } else if (range(angle, 202.5, 247.5)) {
            startAngle = 180.0f;
        } else if (range(angle, 247.5, 292.5)) {
            startAngle = 225.0f;
        } else if (range(angle, 292.5, 337.5)) {
            startAngle = 270.0f;
        }
        invalidate();
    }

    private boolean range(double value, double min, double max) {
        if (min < value && value <= max) {
            return true;
        }
        return false;
    }
}
