package com.example.ralff.doodleloputoo.doodlelibrary.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;



public class AssetManager {
	private static AssetManager _instance;

	private LruCache<Integer, Bitmap> mMemoryCache;

	private AssetManager() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(Integer key, Bitmap value) {
				return value.getByteCount() / 1024;
			}
		};
	}

	public static AssetManager getInstance() {
		if (_instance == null)
			_instance = new AssetManager();

		return _instance;
	}

	public void addBitmapToMemoryCache(int key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(int key) {
		return mMemoryCache.get(key);
	}

	
	public Bitmap getBitmapFromMemCache(Resources res, int key, int reqWidth, int reqHeight) {
		if (getBitmapFromMemCache(key) == null) {
			addBitmapToMemoryCache(key, decodeSampledBitmapFromResource(res, key, reqWidth, reqHeight));
		}

		return getBitmapFromMemCache(key);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
}
