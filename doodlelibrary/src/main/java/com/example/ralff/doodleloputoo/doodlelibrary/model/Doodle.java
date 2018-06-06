package com.example.ralff.doodleloputoo.doodlelibrary.model;

import android.util.Log;

import java.util.ArrayList;



public class Doodle extends Entity {
    private final String TAG = "Doodle";
    private float highestY;
    private float jumpSize;

    private float gravity;

    private boolean grounded;

    public Doodle(float x, float y, float width, float height, float jumpSize, float gravity) {
        super(x, y, width, height);
        this.jumpSize = jumpSize;
        this.gravity = gravity;
        this.grounded = false;

        Log.i(TAG, "New Doodle created, jumpSize = " + jumpSize + ", gravity = " + gravity);
    }

    
    public boolean checkCollision(ArrayList<Entity> entities) {
        boolean colliding = collidingWithPlatforms(entities);
        if (colliding) {
            this.grounded = true;
            this.velocityY = 0;
        } else {
           
            this.grounded = false;
        }
        return colliding;
    }

    
    private boolean collidingWithPlatforms(ArrayList<Entity> entities) {
        boolean isCollidingWithPlatform = false;
        for (Entity platform : entities) {
            if (platform instanceof Doodle)
                continue;

            if (isColliding(platform, velocityY)) {
                isCollidingWithPlatform = true;

                
                this.setY(platform.getY() - this.getHeight());
                break;
            }

        }

        return isCollidingWithPlatform;
    }

   
    private boolean isColliding(Entity entity, float velocityY) {
        boolean isColliding = false;

        float myXPosition = getX();
        float myWidth = getWidth();
        float margin = (myWidth / 4);
        float myXLeftMargin = myXPosition - margin;
        float myXRightMargin = myXPosition + margin;
        float myXEnd = myXLeftMargin + myWidth;

        float entityXPosition = entity.getX();
        float entityWidth = entity.getWidth();
        float entityXEnd = entityXPosition + entityWidth;

        if (myXRightMargin <= entityXEnd && entityXPosition <= myXEnd) {
            
            float myYPosition = getY();
            float myHeight = getHeight();
            float myYEnd = myYPosition + myHeight;

            float entityYPosition = entity.getY();
            float entityHeight = entity.getHeight() + velocityY;
            float entityYEnd = entityYPosition + entityHeight;

            if (myYEnd >= entityYPosition && myYEnd <= entityYEnd) {
                isColliding = true;
            }
        }

        return isColliding;
    }

    @Override
    public void update(double dt) {
        if (!grounded) {
            setVelocityY(getVelocityY() + gravity);
        }

        super.update(dt);
    }

    @Override
    public void setY(float y) {
        super.setY(y);

        if (y <= this.highestY) {
            this.highestY = y;
        }
    }

    public void jump() {
        Log.i(TAG, "Doodle jump occurred!");

        this.velocityY = -jumpSize;
    }

    public float getHighestY() {
        return highestY;
    }
}
