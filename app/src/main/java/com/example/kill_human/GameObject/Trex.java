package com.example.kill_human.GameObject;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import com.example.kill_human.Animation;
import com.example.kill_human.GamePanel;



public class Trex extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private double dya;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;
    public static final int SCREENWIDTH = 100;
    public static final int SCREENHEIGHT = 100;
    private static final int MOVEMENTSPEED = 10;
    private static final int FALLDOWNSPEED =  1;
    private static final int JUMPHEIGHT = 80;
    private static final int INITHEIGHT = 365;
    private  float scaleWidthFactor,scaleHeightFactor;
    private boolean down;

    public Trex(Bitmap res, int w, int h, int numFrames) {


        dy = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        // 计算缩放比例
        scaleWidthFactor = ((float) SCREENWIDTH) / width;
        scaleHeightFactor = ((float) SCREENHEIGHT) / height;
        //存储理想的大小
        scaleWidth = SCREENWIDTH;
        scaleHeight = SCREENHEIGHT;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthFactor, scaleHeightFactor);
         spritesheet = Bitmap.createBitmap(spritesheet, 0, 0, width, height,matrix,true);
        /*
        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height,matrix,true);
        }
*/
        x = GamePanel.WIDTH / 2;
        y = INITHEIGHT;
        System.out.println("屏幕宽度 :" + y);
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }

    public void setUp(boolean b){up = b;}

    public void setDown(boolean down) {
        this.down = down;
    }

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            //score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if(up){
                 while(y > INITHEIGHT - JUMPHEIGHT){
                     y -= FALLDOWNSPEED;
                 }
             } else if (down){
                  while (y < INITHEIGHT){
                      y += FALLDOWNSPEED;
                  }
              }
                 if(y < INITHEIGHT - JUMPHEIGHT){
                  y = INITHEIGHT - JUMPHEIGHT;
              }
              if(y > INITHEIGHT){
                  y = INITHEIGHT;
              }

        }


    public void changeDirection(int it){
         if(x <= 0){
             x = 0;
         }
         if(x >= GamePanel.WIDTH - SCREENWIDTH){
             x = GamePanel.WIDTH - SCREENWIDTH;
         }
         if(it > 0){
             x += MOVEMENTSPEED;
         } else {
             x -= MOVEMENTSPEED;
         }
    }



    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(spritesheet,x,y,null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}
    public void resetBitmap(Bitmap bitmap){
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthFactor, scaleHeightFactor);
        spritesheet = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix,true);

    }

    public void setScore(int score) {
        this.score = score;
    }
}
