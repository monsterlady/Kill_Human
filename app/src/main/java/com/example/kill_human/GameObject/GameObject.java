package com.example.kill_human.GameObject;

import android.graphics.Rect;

public abstract class GameObject {
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int scaleWidth;
    protected int scaleHeight;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setScaleHeight(int scaleHeight) {
        this.scaleHeight = scaleHeight;
    }

    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public int getScaleHeight() {
        return scaleHeight;
    }

    public int getScaleWidth() {
        return scaleWidth;
    }

    public Rect getRectangle(){
        return new Rect(x,y,x+scaleWidth,y+scaleHeight);
    }

}
