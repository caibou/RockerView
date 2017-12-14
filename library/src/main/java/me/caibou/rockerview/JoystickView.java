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

    private final int EDGE_RADIUS;
    private final int STICK_RADIUS;
    private final int DR;

    private Region ballRegion = new Region();
    private Path stickEdgePath = new Path();
    private Path stickBallPath = new Path();

    private Paint paint = new Paint();

    private Point center;
    private float stickX, stickY;

    private int stickBallColor;

    public JoystickView(Context context) {
        this(context, null);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoystickView);

        EDGE_RADIUS = typedArray.getInt(R.styleable.JoystickView_edge_radius, 200);
        STICK_RADIUS = typedArray.getInt(R.styleable.JoystickView_stick_radius, EDGE_RADIUS / 2);
        stickBallColor = typedArray.getColor(R.styleable.JoystickView_stick_color, Color.parseColor("#97f5964d"));
        DR = EDGE_RADIUS - STICK_RADIUS;
        typedArray.recycle();


        center = centerPoint();
        stickX = center.x;
        stickY = center.y;
        initialStickRange();
    }

    private void initialStickRange() {
        Region rockerRegion = new Region(center.x - DR, center.y - DR,
                center.x + DR, center.y + DR);
        Path rockerRulePath = new Path();
        rockerRulePath.addCircle(center.x, center.y, DR, Path.Direction.CW);
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
        stickEdgePath.addCircle(center.x, center.y, EDGE_RADIUS, Path.Direction.CW);
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        canvas.drawPath(stickEdgePath, paint);
    }

    protected void drawStickBall(Canvas canvas) {
        stickBallPath.reset();
        stickBallPath.addCircle(stickX, stickY, STICK_RADIUS, Path.Direction.CW);
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
            stickX = dx * DR / scale + center.x;
            stickY = dy * DR / scale + center.y;
        }
        invalidate();
    }

    private void resetStick() {
        stickX = center.x;
        stickY = center.y;
        invalidate();
    }

    @Override
    protected void actionDown(float x, float y, double angle) {
        updateStickPos(x, y);
    }

    @Override
    protected void actionMove(float x, float y, double angle) {
        updateStickPos(x, y);
    }

    @Override
    protected void actionUp(float x, float y, double angle) {
        resetStick();
    }

}
