package me.caibou.rockerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author caibou
 */
public class JoystickView extends RockerView {

    private Region ballRegion = new Region();
    private Path stickEdgePath = new Path();
    private Path stickBallPath = new Path();

    private Paint paint = new Paint();

    private Point center;
    private float stickX, stickY;
    private int edgeRadius;
    private int stickRadius;
    private int dr;

    private int stickBallColor;

    private OnAngleUpdateListener angleUpdateListener;

    public JoystickView(Context context) {
        this(context, null);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialData(context, attrs);
        initialStickRange();
    }

    private void initialData(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoystickView);
        edgeRadius = typedArray.getInt(R.styleable.JoystickView_edge_radius, 200);
        stickRadius = typedArray.getInt(R.styleable.JoystickView_stick_radius, edgeRadius / 2);
        stickBallColor = typedArray.getColor(R.styleable.JoystickView_stick_color,
                getResources().getColor(R.color.stick_default_color));
        typedArray.recycle();

        center = centerPoint();
        stickX = center.x;
        stickY = center.y;
        dr = edgeRadius - stickRadius;
    }

    private void initialStickRange() {
        Region rockerRegion = new Region(center.x - dr, center.y - dr,
                center.x + dr, center.y + dr);
        Path rockerRulePath = new Path();
        rockerRulePath.addCircle(center.x, center.y, dr, Path.Direction.CW);
        ballRegion.setPath(rockerRulePath, rockerRegion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        drawRockerEdge(canvas);
        drawStickBall(canvas);
    }

    protected void drawRockerEdge(Canvas canvas) {
        stickEdgePath.reset();
        stickEdgePath.addCircle(center.x, center.y, edgeRadius, Path.Direction.CW);
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        canvas.drawPath(stickEdgePath, paint);
    }

    protected void drawStickBall(Canvas canvas) {
        stickBallPath.reset();
        stickBallPath.addCircle(stickX, stickY, stickRadius, Path.Direction.CW);
        paint.reset();
        paint.setColor(stickBallColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(stickBallPath, paint);
    }

    private void updateStickPos(float x, float y) {
        if (ballRegion.contains((int) x, (int) y)) {
            stickX = x;
            stickY = y;
        } else {
            float dx = x - center.x;
            float dy = y - center.y;
            float scale = (float) Math.sqrt((Math.pow(dx, 2) + Math.pow(dy, 2)));
            stickX = dx * dr / scale + center.x;
            stickY = dy * dr / scale + center.y;
        }
        invalidate();
    }

    private void resetStick() {
        stickX = center.x;
        stickY = center.y;
        invalidate();
    }

    private void updateAngle(double angle, int action) {
        if (angleUpdateListener != null) {
            angleUpdateListener.onAngleUpdate(angle, action);
        }
    }

    @Override
    protected void actionDown(float x, float y, double angle) {
        updateStickPos(x, y);
        updateAngle(angle, ACTION_PRESSED);
    }

    @Override
    protected void actionMove(float x, float y, double angle) {
        updateStickPos(x, y);
        updateAngle(angle, ACTION_MOVE);
    }

    @Override
    protected void actionUp(float x, float y, double angle) {
        resetStick();
        updateAngle(angle, ACTION_RELEASE);
    }

    /**
     * Register a callback to be invoked when the angle is updated.
     *
     * @param angleUpdateListener The callback that will run.
     */
    public void setAngleUpdateListener(OnAngleUpdateListener angleUpdateListener) {
        this.angleUpdateListener = angleUpdateListener;
    }

    /**
     * Interface definition for a callback to be invoked when The angle between the finger
     * and the center of the circle update.
     */
    public interface OnAngleUpdateListener {

        /**
         * Called when angle has been clicked.
         *
         * @param angle  The angle between the finger and the center of the circle.
         * @param action action of the finger.
         */
        void onAngleUpdate(double angle, int action);
    }
}
