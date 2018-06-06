package com.example.ralff.doodleloputoo.doodlelibrary.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.ralff.doodleloputoo.doodlelibrary.R;
import com.example.ralff.doodleloputoo.doodlelibrary.graphics.AssetManager;
import com.example.ralff.doodleloputoo.doodlelibrary.graphics.animation.Animation;
import com.example.ralff.doodleloputoo.doodlelibrary.listener.DoodleListener;
import com.example.ralff.doodleloputoo.doodlelibrary.listener.ScreenListener;
import com.example.ralff.doodleloputoo.doodlelibrary.model.Doodle;
import com.example.ralff.doodleloputoo.doodlelibrary.model.Entity;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class DoodleGame implements ScreenListener {
	private final String TAG = "DoodleGame";

	
	private static final int DOODLE_START_Y = -100;

	
	private static final int DOODLE_WIDTH = 50;

	
	private static final int DOODLE_HEIGHT = 50;

	
	private static final int DOODLE_JUMP_SIZE = 40;

	
	private static final float DOODLE_GRAVITY = 0.8f;

	
	private static final int PLATFORM_WIDTH = 100;

	
	private static final int PLATFORM_HEIGHT = 10;

	
	private static final int MIN_DIFFERENCE = 100;

	
	private static final int MAX_DIFFERENCE = 500;

	
	private static final int GENERATION_COUNT = 100;

	
	private static final int GENERATION_START_THRESHOLD = 3000;

	private static final int SCORE_UPDATE_INTERVAL = 100;

	
	private static final boolean TIMER_ENABLED = false;

	
	private boolean isStarted;

	
	private int screenWidth;

	
	private int screenHeight;

	
	private ScrollingCamera camera;

	
	private Doodle doodle;

	
	private ArrayList<Entity> entities;

	
	private float lastYGenerated;


	private DoodleListener doodleListener;

	
	private ScheduledExecutorService ses;


	private boolean timerNeedReset;

	
	private boolean isTimerStarted;

	
	private int timerTimeToCountDownInS;

	
	private int timerTimeToCountDownInMS;

	
	private ScheduledFuture timerLoop;

	
	private ScheduledFuture timerCountDown;

	
	private DifficultyHandler difficultyHandler;

	
	private Context context;

	public DoodleGame(Context context) {
		entities = new ArrayList<>();
		isStarted = false;
		timerNeedReset = false;
		isTimerStarted = false;
		this.context = context;
	}

	
	public void startGame(final DoodleListener doodleListener) {
		Log.i(TAG, "Start game called!");
		this.doodleListener = doodleListener;

		ses = Executors.newScheduledThreadPool(2);

		timerTimeToCountDownInS = 8;
		timerTimeToCountDownInMS = timerTimeToCountDownInS * 1000;
		difficultyHandler = new DifficultyHandler(PLATFORM_WIDTH, MIN_DIFFERENCE, MAX_DIFFERENCE, timerTimeToCountDownInS);

		loadResources();

		entities.clear();

		Log.i(TAG, "Setting up camera, doodle and platform");
		
		camera = new ScrollingCamera(new Rect(0, 0, getScreenWidth(), getScreenHeight()));
		doodle = new Doodle(getScreenWidth() / 2 - DOODLE_WIDTH, DOODLE_START_Y, DOODLE_WIDTH, DOODLE_HEIGHT, DOODLE_JUMP_SIZE, DOODLE_GRAVITY);

		Animation doodleAnimation = Animation.fromBitmaps(1000, AssetManager.getInstance().getBitmapFromMemCache(R.drawable.circle), AssetManager.getInstance().getBitmapFromMemCache(R.drawable.circle_blue), AssetManager.getInstance().getBitmapFromMemCache(R.drawable.circle_green));
		doodle.setAnimation(doodleAnimation);


		entities.add(doodle);

		
		Entity platform = new Entity(doodle.getX() - (doodle.getWidth() / 2), doodle.getY() + doodle.getHeight(), PLATFORM_WIDTH, PLATFORM_HEIGHT, AssetManager.getInstance().getBitmapFromMemCache(R.drawable.platform));
		entities.add(platform);

		
		lastYGenerated = platform.getY();


		Log.i(TAG, "Setting up score changed runnable");
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				
				if (!isStarted)
					return;

				
				if (doodleListener != null && doodle != null) {
					float resultScore = doodle.getHighestY() * -1;
					doodleListener.scoreChanged(Math.round(resultScore));
				}
			}
		}, 0, SCORE_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);

		resetTimer();
		startTimer();
		timerNeedReset = true;

		isStarted = true;
		Log.i(TAG, "Game started!");
	}

	
	public void stopGame() {
		Log.i(TAG, "Game stopped!");
		isStarted = false;

		if (ses != null) {
			Log.i(TAG, "Shutting down ScheduledExecutorService");
			ses.shutdownNow();
		}

		if (doodleListener != null) {
			Log.i(TAG, "Callback to gameOver");
			
			float resultScore = doodle.getHighestY() * -1;
			doodleListener.gameOver(Math.round(resultScore));
		}
	}

	
	public void update(double dt) {
		if (isStarted) {
			if (doodle.checkCollision(entities)) {
				if (isTimerStarted && timerNeedReset) {
					
					timerNeedReset = false;
					resetTimer();
					startTimer();
				}
			} else {
				timerNeedReset = true;
			}

			camera.update(dt, doodle);

			generatePlatforms();
			cleanupOldPlatforms();

			if (!doodle.isInScreen(camera)) {
				Log.i(TAG, "Doodle left screen");
				stopGame();
			}

			if (difficultyHandler.needNewValues(doodle.getHighestY())) {
				difficultyHandler.setNewValues(doodle.getHighestY());
			}

		}
	}

	
	public void draw(Canvas canvas) {
		if (isStarted)
			camera.draw(canvas);
	}

	
	private void generatePlatforms() {
		if (lastYGenerated < 0 && doodle.getHighestY() > lastYGenerated + GENERATION_START_THRESHOLD)
			return;

		Log.i(TAG, "Generating new platforms");

		Random rnd = new Random();

		for (int i = 0; i < GENERATION_COUNT; i++) {
			int x = rnd.nextInt(getScreenWidth() - 100) + 1;

			float randomY = rnd.nextFloat() * (difficultyHandler.getMaxDifference() - difficultyHandler.getMinDifference()) + difficultyHandler.getMinDifference();
			if (randomY > 0) {
				
				randomY *= -1;
			}

			float platformY = lastYGenerated + randomY;

			lastYGenerated = platformY;

			Entity entity = new Entity(x, platformY, difficultyHandler.getPlatformWidth(), PLATFORM_HEIGHT, AssetManager.getInstance().getBitmapFromMemCache(R.drawable.platform));
			entities.add(entity);
		}

		camera.setEntities(entities);

		Log.i(TAG, "New platforms generated, count = " + GENERATION_COUNT);
	}

	
	private void cleanupOldPlatforms() {
		float screenBorder = (doodle.getY() + getScreenHeight() / 2);

		ArrayList<Integer> entityIndicesToRemove = new ArrayList<>();
		for (Entity entity : entities) {
			if (entity instanceof Doodle)
				continue;

			
			if (entity.getY() >= screenBorder) {
				entityIndicesToRemove.add(entities.indexOf(entity));
			}
		}

		for (int index : entityIndicesToRemove) {
			entities.remove(index);
		}

		if (entityIndicesToRemove.size() > 0) {
			camera.setEntities(entities);
			Log.i(TAG, "Cleaning up, total cleaned up = " + entityIndicesToRemove.size());
		}
	}

	
	private void loadResources() {
		Log.i(TAG, "Starting load resources.");
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.platform, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.platform, PLATFORM_WIDTH, PLATFORM_HEIGHT));
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.circle, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.circle, DOODLE_WIDTH, DOODLE_HEIGHT));
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.circle_blue, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.circle_blue, DOODLE_WIDTH, DOODLE_HEIGHT));
		AssetManager.getInstance().addBitmapToMemoryCache(R.drawable.circle_green, AssetManager.decodeSampledBitmapFromResource(context.getResources(), R.drawable.circle_green, DOODLE_WIDTH, DOODLE_HEIGHT));

		Log.i(TAG, "Done loading resources.");
	}

	@Override
	public void screenTouched(float xPosition, float yPosition) {
		
		if (!isStarted)
			return;

		Log.i(TAG, "Screen touched, xPosition = " + xPosition + ", yPosition = " + yPosition);
		if (doodle != null)
			doodle.jump();

		timerNeedReset = true;

		if (!isTimerStarted) {
			startTimer();
		}
	}

	@Override
	public void screenSizeChanged(int width, int height) {
		Log.i(TAG, "Screen size changed, width = " + width + ", height = " + height);
		this.screenWidth = width;
		this.screenHeight = height;
	}

	@Override
	public void rotationChanged(float newRotation) {
		
		float velocityX = newRotation * -1;

		if (doodle != null)
			doodle.setVelocityX(velocityX);
	}

	
	public void startTimer() {
		if (!TIMER_ENABLED)
			return;

		if (timerLoop == null) {
			
			isTimerStarted = true;
			Log.i(TAG, "Timer started");
			timerCountDown = ses.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					doodleListener.updateTimer(timerTimeToCountDownInMS);
					timerTimeToCountDownInMS -= 100;
				}
			}, 0, 100, TimeUnit.MILLISECONDS);

			timerLoop = ses.schedule(new Runnable() {
				@Override
				public void run() {
					timerCountDown.cancel(true);
					stopGame();
				}
			}, timerTimeToCountDownInS, TimeUnit.SECONDS);
		}
	}

	
	public void resetTimer() {
		if (!TIMER_ENABLED)
			return;

		if (timerLoop != null) {
			Log.i(TAG, "Timer reseted");
			timerTimeToCountDownInS = difficultyHandler.getInitialCountDownTimerInS();
			timerTimeToCountDownInMS = timerTimeToCountDownInS * 1000;
			doodleListener.updateTimer(timerTimeToCountDownInS);
			timerCountDown.cancel(true);
			isTimerStarted = false;
			timerLoop.cancel(true);
			timerLoop = null;
		}
	}

	
	public int getScreenWidth() {
		return screenWidth;
	}

	
	public int getScreenHeight() {
		return screenHeight;
	}
}
