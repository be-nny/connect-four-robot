package com.feed;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.event.*;

public class StandardCam extends VideoFeed implements MouseMotionListener, MouseListener, ActionListener {

    //roi
    private int roi_width;
    private int roi_height;
    private int start_x = 0;
    private int start_y = 0;
    private int end_x = CAM_WIDTH;
    private int end_y = CAM_HEIGHT;

    private Rect roi;

    //reset btm
    private JButton resetBtn = new JButton("Reset Region of Interest");
    private JButton runBtn = new JButton("Start/Stop");

    private boolean isRun;

    public StandardCam(){
        //reset btn
        this.resetBtn.addActionListener(this);
        this.resetBtn.setActionCommand("reset");

        this.runBtn.addActionListener(this);
        this.runBtn.setActionCommand("run");
        this.runBtn.setText("Start");
        this.isRun = false;

        //webcam stream
        this.webcamPanel.add(new JLabel("Standard View"));
        this.webcamPanel.add(resetBtn);
        this.webcamPanel.add(runBtn);
        this.webcamPanel.addMouseListener(this);

        //creating region of interest box
        this.roi = new Rect();
        setROI();
        //bounding box
    }

    public void update(){
        this.setImageFrame(getVideoMat());
    }

    @Override
    public Mat getVideoMat(){
        this.videoCapture.read(this.mat);
        Imgproc.resize(this.mat, this.mat, new Size(CAM_WIDTH, CAM_HEIGHT));
        drawROI();
        return this.mat;
    }

    private void drawROI(){
        Imgproc.rectangle(this.mat, roi, new Scalar(0,255,0), 3);
    }

    public void setROI(){
        roi_width = Math.abs(end_x - start_x);
        roi_height = Math.abs(end_y - start_y);
        if((end_x - start_x) < 0 || (end_y - start_y) < 0) {
            this.roi.x = end_x;
            this.roi.y = end_y;
        } else{
            this.roi.x = start_x;
            this.roi.y = start_y;
        }

        this.roi.width = roi_width;
        this.roi.height = roi_height;
        drawROI();
    }

    public void resetROI(){
        start_x = 0;
        start_y = 0;
        end_x = CAM_WIDTH;
        end_y = CAM_HEIGHT;
        setROI();
    }

    public Rect getRoi(){
        return this.roi;
    }

    public int getRoi_width(){
        return this.roi_width;
    }

    public int getRoi_height(){
        return this.roi_height;
    }

    public JPanel getWebcamPanel(){
        return this.webcamPanel;
    }

    public boolean isRun(){
        return this.isRun;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        start_x = e.getX();
        start_y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        end_x = e.getX();
        end_y = e.getY();
        setROI();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(String.valueOf(e.getActionCommand()).equals("reset")){
            resetROI();
        }

        if(String.valueOf(e.getActionCommand()).equals("run")){
            if(this.isRun){
                this.isRun = false;
                this.runBtn.setText("Start");
            } else {
                this.isRun = true;
                this.runBtn.setText("Stop");
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        end_x = e.getX();
        end_y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        end_x = e.getX();
        end_y = e.getY();
        setROI();
    }
}