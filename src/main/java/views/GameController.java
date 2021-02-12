package views;

import cards.Card;
import core.Logic;
import core.Pair;
import core.Result;
import packets.GameInfoPacket;

import java.util.List;

public interface GameController {

    void create(GameInfoPacket g, Logic logic);

    void addHandCard(Card c, int position);

    void addCardToField(Card card, Pair<Integer,Integer> pos);

    void removeCardFromHand(int position);

    void close(Result result);

    void selectCells(List<Pair<Integer, Integer>> cells);

    void clearSelections();

    void setCanPlay(Boolean b);

    void removeCardFromField(Pair<Integer, Integer> pos);
}
