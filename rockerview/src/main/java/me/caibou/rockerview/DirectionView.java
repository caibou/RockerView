package me.caibou.rockerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author caibou
 */
public class DirectionView extends RockerView {

    private static final int INSIDE_CIRCLE_RADIUS = 30;
    private static final int INDICATOR_SWEEP_ANGLE = 90;

    public enum Direction {
        NONE, UP, DOWN, LEFT, RIGHT,
        UP_AND_LEFT, UP_AND_RIGHT, DOWN_AND_LEFT, DOWN_AND_RIGHT
    }

    private boolean pressedStatus = false;
    private int edgeRadius, buttonRadius, sideWidth;
    private int indicatorColor;
    private float startAngle = 337.5f;

    private Region invalidRegion = new Region();
    private Point centerPoint = new Point();

    private Paint paint = new Paint();
    private Path edgePath, directPath, indicatorPath;
    private RectF indicatorRect;

    private DirectionChangeListener directionChangeListener;

    public DirectionView(Context context) {
        this(context, null);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialAttr(context, attrs);
        initialData();
        resetInvalidRegion();
    }

    private void initialAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DirectionView);
        buttonRadius = typedArray.getDimensionPixelSize(R.styleable.DirectionView_button_outside_circle_radius, 180);
        edgeRadius = typedArray.getDimensionPixelSize(R.styleable.DirectionView_edge_radius, 200);
        sideWidth = typedArray.getDimensionPixelSize(R.styleable.DirectionView_button_side_width, 120);
        indicatorColor = typedArray.getColor(R.styleable.DirectionView_indicator_color, Color.GREEN);
        typedArray.recycle();
    }

    private void initialData() {
        int sideLengthOfCenter = (int) Math.sqrt(Math.pow(buttonRadius, 2) - Math.pow(sideWidth / 2, 2));
        int sideLength = sideLengthOfCenter - sideWidth / 2;

        centerPoint = centerPoint();

        edgePath = new Path();
        edgePath.addCircle(centerPoint.x, centerPoint.y, edgeRadius, Path.Direction.CW);

        directPath = new Path();
        directPath.moveTo(-sideLengthOfCenter + centerPoint.x, -sideWidth / 2 + centerPoint.y);
        directPath.rLineTo(sideLength, 0);
        directPath.rLineTo(0, -sideLength);
        directPath.rLineTo(sideWidth, 0);
        directPath.rLineTo(0, sideLength);
        directPath.rLineTo(sideLength, 0);
        directPath.rLineTo(0, sideWidth);
        directPath.rLineTo(-sideLength, 0);
        directPath.rLineTo(0, sideLength);
        directPath.rLineTo(-sideWidth, 0);
        directPath.rLineTo(0, -sideLength);
        directPath.rLineTo(-sideLength, 0);
        directPath.rLineTo(0, -sideWidth);
        directPath.addCircle(centerPoint.x, centerPoint.y, INSIDE_CIRCLE_RADIUS, Path.Direction.CW);

        indicatorRect = new RectF();
        indicatorRect.set(centerPoint.x - INSIDE_CIRCLE_RADIUS, centerPoint.y - INSIDE_CIRCLE_RADIUS,
                centerPoint.x + INSIDE_CIRCLE_RADIUS, centerPoint.y + INSIDE_CIRCLE_RADIUS);
        indicatorPath = new Path();
    }

    private void resetInvalidRegion() {
        int invalidRadius = edgeRadius / 3;
        Region invalidRegionClip = new Region(centerPoint.x - invalidRadius, centerPoint.y - invalidRadius,
                centerPoint.x + invalidRadius, centerPoint.y + invalidRadius);
        Path eventInvalidPath = new Path();
        eventInvalidPath.addCircle(centerPoint.x, centerPoint.y, invalidRadius, Path.Direction.CW);
        invalidRegion.setPath(eventInvalidPath, invalidRegionClip);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        drawEdge(canvas);
        drawDirectButton(canvas);
        if (pressedStatus) {
            drawIndicator(canvas);
        }
    }

    protected void drawEdge(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPath(edgePath, paint);
    }

    protected void drawDirectButton(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        canvas.drawPath(directPath, paint);
    }

    protected void drawIndicator(Canvas canvas) {
        paint.reset();
        paint.setColor(indicatorColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        indicatorPath.reset();
        indicatorPath.addArc(indicatorRect, startAngle, INDICATOR_SWEEP_ANGLE);
        canvas.drawPath(indicatorPath, paint);
    }

    @Override
    protected void actionDown(float x, float y, double angle) {
        pressedStatus = true;
        if (!invalidRegion.contains((int) x, (int) y)) {
            updateIndicator(angle);
        }
    }

    @Override
    protected void actionMove(float x, float y, double angle) {
        if (!invalidRegion.contains((int) x, (int) y)) {
            updateIndicator(angle);
        }
    }

    @Override
    protected void actionUp(float x, float y, double angle) {
        resetIndicator();
        notifyDirection(Direction.NONE);
    }

    private void resetIndicator() {
        pressedStatus = false;
        invalidate();
    }

    private void updateIndicator(double angle) {
        if (Utils.range(angle, 337.5, 360) || Utils.range(angle, 0, 22.5)) {
            startAngle = 315.0f;
            notifyDirection(Direction.RIGHT);
        } else if (Utils.range(angle, 22.5, 67.5)) {
            startAngle = 0.0f;
            notifyDirection(Direction.DOWN_AND_RIGHT);
        } else if (Utils.range(angle, 67.5, 112.5)) {
            startAngle = 45.0f;
            notifyDirection(Direction.DOWN);
        } else if (Utils.range(angle, 112.5, 157.5)) {
            startAngle = 90.0f;
            notifyDirection(Direction.DOWN_AND_LEFT);
        } else if (Utils.range(angle, 157.5, 202.5)) {
            startAngle = 135.0f;
            notifyDirection(Direction.LEFT);
        } else if (Utils.range(angle, 202.5, 247.5)) {
            startAngle = 180.0f;
            notifyDirection(Direction.UP_AND_LEFT);
        } else if (Utils.range(angle, 247.5, 292.5)) {
            startAngle = 225.0f;
            notifyDirection(Direction.UP);
        } else if (Utils.range(angle, 292.5, 337.5)) {
            startAngle = 270.0f;
            notifyDirection(Direction.UP_AND_RIGHT);
        }
        invalidate();
    }

    private void notifyDirection(Direction direction) {
        if (directionChangeListener != null) {
            directionChangeListener.onDirectChange(direction);
        }
    }

    /**
     * Register a callback to be invoked when the direction changed.
     *
     * @param directionChangeListener The callback that will run.
     */
    public void setDirectionChangeListener(DirectionChangeListener directionChangeListener) {
        this.directionChangeListener = directionChangeListener;
    }

    /**
     * Interface definition for a callback to be invoked when The direction changed.
     */
    public interface DirectionChangeListener {

        /**
         * Called when direction changed.
         *
         * @param direction The direction of the center to finger.
         */
        void onDirectChange(Direction direction);
    }
}
