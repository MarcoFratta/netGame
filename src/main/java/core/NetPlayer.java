package core;

import net.Comunicator;

import java.net.Socket;

public class NetPlayer extends LocalPlayer{
    private static final long serialVersionUID = 5506535315042162996L;
    private final Socket socket;
    private final Comunicator comunicator;

    public NetPlayer(int id,String name, Socket socket, Comunicator comunicator) {
        super(id,name);
        this.socket = socket;
        this.comunicator = comunicator;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Comunicator getComunicator() {
        return this.comunicator;
    }

    @Override
    public String toString() {
        /*return "LocalPlayer{" +
                "id="+getId()+
                "name='" + getName() + '\'' +
                ", address=" + socket.getInetAddress() +
                ", port=" + socket.getPort() +
                '}';

         */
        return super.toString();
    }
}
