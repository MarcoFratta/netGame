package core;

import rules.GameRules;

import java.util.ArrayList;
import java.util.List;
/*
    defines a manager for adding players, and start a match

 */

public class GameManager {

    private static final int MIN_PLAYERS = 2;

    private final Logic logic ;
    private final List<Player> playerList;
    private GameRules gameRules;

    public GameManager(GameRules gameRules){
        this.gameRules = gameRules;
        this.logic = new DefaultLogic(gameRules);
        this.playerList = new ArrayList<>();
    }

    public void addPLayer(Player p){
        this.playerList.add(p);
    }

    public Logic startGame(){
        if(this.playerList.size() < MIN_PLAYERS){
            throw new IllegalStateException();
        }
        logic.startGame(List.copyOf(playerList));
        return logic;
    }

}
