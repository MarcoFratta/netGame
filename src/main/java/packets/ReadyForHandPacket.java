package packets;

public class ReadyForHandPacket implements Packet {

    private static final long serialVersionUID = 6131519597166067721L;
    private final int id;
    public ReadyForHandPacket(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
