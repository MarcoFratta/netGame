package net;

import core.Pair;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class GameServer extends Thread {

    public static final int port = 5005;

    private  ServerSocket serverSocket;
    private final Map<Socket, Pair<ObjectInputStream,ObjectOutputStream>> clientSockets;
    private boolean stopped = false;
    private final HostManager manager;
    private final int clientsNumber;

    public GameServer(HostManager manager,int clientsNumber) {
        this.manager = manager;
        this.clientsNumber = clientsNumber;
        this.clientSockets = new HashMap<>();
    }

    @Override
    public void run() {
        create();
        waitForClient();
        if(clientSockets.size() == clientsNumber){
            manager.startMatch(Map.copyOf(this.clientSockets));
        }
    }

    private void create(){
        try {
            serverSocket = new ServerSocket(port);
           // serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),port));
            System.out.println("Server created -> " + serverSocket.getInetAddress()+" "+serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForClient() {
        System.out.println("Server in ascolto..");
        while (this.clientSockets.size() < this.clientsNumber && !stopped) {
            try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("client connected ["+clientSockets.size()+"]" + clientSocket);
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    ObjectInputStream i = new ObjectInputStream(inputStream);
                    ObjectOutputStream o = new ObjectOutputStream(outputStream);
                    clientSockets.put(clientSocket,new Pair<>(i,o));
                    this.manager.playerConnected(clientSockets.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Client non connected");
            }
        }
    }

    public void stopHost() {
        this.stopped = true;
        try {
            serverSocket.close();
                clientSockets.forEach((k,v)-> {
                    try {
                        k.close();
                        v.getY().close();
                        v.getY().close();
                    } catch (IOException e) {
                        System.out.println("Cant close connection");
                    }
                });
            } catch (IOException ioException) {
                System.out.println("Cant stop the server");
            }
    }
}
