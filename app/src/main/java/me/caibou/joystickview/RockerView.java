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
import android.view.MotionEvent;
import android.view.View;

/**
 * @author caibou
 */
public class RockerView extends View {

    private static final String TAG = "GameButton";
    private static final int SIDE_LENGTH = 400;
    private static final int EDGE_RADIUS = 200;
    private static final int BALL_RADIUS = EDGE_RADIUS / 2;
    private static final int DR = EDGE_RADIUS - BALL_RADIUS;

    private Region edgeRegion = new Region();
    private Region ballRegion = new Region();
    private Path rockerEdgePath = new Path();
    private Path rockerBallPath = new Path();

    private Point center = new Point();

    private Paint paint = new Paint();

    private float rockerX, rockerY;

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

    private void initialize() {
        center.x = SIDE_LENGTH / 2;
        center.y = SIDE_LENGTH / 2;
        rockerX = center.x;
        rockerY = center.y;
        moveRule();
    }

    protected void moveRule() {
        Path edgeRulePath = new Path();
        edgeRulePath.addCircle(center.x, center.y, EDGE_RADIUS, Path.Direction.CW);
        Region globalRegion = new Region(center.x - EDGE_RADIUS, center.y - EDGE_RADIUS,
                center.x + EDGE_RADIUS, center.y + EDGE_RADIUS);
        edgeRegion.setPath(edgeRulePath, globalRegion);

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
        rockerEdgePath.reset();
        rockerEdgePath.addCircle(center.x, center.y, EDGE_RADIUS, Path.Direction.CW);
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        canvas.drawPath(rockerEdgePath, paint);
    }

    protected void drawRockerBall(Canvas canvas) {
        rockerBallPath.reset();
        rockerBallPath.addCircle(rockerX, rockerY, BALL_RADIUS, Path.Direction.CW);
        paint.reset();
        paint.setColor(Color.parseColor("#97f5964d"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(rockerBallPath, paint);
    }

    private void updateRocker(float x, float y) {
        if (ballRegion.contains((int) x, (int) y)) {
            rockerX = x;
            rockerY = y;
        } else {
            float dx = x - center.x;
            float dy = y - center.y;
            float scale = (float) Math.sqrt((Math.pow(dx, 2) + Math.pow(dy, 2)));
            rockerX = dx * DR / scale + center.x;
            rockerY = dy * DR / scale + center.y;
        }
        invalidate();
    }

    private void resetRocker() {
        rockerX = center.x;
        rockerY = center.y;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (edgeRegion.contains((int) x, (int) y)) {
                    updateRocker(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                updateRocker(x, y);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                resetRocker();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(SIDE_LENGTH, SIDE_LENGTH);
    }

}
