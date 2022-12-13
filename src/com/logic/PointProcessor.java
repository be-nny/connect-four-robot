package com.logic;

import com.pulsford.connect4.ai.Board;
import org.opencv.core.Rect;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class PointProcessor {

    private int roi_width;
    private int roi_height;

    private int num_cols;
    private int num_rows;
    private char[][] grid;


    /**
     * @apiNote Updates 2d grid array which can be called
     * @param cols number of connect4 grid columns
     * @param rows number of connect4 grid rows
     * */
    public PointProcessor(int cols, int rows) {
        //setting the grid
        this.num_cols = cols;
        this.num_rows = rows;
        this.grid = setGrid();
    }

    /**
     * @param width width of the ROI
     * @param height height of the ROI
     * @param red red token points
     * @param yellow yellow token points
     * */
    public void update(ArrayList<Point> yellow, ArrayList<Point> red, int width, int height, Rect roi) {
        this.roi_width = width;
        this.roi_height = height;

        this.grid = setGrid();
        writeToGrid(yellow, 'Y', roi);
        writeToGrid(red, 'R', roi);

    }

    /**
     * @return returns 2d array full of '0'
     * */
    private char[][] setGrid(){
        char[][] grid = new char[this.num_rows][this.num_cols];
        for(int row = 0; row < this.num_rows; row ++){
            for(int col = 0; col < this.num_cols; col ++){
                //y (row) then x (col) coord
                grid[row][col] = '0';
            }
        }
        return grid;
    }

    private void writeToGrid(ArrayList<Point> tokens, char colour, Rect roi){
        for(Point point: tokens){
            if(roi.contains(new org.opencv.core.Point(point.x, point.y))){
                int x = point.x - roi.x;
                int y = point.y - roi.y;
                int col = 0;
                int row = 0;

                int col_width = this.roi_width/this.num_cols;
                int row_width = this.roi_height/this.num_rows;
                for(int c = 0; c < this.num_cols; c ++){
                    if((col_width + (col_width*c)) >= x){
                        col = c;
                        break;
                    }
                }

                for(int r = 0; r < this.num_rows; r ++){
                    if((row_width + (row_width*r)) >= y){
                        row = r;
                        break;
                    }
                }
                this.grid[row][col] = colour;
            }
        }
    }

    /**
     * @return the 2d array for the grid
     * */
    public char[][] getGrid(){
        return this.grid;
    }

    public boolean isRobotTurn(){
        int red = 0;
        int yel = 0;
        for(int row = 0; row < this.num_rows; row ++){
            for(int col = 0; col < this.num_cols; col ++){
                //y (row) then x (col) coord
                if (grid[row][col] == 'R') {
                    red += 1;
                } else if(grid[row][col] == 'Y'){
                    yel += 1;
                }
            }
        }

        if(red > yel){
            return true;
        } else{
            return  false;
        }
    }
}
