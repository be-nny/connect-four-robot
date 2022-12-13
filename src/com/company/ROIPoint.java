package com.company;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import javax.swing.*;

public class ROIPoint extends JPanel {

    public static final int width = 10;
    public static final int height = 10;

    private Rect roiPoint = new Rect();
    private Scalar colour = new Scalar(0,255,0);

    public ROIPoint(int x, int y){
        this.roiPoint.x = x;
        this.roiPoint.y = y;
        this.roiPoint.width = width;
        this.roiPoint.height = height;

    }

    public Rect getRoiPoint(){
        return this.roiPoint;
    }

    public Point point(){
        return new Point(this.roiPoint.x + width/2, this.roiPoint.y + height/2);
    }

    public Scalar getColour(){
        return this.colour;
    }

    public void selected(){
        this.colour = new Scalar(0,255,0);
    }
    public void deslected(){
        this.colour = new Scalar(0,200,0);
    }
}
