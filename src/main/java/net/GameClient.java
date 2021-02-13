package net;


import packets.GameInfoPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient extends Thread{

    private static final int port = 5005;
    private final InetAddress address;

    private Socket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final ClientManager manager;

    public GameClient(InetAddress address, ClientManager menuManager){
        this.address = address;
        this.manager = menuManager;
    }

    @Override
    public void run() {
        try {
            this.connect();
        } catch (IOException e) {
            System.out.println("Connessione fallita");
        }
        this.waitForStart();
    }

    private void waitForStart() {
        try {
            GameInfoPacket s = (GameInfoPacket) this.input.readObject();
            this.manager.startMatch(s, this.input, this.output);

            System.out.println("message received ->"+s.getDeckType());
        } catch (IOException e) {
            System.out.println("Client connection error");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
    }


    private void connect() throws IOException {

        this.serverSocket = new Socket(this.address,port);
        this.output = new ObjectOutputStream(this.serverSocket.getOutputStream());
        this.input = new ObjectInputStream(this.serverSocket.getInputStream());
            System.out.println("Connessione riuscita");

    }
}
