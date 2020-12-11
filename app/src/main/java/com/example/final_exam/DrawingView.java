package com.example.final_exam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View{
    ArrayList<MyShape> myShapeArrayList = new ArrayList<>();
    MyShape myShape;
    Path pt = new Path();
    final static int LINE=1, RECT=2, CIRCLE=3, CURVE=4,ERASE=5;
    final static int black=Color.BLACK, blue = Color.BLUE, cyan=Color.CYAN,gray=Color.GRAY;
    final static int green=Color.GREEN,red=Color.RED,yellow=Color.YELLOW;
    static int currentColor = black;
    static int currentshape = LINE;
    int startX=-1, startY=-1, endX=-1, endY=-1;

    public DrawingView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(10f);
        p.setColor(currentColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);

        myShape = new MyShape(currentshape,startX,startY,endX,endY,p);
        myShapeArrayList.add(myShape);

        for(MyShape mShape: myShapeArrayList){
            draw_shape(mShape, canvas);
        }

        if(myShape != null)
            draw_shape(myShape,canvas);
    }

    public void draw_shape(MyShape myShape, Canvas canvas){
        switch (myShape.shape_type){
            case LINE:
                canvas.drawLine(myShape.startX, myShape.startY,myShape.stopX, myShape.stopY, myShape.paint);
            break;
            case CIRCLE:
                int radius = (int) Math.sqrt(Math.pow(myShape.stopX - myShape.startX, 2) + Math.pow(myShape.stopY - myShape.startY, 2));
                canvas.drawCircle(myShape.startX, myShape.startY, radius,myShape.paint);
                break;
            case RECT:
                Rect rect = new Rect(myShape.startX,myShape.startY, myShape.stopX, myShape.stopY);
                canvas.drawRect(rect, myShape.paint);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int)eX;
                startY = (int)eY;
                endX = (int)eX;
                endY = (int)eY;
                pt.moveTo(eX, eY);
                return true;
            case MotionEvent.ACTION_MOVE:
                endX = (int)eX;
                endY = (int)eY;
                pt.lineTo(eX, eY);
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        return true;
    }

    // 그렸던 것들을 저장하는 클래스
    private static class MyShape{
        int shape_type , startX, startY, stopX,stopY;
        Paint paint;
        public MyShape(int shape_type, int startX, int startY, int stopX, int stopY, Paint paint){
            this.shape_type = shape_type;
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
            this.paint = paint;

            switch(currentColor){
                case black:
                    paint.setColor(black);
                    break;
                case blue:
                    paint.setColor(blue);
                    break;
                case cyan:
                    paint.setColor(cyan);
                    break;
                case gray:
                    paint.setColor(gray);
                    break;
                case green:
                    paint.setColor(green);
                    break;
               // case pink:
               //     paint.setColor(pink);
               //     break;
                case red:
                    paint.setColor(red);
                    break;
                case yellow:
                    paint.setColor(yellow);
                    break;
            }
        }
    }

    @Override
    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        super.addChildrenForAccessibility(outChildren);
    }
}
