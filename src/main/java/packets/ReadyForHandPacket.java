package packets;

public class ReadyForHandPacket implements Packet {

    private int id;
    public ReadyForHandPacket(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
