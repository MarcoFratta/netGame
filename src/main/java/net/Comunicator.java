package net;


import packets.Packet;

import java.util.function.Consumer;

public interface Comunicator {

    void send(Object b);

    Object receive() ;

}
