package rules;

import core.NetPlayer;
import core.Pair;
import core.Player;
import javafx.stage.Window;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimpleGameRules implements GameRules{

    public static final int DEF_MIN_PLAYERS = 2;
    public static final int DEF_MAX_PLAYERS = 4;
    public static final int DEF_HAND_SIZE = 5;
    public static final int DEF_FIELD_SIZE = 4;
    public static final int DEF_DECK_SIZE = 40;

    private final WinCondition winCondition;
    private final int maxPlayers,minPlayers,handSize,deckSize,fieldSize;
    private final String deckPath;
    private final Function<List<NetPlayer>,Player> firstPlayer;

    public SimpleGameRules(int maxPlayers, int minPlayers, int handSize, String deckPath,
                           int deckSize, int fieldSize, Function<List<NetPlayer>,Player> function) {
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.handSize = handSize;
        this.deckPath = deckPath;
        this.deckSize = deckSize;
        this.fieldSize = fieldSize;
        this.firstPlayer = function;
        this.winCondition = (field) -> {
            var list = Stream.iterate(0, j -> j + 1).limit(field.getSize()).map(i ->
                    Stream.iterate(0, j -> j + 1).limit(field.getSize())
                            .map(j -> new Pair<>(i, j))
                            .collect(Collectors.toSet()))
                    .collect(Collectors.toList());
            list.addAll(Stream.iterate(0, j -> j + 1).limit(field.getSize()).map(i ->
                    Stream.iterate(0, j -> j + 1).limit(field.getSize())
                            .map(j -> new Pair<>(j, i))
                            .collect(Collectors.toSet()))
                    .collect(Collectors.toList()));
            list.add(Stream.iterate(0, j -> j + 1).limit(field.getSize())
                    .map(i -> new Pair<>(i, i))
                    .collect(Collectors.toSet()));
            list.add(Stream.iterate(0, j -> j + 1).limit(field.getSize())
                    .map(i -> new Pair<>(i, field.getSize() - i - 1))
                    .collect(Collectors.toSet()));
            list.forEach(System.out::println);

            return list.stream().anyMatch(s -> s.stream()
                    .allMatch(c -> !field.isFree(c) &&
                            field.getCard(c).get().getNumber() % 2 == 0)) ||
                    list.stream().anyMatch(s -> s.stream()
                            .allMatch(c -> !field.isFree(c) &&
                                    field.getCard(c).get().getNumber() % 2 != 0));
        };
    }

    public SimpleGameRules(String deckPath) {
        this(DEF_MAX_PLAYERS, DEF_MIN_PLAYERS, DEF_HAND_SIZE, deckPath, DEF_DECK_SIZE, DEF_FIELD_SIZE, l -> l.get(0));
    }

    @Override
    public WinCondition getWinCondition() {
        return this.winCondition;
    }

    @Override
    public int getHandSize() {
        return this.handSize;
    }

    @Override
    public int getMaxPLayers() {
        return this.maxPlayers;
    }

    @Override
    public int getMinPLayers() {
        return this.minPlayers;
    }

    @Override
    public int getDeckSize() {
        return this.deckSize;
    }

    @Override
    public String getDeckPath() {
        return this.deckPath;
    }

    @Override
    public Function<List<NetPlayer>, Player> getFirstPlayer() {
        return this.firstPlayer;
    }

    @Override
    public int getFieldSize() {
        return this.fieldSize;
    }
}
