package com.example.ralff.doodleloputoo.doodlelibrary.logic;


public enum Difficulty {
    VERY_EASY(20000),
    EASY(40000),
    NORMAL(60000),
    HARD(80000),
    VERY_HARD(100000);


    private final int difficultLevel;

    Difficulty(int difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

    public int getValue() {
        return difficultLevel;
    }

    
    public static Difficulty getDifficulty(float YAS) {
        Difficulty difficulty;

        if (YAS <= VERY_EASY.getValue()) {
            difficulty = VERY_EASY;
        } else if (YAS >= VERY_EASY.getValue() && YAS <= EASY.getValue()) {
            difficulty = EASY;
        } else if (YAS >= EASY.getValue() && YAS <= NORMAL.getValue()) {
            difficulty = NORMAL;
        } else if (YAS >= NORMAL.getValue() && YAS <= HARD.getValue()) {
            difficulty = HARD;
        } else if (YAS >= HARD.getValue() && YAS <= VERY_HARD.getValue()) {
            difficulty = VERY_HARD;
        } else {
            difficulty = VERY_HARD;
        }

        return difficulty;
    }

}
