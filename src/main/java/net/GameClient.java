package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameClient extends Thread{

    private final int port;
    private final InetAddress address;

    private Socket serverSocket;
    private DataInputStream input;
    private DataOutputStream output;

    private MenuManager manager;

    public GameClient(InetAddress address, int port, MenuManager menuManager){
        this.address = address;
        this.port = port;
        this.manager = menuManager;
    }

    @Override
    public void run() {
        connect();
        manager.connected(serverSocket.getInetAddress(),serverSocket.getPort());
        sendMessage();
    }

    private void sendMessage() {
        String message = "ciao \n";
        try {
            System.out.println("Sending -> "+ message);
           // output.writeBytes(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            serverSocket = new Socket(address,port);
            input = new DataInputStream(serverSocket.getInputStream());
            output = new DataOutputStream(serverSocket.getOutputStream());
            System.out.println("Connessione riuscita");
        } catch (IOException e) {
            System.out.println("Unknown host");
            e.printStackTrace();
        }

    }
}
