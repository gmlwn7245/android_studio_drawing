package com.example.final_exam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    Button colorBtn, widthBtn;
    final static int LINE=1, RECT=2, CIRCLE=3, CURVE=4,ERASE=5;
    final static int black=Color.BLACK, blue = Color.BLUE, cyan=Color.CYAN,gray=Color.GRAY;
    final static int green=Color.GREEN,red=Color.RED,yellow=Color.YELLOW;
    static int currentColor = black;
    static int currentshape = LINE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingView(this));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_main,null);
        addContentView(v , new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));


        colorBtn = (Button) (findViewById(R.id.color_btn));
        widthBtn = (Button) (findViewById(R.id.width_btn));
    }

    //erase 버튼
    /*public void clear(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("전부 삭제하겠습니까?");
        alertDialogBuilder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                v.setVisibility(v.GONE);
                v.setBackgroundColor(Color.parseColor("#DCFBCC"));
                v.invalidate();
                Toast.makeText(MainActivity.this, "삭제하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "취소하였습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

    // menu-checkbox 속성
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean item_state = item.isChecked();
        switch(item.getItemId()) {
            case R.id.blur_menu:
                case R.id.emboss_menu:
                item.setChecked(!item_state);
                return true;
            case R.id.color_btn:
                CustomDialog colorDialog = new CustomDialog(MainActivity.this);
                colorDialog.callFunction("color");
                return true;
            case R.id.width_btn:
                CustomDialog widthDialog = new CustomDialog(MainActivity.this);
                widthDialog.callFunction("width");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Btn에 이벤트 처리
    public boolean mOnClick(View view){
        switch (view.getId()){
            case R.id.line:
                Toast.makeText(getApplicationContext(),"line",Toast.LENGTH_SHORT).show();
                currentshape = LINE;
                return true;
            case R.id.rect:
                currentshape = RECT;
                Toast.makeText(getApplicationContext(),"rect",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.circle:
                currentshape = CIRCLE;
                Toast.makeText(getApplicationContext(),"circle",Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public static class DrawingView extends View{
        ArrayList<MyShape> myShapeArrayList = new ArrayList<>();
        MyShape myShape;
        Path pt = new Path();
        int startX=-1, startY=-1, endX=-1, endY=-1, status = 0;

        public DrawingView(Context context) {
            super(context);
            setBackgroundColor(Color.parseColor("#cfffe5"));
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
            if(status == 0){
                myShapeArrayList.add(myShape);
            }
            for(MyShape mShape: myShapeArrayList){
                draw_shape(mShape, canvas);
            }

            if(myShape != null){
                draw_shape(myShape,canvas);
            }


        }

        public void draw_shape(MyShape myShape, Canvas canvas){
            switch (myShape.shape_type){
                case LINE:
                    canvas.drawPath(pt,myShape.paint);
                    break;
                case CIRCLE:
                    int radius = (int) Math.sqrt(Math.pow(myShape.stopX - myShape.startX, 2) + Math.pow(myShape.stopY - myShape.startY, 2));
                    canvas.drawCircle(myShape.startX, myShape.startY, radius,myShape.paint);
                    break;
                case RECT:
                    Rect rect = new Rect(myShape.startX,myShape.startY, myShape.stopX, myShape.stopY);
                    canvas.drawRect(rect, myShape.paint);
                    break;
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
                    status = 1;
                    endX = (int)eX;
                    endY = (int)eY;
                    if(currentshape==LINE){
                        pt.lineTo(eX, eY);
                    }
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    status = 0;
                    this.invalidate();
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


}