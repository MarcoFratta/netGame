import cards.Deck;
import cards.DeckImpl;
import core.*;
import net.Main;
import org.junit.Test;
import rules.SimpleGameRules;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class TestTwoPlayers {


    @Test
    public void testLoadGame() throws UnknownHostException {


        Main.main(null);

        GameManager gameManager = new GameManager(new SimpleGameRules(getClass().getResource("/cards_file/napoletane.json").getFile()));
        try {
            gameManager.startGame();
            fail("can't start game without players");
        } catch (IllegalStateException e) {

        } catch (Exception e) {
            fail("wrong exception raised");
        }

        Player player1 = new LocalPlayer("p1", InetAddress.getLocalHost(), 6001);
        Player player2 = new LocalPlayer("p1", InetAddress.getLocalHost(), 6002);
        gameManager.addPLayer(player1);
        try {
            gameManager.startGame();
            fail("can't start game without players");
        } catch (IllegalStateException e) {

        } catch (Exception e) {
            fail("wrong exception raised");
        }
        gameManager.addPLayer(player2);
        try {
            gameManager.startGame();

        } catch (IllegalStateException e) {
            fail("Match should start");
        } catch (Exception e) {
            fail("wrong exception raised");
        }
    }
    @Test
    public void testMatch() throws UnknownHostException {
        Main.main(null);

        Player player1 = new LocalPlayer("p1", InetAddress.getLocalHost(), 6001);
        Player player2 = new LocalPlayer("p1", InetAddress.getLocalHost(), 6002);

        GameManager manager = new GameManager(new SimpleGameRules(2,2,4,
                getClass().getResource("/cards_file/napoletane.json").getFile(),40,4,a->a.get(0)));

        manager.addPLayer(player1);
        manager.addPLayer(player2);
        DefaultLogic l =  (DefaultLogic) manager.startGame();
        assertEquals(2, l.getHands().size());

    }

    @Test
    public void testLoadFromJson(){

        try {
            Main.main(null);
            Deck d = new DeckImpl(getClass().getResource("/cards_file/napoletane.json").getFile(),40);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
