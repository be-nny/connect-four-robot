package com.feed;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FilteredCam extends VideoFeed implements ChangeListener, MouseListener {

    //sliders
    private JPanel sliders;
    private JPanel minSliders;
    private JPanel maxSliders;

    private static final int MIN_HUE = 0;
    private static final int MAX_HUE = 180;
    private static final int MIN_VAL = 0;
    private static final int MAX_VAL = 255;
    private static final int MIN_SAT = 0;
    private static final int MAX_SAT = 255;

    private JSlider minHueSlider;
    private JSlider minSatSlider;
    private JSlider minValSlider;

    private JSlider maxHueSlider;
    private JSlider maxSatSlider;
    private JSlider maxValSlider;

    private Scalar lowerRange;
    private Scalar upper_range;

    private JLabel minValLabel = new JLabel("MIN HSV");
    private JLabel maxValLabel = new JLabel("MAX HSV");

    //main panel
    private JPanel mainPanel;
    private static final Color bckColor = new Color(230,230,230);

    //deteted points
    private List<Point> centers;

    public FilteredCam(String text){
        this.mainPanel = new JPanel();
        this.mainPanel.setBackground(bckColor);

        GridBagConstraints c = new GridBagConstraints();
        this.mainPanel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        this.webcamPanel.add(new JLabel(text), c);
        setSliders();

        c.gridx = 0;
        c.gridy = 1;
        this.mainPanel.add(this.webcamPanel, c);

        c.gridx = 0;
        c.gridy = 2;
        this.mainPanel.add(this.sliders, c);

        switch (text){
            case "Yellow Filter":
                this.minHueSlider.setValue(57);
                this.minSatSlider.setValue(110);
                this.minValSlider.setValue(225);
                this.maxHueSlider.setValue(91);
                this.maxSatSlider.setValue(213);
                this.maxValSlider.setValue(255);
                break;
            case "Red Filter":
                this.minHueSlider.setValue(106);
                this.minSatSlider.setValue(127);
                this.minValSlider.setValue(203);
                this.maxHueSlider.setValue(114);
                this.maxSatSlider.setValue(231);
                this.maxValSlider.setValue(255);
        }
    }

    private void setSliders(){
        this.sliders = new JPanel(new FlowLayout());
        this.sliders.setBackground(bckColor);

        this.minSliders = new JPanel();
        this.minSliders.setBackground(bckColor);
        this.minSliders.setLayout(new BoxLayout(this.minSliders, BoxLayout.Y_AXIS));
        this.maxSliders = new JPanel();
        this.maxSliders.setBackground(bckColor);
        this.maxSliders.setLayout(new BoxLayout(this.maxSliders, BoxLayout.Y_AXIS));

        //labels
        Hashtable sat_vat_table = new Hashtable();
        sat_vat_table.put(MAX_VAL, new JLabel(String.valueOf(MAX_VAL)));
        sat_vat_table.put(MIN_VAL, new JLabel(String.valueOf(MIN_VAL)));

        Hashtable hue_table = new Hashtable();
        hue_table.put(MAX_HUE, new JLabel(String.valueOf(MAX_HUE)));
        hue_table.put(MIN_HUE, new JLabel(String.valueOf(MIN_HUE)));

        //setting sliders
        this.minHueSlider = new JSlider(JSlider.HORIZONTAL, MIN_HUE, MAX_HUE, 1);
        this.minHueSlider.addChangeListener(this);
        this.minHueSlider.setLabelTable(hue_table);
        this.minHueSlider.setPaintLabels(true);

        this.minSatSlider = new JSlider(JSlider.HORIZONTAL, MIN_SAT, MAX_SAT, 1);
        this.minSatSlider.addChangeListener(this);
        this.minSatSlider.setLabelTable(sat_vat_table);
        this.minSatSlider.setPaintLabels(true);

        this.minValSlider = new JSlider(JSlider.HORIZONTAL, MIN_VAL, MAX_VAL, 1);
        this.minValSlider.addChangeListener(this);
        this.minValSlider.setLabelTable(sat_vat_table);
        this.minValSlider.setPaintLabels(true);

        this.minSliders.add(this.minValLabel);
        this.minSliders.add(this.minHueSlider);
        this.minSliders.add(this.minSatSlider);
        this.minSliders.add(this.minValSlider);

        //setting sliders
        this.maxHueSlider = new JSlider(JSlider.HORIZONTAL, MIN_HUE, MAX_HUE, 1);
        this.maxHueSlider.addChangeListener(this);
        this.maxHueSlider.setLabelTable(hue_table);
        this.maxHueSlider.setPaintLabels(true);

        this.maxSatSlider = new JSlider(JSlider.HORIZONTAL, MIN_SAT, MAX_SAT, 1);
        this.maxSatSlider.addChangeListener(this);
        this.maxSatSlider.setLabelTable(sat_vat_table);
        this.maxSatSlider.setPaintLabels(true);

        this.maxValSlider = new JSlider(JSlider.HORIZONTAL, MIN_VAL, MAX_VAL, 1);
        this.maxValSlider.addChangeListener(this);
        this.maxValSlider.setLabelTable(sat_vat_table);
        this.maxValSlider.setPaintLabels(true);

        this.maxSliders.add(this.maxValLabel);
        this.maxSliders.add(this.maxHueSlider);
        this.maxSliders.add(this.maxSatSlider);
        this.maxSliders.add(this.maxValSlider);

        //values
        this.lowerRange = new Scalar(this.minHueSlider.getValue(), this.minSatSlider.getValue(), this.minValSlider.getValue());
        this.upper_range = new Scalar(this.maxHueSlider.getValue(), this.maxSatSlider.getValue(), this.maxValSlider.getValue());

        this.minValLabel.setText("MIN HSV " + "( " + this.lowerRange.val[0] + ", " + this.lowerRange.val[1] + ", " + this.lowerRange.val[2] + " )");
        this.maxValLabel.setText("MAX HSV " + "( " + this.upper_range.val[0] + ", " + this.upper_range.val[1] + ", " + this.upper_range.val[2] + " )");

        this.sliders.add(this.minSliders);
        this.sliders.add(this.maxSliders);
    }

    public void update(){
        Mat mat = this.getVideoMat();
        Mat blur = new Mat();
        Mat hsv = new Mat();
        Mat mask = new Mat();

        //rendering the hsv ranges
        Imgproc.blur(mat, blur, new Size(7,7));
        Imgproc.cvtColor(blur, hsv, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsv, this.lowerRange, this.upper_range, mask);

        //creating mat erode and dilate elements and applying them
        Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(14, 14));
        Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7,7));
        Imgproc.erode(mask, mask, erode);
        Imgproc.dilate(mask, mask, dilate);

        //finding contours
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        if (hierarchy.size().width > 0 && hierarchy.size().height > 0){
            for(int i = 0; i >=0; i = (int) hierarchy.get(0, i)[0]){
                Imgproc.drawContours(mat, contours, i, new Scalar(255,0,0), 1);
            }
        }

        //getting moments
        List<Moments> moments = new ArrayList<>(contours.size());
        for(int i = 0; i <contours.size(); i++){
            moments.add(Imgproc.moments(contours.get(i)));
        }

        //getting the centers using moments formula
        //to get an average center
        this.centers = new ArrayList<>(contours.size());

        for(int i = 0; i < contours.size(); i ++) {
            if(i > 21){
                break;
            }
            this.centers.add(new Point((int) (moments.get(i).m10 / (moments.get(i).m00 + 1e-5)), (int) (moments.get(i).m01 / (moments.get(i).m00 + 1e-5))));
            //drawing the points
            Imgproc.circle(mat, new org.opencv.core.Point(this.centers.get(i).x, this.centers.get(i).y), 10, new Scalar(0, 255, 0), 3, 1);
        }

        this.setImageFrame(mat);
    }

    public ArrayList getPoints(){
        return (ArrayList) this.centers;
    }

    public JPanel getPanel(){
        return this.mainPanel;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.lowerRange = new Scalar(this.minHueSlider.getValue(), this.minSatSlider.getValue(), this.minValSlider.getValue());
        this.upper_range = new Scalar(this.maxHueSlider.getValue(), this.maxSatSlider.getValue(), this.maxValSlider.getValue());

        this.minValLabel.setText("MIN HSV " + "( " + this.lowerRange.val[0] + ", " + this.lowerRange.val[1] + ", " + this.lowerRange.val[2] + " )");
        this.maxValLabel.setText("MAX HSV " + "( " + this.upper_range.val[0] + ", " + this.upper_range.val[1] + ", " + this.upper_range.val[2] + " )");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
