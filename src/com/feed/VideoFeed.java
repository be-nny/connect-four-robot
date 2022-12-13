package com.feed;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public abstract class VideoFeed extends JPanel {

    public static int scale = 3;
    public static int CAM_WIDTH = 1920/scale;
    public static int CAM_HEIGHT = 1080/scale;
    public static Dimension videoFeedDimens = new Dimension(CAM_WIDTH, CAM_HEIGHT);

    public JPanel webcamPanel;
    public JLabel webcam;

    protected VideoCapture videoCapture;
    protected Mat mat;

    public VideoFeed(){
        //setting up the video input
        this.videoCapture = new VideoCapture(0);
        this.mat = new Mat();

        //webcam panel
        this.webcamPanel = new JPanel();
        this.webcamPanel.setBackground(new Color(220,220,220));
        this.webcamPanel.setLayout(new BoxLayout(this.webcamPanel, BoxLayout.Y_AXIS));
        this.webcam = new JLabel();
        this.webcamPanel.add(this.webcam);
    }

    public Mat getVideoMat(){
        this.videoCapture.read(this.mat);
        Imgproc.resize(this.mat, this.mat, new Size(videoFeedDimens.width, videoFeedDimens.height));
        return this.mat;
    }

    public void setImageFrame(Mat frame){
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, matOfByte);

        byte[] bytes = matOfByte.toArray();
        BufferedImage bufferedImage;
        //displaying the video stream.
        try {
            InputStream in = new ByteArrayInputStream(bytes);
            bufferedImage = ImageIO.read(in);
            ImageIcon icon = new ImageIcon(bufferedImage);
            this.webcam.setIcon(icon);
            this.webcamPanel.revalidate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}