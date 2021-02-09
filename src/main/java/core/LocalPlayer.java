package core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Objects;

public class LocalPlayer implements Player{
    private final String name;
    private final InetAddress address;
    private final int port;

    public LocalPlayer(String name , InetAddress address, int port){
        this.address = address;
        this.name = name;
        this.port = port;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Socket getSocket() throws IOException {
        return new Socket(address,port);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, address, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalPlayer that = (LocalPlayer) o;
        return port == that.port && name.equals(that.name) && address.equals(that.address);
    }

    @Override
    public String toString() {
        return "LocalPlayer{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", port=" + port +
                '}';
    }
}
