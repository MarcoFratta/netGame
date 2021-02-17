package packets;

import rules.Result;


public class GameOverPacket implements Packet {

    private static final long serialVersionUID = 7021394636093631434L;
    private final Result result;

    public GameOverPacket(Result res) {
        this.result = res;
    }

    public Result getResult() {
        return this.result;
    }
}
