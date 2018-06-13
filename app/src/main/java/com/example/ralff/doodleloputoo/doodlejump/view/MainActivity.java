package com.example.ralff.doodleloputoo.doodlejump.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.ralff.doodleloputoo.R;
import com.example.ralff.doodleloputoo.doodlejump.listener.UiListener;
import com.example.ralff.doodleloputoo.doodlejump.view.fragment.AttractFragment;
import com.example.ralff.doodleloputoo.doodlejump.view.fragment.GameOverFragment;
import com.example.ralff.doodleloputoo.doodlejump.view.fragment.GameOverlayFragment;
import com.example.ralff.doodleloputoo.doodlejump.view.fragment.MainMenuFragment;
import com.example.ralff.doodleloputoo.doodlejump.view.fragment.ScoreboardFragment;
import com.example.ralff.doodleloputoo.doodlelibrary.graphics.view.DoodleSurfaceView;
import com.example.ralff.doodleloputoo.doodlelibrary.listener.DoodleListener;
import com.example.ralff.doodleloputoo.doodlelibrary.model.Score;

public class MainActivity extends Activity implements DoodleListener, UiListener {
    private final String TAG = "MainActivity";

    
    private static final long IDLE_TIME = 10000;

    private DoodleSurfaceView doodleSurfaceView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private UiState currentUiState;

    private Score currentScore;

    private Handler idleHandler;
    private Runnable idleRunnable;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUiState(UiState.MAIN_MENU);

        currentScore = new Score();

        idleHandler = new Handler(Looper.getMainLooper());
        idleRunnable = new Runnable() {
            @Override
            public void run() {
                
                goAttractMode();
            }
        };


        doodleSurfaceView = (DoodleSurfaceView) findViewById(R.id.doodle_surface_view);

        hideSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                hideSystemUI();
            }
        });

        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onPause() {
        super.onPause();

        doodleSurfaceView.pause();

        idleHandler.removeCallbacks(idleRunnable);

        if (currentUiState == UiState.GAME)
            unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        doodleSurfaceView.resume();

        if (currentUiState == UiState.GAME)
            registerListeners();

        if (currentUiState == UiState.MAIN_MENU) {
            scheduleIdleCallback();
        }

        switchViews();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (currentUiState == UiState.MAIN_MENU) {
            scheduleIdleCallback();
        }
    }



    private void scheduleIdleCallback() {
        Log.i(TAG, "Starting delayed idle runnable.");
        idleHandler.removeCallbacks(idleRunnable);
        idleHandler.postDelayed(idleRunnable, IDLE_TIME);
    }

    
     
    private void registerListeners() {
        mSensorManager.registerListener(doodleSurfaceView, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    
    private void unregisterListeners() {
        mSensorManager.unregisterListener(doodleSurfaceView);
    }

    
    private void goAttractMode() {
        Log.i(TAG, "Switching to attract mode!");
        setUiState(UiState.ATTRACT);
        switchViews();
    }

    
    private void hideSystemUI() {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    
    private void setUiState(UiState uiState) {
        this.currentUiState = uiState;
        Log.i(TAG, "UI State changed, new = " + uiState);
    }

    
    private void switchViews() {
        if (currentUiState != UiState.MAIN_MENU)
            idleHandler.removeCallbacks(idleRunnable);

        String tag;

        Bundle arguments = new Bundle();
        Fragment newFragment;

        switch (currentUiState) {
            case GAME: {
                newFragment = new GameOverlayFragment();
                tag = GameOverlayFragment.TAG;
                break;
            }
            case GAME_OVER: {
                newFragment = new GameOverFragment();
                arguments.putSerializable(GameOverFragment.ARG_FINAL_SCORE, currentScore);
                tag = GameOverFragment.TAG;
                break;
            }
            case SCOREBOARD: {
                newFragment = new ScoreboardFragment();
                tag = ScoreboardFragment.TAG;
                break;
            }
            case ATTRACT: {
                newFragment = new AttractFragment();
                tag = AttractFragment.TAG;
                break;
            }
            default:
            case MAIN_MENU: {
                newFragment = new MainMenuFragment();
                tag = MainMenuFragment.TAG;
                break;
            }
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        newFragment.setArguments(arguments);
        transaction.replace(R.id.activity_main, newFragment, tag);
        transaction.commit();

        currentFragment = newFragment;
    }

    @Override
    public void gameOver(final int score) {
   
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScore.setScore(score);

                unregisterListeners();
                setUiState(UiState.GAME_OVER);
                switchViews();
            }
        });
    }

    @Override
    public void scoreChanged(final int newScore) {
        
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScore.setScore(newScore);
                
                if (currentFragment instanceof GameOverlayFragment)
                    ((GameOverlayFragment) currentFragment).setScore(newScore);
            }
        });
    }

    @Override
    public void updateTimer(final long timeLeft) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentFragment instanceof GameOverlayFragment)
                    ((GameOverlayFragment) currentFragment).setRemainingTime(timeLeft);
            }
        });
    }

    @Override
    public void onMainMenu() {
        setUiState(UiState.MAIN_MENU);
        switchViews();

        scheduleIdleCallback();
    }

    @Override
    public void onStartGame() {
        setUiState(UiState.GAME);

        registerListeners();

        switchViews();

        doodleSurfaceView.startGame(this);
    }

    @Override
    public void onStopGame() {
        unregisterListeners();
        doodleSurfaceView.stopGame();
    }

    @Override
    public void onShowScoreboard() {
        setUiState(UiState.SCOREBOARD);
        switchViews();
    }
}