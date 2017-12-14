package me.caibou.joystickview;

import android.content.Context;
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
public class JoystickView extends RockerView{

    private static final int EDGE_RADIUS = 200;
    private static final int BALL_RADIUS = EDGE_RADIUS / 2;
    private static final int DR = EDGE_RADIUS - BALL_RADIUS;

    private Region ballRegion = new Region();
    private Path stickEdgePath = new Path();
    private Path stickBallPath = new Path();

    private Paint paint = new Paint();

    private Point center;
    private float stickX, stickY;

    public JoystickView(Context context) {
        this(context, null);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JoystickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        drawRockerBall(canvas);
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

    protected void drawRockerBall(Canvas canvas) {
        stickBallPath.reset();
        stickBallPath.addCircle(stickX, stickY, BALL_RADIUS, Path.Direction.CW);
        paint.reset();
        paint.setColor(Color.parseColor("#97f5964d"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(stickBallPath, paint);
    }

    private void updateRocker(float x, float y) {
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

    @Override
    protected void actionDown(float x, float y, double angle) {
        updateRocker(x, y);
    }

    @Override
    protected void actionMove(float x, float y, double angle) {
        updateRocker(x, y);
    }

    @Override
    protected void actionUp(float x, float y, double angle) {
        resetRocker();
    }

    private void resetRocker() {
        stickX = center.x;
        stickY = center.y;
        invalidate();
    }

    @Override
    public int radius() {
        return EDGE_RADIUS;
    }
}
