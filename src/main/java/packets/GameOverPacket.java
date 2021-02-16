package packets;

import rules.Result;

import java.io.Serial;

public class GameOverPacket implements Packet {

    @Serial
    private static final long serialVersionUID = 7021394636093631434L;
    private final Result result;

    public GameOverPacket(Result res) {
        this.result = res;
    }

    public Result getResult() {
        return this.result;
    }
}
