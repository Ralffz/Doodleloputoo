package com.example.ralff.doodleloputoo.doodlelibrary.logic;



public class DifficultyHandler {

    
    private static final int MIN_DIFFERENCE_EASY = 20;

    
    private static final int MAX_DIFFERENCE_EASY = 20;

    
    private static final int PLATFORM_WIDTH_EASY = 10;

    
    private static final int MIN_DIFFERENCE_NORMAL = 30;

    
    private static final int MAX_DIFFERENCE_NORMAL = 15;

    
    private static final int PLATFORM_WIDTH_NORMAL = 5;

    
    private static final int MIN_DIFFERENCE_HARD = 15;

    
    private static final int MAX_DIFFERENCE_HARD = 30;

    
    private static final int PLATFORM_WIDTH_HARD = 10;

    
    private static final int MIN_DIFFERENCE_VERY_HARD = 20;

    
    private static final int MAX_DIFFERENCE_VERY_HARD = 15;

    
    private static final int PLATFORM_DIFFERENCE_VERY_HARD = 5;

    
    private static final int LOWEST_COUNT_DOWN_IN_SECONDS = 4;

    private float initialPlatformWidth;

    
    private float initialMinDifference;

    
    private float initialMaxDifference;

    
    private int initialCountDownTimerInS;

    
    private Difficulty lastDifficulty;

    public void setInitialCountDownTimerInS(int initialCountDownTimerInS) {
        this.initialCountDownTimerInS = initialCountDownTimerInS;
    }

    
    public DifficultyHandler(float initialPlatformWidth, float initialMinDifference, float initialMaxDifference, int initialCountDownTimerInS) {
        this.initialPlatformWidth = initialPlatformWidth;
        this.initialMinDifference = initialMinDifference;
        this.initialMaxDifference = initialMaxDifference;
        this.initialCountDownTimerInS = initialCountDownTimerInS;
        lastDifficulty = Difficulty.VERY_EASY;
    }

    public void setNewValues(float YAS){
        YAS *= -1;
        lastDifficulty = Difficulty.getDifficulty(YAS);

        switch (lastDifficulty){
            case EASY:
                initialMinDifference += MIN_DIFFERENCE_EASY;
                initialMaxDifference += MAX_DIFFERENCE_EASY;
                initialPlatformWidth -= PLATFORM_WIDTH_EASY;
                break;
            case NORMAL:
                initialMinDifference += MIN_DIFFERENCE_NORMAL;
                initialMaxDifference += MAX_DIFFERENCE_NORMAL;
                initialPlatformWidth -= PLATFORM_WIDTH_NORMAL;
                break;
            case HARD:
                initialMinDifference += MIN_DIFFERENCE_HARD;
                initialMaxDifference += MAX_DIFFERENCE_HARD;
                initialPlatformWidth -= PLATFORM_WIDTH_HARD;
                break;
            
            default:
            case VERY_HARD:
                initialMinDifference += MIN_DIFFERENCE_VERY_HARD;
                initialMaxDifference += MAX_DIFFERENCE_VERY_HARD;
                initialPlatformWidth -= PLATFORM_DIFFERENCE_VERY_HARD;
                break;
        }
        
        if (!lastDifficulty.equals(Difficulty.VERY_EASY) && getInitialCountDownTimerInS() >= LOWEST_COUNT_DOWN_IN_SECONDS){
            setCountDownTimer(getInitialCountDownTimerInS() -1);
        }
    }

    public boolean needNewValues(float YAS){
        boolean needNewValues = false;

        
        YAS *= -1;

        if (lastDifficulty.getValue() <= YAS){
            needNewValues = true;
        }

        return needNewValues;
    }

    public float getPlatformWidth() {
        return initialPlatformWidth;
    }

    private void setPlatformWidth(int initialPlatformWidth) {
        this.initialPlatformWidth = initialPlatformWidth;
    }

    public float getMinDifference() {
        return initialMinDifference;
    }

    private void setMinDifference(int initialMinDifference) {
        this.initialMinDifference = initialMinDifference;
    }

    public float getMaxDifference() {
        return initialMaxDifference;
    }

    private void setInitialMaxDifference(int initialMaxDifference) {
        this.initialMaxDifference = initialMaxDifference;
    }

    public int getInitialCountDownTimerInS() {
        return initialCountDownTimerInS;
    }

    private void setCountDownTimer(int initialCountDownTimerInS) {
        this.initialCountDownTimerInS = initialCountDownTimerInS;
    }
}
