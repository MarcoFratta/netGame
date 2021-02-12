package net;

import core.Pair;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public interface Manager {
    void action(String args);
    void stopAction();
}
