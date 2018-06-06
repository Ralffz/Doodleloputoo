package com.example.ralff.doodleloputoo.doodlelibrary.graphics.animation;

import android.graphics.Bitmap;

import java.util.ArrayList;


public class Animation {
	
	private ArrayList<AnimationFrame> mFrames;

	
	private float animationLength;

	public Animation() {
		mFrames = new ArrayList<>();
		animationLength = 0.0f;
	}

	
	public void addFrames(AnimationFrame... frames) {
		for (AnimationFrame frame : frames) {
			mFrames.add(frame);
			animationLength += frame.getFrameTime();
		}
	}

	
	public AnimationFrame getFrame(float animationTime) {
		AnimationFrame result = null;
		final float length = getAnimationLength();
		if (length > 0.0f) {
			final ArrayList<AnimationFrame> frames = mFrames;
			final int frameCount = frames.size();
			result = frames.get(frameCount - 1);

			if (frameCount > 1) {
				float currentTime = 0.0f;
				float cycleTime = animationTime % length;

				if (cycleTime < length) {
					for (int x = 0; x < frameCount; x++) {
						AnimationFrame frame = frames.get(x);
						currentTime += frame.getFrameTime();
						if (currentTime > cycleTime) {
							result = frame;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	public float getAnimationLength() {
		return animationLength;
	}

	
	
	public static Animation fromSpriteSheet(Bitmap spriteSheet, int row, int tileSizeX, int tileSizeY, float frameDuration) {
		Animation animation = new Animation();

		
		int bitmapWidth = spriteSheet.getWidth();

		
		int amountOfImages = bitmapWidth / tileSizeX;
		for (int i = 0; i < amountOfImages; i++) {
			
			int startX = (i * tileSizeX);
			int startY = (row * tileSizeY);

			Bitmap sprite = Bitmap.createBitmap(spriteSheet, startX, startY, tileSizeX, tileSizeY);

			AnimationFrame frame = new AnimationFrame(sprite, frameDuration);
			animation.addFrames(frame);
		}

		return animation;
	}

	
	public static Animation fromBitmaps(float frameDuration, Bitmap... bitmaps) {
		Animation animation = new Animation();

		for (Bitmap bitmap : bitmaps) {
			AnimationFrame animationFrame = new AnimationFrame(bitmap, frameDuration);
			animation.addFrames(animationFrame);
		}

		return animation;
	}
}
