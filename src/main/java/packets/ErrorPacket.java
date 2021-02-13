package packets;

public class ErrorPacket implements Packet {

    private static final long serialVersionUID = 5887592025383177493L;
    private final String message;
    public ErrorPacket(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
