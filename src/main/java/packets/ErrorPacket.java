package packets;

public class ErrorPacket implements Packet {

    private final String message;
    public ErrorPacket(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
