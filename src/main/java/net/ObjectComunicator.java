package net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectComunicator implements Comunicator{

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public ObjectComunicator(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public  void send(Object b) {
        try{
            this.output.writeObject(b);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public  Object receive() {
        Object p = null;
        try {
             p = this.input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
