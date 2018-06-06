package com.example.ralff.doodleloputoo.doodlelibrary.graphics.animation;

import android.graphics.Bitmap;



public class AnimationFrame {
	
	private Bitmap bitmap;

	
	private float frameTime;

	public AnimationFrame(Bitmap bitmap, float frameTime) {
		this.bitmap = bitmap;
		this.frameTime = frameTime;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public float getFrameTime() {
		return frameTime;
	}
}
