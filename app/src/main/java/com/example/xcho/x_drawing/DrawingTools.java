package com.example.xcho.x_drawing;

import android.graphics.Path;

public class DrawingTools extends Path {

    private int brushColor;
    private int brushSize;


    public int getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(int brushColor) {
        this.brushColor = brushColor;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public DrawingTools(int brushColor, int brushSize) {
        this.brushColor = brushColor;
        this.brushSize = brushSize;
    }
}
