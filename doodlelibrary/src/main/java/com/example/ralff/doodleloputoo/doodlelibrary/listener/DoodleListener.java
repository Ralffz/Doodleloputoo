package com.example.ralff.doodleloputoo.doodlelibrary.listener;



public interface DoodleListener {
    
    void gameOver(int score);

   
    void scoreChanged(int newScore);
    void updateTimer(long timeLeft);
}
