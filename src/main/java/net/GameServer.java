package net;

import javafx.scene.control.MenuItem;

import java.io.*;
import java.net.*;


public class GameServer extends Thread {

    public static final int port = 6200;
    public static final int localPort = 6201;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private BufferedReader input;
    private BufferedOutputStream output;
    private boolean stopped = false;
    private MenuManager manager;
    private boolean isOver;

    public GameServer(MenuManager menuManager) {
        this.manager = menuManager;
    }

    @Override
    public void run() {
        create();
        if(clientSocket!= null){
            startComunication();
        }
    }

    private void create(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server ip -> " + serverSocket.getInetAddress());
            waitForClient();
            if(clientSocket != null) {
                manager.connected(clientSocket.getInetAddress(), clientSocket.getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startComunication() {
        try {
            while (stopped){
                System.out.println("Waiting->...");
                String message =input.readLine();
                System.out.println("Received ->" +message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForClient() {
        System.out.println("Server in ascolto..");
        try {
            clientSocket = serverSocket.accept();
            System.out.println("client>"+clientSocket);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new BufferedOutputStream(new DataOutputStream(clientSocket.getOutputStream()));

        } catch (Exception e) {
            System.out.println("Server stopped");
        }

    }

    public void stopHost() {
        this.stopped = true;
        try {
            serverSocket.close();
            if(clientSocket!=null){
                clientSocket.close();
            }
            if(input != null){
                input.close();
            }
            if(output != null) {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
