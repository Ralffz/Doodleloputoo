package com.example.ralff.doodleloputoo.doodlelibrary.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.ralff.doodleloputoo.doodlelibrary.graphics.animation.Animation;
import com.example.ralff.doodleloputoo.doodlelibrary.graphics.animation.AnimationFrame;
import com.example.ralff.doodleloputoo.doodlelibrary.graphics.view.DoodleSurfaceView;
import com.example.ralff.doodleloputoo.doodlelibrary.logic.ScrollingCamera;



public class Entity {
	private final String TAG = "Entity";

	protected float x;


	protected float y;

	
	protected float width;

	
	protected float height;

	protected float velocityX;

	
	protected float velocityY;

	protected RectF drawingRectangle;

	protected Animation currentAnimation;

	private float animationTime;

	public Entity(float x, float y, float width, float height) {
		this(x, y, width, height, 0, 0);
	}

	public Entity(float x, float y, float width, float height, Bitmap bitmap) {
		this(x, y, width, height, 0, 0, Animation.fromBitmaps(1, bitmap));
	}

	public Entity(float x, float y, float width, float height, float velocityX, float velocityY) {
		this(x, y, width, height, velocityX, velocityY, null);
	}

	public Entity(float x, float y, float width, float height, float velocityX, float velocityY, Animation animation) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.currentAnimation = animation;
		this.drawingRectangle = new RectF();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	/**
	 * Draws this instance
	 *
	 * @param camera The camera to use
	 * @param canvas The canvas to draw onto
	 */
	public void draw(ScrollingCamera camera, Canvas canvas) {
		if (currentAnimation == null)
			return;

		AnimationFrame currentFrame = currentAnimation.getFrame(animationTime);
		if (currentFrame == null)
			return;

		Bitmap image = currentFrame.getBitmap();
		if (image == null)
			return;

		float relativeYPos = camera.getRelativeYPosition(getY());
		drawingRectangle.set(getX(), relativeYPos, getX() + getWidth(), relativeYPos + getHeight());
		canvas.drawBitmap(image, null, drawingRectangle, null);
	}

	
	public void update(double dt) {
		animationTime += dt;

		float newX = getX() + (float) (getVelocityX() * dt * DoodleSurfaceView.TARGET_FPS / DoodleSurfaceView.SECOND);
		float newY = getY() + (float) (getVelocityY() * dt * DoodleSurfaceView.TARGET_FPS / DoodleSurfaceView.SECOND);

		setX(newX);
		setY(newY);
	}

	
	public boolean isInScreen(ScrollingCamera camera) {
		float relativeYPosition = camera.getRelativeYPosition(getY());
		return camera.getScreenHeight() > relativeYPosition;
	}

	public void setAnimation(Animation animation) {
		this.currentAnimation = animation;
	}
}
