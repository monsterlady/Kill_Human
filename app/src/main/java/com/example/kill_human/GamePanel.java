package com.example.kill_human;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.kill_human.GameObject.Primitive;
import com.example.kill_human.GameObject.Trex;
import java.util.ArrayList;
import java.util.Random;



public class GamePanel extends SurfaceView implements SurfaceHolder.Callback,Runnable
{

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 512;
    private static final int INITHEIGHTFORPRIMITIVE = 405;
    private MainThread thread;
    private BackGroud bg;
    private Trex trex;
    private ArrayList<Primitive> primitives;
    private long primitiveStratTime;
    private boolean isJumpping;
    private Random random = new Random();
    private static final int RESPAWNAREAWIDTH = 100;

    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);

    }
    public GamePanel(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();
                retry = false;
            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new BackGroud(BitmapFactory.decodeResource(getResources(), R.drawable.forest));
        trex = new Trex(BitmapFactory.decodeResource(getResources(), R.drawable.d1), 229, 239,1);
        primitives = new ArrayList<>();
        primitiveStratTime = System.nanoTime();
       // setLongClickable(false);

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void draw(Canvas canvas) {

        final float scaleFactorX = (float) getWidth() / WIDTH;
        final float scaleFactorY = (float) getHeight() / HEIGHT;
//System.out.println("屏幕宽:" + getWidth() + " 屏幕高: " + getHeight()  );
        if (canvas != null) {
            super.draw(canvas);
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            trex.draw(canvas);
            //draw missiles
            for(Primitive m: primitives)
            {
                m.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if(event.getAction()==MotionEvent.ACTION_DOWN ){
            if(!trex.getPlaying())
            {
                trex.setPlaying(true);
            }
            else
            {
               trex.setUp(true);
               trex.setDown(false);
               isJumpping = true;
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            isJumpping = false;
            trex.setUp(false);
            trex.setDown(true);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        if (trex.getPlaying()) {
            bg.update();
            trex.update();

            //add primitive on timer
            long primitiveElapsed = (System.nanoTime() - primitiveStratTime) / 1000000;
            if (primitiveElapsed > (2000 - trex.getScore() / 4)) {
                System.out.println("开始产生原始人" + "\n 分数" + trex.getScore());

                //add the first primitive
                if (primitives.size() == 0) {
                    primitives.add(new Primitive(BitmapFactory.decodeResource(getResources(), R.drawable.dancers), 0, INITHEIGHTFORPRIMITIVE, 32, 33, trex.getScore(), 3));
                } else if(primitives.size() < 5) {
                    primitives.add(new Primitive(BitmapFactory.decodeResource(getResources(), R.drawable.dancers), (int) (random.nextDouble() * (RESPAWNAREAWIDTH)),INITHEIGHTFORPRIMITIVE, 32, 33, trex.getScore(), 3));
                    primitives.add(new Primitive(BitmapFactory.decodeResource(getResources(), R.drawable.dancers2), (int) (random.nextDouble() * (RESPAWNAREAWIDTH)),INITHEIGHTFORPRIMITIVE, 32, 33, trex.getScore(), 3));
                    primitives.add(new Primitive(BitmapFactory.decodeResource(getResources(), R.drawable.dancers3), (int) (random.nextDouble() * (RESPAWNAREAWIDTH)),INITHEIGHTFORPRIMITIVE, 32, 33, trex.getScore(), 3));
                }

                //reset timer
                primitiveStratTime = System.nanoTime();
            }

            for (int i = 0; i < primitives.size(); i++) {
                //update missile
                primitives.get(i).update();

                if (collision(primitives.get(i), trex)) {
                    trex.setScore(trex.getScore()+50);
                    primitives.remove(i);
                    //trex.setPlaying(false);

                    break;
                }
                //remove primitive if it is way off the screen
                if (primitives.get(i).getX() >= WIDTH) {
                    primitives.remove(i);
                    trex.setPlaying(false);
                    break;
                }
            }
        }
    }
//判定系统
    private boolean collision(Primitive primitive, Trex trex) {
            if(Rect.intersects(primitive.getRectangle(),trex.getRectangle()) && !isJumpping)
            {
              if( (primitive.getX() >= trex.getX() && primitive.getX() <= trex.getX() + trex.getRectangle().bottom) || (primitive.getX() + primitive.getRectangle().bottom > trex.getX() && primitive.getX() < trex.getX() )){
                  isJumpping = true;
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

    public void changeDirection(int x){
        //Change Trex Direction
        if(x <= 0){
            trex.resetBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.d2));
        } else {
            trex.resetBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.d1));
        }
        if(trex.getPlaying()){
            trex.changeDirection(x);
        }
        update();
    }
    @Override
    public void run() {

    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Score: " + (trex.getScore()*3), 10, HEIGHT - 10, paint);
        //canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!trex.getPlaying())
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO JUMP UP", WIDTH/2-50, HEIGHT/2 + 20, paint1);
            canvas.drawText("RELEASE TO FALL DOWN", WIDTH/2-50, HEIGHT/2 + 40, paint1);
        }
    }
}