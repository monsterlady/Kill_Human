package com.example.kill_human.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.kill_human.Animation;
import java.util.Random;

import static com.example.kill_human.GameObject.Trex.SCREENWIDTH;

public class Primitive extends GameObject {

    private int score;
    private int speed;
    private Random random = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private static final int PRIMITIVEHIEGHT = 60;
    private static final int PRIMITIVEWIDTH =  60;


    public Primitive(Bitmap res,int x,int y,int w,int h,int s,int numFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;
        speed = 1 + (int) (random.nextDouble()*score/50);

        //cap missile speed
        if(speed>10)speed = 10;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;
        // 设置想要的大小
        int newWidth =  PRIMITIVEWIDTH;
        int newHeight = PRIMITIVEHIEGHT;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        //spritesheet = Bitmap.createBitmap(spritesheet, 0, 0, width, height,matrix,true);
        for(int i = 0; i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height,matrix,true);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);
        width = newWidth;
        height = newHeight;
    }

     public void update(){
        x+=speed;
        animation.update();
     }

     public void draw(Canvas canvas){
         try{
             canvas.drawBitmap(animation.getImage(),x,y,null);
         } catch (Exception e){
             //
         }
     }

}
