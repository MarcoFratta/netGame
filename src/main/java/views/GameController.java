package views;

import cards.Card;
import core.Logic;
import core.Pair;
import rules.Result;
import packets.GameInfoPacket;

import java.util.List;
import java.util.Optional;

public interface GameController {

    void create(GameInfoPacket g, Logic logic);

    void addHandCard(Optional<Card> c, int position);

    void addCardToField(Card card, Pair<Integer,Integer> pos);

    void removeCardFromHand(int position);

    void close(Result result);

    void selectHand(int position);

    void selectCells(List<Pair<Integer, Integer>> cells);

    void clearFieldSelections();

    void setCanPlay(Boolean b);

    void removeCardFromField(Pair<Integer, Integer> pos);

    void clearHandSelections();

    void showResultAndExit(Result result);
}
