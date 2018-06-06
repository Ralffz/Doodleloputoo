package com.example.ralff.doodleloputoo.doodlelibrary.listener;



public interface ScreenListener {
    
    void screenTouched(float xPosition, float yPosition);

    
    void screenSizeChanged(int width, int height);

    
    void rotationChanged(float newRotation);
}
