package com.example.ralff.doodleloputoo.doodlelibrary.logic;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.ralff.doodleloputoo.doodlelibrary.model.Doodle;
import com.example.ralff.doodleloputoo.doodlelibrary.model.Entity;

import java.util.ArrayList;



public class ScrollingCamera {
    
    private ArrayList<Entity> entities;
    private float cameraY;
    private Rect bounds;

    public ScrollingCamera(Rect bounds) {
        entities = new ArrayList<>();
        this.bounds = bounds;
        this.cameraY = bounds.top - bounds.height();
    }

    
    public void update(double dt, Doodle doodle) {
        for (Entity entity : entities) {
            entity.update(dt);
        }

        float lastY = cameraY;

        
        cameraY = doodle.getY() - getScreenHeight() / 2;

        
        if (cameraY >= lastY)
            cameraY = lastY;

        
        if (doodle.getX() >= getScreenWidth()) {
            doodle.setX(0 - doodle.getWidth());
        } else if (doodle.getX() < 0 - doodle.getWidth()) {
            doodle.setX(getScreenWidth());
        }
    }

    
    public void draw(Canvas canvas) {
        for (Entity entity : entities) {
            if (this.isEntityInScreen(entity)) {
                entity.draw(this, canvas);
            }
        }
    }

    
    private boolean isEntityInScreen(Entity entity) {
        float screenCoordinateY = getRelativeYPosition(entity.getY());

        return (entity.getX() >= bounds.left && entity.getX() + entity.getWidth() <= getScreenWidth()) && (screenCoordinateY >= bounds.top && screenCoordinateY + entity.getHeight() <= getScreenHeight());
    }

    
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    
    public float getRelativeYPosition(float yPos) {
        return yPos - this.cameraY;
    }

    
    public int getScreenWidth() {
        return bounds.width();
    }

    
    public int getScreenHeight() {
        return bounds.height();
    }

}
