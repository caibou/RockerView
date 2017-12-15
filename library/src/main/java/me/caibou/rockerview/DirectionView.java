package me.caibou.rockerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author caibou
 */
public class DirectionView extends RockerView {

    private int sideLength;
    private int inSideLength;
    private int turnPosLength;
    private int radius;

    private Point centerPoint = new Point();

    private Paint paint = new Paint();

    private Path directPath = new Path();
    private Path edgePath = new Path();

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
        radius = 200;
        sideLength = 130;
        inSideLength = (int) Math.sqrt(Math.pow(180, 2) - Math.pow(sideLength / 2, 2));
        turnPosLength = sideLength / 2;

        centerPoint.x = radius;
        centerPoint.y = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawEdge(canvas);
        drawDirectButton(canvas);
    }

    private void drawEdge(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        edgePath.reset();
        edgePath.addCircle(centerPoint.x, centerPoint.y, radius, Path.Direction.CW);
        canvas.drawPath(edgePath, paint);
    }

    private void drawDirectButton(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        directPath.moveTo(-inSideLength + radius, -turnPosLength + radius);
        directPath.lineTo(-turnPosLength + radius, -turnPosLength + radius);
        directPath.lineTo(-turnPosLength + radius, -inSideLength + radius);
        directPath.lineTo(turnPosLength + radius, -inSideLength + radius);
        directPath.lineTo(turnPosLength + radius, -turnPosLength + radius);
        directPath.lineTo(inSideLength + radius, -turnPosLength + radius);
        directPath.lineTo(inSideLength + radius, turnPosLength + radius);
        directPath.lineTo(turnPosLength + radius, turnPosLength + radius);
        directPath.lineTo(turnPosLength + radius, inSideLength + radius);
        directPath.lineTo(-turnPosLength + radius, inSideLength + radius);
        directPath.lineTo(-turnPosLength + radius, turnPosLength + radius);
        directPath.lineTo(-inSideLength + radius, turnPosLength + radius);
        directPath.close();
        canvas.drawPath(directPath, paint);
    }
}
