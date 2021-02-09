package core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public interface Player {

    String getName();
    Socket getSocket() throws IOException;
}
