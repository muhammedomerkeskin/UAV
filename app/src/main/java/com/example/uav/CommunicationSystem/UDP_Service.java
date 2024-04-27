package com.example.uav.CommunicationSystem;

import android.util.Log;

import com.example.uav.MainActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDP_Service extends Thread {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private final MainActivity activity;

    public UDP_Service(MainActivity activity) {
        this.activity = activity;
        try {
            this.serverAddress = InetAddress.getByName("127.0.0.1"); // Hedef sunucunun IP adresini buraya yazın
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(5801);
            byte[] receiveData = new byte[64];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                byte[] receivedData = receivePacket.getData();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.DisplayData(receivedData);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
    public void sendData(byte[] sendData) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress serverAddress = null;
                try {
                    serverAddress = InetAddress.getByName("192.168.1.116");
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                DatagramPacket sendingArray = new DatagramPacket(sendData, sendData.length, serverAddress, 2601);
                try {
                    socket.send(sendingArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}


