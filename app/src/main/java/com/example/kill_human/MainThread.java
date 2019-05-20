package com.example.kill_human;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private int FPS = 60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean isRunning;
    public static Canvas canvas;


    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCoutn = 0;
        long targetTime = 1000 / FPS;

        while(isRunning){
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e){
            }
            finally {
                if (canvas!=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = (targetTime - timeMillis);

            try {
                this.sleep(waitTime);
            } catch (Exception e){}
             totalTime += (System.nanoTime() - startTime);
             frameCoutn++;
             if(frameCoutn == FPS){
                 averageFPS =  1000.0 / ((totalTime/frameCoutn)/1000000.0);
                 frameCoutn = 0;
                 totalTime = 0;
                 //System.out.println("破电脑计时帧数： " + averageFPS);
             }
            }
        }


    public void setRunning(boolean b){
        isRunning = b;
    }
}
