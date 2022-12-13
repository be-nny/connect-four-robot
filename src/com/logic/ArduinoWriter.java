package com.logic;

import com.fazecast.jSerialComm.*;
import java.util.Scanner;

public class ArduinoWriter {
    private static final int baud_rate = 9600; //baud rate, more like im baud at this rate
    private static SerialPort port;

    public ArduinoWriter(){
        Scanner scanner = new Scanner(System.in);
        SerialPort comPorts[] = SerialPort.getCommPorts();
        System.out.println("COM port list: ");
        for(int i = 0; i < comPorts.length; i ++){
            System.out.println("\t > [" + i  + "]" + comPorts[i].getDescriptivePortName());
        }
        System.out.println("Select Comm Port: [Enter for Debug Run]");
        if(!scanner.nextLine().isEmpty()){
            port = comPorts[scanner.nextInt()];
            port.openPort();
            port.setBaudRate(baud_rate);
        }
    }
    
    public void write(int col){
        try{
            //writing
            byte[] writeBuffer = String.valueOf(col).getBytes();
            port.writeBytes(writeBuffer, writeBuffer.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read(){
        String out = "";
        if(port.bytesAvailable() > 0){
            byte[] readBuffer = new byte[port.bytesAvailable()];
            for(int i = 0; i < readBuffer.length; i ++){
                out +=String.valueOf(readBuffer[i]);
            }
        }
        return out.toString();
    }
}
