package com.company;

import com.logic.ArduinoWriter;
import com.logic.PointProcessor;
import org.opencv.core.Core;
import com.pulsford.connect4.ai.*;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Window window;
    private static boolean isRunning = false;
    private static ThreadPoolExecutor executor;

    private static Board board;
    private static Game game;
    private static volatile PointProcessor pointProcessor;
    private static ArduinoWriter arduinoWriter;

    private static int look_ahead = 7;

    private static int cols = 7;
    private static int rows = 6;
    private static int next_col;

    private static String prev = "";
    private static boolean pausePlay = false;
    private static String arduinoOutput = "";
    private static String prevArduinoOutput = "";

    public static void main(String[] args) {
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.submit(() -> {
            arduinoWriter = new ArduinoWriter();
            try {
                window = new Window();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            pointProcessor = new PointProcessor(cols,rows);
            board = new Board(pointProcessor.getGrid());
            game = new Game();

            isRunning = true;

            while (isRunning){
                //updating streams
                Window.standardCam.update();
                Window.yellowVideoFilter.update();
                Window.redVideoFilter.update();

                //writing to grid
                updateGrid();
            }
        });
    }

    private synchronized static void updateGrid(){
        //updating the board array
        pointProcessor.update(Window.yellowVideoFilter.getPoints(), Window.redVideoFilter.getPoints(), Window.standardCam.getRoi_width(), Window.standardCam.getRoi_height(), Window.standardCam.getRoi());
        String out = displayBoard();

        //processing the next move
        window.setBoardText(out);
        if(out.equals(prev) && Window.standardCam.isRun() && pointProcessor.isRobotTurn() && !pausePlay){
            pausePlay = true;
            //processing the next move
            processNextMove();

            /*
            pause play is used to stop the robot from deciding a play when it's already moving.
            this means when this else if runs, it is the players turn.
            */
        } else if(!pointProcessor.isRobotTurn()){
            pausePlay = false;
        }
        prev = out;
    }
    private static String displayBoard(){
        //printing in console
        String out = "";
        for(char[] line: pointProcessor.getGrid()) {
            for (char ch : line) {
                out += String.valueOf(ch);
                out += "  ";
            }
            out += "\n";
        }
        return out;
    }
    private static void processNextMove(){
        System.out.println(displayBoard());
        board.setBoardArray(pointProcessor.getGrid());
        next_col = game.makeDecision(board, look_ahead);
        System.out.println(next_col);
        arduinoWriter.write(next_col);
    }
}