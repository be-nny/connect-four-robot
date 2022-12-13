package com.company;

import com.feed.FilteredCam;
import com.feed.StandardCam;

import javax.swing.*;
import java.awt.*;

public class Window {
    //window
    public static JFrame frame = new JFrame("Connect Four v2");
    public static Dimension windowDimens = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

    //webcam
    private static JTabbedPane webcamTabs;

    //panels
    private static JPanel mainPanel = new JPanel();
    private static JPanel filterPanel = new JPanel();
    private static JPanel windowPanel = new JPanel();
    private static JPanel botPanel = new JPanel();
    private static JPanel botTextPanel = new JPanel();

    //bot panel
    private static JTextArea botOutPut = new JTextArea();

    //cams
    public static FilteredCam yellowVideoFilter;
    public static FilteredCam redVideoFilter;

    public static StandardCam standardCam = new StandardCam();

    public Window() throws AWTException {
        yellowVideoFilter = new FilteredCam("Yellow Filter");
        redVideoFilter = new FilteredCam("Red Filter");

        //setting up the frame
        frame.setSize(windowDimens);
        frame.setPreferredSize(windowDimens);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBotTextPanel();

        //adding the webcam sources
        filterPanel.add(yellowVideoFilter.getPanel());
        mainPanel.add(standardCam.getWebcamPanel());
        filterPanel.add(redVideoFilter.getPanel());

        //setting grid-bag
        windowPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //webcam tab
        c.ipadx = 5;
        c.ipady = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        windowPanel.add(createWebcamTab(), c);

        //bot panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 5;
        c.ipady = 5;
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 2;
        windowPanel.add(createBotPanel(), c);

        frame.add(windowPanel);
        frame.pack();
    }

    private static void setBotTextPanel(){
        botTextPanel.setLayout(new BoxLayout(botTextPanel, BoxLayout.Y_AXIS));
        botTextPanel.setVisible(true);
        botOutPut.setVisible(true);
        botOutPut.setEditable(false);
    }

    private static JPanel createBotPanel(){
        botPanel.setBackground(new Color(220,220,220));
        botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.Y_AXIS));
        botPanel.add(Box.createRigidArea(new Dimension(0,5)));
        botPanel.add(new JLabel("Board Text: "));
        botPanel.add(Box.createRigidArea(new Dimension(0,5)));
        botPanel.add(botOutPut);
        return botPanel;
    }

    public void setBoardText(String text){
        botOutPut.setText(text);
    }

    private static JTabbedPane createWebcamTab(){
        //adding the displays
        webcamTabs = new JTabbedPane();
        webcamTabs.add("Main", mainPanel);
        webcamTabs.add("Filters", filterPanel);

        return webcamTabs;
    }
}
